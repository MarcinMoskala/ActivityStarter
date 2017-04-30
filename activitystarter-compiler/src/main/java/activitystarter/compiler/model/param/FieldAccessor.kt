package activitystarter.compiler.model.param

import activitystarter.compiler.error.Errors
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

class FieldAccessor(element: Element) {

    private val enclosingElement = element.enclosingElement as TypeElement
    private val fieldName: String = element.simpleName.toString()
    private val setterType: FieldAccessType = getFieldAccessType(element, "set", "set")
    private val getterType: FieldAccessType = getFieldAccessType(element, "get", "is")

    fun isAccessible() = setterType != FieldAccessType.Inaccessible && getterType != FieldAccessType.Inaccessible

    fun makeSetter(whatToSet: String): String? = when (setterType) {
        FieldAccessType.Accessible -> "$fieldName = $whatToSet"
        FieldAccessType.ByMethod -> "set${fieldName.capitalize()}($whatToSet)"
        FieldAccessType.ByNoIsMethod -> "set${fieldName.substring(2)}($whatToSet)"
        FieldAccessType.Inaccessible -> throw Error(Errors.noSetter)
    }

    fun getFieldValue(): String? = when (setterType) {
        FieldAccessType.Accessible -> fieldName
        FieldAccessType.ByMethod -> "get${fieldName.capitalize()}()"
        FieldAccessType.ByNoIsMethod -> "is${fieldName.substring(2)}()"
        FieldAccessType.Inaccessible -> throw Error(Errors.noSetter)
    }

    private fun getFieldAccessType(element: Element, functionModifier: String, isFunctionModifier: String) = when {
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
