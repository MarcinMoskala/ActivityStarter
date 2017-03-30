package activitystarter.compiler

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import activitystarter.compiler.KnownClassType.*
import activitystarter.compiler.classbinding.*
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.CLASS
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

internal fun parseArg(element: Element, builderMap: MutableMap<TypeElement, ClassBinding>) {
    val enclosingElement = element.enclosingElement as TypeElement
    val elementType = getElementType(element)
    val errorText = getFieldError(element, elementType, enclosingElement)
    if (errorText != null) {
        parsingError<Arg>(errorText, element, enclosingElement)
    } else {
        parseClass(enclosingElement, builderMap)
    }
}

internal fun parseClass(element: Element, builderMap: MutableMap<TypeElement, ClassBinding>) {
    val typeElement = element as TypeElement
    if (builderMap.containsKey(typeElement)) return

    val elementType = KnownClassType.getByType(getElementType(element))
    val errorText = getClassError(elementType)
    if (errorText != null) {
        parsingError<MakeActivityStarter>(errorText, element, element)
    } else {
        val classBinding = getClassBinding(elementType!!, typeElement)
        builderMap.put(typeElement, classBinding)
    }
}

private fun getClassError(elementType: KnownClassType?) = when {
    elementType == null -> Errors.wrongClassType
    else -> null
}

private fun getFieldError(element: Element, elementType: TypeMirror, enclosingElement: TypeElement) = when {
    enclosingElement.kind != CLASS -> Errors.notAClass
    enclosingElement.modifiers.contains(PRIVATE) -> Errors.privateClass
//    !isFieldValidType(elementType) -> Errors.notSupportedType
    !FieldAccessor(element).isAccessible() -> Errors.inaccessibleField
//    (getElementType(enclosingElement).isSubtypeOfType(BROADCAST_RECEIVER_TYPE) && !isBasicSupportedType(elementType)) -> Errors.notBasicTypeInReceiver
    else -> null
}

private fun getClassBinding(elementType: KnownClassType, typeElement: TypeElement): ClassBinding = when (elementType) {
    Activity -> ActivityBinding(typeElement)
    Fragment -> FragmentBinding(typeElement)
    Service -> ServiceBinding(typeElement)
    BroadcastReceiver -> BroadcastReceiverBinding(typeElement)
}

private inline fun <reified T : Annotation> parsingError(text: String, element: Element, enclosingElement: TypeElement) {
    error(enclosingElement, "@%s %s $text (%s)", T::class.java.simpleName, enclosingElement.qualifiedName, element.simpleName)
}

// TODO
//private fun isFieldValidType(elementType: TypeMirror) = isBasicSupportedType(elementType) || isSubtypeOfSupportedTypes(elementType)

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