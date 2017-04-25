package activitystarter.compiler.issubtype

import org.junit.Test

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
        ParamProcessor.compile(
                paramTypeName = A::class.java.simpleName,
                paramTypeImport = "activitystarter.compiler.issubtype.*",
                processor = IsSubtypeHelperProcessor(B::class.java.canonicalName))
    }

    inline fun <reified A : Any, reified B : Any> assertIsNotSubtypeOf() {
        ParamProcessor.compileAndExpectError(
                paramTypeName = A::class.java.simpleName,
                paramTypeImport = "activitystarter.compiler.issubtype.*",
                processor = IsSubtypeHelperProcessor(B::class.java.canonicalName),
                errorContaining = IsSubtypeHelperProcessor.notSubtypeError
        )
    }

}