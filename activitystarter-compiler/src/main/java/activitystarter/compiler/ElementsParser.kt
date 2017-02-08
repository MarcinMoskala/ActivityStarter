package activitystarter.compiler

import activitystarter.Arg
import activitystarter.compiler.KnownClassType.*
import activitystarter.compiler.classbinding.*
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.CLASS
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

internal fun parseArg(element: Element, builderMap: MutableMap<TypeElement, ClassBinding>) {
    val enclosingElement = element.enclosingElement as TypeElement
    fun check(assertion: Boolean, errorText: String): Boolean {
        if (!assertion) inaccessibleError(errorText, Arg::class.java, element, enclosingElement)
        return assertion
    }

    val elementType = getElementType(element)
    var correct = check(enclosingElement.kind == CLASS, Errors.notAClass)
            // Verify containing class visibility is not private.
            && check(!enclosingElement.modifiers.contains(PRIVATE), Errors.privateClass)
            && check(isFieldValidType(elementType), Errors.notSupportedType)
            && check(getFieldAccessibility(element) != FieldVeryfyResult.Inaccessible, Errors.inaccessibleField)
            // TODO NOT WORKING
            && check(!(getElementType(enclosingElement).isSubtypeOfType(BROADCAST_RECEIVER_TYPE) && !isBasicSupportedType(elementType)), Errors.notBasicTypeInReceiver)

    if (correct)
        parseClass(enclosingElement, builderMap)
}

internal fun parseClass(element: Element, builderMap: MutableMap<TypeElement, ClassBinding>) {
    val typeElement = element as TypeElement
    if (builderMap.containsKey(typeElement)) return

    val elementType = KnownClassType.getByType(getElementType(element))
    if (elementType == null) {
        error(element, "@%s %s Is in wroing type. It needs to be Activity, Froagment or Service. (%s.%s)",
                Arg::class.java.simpleName, element, element.qualifiedName,
                element.simpleName)
        return
    }

    val classBinding = when (elementType) {
        Activity -> ActivityBinding(typeElement)
        Fragment -> FragmentBinding(typeElement)
        Service -> ServiceBinding(typeElement)
        BroadcastReceiver -> BroadcastReceiverBinding(typeElement)
    }

    builderMap.put(typeElement, classBinding)
}

private fun inaccessibleError(text: String, annotationClass: Class<out Annotation>, element: Element, enclosingElement: TypeElement) {
    error(enclosingElement, "@%s %s $text (%s)", annotationClass.simpleName, enclosingElement.qualifiedName, element.simpleName)
}

private fun isFieldValidType(elementType: TypeMirror) =
        isBasicSupportedType(elementType) || isSubtypeOfSupportedTypes(elementType)

fun isSubtypeOfSupportedTypes(elementType: TypeMirror) =
        elementType.isSubtypeOfType(SERIALIZABLE_TYPE, PARCELABLE_TYPE)

private fun isBasicSupportedType(elementType: TypeMirror) = elementType.kind in listOf(TypeKind.BOOLEAN, TypeKind.INT, TypeKind.FLOAT, TypeKind.DOUBLE, TypeKind.CHAR) ||
        TypeName.get(elementType) == TypeName.get(String::class.java)

enum class KnownClassType(vararg val typeString: String) {
    Activity(ACTIVITY_TYPE),
    Fragment(FRAGMENT_TYPE, FRAGMENTv4_TYPE),
    Service(SERVICE_TYPE),
    BroadcastReceiver(BROADCAST_RECEIVER_TYPE);

    companion object {
        fun getByType(elementType: TypeMirror): KnownClassType? = KnownClassType.values()
                .first { elementType.isSubtypeOfType(*it.typeString) }
    }
}

private val ACTIVITY_TYPE = "android.app.Activity"
private val FRAGMENT_TYPE = "android.app.Fragment"
private val FRAGMENTv4_TYPE = "android.support.v4.app.Fragment"
private val SERVICE_TYPE = "android.app.Service"
private val BROADCAST_RECEIVER_TYPE = "android.content.BroadcastReceiver"
private val SERIALIZABLE_TYPE = "java.io.Serializable"
private val PARCELABLE_TYPE = "android.os.Parcelable"