package activitystarter.generation

import activitystarter.compiler.error.Errors
import org.junit.Test

@Suppress("IllegalIdentifier")
class ActivityWithConvertersTest: GenerationTest() {

    @Test
    fun `Configuration with Converter is compiling`() {
        filePrecessingCorrect("withConverters/ActivityWithIntToLongConverter")
        filePrecessingCorrect("withConverters/ActivityWithObjectToParcelableConverter")
    }

    @Test
    fun `Parcelar is throwing error when packed object is not implementing interface`() {
        filePrecessingCheckError("shouldThrowError/ActivityWithObjectToParcelableWithoutInterfaceConverter", Errors.notSupportedType)
    }
}