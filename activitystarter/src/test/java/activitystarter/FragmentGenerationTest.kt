package activitystarter

import org.junit.Test


class FragmentGenerationTest: GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        filePrecessingComparator("fragment/EmptyAnnotated")
    }

    @Test
    fun singleArgGenerationTest() {
        filePrecessingComparator("fragment/SimpleGeneration")
    }

    @Test
    fun multipleOptionalArgGenerationTest() {
        filePrecessingComparator("fragment/MultipleOptional")
    }

    @Test
    fun allGenerationTest() {
        dirPrecessingComparator("fragment")
    }
}