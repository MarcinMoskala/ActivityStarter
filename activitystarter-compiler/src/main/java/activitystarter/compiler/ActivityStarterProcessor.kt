package activitystarter.compiler

import activitystarter.ActivityStarterConfig
import activitystarter.Arg
import activitystarter.MakeActivityStarter
import activitystarter.compiler.error.Errors
import activitystarter.compiler.error.error
import activitystarter.compiler.error.messanger
import activitystarter.compiler.model.ProjectModel
import activitystarter.compiler.model.classbinding.ClassBindingFactory
import activitystarter.wrapping.ArgWrapper
import com.google.auto.service.AutoService
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

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
        val projectModel = ProjectModel(
                converters = getConvertersFromConfig(env),
                classesToProcess = getClassesToMakeStarters(env)
                        .mapNotNull { ClassBindingFactory(it).create() }
                        .toSet()
        )
        processProject(projectModel)
        return true
    }

    private fun getConvertersFromConfig(env: RoundEnvironment): List<KClass<out ArgWrapper<*, *>>> {
        val configs = env.processAnnotatedElements<ActivityStarterConfig, ActivityStarterConfig> { element ->
            element.getAnnotation(ActivityStarterConfig::class.java)
        }
        return when(configs.size) {
            0 -> emptyList()
            1 -> configs.single().converters.toList()
            else -> { error(Errors.moreThenOneConfig); throw Error(Errors.moreThenOneConfig) }
        }
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

    private fun processProject(model: ProjectModel) {
        for (classBinding in model.classesToProcess) {
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