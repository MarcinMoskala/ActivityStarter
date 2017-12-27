package activitystarter.generation

import org.junit.Test


class FragmentGenerationTest: GenerationTest() {

    @Test
    fun emptyAnnotatedGenerationTest() {
        filePrecessingComparator("fragment/EmptyAnnotated")
    }

    @Test
    fun simpleArgGenerationTest() {
        filePrecessingComparator("fragment/Simple")
    }

    @Test
    fun optionalArgGenerationTest() {
        filePrecessingComparator("fragment/Optional")
    }

    @Test
    fun multipleOptionalArgGenerationTest() {
        filePrecessingComparator("fragment/MultipleOptional")
    }

    @Test
    fun conflictedOptionalArgGenerationTest() {
        filePrecessingComparator("fragment/ConflictedOptional")
    }

    @Test
    fun setterGetterGenerationTest() {
        filePrecessingComparator("fragment/SetterGetter")
    }

    @Test
    fun allGenerationTest() {
        dirPrecessingComparator("fragment")
    }
}