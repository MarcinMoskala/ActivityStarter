package activitystarter

import org.junit.Test

class ServiceGenerationTest: GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        filePrecessingComparator("service/EmptyAnnotated")
    }

    @Test
    fun singleArgGenerationTest() {
        filePrecessingComparator("service/SimpleGeneration")
    }

    @Test
    fun allGenerationTest() {
        dirPrecessingComparator("service")
    }
}