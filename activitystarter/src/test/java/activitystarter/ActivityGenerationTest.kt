package activitystarter

import org.junit.Test

class ActivityGenerationTest: GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        filePrecessingComparator("activity/EmptyAnnotated")
    }

    @Test
    fun singleArgGenerationTest() {
        filePrecessingComparator("activity/SimpleGeneration")
    }

    @Test
    fun setterGetterGenerationTest() {
        filePrecessingComparator("activity/SetterGetterGeneration")
    }

    @Test
    fun optionalArgGenerationTest() {
        filePrecessingComparator("activity/Optional")
    }

    @Test
    fun multipleOptionalArgGenerationTest() {
        filePrecessingComparator("activity/MultipleOptional")
    }

    @Test
    fun conflictedOptionalArgGenerationTest() {
        filePrecessingComparator("activity/ConflictedOptional")
    }

    @Test
    fun allGenerationTest() {
        dirPrecessingComparator("activity")
    }
}