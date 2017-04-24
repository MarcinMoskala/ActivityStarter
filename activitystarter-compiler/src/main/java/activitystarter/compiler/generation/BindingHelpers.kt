package activitystarter.compiler.generation

import activitystarter.compiler.model.param.ParamType
import com.google.auto.common.MoreElements.getPackage
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ClassName.get
import com.squareup.javapoet.TypeName
import javax.lang.model.element.TypeElement

fun getBindingClassName(enclosingElement: javax.lang.model.element.TypeElement): com.squareup.javapoet.ClassName {
    val packageName = getPackage(enclosingElement).qualifiedName.toString()
    val className = enclosingElement.qualifiedName.toString().substring(packageName.length + 1)
    return get(packageName, className + "Starter")
}

fun getBundleSetterFor(type: activitystarter.compiler.model.param.ParamType) = when (type) {
    activitystarter.compiler.model.param.ParamType.String -> "putString"
    activitystarter.compiler.model.param.ParamType.Int -> "putInt"
    activitystarter.compiler.model.param.ParamType.Long -> "putLong"
    activitystarter.compiler.model.param.ParamType.Float -> "putFloat"
    activitystarter.compiler.model.param.ParamType.Boolean -> "putBoolean"
    activitystarter.compiler.model.param.ParamType.Double -> "putDouble"
    activitystarter.compiler.model.param.ParamType.Char -> "putChar"
    activitystarter.compiler.model.param.ParamType.Byte -> "putByte"
    activitystarter.compiler.model.param.ParamType.Short -> "putShort"
    activitystarter.compiler.model.param.ParamType.CharSequence -> "putCharSequence"
    activitystarter.compiler.model.param.ParamType.BooleanArray -> "putBooleanArray"
    activitystarter.compiler.model.param.ParamType.ByteArray -> "putByteArray"
    activitystarter.compiler.model.param.ParamType.ShortArray -> "putShortArray"
    activitystarter.compiler.model.param.ParamType.CharArray -> "putCharArray"
    activitystarter.compiler.model.param.ParamType.IntArray -> "putIntArray"
    activitystarter.compiler.model.param.ParamType.LongArray -> "putLongArray"
    activitystarter.compiler.model.param.ParamType.FloatArray -> "putFloatArray"
    activitystarter.compiler.model.param.ParamType.DoubleArray -> "putDoubleArray"
    activitystarter.compiler.model.param.ParamType.StringArray -> "putStringArray"
    activitystarter.compiler.model.param.ParamType.CharSequenceArray -> "putCharSequenceArray"
    activitystarter.compiler.model.param.ParamType.IntegerArrayList -> "putIntegerArrayList"
    activitystarter.compiler.model.param.ParamType.StringArrayList -> "putStringArrayList"
    activitystarter.compiler.model.param.ParamType.CharSequenceArrayList -> "putCharSequenceArrayList"
    activitystarter.compiler.model.param.ParamType.ParcelableSubtype -> "putParcelable"
    activitystarter.compiler.model.param.ParamType.SerializableSubtype -> "putSerializable"
    activitystarter.compiler.model.param.ParamType.ParcelableArrayListSubtype -> "putParcelableArrayList"
}

fun getBundleGetter(bundleName: String, paramType: activitystarter.compiler.model.param.ParamType, typeName: com.squareup.javapoet.TypeName, keyName: String): String {
    val bundleGetterCall = activitystarter.compiler.generation.getBundleGetterCall(paramType)
    val getArgumentValue = "$bundleName.$bundleGetterCall($keyName)"
    return if(paramType.typeUsedBySupertype()) "($typeName) $getArgumentValue" else getArgumentValue
}

private fun getBundleGetterCall(paramType: activitystarter.compiler.model.param.ParamType) = when (paramType) {
    activitystarter.compiler.model.param.ParamType.String -> "getString"
    activitystarter.compiler.model.param.ParamType.Int -> "getInt"
    activitystarter.compiler.model.param.ParamType.Long -> "getLong"
    activitystarter.compiler.model.param.ParamType.Float -> "getFloat"
    activitystarter.compiler.model.param.ParamType.Boolean -> "getBoolean"
    activitystarter.compiler.model.param.ParamType.Double -> "getDouble"
    activitystarter.compiler.model.param.ParamType.Char -> "getChar"
    activitystarter.compiler.model.param.ParamType.Byte -> "getByte"
    activitystarter.compiler.model.param.ParamType.Short -> "getShort"
    activitystarter.compiler.model.param.ParamType.CharSequence -> "getCharSequence"
    activitystarter.compiler.model.param.ParamType.BooleanArray -> "getBooleanArray"
    activitystarter.compiler.model.param.ParamType.ByteArray -> "getByteArray"
    activitystarter.compiler.model.param.ParamType.ShortArray -> "getShortArray"
    activitystarter.compiler.model.param.ParamType.CharArray -> "getCharArray"
    activitystarter.compiler.model.param.ParamType.IntArray -> "getIntArray"
    activitystarter.compiler.model.param.ParamType.LongArray -> "getLongArray"
    activitystarter.compiler.model.param.ParamType.FloatArray -> "getFloatArray"
    activitystarter.compiler.model.param.ParamType.DoubleArray -> "getDoubleArray"
    activitystarter.compiler.model.param.ParamType.StringArray -> "getStringArray"
    activitystarter.compiler.model.param.ParamType.CharSequenceArray -> "getCharSequenceArray"
    activitystarter.compiler.model.param.ParamType.IntegerArrayList -> "getIntegerArrayList"
    activitystarter.compiler.model.param.ParamType.StringArrayList -> "getStringArrayList"
    activitystarter.compiler.model.param.ParamType.CharSequenceArrayList -> "getCharSequenceArrayList"
    activitystarter.compiler.model.param.ParamType.ParcelableSubtype -> "getParcelable"
    activitystarter.compiler.model.param.ParamType.SerializableSubtype -> "getSerializable"
    activitystarter.compiler.model.param.ParamType.ParcelableArrayListSubtype -> "getParcelableArrayList"
}

