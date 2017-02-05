package activitystarter.compiler

import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.TypeVariable

fun capitalizeFirstLetter(original: String?) =
        if (original == null || original.isEmpty()) original
        else original.substring(0, 1).toUpperCase() + original.substring(1)


fun getElementType(element: Element): TypeMirror = element.asType()
        .let { if (it.kind == TypeKind.TYPEVAR) (it as TypeVariable).upperBound else it }