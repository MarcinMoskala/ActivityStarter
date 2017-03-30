package activitystarter.compiler

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
        fun fromType(typeMirror: TypeMirror): ParamType {
            return Boolean
        }
    }
}