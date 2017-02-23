package activitystarter.compiler

import activitystarter.Arg
import activitystarter.MakeActivityStarter
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
    val elementType = getElementType(element)
    if (!correctField(element, elementType, enclosingElement)) return
    parseClass(enclosingElement, builderMap)
}

internal fun parseClass(element: Element, builderMap: MutableMap<TypeElement, ClassBinding>) {
    val typeElement = element as TypeElement
    if (builderMap.containsKey(typeElement)) return

    val elementType = KnownClassType.getByType(getElementType(element))
    if (!correctClass(typeElement, elementType)) return

    val classBinding = getClassBinding(elementType!!, typeElement)
    builderMap.put(typeElement, classBinding)
}

private fun correctClass(element: TypeElement, elementType: KnownClassType?): Boolean {
    fun check(assertion: Boolean, errorText: String) = parsingError(assertion, errorText, MakeActivityStarter::class.java, element, element)
    return check(elementType != null, Errors.wrongClassType)
}

private fun correctField(element: Element, elementType: TypeMirror, enclosingElement: TypeElement): Boolean {
    fun check(assertion: Boolean, errorText: String) = parsingError(assertion, errorText, Arg::class.java, element, enclosingElement)
    return check(enclosingElement.kind == CLASS, Errors.notAClass)
            && check(!enclosingElement.modifiers.contains(PRIVATE), Errors.privateClass)
            && check(isFieldValidType(elementType), Errors.notSupportedType)
            && check(FieldAccessor(element).isAccessible(), Errors.inaccessibleField)
            && check(!(getElementType(enclosingElement).isSubtypeOfType(BROADCAST_RECEIVER_TYPE) && !isBasicSupportedType(elementType)), Errors.notBasicTypeInReceiver)
}

private fun getClassBinding(elementType: KnownClassType, typeElement: TypeElement): ClassBinding {
    return when (elementType) {
        Activity -> ActivityBinding(typeElement)
        Fragment -> FragmentBinding(typeElement)
        Service -> ServiceBinding(typeElement)
        BroadcastReceiver -> BroadcastReceiverBinding(typeElement)
    }
}

private fun parsingError(assertion: Boolean, text: String, annotationClass: Class<out Annotation>, element: Element, enclosingElement: TypeElement): Boolean {
    if (!assertion) error(enclosingElement, "@%s %s $text (%s)", annotationClass.simpleName, enclosingElement.qualifiedName, element.simpleName)
    return assertion
}

private fun isFieldValidType(elementType: TypeMirror) = isBasicSupportedType(elementType) || isSubtypeOfSupportedTypes(elementType)

private fun isSubtypeOfSupportedTypes(elementType: TypeMirror) =
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