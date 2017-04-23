package activitystarter.generation

import org.junit.Test

@Suppress("IllegalIdentifier")
class ActivityWithConvertersTest: GenerationTest() {

    @Test
    fun `Configuration with Converter is compiling`() {
        filePrecessingCorrect("withConverters/ActivityWithIntToLongConverter")
    }
}