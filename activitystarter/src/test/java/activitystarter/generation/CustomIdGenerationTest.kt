package activitystarter.generation

import org.junit.Test

class CustomIdGenerationTest: GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        filePrecessingComparator("customId/Simple")
    }

    @Test
    fun optionalGenerationTest() {
        filePrecessingComparator("customId/Optional")
    }

    @Test
    fun multipleOptionalGenerationTest() {
        filePrecessingComparator("customId/MultipleOptional")
    }

    @Test
    fun allGenerationTest() {
        dirPrecessingComparator("customId")
    }
}