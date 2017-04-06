package activitystarter.compiler.utils

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.TypeVariable

val INTENT = ClassName.get("android.content", "Intent")
val BUNDLE = ClassName.get("android.os", "Bundle")
val CONTEXT = ClassName.get("android.content", "Context")
val ACTIVITY = ClassName.get("android.app", "Activity")
val STRING = ClassName.get("java.lang", "String")

fun getElementType(element: Element): TypeMirror = element.asType()
        .let { if (it.kind == TypeKind.TYPEVAR) (it as TypeVariable).upperBound else it }

inline fun MethodSpec.Builder.doIf(check: Boolean, f: MethodSpec.Builder.() -> Unit) = apply {
    if (check) f()
}

inline fun TypeSpec.Builder.doIf(check: Boolean, f: TypeSpec.Builder.() -> Unit) = apply {
    if (check) f()
}

fun <T> List<T>.addIf(condition: Boolean, vararg e: T): List<T> {
    return if(condition) this + listOf(*e) else this
}