package activitystarter.generation

import org.junit.Test

class ServiceGenerationTest: GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        filePrecessingComparator("service/EmptyAnnotated")
    }

    @Test
    fun singleArgGenerationTest() {
        filePrecessingComparator("service/Simple")
    }

    @Test
    fun complexArgGenerationTest() {
        filePrecessingComparator("service/Complex")
    }

    @Test
    fun allGenerationTest() {
        dirPrecessingComparator("service")
    }
}