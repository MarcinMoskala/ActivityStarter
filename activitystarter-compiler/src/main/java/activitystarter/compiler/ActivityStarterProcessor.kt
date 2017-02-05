package activitystarter.compiler

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import com.google.auto.service.AutoService
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class ActivityStarterProcessor : AbstractProcessor() {

    private lateinit var filer: Filer

    @Synchronized override fun init(env: ProcessingEnvironment) {
        super.init(env)
        filer = env.filer
    }

    override fun getSupportedAnnotationTypes() = listOf<Class<*>>(Arg::class.java, MakeActivityStarter::class.java)
            .map { it.canonicalName }.toSet()

    override fun process(elements: Set<TypeElement>, env: RoundEnvironment): Boolean {
        val bindingMap = findAndParseTargets(env)
        processTargets(bindingMap)
        return true
    }

    override fun getSupportedSourceVersion() = SourceVersion.latestSupported()

    private fun findAndParseTargets(env: RoundEnvironment): Map<TypeElement, ClassBinding> {
        val builderMap = LinkedHashMap<TypeElement, ClassBinding>()
        val parser = ElementsParser(processingEnv.messager)

        processAnnotation<Arg>(env) { element ->
            parser.parseArg(element, builderMap)
        }

        processAnnotation<MakeActivityStarter>(env) { element ->
            parser.parseClass(element, builderMap)
        }

        return builderMap
    }

    private inline fun <reified T: Annotation> processAnnotation(env: RoundEnvironment, process: (Element)->Unit) {
        for (element in env.getElementsAnnotatedWith(T::class.java)) {
            try {
                process(element)
            } catch (e: Exception) {
                logParsingError(element, T::class.java, e)
            }
        }
    }

    private fun processTargets(bindingMap: Map<TypeElement, ClassBinding>) {
        for ((typeElement, binding) in bindingMap) {
            try {
                binding.brewJava().writeTo(filer)
            } catch (e: IOException) {
                error(typeElement, "Unable to write binding for type %s: %s", typeElement, e.message ?: "")
            }
        }
    }

    private fun logParsingError(element: Element, annotation: Class<out Annotation>, e: Exception) {
        val stackTrace = StringWriter()
        e.printStackTrace(PrintWriter(stackTrace))
        error(element, "Unable to parse @%s binding.\n\n%s", annotation.simpleName, stackTrace)
    }

    private fun error(element: Element, message: String, vararg args: Any) {
        printMessage(Diagnostic.Kind.ERROR, element, message, args)
    }

    private fun printMessage(kind: Diagnostic.Kind, element: Element, message: String, args: Array<out Any>) {
        processingEnv.messager.printMessage(kind, if (args.isNotEmpty()) String.format(message, *args) else message, element)
    }
}