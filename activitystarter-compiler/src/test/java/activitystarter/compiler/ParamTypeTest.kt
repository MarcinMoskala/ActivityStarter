package activitystarter.compiler

import activitystarter.compiler.helpers.TypeMirrors
import com.squareup.javapoet.TypeName
import org.junit.Assert
import org.junit.Test
import java.util.*
import javax.lang.model.type.TypeMirror

class ParamTypeTest {

    @Test
    fun `There is class that represents type that include all supported types`() {
        ParamType::class.java
    }

    @Test
    fun `Type mapper is mapping basic types to its equivalents`() {
        assertParamTypeFromTypeMapping(
                ParamType.Int to TypeMirrors.Int,
                ParamType.Long to TypeMirrors.Long,
                ParamType.Float to TypeMirrors.Float,
                ParamType.Boolean to TypeMirrors.Boolean,
                ParamType.Double to TypeMirrors.Double,
                ParamType.Char to TypeMirrors.Char,
                ParamType.Byte to TypeMirrors.Byte,
                ParamType.Short to TypeMirrors.Short
        )
    }

    @Test
    fun `Type mapper is mapping String and CharSequence to its equivalents`() {
        assertParamTypeFromTypeMapping(
                ParamType.String to TypeMirrors.String,
                ParamType.CharSequence to TypeMirrors.CharSequence
        )
    }

    @Test
    fun `Type mapper is mapping Arrays to its equivalents`() {
        assertParamTypeFromTypeMapping(
                ParamType.IntArray to TypeMirrors.IntArray,
                ParamType.LongArray to TypeMirrors.LongArray,
                ParamType.FloatArray to TypeMirrors.FloatArray,
                ParamType.BooleanArray to TypeMirrors.BooleanArray,
                ParamType.DoubleArray to TypeMirrors.DoubleArray,
                ParamType.CharArray to TypeMirrors.CharArray,
                ParamType.ByteArray to TypeMirrors.ByteArray,
                ParamType.ShortArray to TypeMirrors.ShortArray
        )
    }

    @Test
    fun `Type mapper is mapping ArrayLists to its equivalents`() {
        assertParamTypeFromTypeMapping(
                ParamType.IntegerArrayList to TypeMirrors.IntegerArrayList,
                ParamType.StringArrayList to TypeMirrors.StringArrayList,
                ParamType.CharSequenceArrayList to TypeMirrors.CharSequenceArrayList
        )
    }

    fun assertParamTypeFromTypeMapping(vararg paramTypeToTypeMirror: Pair<ParamType, TypeMirror>) {
        for((paramType, typeMirror) in paramTypeToTypeMirror) {
            Assert.assertEquals(paramType, ParamType.fromType(typeMirror))
        }
    }
}