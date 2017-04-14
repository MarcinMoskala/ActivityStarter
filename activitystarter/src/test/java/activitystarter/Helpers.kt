@file:Suppress("IllegalIdentifier")

package activitystarter

import activitystarter.wrapper.WrapperManager
import activitystarter.wrapper.WrapperManagerBuildTest
import android.accounts.Account
import android.os.Parcelable
import org.junit.Assert
import org.junit.Test

internal fun assertTypeEquals(class1: Class<*>, class2: Class<*>) {
    Assert.assertEquals(class1.canonicalName, class2.canonicalName)
}

internal fun assertSubtype(clazz: Class<*>, superclass: Class<*>) {
    Assert.assertTrue("$clazz is not sublcass of $superclass.", clazz.isAssignableFrom(superclass))
}

private fun assertThrowsError(f: () -> Unit) {
    try {
        f()
        throw Error("Function didn't throw error.")
    } catch (t: Throwable) {
        // noop
    }
}

class HelpersTests() {

    @Test fun `AssertSubtype simple subtypes are passing`() {
        assertSubtype(Parcelable::class.java, Account::class.java)
        assertSubtype(String::class.java, String::class.java)
    }

}