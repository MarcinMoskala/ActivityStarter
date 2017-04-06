package activitystarter.compiler.utils

import org.junit.Test

import org.junit.Assert.*

class CamelCaseToUppercaseUnderscoreFunKtTest {

    @Test fun `Empty is translated to empty`() {
        assertEquals("", camelCaseToUppercaseUnderscore(""))
    }

}