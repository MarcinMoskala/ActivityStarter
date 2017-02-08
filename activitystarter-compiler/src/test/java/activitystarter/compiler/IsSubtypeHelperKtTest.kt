package activitystarter.compiler

import org.junit.Assert.assertTrue
import org.junit.Test

class IsSubtypeHelperKtTest {

    abstract class A()
    interface B
    abstract class C()

    open class D(): A(), B
    interface E: B

    class F: E, D()
    class G: C()

    @Test
    fun testIsSubtypeOf() {
        // TODO Mock TypeMirror to test isSubtypeOfType
        assertTrue(true)
    }
}