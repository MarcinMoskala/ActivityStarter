package activitystarter

import activitystarter.compiler.Errors
import org.junit.Test

class ActivityGenerationErrorTest : GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        filePrecessingCheckError("shouldThrowError/ActivityPrivateFIeld", Errors.inaccessibleField)
    }

    @Test
    fun serviceParcelableFieldTest() {
        filePrecessingCheckError("shouldThrowError/ServiceParcelableField", Errors.notBasicTypeInReceiver)
    }
}