package activitystarter.compiler

import activitystarter.compiler.FieldAccessor.FieldVeryfyResult.*
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

class FieldAccessor {

    val type: FieldVeryfyResult
    val fieldName: String

    constructor(element: Element) {
        val enclosingElement = element.enclosingElement as TypeElement
        fieldName = element.simpleName.toString()
        type = when {
            PRIVATE !in element.modifiers -> Accessible
            hasNotPrivateMethodNamed(enclosingElement, "set" + capitalizeFirstLetter(fieldName)) -> BySetter
            fieldName.substring(0, 2) == "is" && hasNotPrivateMethodNamed(enclosingElement, "set" + fieldName.substring(2)) -> ByNoIsSetter
            else -> Inaccessible
        }

    }

    fun isAccessible() = type != Inaccessible

    fun setToField(whatToSet: String): String? = when (type) {
        FieldVeryfyResult.Accessible -> "$fieldName = $whatToSet"
        BySetter -> "set${capitalizeFirstLetter(fieldName)}($whatToSet)"
        ByNoIsSetter -> "set${fieldName.substring(2)}($whatToSet)"
        Inaccessible -> throw Error(Errors.noSetter)
    }

    private fun hasNotPrivateMethodNamed(enclosingElement: TypeElement, fieldName: String): Boolean {
        for (e in ElementFilter.methodsIn(enclosingElement.enclosedElements)) {
            if (e.simpleName.contentEquals(fieldName) && PRIVATE !in e.modifiers)
                return true
        }
        return false
    }

    enum class FieldVeryfyResult {
        Accessible,
        BySetter,
        ByNoIsSetter,
        Inaccessible
    }
}
