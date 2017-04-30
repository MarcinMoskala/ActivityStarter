package activitystarter.compiler.generation

import activitystarter.compiler.model.param.ParamType
import com.google.auto.common.MoreElements.getPackage
import com.squareup.javapoet.ClassName.get

fun getBindingClassName(enclosingElement: javax.lang.model.element.TypeElement): com.squareup.javapoet.ClassName {
    val packageName = getPackage(enclosingElement).qualifiedName.toString()
    val className = enclosingElement.qualifiedName.toString().substring(packageName.length + 1)
    return get(packageName, className + "Starter")
}

fun getBundleSetterFor(type: ParamType) = when (type) {
    ParamType.String -> "putString"
    ParamType.Int -> "putInt"
    ParamType.Long -> "putLong"
    ParamType.Float -> "putFloat"
    ParamType.Boolean -> "putBoolean"
    ParamType.Double -> "putDouble"
    ParamType.Char -> "putChar"
    ParamType.Byte -> "putByte"
    ParamType.Short -> "putShort"
    ParamType.CharSequence -> "putCharSequence"
    ParamType.BooleanArray -> "putBooleanArray"
    ParamType.ByteArray -> "putByteArray"
    ParamType.ShortArray -> "putShortArray"
    ParamType.CharArray -> "putCharArray"
    ParamType.IntArray -> "putIntArray"
    ParamType.LongArray -> "putLongArray"
    ParamType.FloatArray -> "putFloatArray"
    ParamType.DoubleArray -> "putDoubleArray"
    ParamType.StringArray -> "putStringArray"
    ParamType.CharSequenceArray -> "putCharSequenceArray"
    ParamType.IntegerArrayList -> "putIntegerArrayList"
    ParamType.StringArrayList -> "putStringArrayList"
    ParamType.CharSequenceArrayList -> "putCharSequenceArrayList"
    ParamType.ParcelableSubtype -> "putParcelable"
    ParamType.SerializableSubtype -> "putSerializable"
    ParamType.ParcelableArrayListSubtype -> "putParcelableArrayList"
    else -> throw Error("Type not supported")
}

fun getBundleGetter(bundleName: String, paramType: ParamType, keyName: String): String {
    val bundleGetterCall = activitystarter.compiler.generation.getBundleGetterCall(paramType)
    return "$bundleName.$bundleGetterCall($keyName)"
}

private fun getBundleGetterCall(paramType: ParamType) = when (paramType) {
    ParamType.String -> "getString"
    ParamType.Int -> "getInt"
    ParamType.Long -> "getLong"
    ParamType.Float -> "getFloat"
    ParamType.Boolean -> "getBoolean"
    ParamType.Double -> "getDouble"
    ParamType.Char -> "getChar"
    ParamType.Byte -> "getByte"
    ParamType.Short -> "getShort"
    ParamType.CharSequence -> "getCharSequence"
    ParamType.BooleanArray -> "getBooleanArray"
    ParamType.ByteArray -> "getByteArray"
    ParamType.ShortArray -> "getShortArray"
    ParamType.CharArray -> "getCharArray"
    ParamType.IntArray -> "getIntArray"
    ParamType.LongArray -> "getLongArray"
    ParamType.FloatArray -> "getFloatArray"
    ParamType.DoubleArray -> "getDoubleArray"
    ParamType.StringArray -> "getStringArray"
    ParamType.CharSequenceArray -> "getCharSequenceArray"
    ParamType.IntegerArrayList -> "getIntegerArrayList"
    ParamType.StringArrayList -> "getStringArrayList"
    ParamType.CharSequenceArrayList -> "getCharSequenceArrayList"
    ParamType.ParcelableSubtype -> "getParcelable"
    ParamType.SerializableSubtype -> "getSerializable"
    ParamType.ParcelableArrayListSubtype -> "getParcelableArrayList"
    else -> throw Error("Type not supported")
}

fun getPutArgumentToIntentMethodName(paramType: ParamType) = when(paramType) {
    ParamType.IntegerArrayList -> "putIntegerArrayListExtra"
    ParamType.CharSequenceArrayList -> "putCharSequenceArrayListExtra"
    ParamType.ParcelableArrayListSubtype -> "putParcelableArrayListExtra"
    ParamType.StringArrayList -> "putStringArrayListExtra"
    else -> "putExtra"
}

fun getIntentGetterFor(paramType: ParamType, key: String): String {
    val getter = getIntentGetterForParamType(paramType, key)
    return "intent.$getter"
}

private fun getIntentGetterForParamType(paramType: ParamType, key: String) = when (paramType) {
    ParamType.String -> "getStringExtra($key)"
    ParamType.Int -> "getIntExtra($key, 0)"
    ParamType.Long -> "getLongExtra($key, 0L)"
    ParamType.Float -> "getFloatExtra($key, -0F)"
    ParamType.Boolean -> "getBooleanExtra($key, false)"
    ParamType.Double -> "getDoubleExtra($key, -0D)"
    ParamType.Char -> "getCharExtra($key, '\\u0000')"
    ParamType.Byte -> "getByteExtra($key, (byte) 0)"
    ParamType.Short -> "getShortExtra($key, (short) 0)"
    ParamType.CharSequence -> "getCharSequenceExtra($key)"
    ParamType.BooleanArray -> "getBooleanArrayExtra($key)"
    ParamType.ByteArray -> "getByteArrayExtra($key)"
    ParamType.ShortArray -> "getShortArrayExtra($key)"
    ParamType.CharArray -> "getCharArrayExtra($key)"
    ParamType.IntArray -> "getIntArrayExtra($key)"
    ParamType.LongArray -> "getLongArrayExtra($key)"
    ParamType.FloatArray -> "getFloatArrayExtra($key)"
    ParamType.DoubleArray -> "getDoubleArrayExtra($key)"
    ParamType.StringArray -> "getStringArrayExtra($key)"
    ParamType.CharSequenceArray -> "getCharSequenceArrayExtra($key)"
    ParamType.IntegerArrayList -> "getIntegerArrayListExtra($key)"
    ParamType.StringArrayList -> "getStringArrayListExtra($key)"
    ParamType.CharSequenceArrayList -> "getCharSequenceArrayListExtra($key)"
    ParamType.ParcelableSubtype -> "getParcelableExtra($key)"
    ParamType.SerializableSubtype -> "getSerializableExtra($key)"
    ParamType.ParcelableArrayListSubtype -> "getParcelableArrayListExtra($key)"
    else -> throw Error("Type not supported")
}