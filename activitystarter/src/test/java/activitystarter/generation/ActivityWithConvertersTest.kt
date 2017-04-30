package activitystarter.generation

import activitystarter.compiler.error.Errors
import org.junit.Test

@Suppress("IllegalIdentifier")
class ActivityWithConvertersTest: GenerationTest() {

    @Test
    fun intToLongConversionTest() {
        filePrecessingComparator("withConverters/ActivityWithIntToLongConverter")
    }

    @Test
    fun toParcelableConversionTest() {
        filePrecessingComparator("withConverters/ActivityWithObjectToParcelableConverter")
    }

    @Test
    fun `Parcelar is throwing error when packed object is not implementing interface`() {
        filePrecessingCheckError("shouldThrowError/ActivityWithObjectToParcelableWithoutInterfaceConverter", Errors.notSupportedType)
    }
}