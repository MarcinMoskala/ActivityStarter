package activitystarter.compiler

import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

// Total SHIT TODO Write it in solid, functional way
fun TypeMirror.isSubtypeOfType(otherType: String): Boolean {
    if (otherType == this.toString()) return true
    if (this.kind != TypeKind.DECLARED) return false
    val declaredType = this as DeclaredType
    val typeArguments = declaredType.typeArguments
    if (typeArguments.size > 0) {
        val typeString = StringBuilder(declaredType.asElement().toString())
        typeString.append('<')
        for (i in typeArguments.indices) {
            if (i > 0) {
                typeString.append(',')
            }
            typeString.append('?')
        }
        typeString.append('>')
        if (typeString.toString() == otherType) {
            return true
        }
    }
    val element = declaredType.asElement() as? TypeElement ?: return false
    return element.superclass.isSubtypeOfType(otherType) || element.interfaces.any { it.isSubtypeOfType(otherType) }
}