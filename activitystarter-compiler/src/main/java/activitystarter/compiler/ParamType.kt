package activitystarter.compiler

import com.squareup.javapoet.TypeName
import javax.lang.model.type.ArrayType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

enum class ParamType {
    String,
    Int,
    Long,
    Float,
    Boolean,
    Double,
    Char,
    Byte,
    Short,
    CharSequence,
    ParcelableArray,
    ParcelableArrayList,
    IntegerArrayList,
    StringArrayList,
    CharSequenceArrayList,
    BooleanArray,
    ByteArray,
    ShortArray,
    CharArray,
    IntArray,
    LongArray,
    FloatArray,
    DoubleArray,
    StringArray,
    CharSequenceArray,
    Bundle,
    IBinder,
    SerializableSubtype,
    ParcelableSubtype;

    companion object {
        val stringTypeName = TypeName.get(kotlin.String::class.java)!!
        val charSequenceTypeName = TypeName.get(kotlin.CharSequence::class.java)!!

        fun fromType(typeMirror: TypeMirror): ParamType? =
                getByKind(typeMirror) ?: getByName(typeMirror)

        private fun getByKind(typeMirror: TypeMirror): ParamType? = when (typeMirror.kind) {
            TypeKind.BOOLEAN -> Boolean
            TypeKind.BYTE -> Byte
            TypeKind.SHORT -> Short
            TypeKind.INT -> Int
            TypeKind.LONG -> Long
            TypeKind.CHAR -> Char
            TypeKind.FLOAT -> Float
            TypeKind.DOUBLE -> Double
            TypeKind.ARRAY -> getArrayType(typeMirror as ArrayType)
            else -> null
        }

        private fun getByName(typeMirror: TypeMirror): ParamType? = when (TypeName.get(typeMirror)) {
            stringTypeName -> String
            charSequenceTypeName -> CharSequence
            else -> null
        }

        private fun getArrayType(arrayType: ArrayType): ParamType? = when (arrayType.toString()) {
            "String[]" -> StringArray
            "CharSequence[]" -> CharSequenceArray
            "int[]" -> IntArray
            "long[]" -> LongArray
            "float[]" -> FloatArray
            "boolean[]" -> BooleanArray
            "double[]" -> DoubleArray
            "char[]" -> CharArray
            "byte[]" -> ByteArray
            "short[]" -> ShortArray
            else -> null
        }
    }
}