fun getPutArgumentToIntentMethodName(paramType: activitystarter.compiler.model.param.ParamType) = when(paramType) {
    activitystarter.compiler.model.param.ParamType.IntegerArrayList -> "putIntegerArrayListExtra"
    activitystarter.compiler.model.param.ParamType.CharSequenceArrayList -> "putCharSequenceArrayListExtra"
    activitystarter.compiler.model.param.ParamType.ParcelableArrayListSubtype -> "putParcelableArrayListExtra"
    activitystarter.compiler.model.param.ParamType.StringArrayList -> "putStringArrayListExtra"
    else -> "putExtra"
}

fun getIntentGetterFor(paramType: activitystarter.compiler.model.param.ParamType, typeName: com.squareup.javapoet.TypeName, key: String): String {
    val getter = activitystarter.compiler.generation.getIntentGetterForParamType(paramType, key)
    val getArgumentValue = "intent.$getter"
    return if(paramType.typeUsedBySupertype()) "($typeName) $getArgumentValue" else getArgumentValue
}

private fun getIntentGetterForParamType(paramType: activitystarter.compiler.model.param.ParamType, key: String) = when (paramType) {
    activitystarter.compiler.model.param.ParamType.String -> "getStringExtra($key)"
    activitystarter.compiler.model.param.ParamType.Int -> "getIntExtra($key, 0)"
    activitystarter.compiler.model.param.ParamType.Long -> "getLongExtra($key, 0L)"
    activitystarter.compiler.model.param.ParamType.Float -> "getFloatExtra($key, -0F)"
    activitystarter.compiler.model.param.ParamType.Boolean -> "getBooleanExtra($key, false)"
    activitystarter.compiler.model.param.ParamType.Double -> "getDoubleExtra($key, -0D)"
    activitystarter.compiler.model.param.ParamType.Char -> "getCharExtra($key, '\\u0000')"
    activitystarter.compiler.model.param.ParamType.Byte -> "getByteExtra($key, (byte) 0)"
    activitystarter.compiler.model.param.ParamType.Short -> "getShortExtra($key, (short) 0)"
    activitystarter.compiler.model.param.ParamType.CharSequence -> "getCharSequenceExtra($key)"
    activitystarter.compiler.model.param.ParamType.BooleanArray -> "getBooleanArrayExtra($key)"
    activitystarter.compiler.model.param.ParamType.ByteArray -> "getByteArrayExtra($key)"
    activitystarter.compiler.model.param.ParamType.ShortArray -> "getShortArrayExtra($key)"
    activitystarter.compiler.model.param.ParamType.CharArray -> "getCharArrayExtra($key)"
    activitystarter.compiler.model.param.ParamType.IntArray -> "getIntArrayExtra($key)"
    activitystarter.compiler.model.param.ParamType.LongArray -> "getLongArrayExtra($key)"
    activitystarter.compiler.model.param.ParamType.FloatArray -> "getFloatArrayExtra($key)"
    activitystarter.compiler.model.param.ParamType.DoubleArray -> "getDoubleArrayExtra($key)"
    activitystarter.compiler.model.param.ParamType.StringArray -> "getStringArrayExtra($key)"
    activitystarter.compiler.model.param.ParamType.CharSequenceArray -> "getCharSequenceArrayExtra($key)"
    activitystarter.compiler.model.param.ParamType.IntegerArrayList -> "getIntegerArrayListExtra($key)"
    activitystarter.compiler.model.param.ParamType.StringArrayList -> "getStringArrayListExtra($key)"
    activitystarter.compiler.model.param.ParamType.CharSequenceArrayList -> "getCharSequenceArrayListExtra($key)"
    activitystarter.compiler.model.param.ParamType.ParcelableSubtype -> "getParcelableExtra($key)"
    activitystarter.compiler.model.param.ParamType.SerializableSubtype -> "getSerializableExtra($key)"
    activitystarter.compiler.model.param.ParamType.ParcelableArrayListSubtype -> "getParcelableArrayListExtra($key)"
}