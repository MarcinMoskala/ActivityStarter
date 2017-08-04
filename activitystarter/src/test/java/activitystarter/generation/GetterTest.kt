package activitystarter.generation

import activitystarter.compiler.error.Errors
import org.junit.Test

@Suppress("IllegalIdentifier")
class GetterTest: GenerationTest() {

    @Test
    fun singleGetterTest() {
        filePrecessingComparator("getter/Single")
    }
}