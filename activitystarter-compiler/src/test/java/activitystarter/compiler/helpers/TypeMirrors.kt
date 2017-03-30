package activitystarter.compiler.helpers

import activitystarter.Arg
import activitystarter.compiler.getElementType
import activitystarter.compiler.issubtype.IsSubtypeHelperProcessor
import activitystarter.compiler.messanger
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
import javax.lang.model.type.TypeMirror
import javax.tools.JavaFileObject

object TypeMirrors {
    val String by lazy { getTypeMirror<String>() }
    val Int by lazy { getTypeMirror<Int>() }
    val Boolean by lazy { getTypeMirror<Boolean>() }
    val Char by lazy { getTypeMirror<Char>() }
    val Byte by lazy { getTypeMirror<Byte>() }
    val Short by lazy { getTypeMirror<Short>() }
    val Long by lazy { getTypeMirror<Long>() }
    val Double by lazy { getTypeMirror<Double>() }
    val Float by lazy { getTypeMirror<Float>() }
    val IntArray by lazy { getTypeMirror("int[]") }

    private inline fun <reified T: Any> getTypeMirror(): TypeMirror {
        val clazz = T::class
        val name = clazz.javaPrimitiveType?.name ?: clazz.simpleName!!
        return getTypeMirror(name)
    }

    private fun getTypeMirror(typeName: String): TypeMirror {
        val source = JavaFileObjects.forSourceString("mm.Main", """
package mm;
import activitystarter.Arg;

public class Main {
    @Arg $typeName a;
}
        """)
        var type: TypeMirror? = null
        Truth.assertAbout<JavaSourcesSubject.SingleSourceAdapter, JavaFileObject, JavaSourceSubjectFactory>(JavaSourceSubjectFactory.javaSource())
                .that(source)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(GetTypeMirrorHelperProcessor({ type = it }))!!
                .compilesWithoutError()
        return type!!
    }

    class GetTypeMirrorHelperProcessor(val typeCallback: (TypeMirror)->Unit) : ParamProcessor() {

        override fun onParamFound(element: Element) {
            typeCallback(getElementType(element))
        }
    }
}