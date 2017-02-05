package activitystarter.compiler

import activitystarter.Arg
import activitystarter.compiler.classbinding.ActivityBinding
import activitystarter.compiler.classbinding.ClassBinding
import activitystarter.compiler.classbinding.FragmentBinding
import com.squareup.javapoet.TypeName
import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.CLASS
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

internal fun parseArg(element: Element, builderMap: MutableMap<TypeElement, ClassBinding>) {
    val enclosingElement = element.enclosingElement as TypeElement

    if (isInaccessibleViaGeneratedCode(Arg::class.java, "fields", element)
            || veryfyFieldType(element, enclosingElement, getElementType(element)))
        return


    if (getFieldAccessibility(element) == FieldVeryfyResult.Inaccessible) {
        error(enclosingElement, "@%s %s Inaccessable element. (%s.%s)",
                Arg::class.java.simpleName, element, enclosingElement.qualifiedName,
                element.simpleName)
        return
    }

    parseClass(enclosingElement, builderMap)
}

internal fun parseClass(element: Element, builderMap: MutableMap<TypeElement, ClassBinding>) {
    val typeElement = element as TypeElement
    if (builderMap.containsKey(typeElement)) return

    val elementType = getKnownClassType(getElementType(element))
    if (elementType == null) {
        error(element, "@%s %s Is in wroing type. It needs to be Activity, Froagment or Service. (%s.%s)",
                Arg::class.java.simpleName, element, element.qualifiedName,
                element.simpleName)
        return
    }

    val classBinding = when (elementType) {
        KnownClassType.Activity -> ActivityBinding(typeElement)
        KnownClassType.Fragment -> FragmentBinding(typeElement)
    }

    builderMap.put(typeElement, classBinding)
}

private fun isInaccessibleViaGeneratedCode(annotationClass: Class<out Annotation>, targetThing: String, element: Element): Boolean {
    val enclosingElement = element.enclosingElement as TypeElement

    // Verify containing type.
    if (enclosingElement.kind != CLASS) {
        inaccessibleError("may only be contained in classes.", annotationClass, element, enclosingElement, targetThing)
        return true
    }

    // Verify containing class visibility is not private.
    if (enclosingElement.modifiers.contains(PRIVATE)) {
        inaccessibleError("may not be contained in private classes.", annotationClass, element, enclosingElement, targetThing)
        return true
    }

    return false
}

private fun inaccessibleError(text: String, annotationClass: Class<out Annotation>, element: Element, enclosingElement: TypeElement, targetThing: String) {
    error(enclosingElement, "@%s %s $text (%s.%s)",
            annotationClass.simpleName, targetThing, enclosingElement.qualifiedName,
            element.simpleName)
}


private fun veryfyFieldType(element: Element, enclosingElement: TypeElement, elementType: TypeMirror): Boolean {
    if (!isFieldValidType(elementType)) {
        error(element, "@%s fields must extend from Serializable, Parcelable or beof type String, int, float, double, char or boolean. (%s.%s)",
                Arg::class.java.simpleName, enclosingElement.qualifiedName, element.simpleName)
        return true
    }
    return false
}

private fun isFieldValidType(elementType: TypeMirror): Boolean {
    return elementType.kind in listOf(TypeKind.BOOLEAN, TypeKind.INT, TypeKind.FLOAT, TypeKind.DOUBLE, TypeKind.CHAR) ||
            TypeName.get(elementType) == TypeName.get(String::class.java) ||
            isSubtypeOfType(elementType, SERIALIZABLE_TYPE) ||
            isSubtypeOfType(elementType, PARCELABLE_TYPE)
}

private fun getKnownClassType(elementType: TypeMirror): KnownClassType? = when {
    isSubtypeOfType(elementType, ACTIVITY_TYPE) -> KnownClassType.Activity
    isSubtypeOfType(elementType, FRAGMENT_TYPE) || isSubtypeOfType(elementType, FRAGMENTv4_TYPE) -> KnownClassType.Fragment
    else -> null
}

enum class KnownClassType {
    Activity,
    Fragment
}

private val ACTIVITY_TYPE = "android.app.Activity"
private val FRAGMENT_TYPE = "android.app.Fragment"
private val FRAGMENTv4_TYPE = "android.support.v4.app.Fragment"
private val SERIALIZABLE_TYPE = "java.io.Serializable"
private val PARCELABLE_TYPE = "android.os.Parcelable"