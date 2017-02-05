package activitystarter.compiler

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import com.squareup.javapoet.TypeName
import java.util.*
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.CLASS
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

class ElementsParser(private val messager: Messager) {

    internal fun parseArg(element: Element, builderMap: MutableMap<TypeElement, ClassBinding>) {
        val enclosingElement = element.enclosingElement as TypeElement

        if (isInaccessibleViaGeneratedCode(Arg::class.java, "fields", element))
            return

        val elementType = getElementType(element)

        if (veryfyFieldType(element, enclosingElement, elementType))
            return

        val fieldVeryfyResult = getFieldAccessibility(element)

        if (fieldVeryfyResult == FieldVeryfyResult.Inaccessible) {
            error(enclosingElement, "@%s %s Inaccessable element. (%s.%s)",
                    Arg::class.java.simpleName, element, enclosingElement.qualifiedName,
                    element.simpleName)
            return
        }

        if (!builderMap.containsKey(enclosingElement)) {
            builderMap.put(enclosingElement, ClassBinding(enclosingElement))
        }
    }

    internal fun parseClass(element: Element, builderMap: MutableMap<TypeElement, ClassBinding>) {
        val typeElement = element as TypeElement
        val elementType = getElementType(element)

        if (veryfyClassType(element, elementType))
            return

        if (!builderMap.containsKey(typeElement)) {
            builderMap.put(typeElement, ClassBinding(typeElement))
        }
    }

    private fun isInaccessibleViaGeneratedCode(annotationClass: Class<out Annotation>, targetThing: String, element: Element): Boolean {
        var hasError = false
        val enclosingElement = element.enclosingElement as TypeElement

        // Verify containing type.
        if (enclosingElement.kind != CLASS) {
            error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)",
                    annotationClass.simpleName, targetThing, enclosingElement.qualifiedName,
                    element.simpleName)
            hasError = true
        }

        // Verify containing class visibility is not private.
        if (enclosingElement.modifiers.contains(PRIVATE)) {
            error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)",
                    annotationClass.simpleName, targetThing, enclosingElement.qualifiedName,
                    element.simpleName)
            hasError = true
        }

        return hasError
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
        return SUPPORTED_BASE_TYPES.contains(elementType.kind) ||
                TypeName.get(elementType) == TypeName.get(String::class.java) ||
                isSubtypeOfType(elementType, SERIALIZABLE_TYPE) ||
                isSubtypeOfType(elementType, PARCELABLE_TYPE)
    }

    private fun veryfyClassType(element: Element, elementType: TypeMirror): Boolean {
        if (!isSubtypeOfType(elementType, ACTIVITY_TYPE)) {
            error(element, "@%s must be addede before Activity class definition. (%s)",
                    MakeActivityStarter::class.java.simpleName, element.simpleName)
            return true
        }
        return false
    }

    private fun error(element: Element, message: String, vararg args: Any) {
        printMessage(Diagnostic.Kind.ERROR, element, message, args)
    }

    private fun printMessage(kind: Diagnostic.Kind, element: Element, message: String, args: Array<out Any>) {
        messager.printMessage(kind, if (args.isNotEmpty()) String.format(message, *args) else message, element)
    }

    companion object {

        private val ACTIVITY_TYPE = "android.app.Activity"
        private val SERIALIZABLE_TYPE = "java.io.Serializable"
        private val PARCELABLE_TYPE = "android.os.Parcelable"

        private val SUPPORTED_BASE_TYPES = Arrays.asList(
                TypeKind.BOOLEAN, TypeKind.INT, TypeKind.FLOAT, TypeKind.DOUBLE, TypeKind.CHAR
        )
    }
}