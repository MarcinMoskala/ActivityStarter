package activitystarter.compiler.issubtype

import activitystarter.compiler.issubtype.IsSubtypeHelperProcessor
import com.google.common.truth.Truth
import com.google.testing.compile.CompileTester
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory
import com.google.testing.compile.JavaSourcesSubject
import org.junit.Test
import javax.tools.JavaFileObject

class IsSubtypeOfTest {

    @Test
    fun subtypesTest() {
        assertIsSubtypeOf<D, A>()
        assertIsSubtypeOf<F, A>()
    }

    @Test
    fun interfaceTest() {
        assertIsSubtypeOf<E, B>()
        assertIsSubtypeOf<F, B>()
        assertIsSubtypeOf<G, B>()
        assertIsSubtypeOf<F, E>()
        assertIsSubtypeOf<G, E>()
    }

    @Test
    fun itselfTest() {
        assertIsSubtypeOf<A, A>()
        assertIsSubtypeOf<B, B>()
        assertIsSubtypeOf<C, C>()
        assertIsSubtypeOf<D, D>()
        assertIsSubtypeOf<E, E>()
        assertIsSubtypeOf<F, F>()
        assertIsSubtypeOf<G, G>()
    }

    @Test
    fun notASubtypeReversedInheritenceTest() {
        assertIsNotSubtypeOf<A, D>()
        assertIsNotSubtypeOf<A, F>()
        assertIsNotSubtypeOf<B, E>()
        assertIsNotSubtypeOf<B, F>()
        assertIsNotSubtypeOf<E, F>()
    }

    @Test
    fun notASubtypeNotConnectedTest() {
        assertIsNotSubtypeOf<E, G>()
        assertIsNotSubtypeOf<A, B>()
        assertIsNotSubtypeOf<D, E>()
        assertIsNotSubtypeOf<E, D>()
    }

    inline fun <reified A : Any, reified B : Any> assertIsSubtypeOf() {
        subtypeComparatorCode<A, B>().compilesWithoutError()
    }

    inline fun <reified A : Any, reified B : Any> assertIsNotSubtypeOf() {
        subtypeComparatorCode<A, B>().failsToCompile().withErrorContaining(IsSubtypeHelperProcessor.notSubtypeError)
    }

    inline fun <reified A : Any, reified B : Any> subtypeComparatorCode(): CompileTester {
        val source = JavaFileObjects.forSourceString("mm.Main", """
package mm;
import activitystarter.compiler.issubtype.*;
import activitystarter.Arg;

public class Main {
    @Arg ${A::class.java.simpleName} a;
}
        """)
        return Truth.assertAbout<JavaSourcesSubject.SingleSourceAdapter, JavaFileObject, JavaSourceSubjectFactory>(JavaSourceSubjectFactory.javaSource())
                .that(source)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(IsSubtypeHelperProcessor(B::class.java.canonicalName))!!
    }
}