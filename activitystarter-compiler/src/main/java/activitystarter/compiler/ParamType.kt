package activitystarter.compiler

import com.squareup.javapoet.TypeName
import com.sun.tools.javac.code.Type
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

    IntegerArrayList,
    StringArrayList,
    CharSequenceArrayList,

    ParcelableSubtype,
    SerializableSubtype,
    ParcelableArraySubtype,
    ParcelableArrayListSubtype;

    companion object {
        val stringTypeName = TypeName.get(kotlin.String::class.java)!!
        val charSequenceTypeName = TypeName.get(kotlin.CharSequence::class.java)!!
        val arrayListRegex by lazy { """ArrayList<([\w.]*)>""".toRegex() }

        fun fromType(typeMirror: TypeMirror): ParamType? =
                getByKind(typeMirror) ?:
                        getByName(typeMirror) ?:
                        getArrayList(typeMirror) ?:
                        getBySupertype(typeMirror)

        private fun getByKind(typeMirror: TypeMirror): ParamType? = when (typeMirror.kind) {
            TypeKind.BOOLEAN -> Boolean
            TypeKind.BYTE -> Byte
            TypeKind.SHORT -> Short
            TypeKind.INT -> Int
            TypeKind.LONG -> Long
            TypeKind.CHAR -> Char
            TypeKind.FLOAT -> Float
            TypeKind.DOUBLE -> Double
            TypeKind.ARRAY -> getArrayType(typeMirror as? Type.ArrayType)
            else -> null
        }

        private fun getByName(typeMirror: TypeMirror): ParamType? = when (TypeName.get(typeMirror)) {
            stringTypeName -> String
            charSequenceTypeName -> CharSequence
            else -> null
        }

        private fun getArrayType(arrayType: Type.ArrayType?): ParamType? {
            val elementType = arrayType?.elemtype ?: return null
            val basicArrayElementType = getArrayParamTypeForElementType(elementType)
            return basicArrayElementType ?: getComplexArrayParamTypeForElementType(elementType)
        }

        private fun getArrayParamTypeForElementType(elementType: Type): ParamType? = when (elementType.toString()) {
            "java.lang.String" -> StringArray
            "java.lang.CharSequence" -> CharSequenceArray
            "int" -> IntArray
            "long" -> LongArray
            "float" -> FloatArray
            "boolean" -> BooleanArray
            "double" -> DoubleArray
            "char" -> CharArray
            "byte" -> ByteArray
            "short" -> ShortArray
            else -> null
        }

        private fun getComplexArrayParamTypeForElementType(elementType: Type): ParamType? = when {
            elementType.isSubtypeOfType("android.os.Parcelable") -> ParcelableArraySubtype
            else -> null
        }

        private fun getArrayList(typeMirror: TypeMirror): ParamType? = when (typeMirror.toString()) {
            "java.util.ArrayList<java.lang.Integer>" -> IntegerArrayList
            "java.util.ArrayList<java.lang.String>" -> StringArrayList
            "java.util.ArrayList<java.lang.CharSequence>" -> CharSequenceArrayList
            else -> null
        }

        private fun getBySupertype(typeMirror: TypeMirror): ParamType? = when {
            typeMirror.isSubtypeOfType("android.os.Parcelable") -> ParcelableSubtype
            typeMirror.isArrayListWithSubtypesOf("android.os.Parcelable") -> ParcelableArrayListSubtype
            typeMirror.isSubtypeOfType("java.io.Serializable") -> SerializableSubtype
            else -> null
        }

        private fun TypeMirror.isArrayListWithSubtypesOf(supertype: kotlin.String): kotlin.Boolean {
            val typeAsString = toString()
            if(!typeAsString.contains(arrayListRegex)) return false
            val elementsType = (this as Type.ClassType).typarams_field[0]
            return elementsType.isSubtypeOfType(supertype)
        }
    }
}
