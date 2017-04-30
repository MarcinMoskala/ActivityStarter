package activitystarter.generation

import org.junit.Test

class ActivityForResultGenerationTest: GenerationTest() {

    @Test
    fun emptyAnnotatedGenerationTest() {
        filePrecessingComparator("activityForResult/EmptyAnnotated")
    }

    @Test
    fun simpleGenerationTest() {
        filePrecessingComparator("activityForResult/Simple")
    }

    @Test
    fun setterGetterGenerationTest() {
        filePrecessingComparator("activityForResult/SetterGetter")
    }

    @Test
    fun optionalArgGenerationTest() {
        filePrecessingComparator("activityForResult/Optional")
    }

    @Test
    fun multipleOptionalArgGenerationTest() {
        filePrecessingComparator("activityForResult/MultipleOptional")
    }

    @Test
    fun conflictedOptionalArgGenerationTest() {
        filePrecessingComparator("activityForResult/ConflictedOptional")
    }

    @Test
    fun allGenerationTest() {
        dirPrecessingComparator("activityForResult")
    }
}