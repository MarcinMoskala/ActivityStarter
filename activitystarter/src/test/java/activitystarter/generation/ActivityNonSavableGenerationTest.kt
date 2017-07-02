package activitystarter.generation

import org.junit.Test

class ActivityNonSavableGenerationTest: GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        filePrecessingComparator("activityNonSavable/EmptyAnnotated")
    }

    @Test
    fun singleArgGenerationTest() {
        filePrecessingComparator("activityNonSavable/Simple")
    }

    @Test
    fun setterGetterGenerationTest() {
        filePrecessingComparator("activityNonSavable/SetterGetter")
    }

    @Test
    fun optionalArgGenerationTest() {
        filePrecessingComparator("activityNonSavable/Optional")
    }

    @Test
    fun multipleOptionalArgGenerationTest() {
        filePrecessingComparator("activityNonSavable/MultipleOptional")
    }

    @Test
    fun conflictedOptionalArgGenerationTest() {
        filePrecessingComparator("activityNonSavable/ConflictedOptional")
    }

    @Test
    fun allGenerationTest() {
        dirPrecessingComparator("activityNonSavable")
    }
}