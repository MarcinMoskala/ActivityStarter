package activitystarter.compiler

import org.junit.Test

import org.junit.Assert.*
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

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