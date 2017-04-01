package activitystarter

import activitystarter.compiler.error.Errors
import org.junit.Test

class GenerationErrorTest : GenerationTest() {

    @Test
    fun simpleGenerationErrorTest() {
        filePrecessingCheckError("shouldThrowError/ActivityPrivateFIeld", Errors.inaccessibleField)
    }

    @Test
    fun serviceParcelableFieldErrorTest() {
        filePrecessingCheckError("shouldThrowError/ServiceParcelableField", Errors.notBasicTypeInReceiver)
    }

    @Test
    fun privateClassErrorTest() {
        filePrecessingCheckError("shouldThrowError/PrivateClass", Errors.privateClass)
    }

    @Test
    fun listErrorTest() {
        filePrecessingCheckError("shouldThrowError/List", Errors.notSupportedType)
    }

    @Test
    fun setterOnlyGenerationErrorTest() {
        filePrecessingCheckError("shouldThrowError/SetterOnly", Errors.inaccessibleField)
    }

    @Test
    fun getterOnlyGenerationErrorTest() {
        filePrecessingCheckError("shouldThrowError/GetterOnly", Errors.inaccessibleField)
    }
}