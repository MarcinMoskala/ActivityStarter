package activitystarter.generation

import org.junit.Test

class ActivityGenerationTest: GenerationTest() {

    @Test
    fun emptyAnnotatedGenerationTest() {
        filePrecessingComparator("activity/EmptyAnnotated")
    }

    @Test
    fun simpleGenerationTest() {
        filePrecessingComparator("activity/Simple")
    }

    @Test
    fun setterGetterGenerationTest() {
        filePrecessingComparator("activity/SetterGetter")
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