package activitystarter.compiler.codegeneration

import activitystarter.compiler.param.ParamType
import activitystarter.compiler.utils.isSubtypeOfType
import com.google.auto.common.MoreElements.getPackage
import com.squareup.javapoet.ClassName.get
import com.squareup.javapoet.TypeName
import javax.lang.model.type.TypeMirror

fun getBindingClassName(enclosingElement: javax.lang.model.element.TypeElement): com.squareup.javapoet.ClassName {
    val packageName = getPackage(enclosingElement).qualifiedName.toString()
    val className = enclosingElement.qualifiedName.toString().substring(packageName.length + 1)
    return get(packageName, className + "Starter")
}

// TODO Rest
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
    ParamType.ParcelableArraySubtype -> "putParcelableArray"
    ParamType.ParcelableArrayListSubtype -> "putParcelableArrayList"
}

fun getBundleGetter(bundleName: String, paramType: ParamType, typeName: TypeName, keyName: String): String {
    val bundleGetterCall = getBundleGetterCall(paramType)
    val getArgumentValue = "$bundleName.$bundleGetterCall(\"$keyName\")"
    return if(paramType.typeUsedBySupertype()) "($typeName) $getArgumentValue" else getArgumentValue
}

fun getBundleGetterCall(paramType: ParamType) = when (paramType) {
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
    ParamType.ParcelableArraySubtype -> "getParcelableArray"
    ParamType.ParcelableArrayListSubtype -> "getParcelableArrayList"
}

fun getPutArgumentToIntentMethodName(paramType: ParamType) = "putExtra"

fun getIntentGetterFor(paramType: ParamType, typeName: TypeName, keyName: String): String {
    val getter = getIntentGetterForParamType(paramType, keyName)
    val getArgumentValue = "intent.$getter"
    return if(paramType.typeUsedBySupertype()) "($typeName) $getArgumentValue" else getArgumentValue
}

private fun getIntentGetterForParamType(paramType: ParamType, keyName: String) = when (paramType) {
    ParamType.String -> "getStringExtra(\"$keyName\")"
    ParamType.Int -> "getIntExtra(\"$keyName\", 0)"
    ParamType.Long -> "getLongExtra(\"$keyName\", 0L)"
    ParamType.Float -> "getFloatExtra(\"$keyName\", -0F)"
    ParamType.Boolean -> "getBooleanExtra(\"$keyName\", false)"
    ParamType.Double -> "getDoubleExtra(\"$keyName\", -0D)"
    ParamType.Char -> "getCharExtra(\"$keyName\", '\\u0000')"
    ParamType.Byte -> "getByteExtra(\"$keyName\", (byte) 0)"
    ParamType.Short -> "getShortExtra(\"$keyName\", (short) 0)"
    ParamType.CharSequence -> "getCharSequenceExtra(\"$keyName\")"
    ParamType.BooleanArray -> "getBooleanArrayExtra(\"$keyName\")"
    ParamType.ByteArray -> "getByteArrayExtra(\"$keyName\")"
    ParamType.ShortArray -> "getShortArrayExtra(\"$keyName\")"
    ParamType.CharArray -> "getCharArrayExtra(\"$keyName\")"
    ParamType.IntArray -> "getIntArrayExtra(\"$keyName\")"
    ParamType.LongArray -> "getLongArrayExtra(\"$keyName\")"
    ParamType.FloatArray -> "getFloatArrayExtra(\"$keyName\")"
    ParamType.DoubleArray -> "getDoubleArrayExtra(\"$keyName\")"
    ParamType.StringArray -> "getStringArrayExtra(\"$keyName\")"
    ParamType.CharSequenceArray -> "getCharSequenceArrayExtra(\"$keyName\")"
    ParamType.IntegerArrayList -> "getIntegerArrayListExtra(\"$keyName\")"
    ParamType.StringArrayList -> "getStringArrayListExtra(\"$keyName\")"
    ParamType.CharSequenceArrayList -> "getCharSequenceArrayListExtra(\"$keyName\")"
    ParamType.ParcelableSubtype -> "getParcelableExtra(\"$keyName\")"
    ParamType.SerializableSubtype -> "getSerializableExtra(\"$keyName\")"
    ParamType.ParcelableArraySubtype -> "getParcelableArrayExtra(\"$keyName\")"
    ParamType.ParcelableArrayListSubtype -> "getParcelableArrayListExtra(\"$keyName\")"
}