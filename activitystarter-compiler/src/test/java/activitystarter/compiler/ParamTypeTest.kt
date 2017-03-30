package activitystarter.compiler

import activitystarter.compiler.helpers.TypeMirrors
import com.squareup.javapoet.TypeName
import org.junit.Assert
import org.junit.Test

class ParamTypeTest {

    @Test
    fun `There is class that represents type that include all supported types`() {
        ParamType::class.java
    }

    @Test
    fun `Type mapper is mapping basic types to its equivalent`() {
        val mirrorToParamTypeMap = mapOf(
                ParamType.Int to TypeMirrors.Int,
                ParamType.Long to TypeMirrors.Long,
                ParamType.Float to TypeMirrors.Float,
                ParamType.Boolean to TypeMirrors.Boolean,
                ParamType.Double to TypeMirrors.Double,
                ParamType.Char to TypeMirrors.Char,
                ParamType.Byte to TypeMirrors.Byte,
                ParamType.Short to TypeMirrors.Short
        )
        for((paramType, typeMirror) in mirrorToParamTypeMap) {
            Assert.assertEquals(paramType, ParamType.fromType(typeMirror))
        }
    }

    @Test
    fun `Type mapper is mapping String and CharSequence to its equivalent`() {
        Assert.assertEquals(ParamType.String, ParamType.fromType(TypeMirrors.String))
        Assert.assertEquals(ParamType.CharSequence, ParamType.fromType(TypeMirrors.CharSequence))
    }
}