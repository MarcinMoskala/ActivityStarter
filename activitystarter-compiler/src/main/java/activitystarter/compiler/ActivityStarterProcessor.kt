package activitystarter.compiler

import activitystarter.ActivityStarterConfig
import activitystarter.Arg
import activitystarter.MakeActivityStarter
import activitystarter.compiler.error.error
import activitystarter.compiler.error.messanger
import activitystarter.compiler.model.ConverterModel
import activitystarter.compiler.model.ProjectConfig
import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.processing.ClassBindingFactory
import activitystarter.compiler.processing.ConverterFaktory
import activitystarter.compiler.processing.getConvertersTypeMirrors
import com.google.auto.service.AutoService
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class ActivityStarterProcessor : AbstractProcessor() {

    private lateinit var filer: Filer

    @Synchronized override fun init(env: ProcessingEnvironment) {
        super.init(env)
        filer = env.filer
        messanger = processingEnv.messager
    }

    override fun getSupportedAnnotationTypes() = listOf<Class<*>>(Arg::class.java, MakeActivityStarter::class.java)
            .map { it.canonicalName }.toSet()

    override fun getSupportedSourceVersion() = SourceVersion.latestSupported()

    override fun process(elements: Set<TypeElement>, env: RoundEnvironment): Boolean {
        val config = ProjectConfig(getConvertersFromConfig(env))
        val classesModels = getClassesToMakeStarters(env)
                        .mapNotNull { ClassBindingFactory(it, config).create() }
                        .toSet()
        processProject(classesModels)
        return true
    }

    private fun getConvertersFromConfig(env: RoundEnvironment): List<ConverterModel> {
        val converters = env.processAnnotatedElements<ActivityStarterConfig, List<ConverterModel>> { element ->
            val configAnnotation = getConvertersTypeMirrors(element)
            ConverterFaktory().create(configAnnotation)
        }
        return converters.flatMap { it }
    }

    private fun getClassesToMakeStarters(env: RoundEnvironment): Set<TypeElement> = setOf<TypeElement>()
            .plus(env.processAnnotatedElements<Arg, TypeElement> { it.enclosingElement as TypeElement })
            .plus(env.processAnnotatedElements<MakeActivityStarter, TypeElement> { it as TypeElement })

    private inline fun <reified T : Annotation, R : Any> RoundEnvironment.processAnnotatedElements(
            process: (Element) -> R
    ): Set<R> = getElementsAnnotatedWith(T::class.java).mapNotNull { element ->
        try {
            process(element)
        } catch (e: Exception) {
            logParsingError(element, T::class.java, e)
            null
        }
    }.toSet()

    private fun processProject(classesToProcess: Set<ClassModel>) {
        for (classBinding in classesToProcess) {
            try {
                classBinding.getClasGeneration().brewJava().writeTo(filer)
            } catch (e: IOException) {
                error("Unable to write binding for typeName %s: %s", classBinding.bindingClassName, e.message ?: "")
            }
        }
    }

    private fun logParsingError(element: Element, annotation: Class<out Annotation>, e: Exception) {
        val stackTrace = StringWriter()
        e.printStackTrace(PrintWriter(stackTrace))
        error(element, "Unable to parse @%s binding.\n\n%s", annotation.simpleName, stackTrace)
    }
}