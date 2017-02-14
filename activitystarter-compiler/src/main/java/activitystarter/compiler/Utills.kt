package activitystarter.compiler

import com.squareup.javapoet.ClassName
import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.TypeVariable

val INTENT = ClassName.get("android.content", "Intent")
val BUNDLE = ClassName.get("android.os", "Bundle")
val CONTEXT = ClassName.get("android.content", "Context")

fun getElementType(element: Element): TypeMirror = element.asType()
        .let { if (it.kind == TypeKind.TYPEVAR) (it as TypeVariable).upperBound else it }