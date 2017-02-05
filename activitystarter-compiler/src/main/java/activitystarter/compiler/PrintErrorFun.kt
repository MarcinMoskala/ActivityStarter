package activitystarter.compiler

import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

var messanger: Messager? = null

fun error(element: Element, message: String, vararg args: Any) {
    printMessage(Diagnostic.Kind.ERROR, element, message, args)
}

private fun printMessage(kind: Diagnostic.Kind, element: Element, message: String, args: Array<out Any>) {
    messanger!!.printMessage(kind, if (args.isNotEmpty()) String.format(message, *args) else message, element)
}