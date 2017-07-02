package activitystarter.compiler.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class CamelCaseToUppercaseUnderscoreFunKtTest {

    @Test fun `Empty is translated to empty`() {
        assertEquals("", camelCaseToUppercaseUnderscore(""))
    }

    @Test fun `Simple underscore camelcase translations to uppercase underscore`() {
        assertEquals("JA_MAN", camelCaseToUppercaseUnderscore("jaMan"))
        assertEquals("KO_KO_KO", camelCaseToUppercaseUnderscore("koKoKo"))
    }


    @Test fun `Simple upperscore camelcase translations to uppercase underscore`() {
        assertEquals("JA_MAN", camelCaseToUppercaseUnderscore("JaMan"))
        assertEquals("KO_KO_KO", camelCaseToUppercaseUnderscore("KoKoKo"))
    }

}