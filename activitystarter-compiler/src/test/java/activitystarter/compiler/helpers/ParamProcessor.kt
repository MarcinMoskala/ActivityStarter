package activitystarter.compiler.helpers

import activitystarter.Arg
import activitystarter.compiler.error.messanger
import com.google.common.truth.Truth
import com.google.testing.compile.CompileTester
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory
import com.google.testing.compile.JavaSourcesSubject
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.JavaFileObject

abstract class ParamProcessor() : AbstractProcessor() {

    private lateinit var filer: Filer

    abstract fun onParamFound(element: Element)

    @Synchronized override fun init(env: ProcessingEnvironment) {
        super.init(env)
        filer = env.filer
        messanger = processingEnv.messager
    }

    override fun getSupportedAnnotationTypes() = listOf<Class<*>>(Arg::class.java).map { it.canonicalName }.toSet()

    override fun process(elements: Set<TypeElement>, env: RoundEnvironment): Boolean {
        for (element in env.getElementsAnnotatedWith(Arg::class.java)) {
            onParamFound(element)
        }
        return true
    }

    override fun getSupportedSourceVersion() = SourceVersion.latestSupported()

    companion object {
        fun compile(paramTypeName: String, processor: ParamProcessor, paramTypeImport: String? = null) {
            getCompileTester(paramTypeName, paramTypeImport, processor).compilesWithoutError()
        }

        fun compileAndExpectError(paramTypeName: String, processor: ParamProcessor, errorContaining: String, paramTypeImport: String? = null) {
            getCompileTester(paramTypeName, paramTypeImport, processor).failsToCompile().withErrorContaining(errorContaining)
        }

        private fun getCompileTester(paramTypeName: String, paramTypeImport: String?, processor: ParamProcessor): CompileTester {
            val extraImport = paramTypeImport?.let { "import $it;" } ?: ""
            val source = JavaFileObjects.forSourceString("mm.Main", """
            |package mm;
            |import activitystarter.Arg;
            |$extraImport
            |
            |public class Main {
            |    @Arg $paramTypeName a;
            |}""".trimMargin())
            return Truth.assertAbout<JavaSourcesSubject.SingleSourceAdapter, JavaFileObject, JavaSourceSubjectFactory>(JavaSourceSubjectFactory.javaSource())
                    .that(source)
                    .withCompilerOptions("-Xlint:-processing")
                    .processedWith(processor)!!
        }
    }
}
