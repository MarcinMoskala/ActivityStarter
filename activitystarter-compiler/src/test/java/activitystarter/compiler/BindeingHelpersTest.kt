package activitystarter.compiler

import activitystarter.compiler.codegeneration.getPutArgumentToIntentMethodName
import activitystarter.compiler.param.ParamType
import org.junit.Assert
import org.junit.Test

class BindeingHelpersTest {

    @Test
    fun `For simple funtions Intent put is putExtra`() {
        val exampleSimpleTypes = listOf(ParamType.Boolean, ParamType.BooleanArray, ParamType.Byte, ParamType.ByteArray, ParamType.Double, ParamType.Short)
        for(simpleType in exampleSimpleTypes) {
            Assert.assertEquals("putExtra", getPutArgumentToIntentMethodName(simpleType))
        }
    }
}