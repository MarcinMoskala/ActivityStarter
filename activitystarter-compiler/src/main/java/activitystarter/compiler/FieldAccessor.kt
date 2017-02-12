package activitystarter.compiler

import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

class FieldAccessor {

    private val setterType: FieldAccessType
    private val getterType: FieldAccessType
    val fieldName: String

    constructor(element: Element) {
        val enclosingElement = element.enclosingElement as TypeElement
        fieldName = element.simpleName.toString()
        setterType = when {
            PRIVATE !in element.modifiers -> FieldAccessType.Accessible
            hasNotPrivateMethodNamed(enclosingElement, "set" + capitalizeFirstLetter(fieldName)) -> FieldAccessType.ByMethod
            fieldName.substring(0, 2) == "is" && hasNotPrivateMethodNamed(enclosingElement, "set" + fieldName.substring(2)) -> FieldAccessType.ByNoIsMethod
            else -> FieldAccessType.Inaccessible
        }
        getterType = when {
            PRIVATE !in element.modifiers -> FieldAccessType.Accessible
            hasNotPrivateMethodNamed(enclosingElement, "get" + capitalizeFirstLetter(fieldName)) -> FieldAccessType.ByMethod
            fieldName.substring(0, 2) == "is" && hasNotPrivateMethodNamed(enclosingElement, "is" + fieldName.substring(2)) -> FieldAccessType.ByNoIsMethod
            else -> FieldAccessType.Inaccessible
        }

    }

    fun isAccessible() = setterType != FieldAccessType.Inaccessible && getterType != FieldAccessType.Inaccessible

    fun setToField(whatToSet: String): String? = when (setterType) {
        FieldAccessType.Accessible -> "$fieldName = $whatToSet"
        FieldAccessType.ByMethod -> "set${capitalizeFirstLetter(fieldName)}($whatToSet)"
        FieldAccessType.ByNoIsMethod -> "set${fieldName.substring(2)}($whatToSet)"
        FieldAccessType.Inaccessible -> throw Error(Errors.noSetter)
    }

    fun getFieldValue(): String? = when (setterType) {
        FieldAccessType.Accessible -> fieldName
        FieldAccessType.ByMethod -> "get${capitalizeFirstLetter(fieldName)}()"
        FieldAccessType.ByNoIsMethod -> "is${fieldName.substring(2)}()"
        FieldAccessType.Inaccessible -> throw Error(Errors.noSetter)
    }

    private fun hasNotPrivateMethodNamed(enclosingElement: TypeElement, fieldName: String): Boolean {
        for (e in ElementFilter.methodsIn(enclosingElement.enclosedElements)) {
            if (e.simpleName.contentEquals(fieldName) && PRIVATE !in e.modifiers)
                return true
        }
        return false
    }

    private enum class FieldAccessType {
        Accessible,
        ByMethod,
        ByNoIsMethod,
        Inaccessible
    }
}
