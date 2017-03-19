package activitystarter.compiler.issubtype

import activitystarter.Arg
import activitystarter.compiler.error
import activitystarter.compiler.getElementType
import activitystarter.compiler.isSubtypeOfType
import activitystarter.compiler.messanger
import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class IsSubtypeHelperProcessor(val ofWhat: String) : AbstractProcessor() {

    private lateinit var filer: Filer

    @Synchronized override fun init(env: ProcessingEnvironment) {
        super.init(env)
        filer = env.filer
        messanger = processingEnv.messager
    }

    override fun getSupportedAnnotationTypes() = listOf<Class<*>>(Arg::class.java)
            .map { it.canonicalName }.toSet()

    override fun process(elements: Set<TypeElement>, env: RoundEnvironment): Boolean {
        for (element in env.getElementsAnnotatedWith(Arg::class.java)) {
            try {
                val isSubtype = getElementType(element).isSubtypeOfType(ofWhat)
                if(!isSubtype) error(element, notSubtypeError)
            } catch (e: Exception) {
                e.printStackTrace()
                error(element, otherError)
            }
        }
        return true
    }

    override fun getSupportedSourceVersion() = SourceVersion.latestSupported()

    companion object {
        val notSubtypeError = "Not a subtype"
        val otherError = "Error"
    }
}