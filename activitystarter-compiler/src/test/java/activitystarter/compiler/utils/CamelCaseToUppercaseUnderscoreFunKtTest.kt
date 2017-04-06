package activitystarter.compiler.utils

import org.junit.Test

import org.junit.Assert.*

class CamelCaseToUppercaseUnderscoreFunKtTest {

    @Test fun `Empty is translated to empty`() {
        assertEquals("", camelCaseToUppercaseUnderscore(""))
    }

    @Test fun `Simple underscore camelcase translations to uppercase underscore`() {
        assertEquals("JA_MAN", camelCaseToUppercaseUnderscore("jaMan"))
        assertEquals("KO_KO_KO", camelCaseToUppercaseUnderscore("koKoKo"))
    }

}