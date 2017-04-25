package activitystarter.compiler

import activitystarter.compiler.helpers.*
import activitystarter.compiler.model.param.ParamType
import com.google.testing.compile.CompilationRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import javax.lang.model.type.TypeMirror

class ParamTypeFromTypeTest {

    @Rule @JvmField val c = CompilationRule()

    @Test
    fun `There is class that represents type that include all supported types`() {
        ParamType::class.java
    }

    @Test
    fun `Type mapper is mapping basic types to its equivalents`() {
        assertParamTypeFromTypeMapping(
                ParamType.Int to c.intTypeMirror,
                ParamType.Long to c.longTypeMirror,
                ParamType.Float to c.floatTypeMirror,
                ParamType.Boolean to c.boolTypeMirror,
                ParamType.Double to c.doubleTypeMirror,
                ParamType.Char to c.charTypeMirror,
                ParamType.Byte to c.byteTypeMirror,
                ParamType.Short to c.shortTypeMirror
        )
    }

    @Test
    fun `Type mapper is mapping String and CharSequence to its equivalents`() {
        assertParamTypeFromTypeMapping(
                ParamType.String to c.stringTypeMirror,
                ParamType.CharSequence to c.charSequenceTypeMirror
        )
    }

    @Test
    fun `Type mapper is mapping Arrays to its equivalents`() {
        assertParamTypeFromTypeMapping(
                ParamType.IntArray to c.intArrayTypeMirror,
                ParamType.LongArray to c.longArrayTypeMirror,
                ParamType.FloatArray to c.floatArrayTypeMirror,
                ParamType.BooleanArray to c.boolArrayTypeMirror,
                ParamType.DoubleArray to c.doubleArrayTypeMirror,
                ParamType.CharArray to c.charArrayTypeMirror,
                ParamType.ByteArray to c.byteArrayTypeMirror,
                ParamType.StringArray to c.stringArrayTypeMirror,
                ParamType.CharSequenceArray to c.charSequenceArrayTypeMirror
        )
    }

    @Test
    fun `Type mapper is mapping ArrayLists to its equivalents`() {
        assertParamTypeFromTypeMapping(
                ParamType.IntegerArrayList to c.integerArrayListTypeMirror,
                ParamType.StringArrayList to c.stringArrayListTypeMirror,
                ParamType.CharSequenceArrayList to c.charSequenceArrayListTypeMirror
        )
    }

    @Test
    fun `Type mapper is mapping Parcelable subtype to ParcelableSubtype ParamType`() {
        assertParamTypeFromTypeMapping(ParamType.ParcelableSubtype to c.subtypeOfParcelableTypeMirror)
    }

    @Test
    fun `Type mapper is mapping Serializable subtype to SerializableSubtype ParamType`() {
        assertParamTypeFromTypeMapping(ParamType.SerializableSubtype to c.subtypeOfSerializableTypeMirror)
    }

    @Test
    fun `Type mapper is mapping only ArrayList of Parcelable subtype to correct ParamType`() {
        assertParamTypeFromTypeMapping(ParamType.ParcelableArrayListSubtype to c.parcelableSubtypeArrayListTypeMirror)
    }

    @Test
    fun `Type mapper is mapping ArrayList of Serializable not to ParcelableSubtypeArrayListSubtype`() {
        Assert.assertNotEquals(ParamType.ParcelableArrayListSubtype, ParamType.fromType(c.subtypeOfSerializableTypeMirror))
    }

    fun assertParamTypeFromTypeMapping(vararg paramTypeToTypeMirror: Pair<ParamType, TypeMirror>) {
        for ((paramType, typeMirror) in paramTypeToTypeMirror) {
            Assert.assertEquals(paramType, ParamType.fromType(typeMirror))
        }
    }
}