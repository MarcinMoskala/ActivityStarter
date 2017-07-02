package activitystarter.compiler.error

import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic.Kind.ERROR

var messanger: Messager? = null

inline fun <reified T : Annotation> parsingError(text: String, element: Element, enclosingElement: TypeElement) {
    error(enclosingElement, "@%s %s $text (%s)", T::class.java.simpleName, enclosingElement.qualifiedName, element.simpleName)
}

fun error(element: Element, message: String, vararg args: Any) {
    messanger!!.printMessage(ERROR, if (args.isNotEmpty()) String.format(message, *args) else message, element)
}

fun error(message: String, vararg args: Any) {
    messanger!!.printMessage(ERROR, if (args.isNotEmpty()) String.format(message, *args) else message)
}
