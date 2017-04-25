package activitystarter.compiler.model.param

import activitystarter.compiler.utils.isSubtypeOfType
import com.squareup.javapoet.TypeName
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
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
    ParcelableArrayListSubtype,

    ObjectSubtype;

    companion object {
        val stringTypeName = TypeName.get(kotlin.String::class.java)!!
        val charSequenceTypeName = TypeName.get(kotlin.CharSequence::class.java)!!
        val integerTypeName = TypeName.get(java.lang.Integer::class.java)!!
        val longTypeName = TypeName.get(java.lang.Long::class.java)!!
        val floatTypeName = TypeName.get(java.lang.Float::class.java)!!
        val doubleTypeName = TypeName.get(java.lang.Double::class.java)!!
        val arrayListRegex by lazy { """ArrayList<([\w.]*)>""".toRegex() }

        fun fromType(typeMirror: TypeMirror): ParamType =
                getByKind(typeMirror) ?:
                        getByName(typeMirror) ?:
                        getArrayList(typeMirror) ?:
                        getBySupertype(typeMirror) ?: ObjectSubtype

        private fun getByKind(typeMirror: TypeMirror): ParamType? = when (typeMirror.kind) {
            TypeKind.BOOLEAN -> Boolean
            TypeKind.BYTE -> Byte
            TypeKind.SHORT -> Short
            TypeKind.INT -> Int
            TypeKind.LONG -> Long
            TypeKind.CHAR -> Char
            TypeKind.FLOAT -> Float
            TypeKind.DOUBLE -> Double
            TypeKind.ARRAY -> getArrayType(typeMirror as? ArrayType)
            else -> null
        }

        private fun getByName(typeMirror: TypeMirror): ParamType? = when (TypeName.get(typeMirror)) {
            stringTypeName -> String
            charSequenceTypeName -> CharSequence
            integerTypeName -> Int
            longTypeName -> Long
            floatTypeName -> Float
            doubleTypeName -> Double
            else -> null
        }

        private fun getArrayType(arrayType: ArrayType?): ParamType? {
            val elementType = arrayType?.componentType ?: return null
            return getArrayParamTypeForElementType(elementType)
        }

        private fun getArrayParamTypeForElementType(elementType: TypeMirror): ParamType? = when (elementType.toString()) {
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
            val elementsType = (this as? DeclaredType)?.typeArguments?.get(0) ?: return false
            return elementsType.isSubtypeOfType(supertype)
        }
    }

    fun typeUsedBySupertype(): kotlin.Boolean = this in listOf(
            ParcelableSubtype,
            SerializableSubtype,
            ObjectSubtype
    )
}
