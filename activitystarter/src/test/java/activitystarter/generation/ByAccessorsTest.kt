package activitystarter.generation

import activitystarter.compiler.error.Errors
import org.junit.Test

@Suppress("IllegalIdentifier")
class ByAccessorsTest : GenerationTest() {

    @Test
    fun singleGetterTest() {
        filePrecessingComparator("byAccessor/Single")
    }

    @Test
    fun getterSetterTest() {
        filePrecessingComparator("byAccessor/GetterSetter")
    }
}