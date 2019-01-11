package activitystarter.compiler.model.param

import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

class FieldAccessor private constructor(
        val fieldName: String,
        private val setterType: FieldAccessType,
        private val getterType: FieldAccessType,
        val fromGetter: Boolean
) {

    fun isSettable() = setterType != FieldAccessType.Inaccessible

    fun isAccessible() = setterType != FieldAccessType.Inaccessible && getterType != FieldAccessType.Inaccessible

    fun makeSetter(whatToSet: String): String? = when (setterType) {
        FieldAccessType.Accessible -> "$fieldName = $whatToSet"
        FieldAccessType.ByMethod -> "set${fieldName.capitalize()}($whatToSet)"
        FieldAccessType.ByNoIsMethod -> "set${fieldName.substring(2)}($whatToSet)"
        FieldAccessType.Inaccessible -> null
    }

    fun makeGetter(): String? = when (getterType) {
        FieldAccessType.Accessible -> fieldName
        FieldAccessType.ByMethod -> "get${fieldName.capitalize()}()"
        FieldAccessType.ByNoIsMethod -> "is${fieldName.substring(2)}()"
        FieldAccessType.Inaccessible -> null
    }

    companion object {

        fun fromGetter(getterMethod: Element, name: String): FieldAccessor {
            val enclosingElement = getterMethod.enclosingElement as TypeElement
            val setterType = getPrivateFieldAccessType(enclosingElement, name, "set", "set")
            return FieldAccessor(name, setterType, FieldAccessType.ByMethod, true)
        }

        fun fromElement(element: Element): FieldAccessor {
            val enclosingElement = element.enclosingElement as TypeElement
            val fieldName = element.simpleName.toString()
            val setterType = getFieldAccessType(enclosingElement, fieldName, element, "set", "set")
            val getterType = getFieldAccessType(enclosingElement, fieldName, element, "get", "is")
            return FieldAccessor(fieldName, setterType, getterType, false)
        }

        private fun getFieldAccessType(enclosingElement: TypeElement, fieldName: String, element: Element, functionModifier: String, isFunctionModifier: String) = when {
            PRIVATE !in element.modifiers -> FieldAccessType.Accessible
            else -> getPrivateFieldAccessType(enclosingElement, fieldName, functionModifier, isFunctionModifier)
        }

        private fun getPrivateFieldAccessType(enclosingElement: TypeElement, fieldName: String, functionModifier: String, isFunctionModifier: String) = when {
            enclosingElement.hasNotPrivateMethodNamed(functionModifier + fieldName.capitalize()) -> FieldAccessType.ByMethod
            fieldName.startsWith("is") && enclosingElement.hasNotPrivateMethodNamed(isFunctionModifier + fieldName.substring(2)) -> FieldAccessType.ByNoIsMethod
            else -> FieldAccessType.Inaccessible
        }

        private fun TypeElement.hasNotPrivateMethodNamed(methodName: String) = ElementFilter
                .methodsIn(enclosedElements)
                .any { e -> e.simpleName.contentEquals(methodName) && PRIVATE !in e.modifiers }
    }
}
