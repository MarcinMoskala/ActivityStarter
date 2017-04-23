package activitystarter.compiler

import activitystarter.compiler.codegeneration.getPutArgumentToIntentMethodName
import activitystarter.compiler.model.param.ParamType
import org.junit.Assert
import org.junit.Test

class BindeingHelpersTest {

    @Test
    fun `For simple types Intent put is putExtra`() {
        val exampleSimpleTypes = listOf(ParamType.Boolean, ParamType.BooleanArray, ParamType.Byte, ParamType.ByteArray, ParamType.Double, ParamType.Short)
        for(simpleType in exampleSimpleTypes) {
            Assert.assertEquals("putExtra", getPutArgumentToIntentMethodName(simpleType))
        }
    }

    @Test
    fun `For ArrayList type Intent put is specific`() {
        val arrayTypeToPutFunctionMap = mapOf(
                ParamType.IntegerArrayList to "putIntegerArrayListExtra",
                ParamType.CharSequenceArrayList to  "putCharSequenceArrayListExtra",
                ParamType.ParcelableArrayListSubtype to "putParcelableArrayListExtra",
                ParamType.StringArrayList to "putStringArrayListExtra"
        )
        for((type, functionName) in arrayTypeToPutFunctionMap) {
            Assert.assertEquals(functionName, getPutArgumentToIntentMethodName(type))
        }
    }
}