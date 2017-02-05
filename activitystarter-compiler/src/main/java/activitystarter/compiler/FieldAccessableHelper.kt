package activitystarter.compiler

import javax.lang.model.element.Element
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

enum class FieldVeryfyResult {
    Accessible,
    BySetter,
    ByIsSetter,
    ByNoIsSetter,
    Inaccessible
}

fun getFieldAccessibility(element: Element): FieldVeryfyResult {
    val enclosingElement = element.enclosingElement as TypeElement

    val modifiers = element.modifiers
    if (modifiers.contains(PRIVATE)) {
        val fieldName = element.simpleName.toString()
        val fieldNameCapitalized = capitalizeFirstLetter(fieldName)
        if (hasMethodNamed(enclosingElement, "get" + fieldNameCapitalized))
            return FieldVeryfyResult.BySetter
        if (fieldName.substring(0, 2) == "is" && hasMethodNamed(enclosingElement, "set" + fieldName.substring(2)))
            return FieldVeryfyResult.ByNoIsSetter
        else
            return FieldVeryfyResult.Inaccessible
    } else {
        return FieldVeryfyResult.Accessible
    }
}

fun getSetter(result: FieldVeryfyResult, fieldName: String): String?  = when (result) {
        FieldVeryfyResult.BySetter -> "set" + capitalizeFirstLetter(fieldName)
        FieldVeryfyResult.ByNoIsSetter -> "set" + fieldName.substring(2)
        else -> null
    }

private fun hasMethodNamed(enclosingElement: TypeElement, fieldName: String): Boolean {
    for (e in ElementFilter.methodsIn(enclosingElement.enclosedElements)) {
        if (e.simpleName.contentEquals(fieldName))
            return true
    }
    return false
}
