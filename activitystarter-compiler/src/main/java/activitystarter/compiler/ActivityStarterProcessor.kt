package activitystarter.compiler

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import activitystarter.compiler.classbinding.ClassBindingFactory
import activitystarter.compiler.error.error
import activitystarter.compiler.error.messanger
import com.google.auto.service.AutoService
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
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
        activitystarter.compiler.error.messanger = processingEnv.messager
    }

    override fun getSupportedAnnotationTypes() = listOf<Class<*>>(Arg::class.java, MakeActivityStarter::class.java)
            .map { it.canonicalName }.toSet()

    override fun process(elements: Set<TypeElement>, env: RoundEnvironment): Boolean {
        val classesToProcess = findClassesToPrecess(env)
        processTargets(classesToProcess)
        return true
    }

    override fun getSupportedSourceVersion() = SourceVersion.latestSupported()

    private fun findClassesToPrecess(env: RoundEnvironment): Set<TypeElement> {
        var classesToProcess = setOf<TypeElement>()

        processAnnotation<Arg>(env) { element ->
            classesToProcess += element.enclosingElement as TypeElement
        }

        processAnnotation<MakeActivityStarter>(env) { element ->
            classesToProcess += element as TypeElement
        }

        return classesToProcess
    }

    private inline fun <reified T : Annotation> processAnnotation(env: RoundEnvironment, process: (Element) -> Unit) {
        for (element in env.getElementsAnnotatedWith(T::class.java)) {
            try {
                process(element)
            } catch (e: Exception) {
                logParsingError(element, T::class.java, e)
            }
        }
    }

    private fun processTargets(classesToProcess: Set<TypeElement>) {
        for (classToPrecess in classesToProcess) {
            try {
                val classBinding = ClassBindingFactory(classToPrecess).create() ?: continue
                classBinding.getClasGeneration().brewJava().writeTo(filer)
            } catch (e: IOException) {
                error(classToPrecess, "Unable to write binding for typeName %s: %s", classToPrecess, e.message ?: "")
            }
        }
    }

    private fun logParsingError(element: Element, annotation: Class<out Annotation>, e: Exception) {
        val stackTrace = StringWriter()
        e.printStackTrace(PrintWriter(stackTrace))
        error(element, "Unable to parse @%s binding.\n\n%s", annotation.simpleName, stackTrace)
    }
}