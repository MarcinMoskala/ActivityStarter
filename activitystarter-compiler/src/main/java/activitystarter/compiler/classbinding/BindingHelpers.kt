package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.isSubtypeOfType
import com.google.auto.common.MoreElements
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import javax.lang.model.element.TypeElement

fun getBindingClassName(enclosingElement: TypeElement): ClassName {
    val packageName = MoreElements.getPackage(enclosingElement).qualifiedName.toString()
    val className = enclosingElement.qualifiedName.toString().substring(packageName.length + 1)
    return ClassName.get(packageName, className + "Starter")
}

// TODO Rest
fun getBundleSetterFor(arg: ArgumentBinding) = when (arg.type) {
    TypeName.get(String::class.java) -> "putString"
    TypeName.INT -> "putInt"
    TypeName.FLOAT -> "putFloat"
    TypeName.BOOLEAN -> "putBoolean"
    TypeName.DOUBLE -> "putDouble"
    TypeName.CHAR -> "putChar"
    else -> getBundleSetterForNonTrivial(arg)
}

// TODO Rest
fun getBundleGetterFor(bundleName: String, arg: ArgumentBinding, keyName: String) = when (arg.type) {
    TypeName.get(String::class.java) -> "$bundleName.getString(\"$keyName\")"
    TypeName.INT -> "$bundleName.getInt(\"$keyName\", -1)"
    TypeName.FLOAT -> "$bundleName.getFloat(\"$keyName\", -1F)"
    TypeName.BOOLEAN -> "$bundleName.getBoolean(\"$keyName\", false)"
    TypeName.DOUBLE -> "$bundleName.getDouble(\"$keyName\", -1D)"
    TypeName.CHAR -> "$bundleName.getChar(\"$keyName\", 'a')"
    else -> getBundleGetterForNonTrival(bundleName, arg, keyName)
}

// TODO
//getCharSequenceExtra
//getParcelableArrayExtra
//getParcelableArrayListExtra
//getIntegerArrayListExtra
//getStringArrayListExtra
//getCharSequenceArrayListExtra
//getBooleanArrayExtra
//getByteArrayExtra
//getShortArrayExtra
//getCharArrayExtra
//getIntArrayExtra
//getLongArrayExtra
//getFloatArrayExtra
//getDoubleArrayExtra
//getStringArrayExtra
//getCharSequenceArrayExtra
//getBundleExtra
//getIBinderExtra
// TODO Error check should depend on this function to make it generic
// TODO This all types needs tests!!!
fun getIntentGetterFor(arg: ArgumentBinding, keyName: String) = when (arg.type) {
    TypeName.get(String::class.java) -> "intent.getStringExtra(\"$keyName\")"
    TypeName.INT -> "intent.getIntExtra(\"$keyName\", -1)"
    TypeName.LONG -> "intent.getLongExtra(\"$keyName\", -1)"
    TypeName.FLOAT -> "intent.getFloatExtra(\"$keyName\", -1F)"
    TypeName.BOOLEAN -> "intent.getBooleanExtra(\"$keyName\", false)"
    TypeName.DOUBLE -> "intent.getDoubleExtra(\"$keyName\", -1D)"
    TypeName.CHAR -> "intent.getCharExtra(\"$keyName\", 'a')"
    TypeName.BYTE -> "intent.getByteExtra(\"$keyName\", 'a')"
    TypeName.SHORT -> "intent.getShortExtra(\"$keyName\", 'a')"
    else -> getIntentGetterForNotTrival(arg, keyName)
}

private fun getBundleSetterForNonTrivial(arg: ArgumentBinding) = when {
    arg.elementType.isSubtypeOfType("android.os.Parcelable") -> "putParcelable"
    arg.elementType.isSubtypeOfType("java.io.Serializable") -> "putSerializable"
    else -> throw Error("Illegal field type" + arg.type)
}

private fun getBundleGetterForNonTrival(bundleName: String, arg: ArgumentBinding, keyName: String) = when {
    arg.elementType.isSubtypeOfType("android.os.Parcelable") -> "(${arg.type}) $bundleName.getParcelable(\"$keyName\")"
    arg.elementType.isSubtypeOfType("java.io.Serializable") -> "(${arg.type}) $bundleName.getSerializable(\"$keyName\")"
    else -> throw Error("Illegal field type" + arg.type)
}

private fun getIntentGetterForNotTrival(arg: ArgumentBinding, keyName: String) = when {
    arg.elementType.isSubtypeOfType("android.os.Parcelable") -> "(${arg.type}) intent.getParcelableExtra(\"$keyName\")"
    arg.elementType.isSubtypeOfType("java.io.Serializable") -> "(${arg.type}) intent.getSerializableExtra(\"$keyName\")"
    else -> throw Error("Illegal field type" + arg.type)
}