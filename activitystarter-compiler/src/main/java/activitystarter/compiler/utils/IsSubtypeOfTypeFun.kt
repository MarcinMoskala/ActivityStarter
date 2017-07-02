package activitystarter.compiler.utils

import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

fun TypeMirror.isSubtypeOfType(vararg otherType: String): Boolean {
    val typeString = this.toString()
    val kind = kind
    return when {
        otherType.any { it.substringBefore("<") == typeString } -> true
        kind != TypeKind.DECLARED -> false
        else -> {
            val element = this.toTypeElement() ?: return false
            element.superclass.isSubtypeOfType(*otherType) || element.interfaces.any { it.isSubtypeOfType(*otherType) }
        }
    }
}

fun TypeMirror.toTypeElement() = (this as? DeclaredType)?.asElement() as? TypeElement