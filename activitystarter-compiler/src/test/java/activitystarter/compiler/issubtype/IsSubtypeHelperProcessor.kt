package activitystarter.compiler.issubtype

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import activitystarter.compiler.classbinding.ClassBinding
import activitystarter.compiler.getElementType
import activitystarter.compiler.isSubtypeOfType
import activitystarter.compiler.messanger
import com.google.auto.service.AutoService
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
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
                if(!isSubtype) activitystarter.compiler.error(element, notSubtypeError)
            } catch (e: Exception) {
                e.printStackTrace()
                activitystarter.compiler.error(element, otherError)
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