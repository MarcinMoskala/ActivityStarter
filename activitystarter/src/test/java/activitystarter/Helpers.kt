@file:Suppress("IllegalIdentifier")

package activitystarter

import activitystarter.Helpers.isSubtype
import android.accounts.Account
import android.os.Parcelable
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

internal fun assertTypeEquals(class1: Class<*>, class2: Class<*>) {
    Assert.assertEquals(class1.canonicalName, class2.canonicalName)
}

internal fun assertSubtype(subclass: Class<*>, superclass: Class<*>) {
    assertTrue("$subclass is not sublcass of $superclass.", isSubtype(subclass, superclass))
}

internal fun assertThrowsError(f: () -> Unit) {
    try {
        f()
        throw Error("Function didn't throw error.")
    } catch (t: Throwable) {
        // noop
    }
}

class HelpersTests() {

    @Test fun `assertSubtype simple subtypes are passing`() {
        assertSubtype(Parcelable::class.java, Account::class.java)
        assertSubtype(String::class.java, String::class.java)
    }

    @Test fun `assertSubtype not subtypes are not passing`() {
        assertThrowsError { assertSubtype(String::class.java, Int::class.java) }
        assertThrowsError { assertSubtype(Long::class.java, Int::class.java) }
    }

    @Test fun `assertSubtype reversed subtypes are not passing`() {
        assertThrowsError { assertSubtype(Account::class.java, Parcelable::class.java) }
    }

    @Test fun `assertThrowsError is passing when failing assert is inside`() {
        val message = "Fake error that should be catched by assertThrowsError"
        assertThrowsError { throw Error(message) }
        assertThrowsError { assertTrue(message, false) }
    }

    @Test fun `assertThrowsError throwing error when there is no error inside`() {
        assertThrowsError { assertThrowsError {  } }
    }
}