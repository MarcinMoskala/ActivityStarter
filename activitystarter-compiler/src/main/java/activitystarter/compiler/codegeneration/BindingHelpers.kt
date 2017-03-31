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
    val bundleGetterCall = getBundleGetterCall(paramType, keyName)
    val getArgumentValue = "$bundleName.$bundleGetterCall"
    return if(paramType.typeUsedBySupertype()) "($typeName) $getArgumentValue" else getArgumentValue
}

// TODO Rest
fun getBundleGetterCall(paramType: ParamType, keyName: String) = when (paramType) {
    ParamType.String -> "getString(\"$keyName\")"
    ParamType.Int -> "getInt(\"$keyName\", 0)"
    ParamType.Long -> "getLong(\"$keyName\", 0L)"
    ParamType.Float -> "getFloat(\"$keyName\", 0F)"
    ParamType.Boolean -> "getBoolean(\"$keyName\", false)"
    ParamType.Double -> "getDouble(\"$keyName\", 0D)"
    ParamType.Char -> "getChar(\"$keyName\", '\\u0000')"
    ParamType.Byte -> TODO()
    ParamType.Short -> TODO()
    ParamType.CharSequence -> TODO()
    ParamType.BooleanArray -> TODO()
    ParamType.ByteArray -> TODO()
    ParamType.ShortArray -> TODO()
    ParamType.CharArray -> TODO()
    ParamType.IntArray -> TODO()
    ParamType.LongArray -> TODO()
    ParamType.FloatArray -> TODO()
    ParamType.DoubleArray -> TODO()
    ParamType.StringArray -> TODO()
    ParamType.CharSequenceArray -> TODO()
    ParamType.IntegerArrayList -> TODO()
    ParamType.StringArrayList -> TODO()
    ParamType.CharSequenceArrayList -> TODO()
    ParamType.ParcelableSubtype -> TODO()
    ParamType.SerializableSubtype -> TODO()
    ParamType.ParcelableArraySubtype -> TODO()
    ParamType.ParcelableArrayListSubtype -> TODO()
}

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
    ParamType.ShortArray -> "getBooleanArrayExtra(\"$keyName\")"
    ParamType.CharArray -> "getShortArrayExtra(\"$keyName\")"
    ParamType.IntArray -> "getIntArrayExtra(\"$keyName\")"
    ParamType.LongArray -> "getLongArrayExtra(\"$keyName\")"
    ParamType.FloatArray -> "getBooleanArrayExtra(\"$keyName\")"
    ParamType.DoubleArray -> "getFloatArrayExtra(\"$keyName\")"
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

private fun getBundleGetterForNonTrival(bundleName: String, typeName: TypeName, typeMirror: TypeMirror, keyName: String) = when {
    typeMirror.isSubtypeOfType("android.os.Parcelable") -> "($typeName) getParcelable(\"$keyName\")"
    typeMirror.isSubtypeOfType("java.io.Serializable") -> "($typeName) getSerializable(\"$keyName\")"
    else -> throw Error("Illegal field typeName" + typeName)
}