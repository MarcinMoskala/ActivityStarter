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
}