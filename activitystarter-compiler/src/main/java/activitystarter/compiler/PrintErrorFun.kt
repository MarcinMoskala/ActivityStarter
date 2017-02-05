package activitystarter.compiler

import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic.Kind.ERROR

var messanger: Messager? = null

fun error(element: Element, message: String, vararg args: Any) {
    messanger!!.printMessage(ERROR, if (args.isNotEmpty()) String.format(message, *args) else message, element)
}