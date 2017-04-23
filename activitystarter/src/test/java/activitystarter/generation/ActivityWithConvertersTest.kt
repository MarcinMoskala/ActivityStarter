package activitystarter.generation

import org.junit.Test

class ActivityWithConvertersTest: GenerationTest() {

    @Test
    fun emptyAnnotatedGenerationTest() {
        filePrecessingCorrect("withConverters/ActivityWithIntToLongConverter")
    }
}