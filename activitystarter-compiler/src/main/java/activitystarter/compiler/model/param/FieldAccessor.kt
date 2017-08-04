package activitystarter.compiler.model.param

import activitystarter.compiler.error.Errors
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

class FieldAccessor private constructor(
        private val fieldName: String,
        private val setterType: FieldAccessType,
        private val getterType: FieldAccessType
) {

    val noSetter = setterType == FieldAccessType.Inaccessible

    fun isAccessible() = setterType != FieldAccessType.Inaccessible && getterType != FieldAccessType.Inaccessible

    fun makeSetter(whatToSet: String): String? = when (setterType) {
        FieldAccessType.Accessible -> "$fieldName = $whatToSet"
        FieldAccessType.ByMethod -> "set${fieldName.capitalize()}($whatToSet)"
        FieldAccessType.ByNoIsMethod -> "set${fieldName.substring(2)}($whatToSet)"
        FieldAccessType.Inaccessible -> null
    }

    fun makeGetter(): String = when (getterType) {
        FieldAccessType.Accessible -> fieldName
        FieldAccessType.ByMethod -> "get${fieldName.capitalize()}()"
        FieldAccessType.ByNoIsMethod -> "is${fieldName.substring(2)}()"
        FieldAccessType.Inaccessible -> throw Error(Errors.noGetter)
    }

    companion object {

        fun fromGetter(name: String) = FieldAccessor(name, FieldAccessType.Inaccessible, FieldAccessType.ByMethod)

        fun fromElement(element: Element): FieldAccessor {
            val enclosingElement = element.enclosingElement as TypeElement
            val fieldName = element.simpleName.toString()
            val setterType = getFieldAccessType(enclosingElement, fieldName, element, "set", "set")
            val getterType = getFieldAccessType(enclosingElement, fieldName, element, "get", "is")
            return FieldAccessor(fieldName, setterType, getterType)
        }

        private fun getFieldAccessType(enclosingElement: TypeElement, fieldName: String, element: Element, functionModifier: String, isFunctionModifier: String) = when {
            PRIVATE !in element.modifiers -> FieldAccessType.Accessible
            hasNotPrivateMethodNamed(enclosingElement, functionModifier + fieldName.capitalize()) -> FieldAccessType.ByMethod
            fieldName.substring(0, 2) == "is" && hasNotPrivateMethodNamed(enclosingElement, isFunctionModifier + fieldName.substring(2)) -> FieldAccessType.ByNoIsMethod
            else -> FieldAccessType.Inaccessible
        }

        private fun hasNotPrivateMethodNamed(enclosingElement: TypeElement, fieldName: String) = ElementFilter
                .methodsIn(enclosingElement.enclosedElements)
                .any { e -> e.simpleName.contentEquals(fieldName) && PRIVATE !in e.modifiers }

        private enum class FieldAccessType {
            Accessible,
            ByMethod,
            ByNoIsMethod,
            Inaccessible
        }
    }
}
