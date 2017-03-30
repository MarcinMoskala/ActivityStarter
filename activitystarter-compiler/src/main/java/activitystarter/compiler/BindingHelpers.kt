package activitystarter.compiler

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.isSubtypeOfType
import com.google.auto.common.MoreElements
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

fun getBindingClassName(enclosingElement: TypeElement): ClassName {
    val packageName = MoreElements.getPackage(enclosingElement).qualifiedName.toString()
    val className = enclosingElement.qualifiedName.toString().substring(packageName.length + 1)
    return ClassName.get(packageName, className + "Starter")
}

// TODO Rest
fun getBundleSetterFor(typeName: TypeName, typeMirror: TypeMirror) = when (typeName) {
    TypeName.get(String::class.java) -> "putString"
    TypeName.INT -> "putInt"
    TypeName.FLOAT -> "putFloat"
    TypeName.BOOLEAN -> "putBoolean"
    TypeName.DOUBLE -> "putDouble"
    TypeName.CHAR -> "putChar"
    else -> getBundleSetterForNonTrivial(typeName, typeMirror)
}

// TODO Rest
fun getBundleGetterFor(bundleName: String, typeName: TypeName, typeMirror: TypeMirror, keyName: String) = when (typeName) {
    TypeName.get(String::class.java) -> "$bundleName.getString(\"$keyName\")"
    TypeName.INT -> "$bundleName.getInt(\"$keyName\", -1)"
    TypeName.LONG -> "$bundleName.getLong(\"$keyName\", -1L)"
    TypeName.FLOAT -> "$bundleName.getFloat(\"$keyName\", -1F)"
    TypeName.BOOLEAN -> "$bundleName.getBoolean(\"$keyName\", false)"
    TypeName.DOUBLE -> "$bundleName.getDouble(\"$keyName\", -1D)"
    TypeName.CHAR -> "$bundleName.getChar(\"$keyName\", 'a')"
    else -> getBundleGetterForNonTrival(bundleName, typeName, typeMirror, keyName)
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
fun getIntentGetterFor(typeName: TypeName, typeMirror: TypeMirror, keyName: String) = when (typeName) {
    TypeName.get(String::class.java) -> "intent.getStringExtra(\"$keyName\")"
    TypeName.INT -> "intent.getIntExtra(\"$keyName\", -1)"
    TypeName.LONG -> "intent.getLongExtra(\"$keyName\", -1)"
    TypeName.FLOAT -> "intent.getFloatExtra(\"$keyName\", -1F)"
    TypeName.BOOLEAN -> "intent.getBooleanExtra(\"$keyName\", false)"
    TypeName.DOUBLE -> "intent.getDoubleExtra(\"$keyName\", -1D)"
    TypeName.CHAR -> "intent.getCharExtra(\"$keyName\", 'a')"
    TypeName.BYTE -> "intent.getByteExtra(\"$keyName\", 'a')"
    TypeName.SHORT -> "intent.getShortExtra(\"$keyName\", 'a')"
    else -> getIntentGetterForNotTrival(typeName, typeMirror, keyName)
}

private fun getBundleSetterForNonTrivial(typeName: TypeName, typeMirror: TypeMirror) = when {
    typeMirror.isSubtypeOfType("android.os.Parcelable") -> "putParcelable"
    typeMirror.isSubtypeOfType("java.io.Serializable") -> "putSerializable"
    else -> throw Error("Illegal field type" + typeName)
}

private fun getBundleGetterForNonTrival(bundleName: String, typeName: TypeName, typeMirror: TypeMirror, keyName: String) = when {
    typeMirror.isSubtypeOfType("android.os.Parcelable") -> "($typeName) $bundleName.getParcelable(\"$keyName\")"
    typeMirror.isSubtypeOfType("java.io.Serializable") -> "($typeName) $bundleName.getSerializable(\"$keyName\")"
    else -> throw Error("Illegal field type" + typeName)
}

private fun getIntentGetterForNotTrival(typeName: TypeName, typeMirror: TypeMirror, keyName: String) = when {
    typeMirror.isSubtypeOfType("android.os.Parcelable") -> "($typeName) intent.getParcelableExtra(\"$keyName\")"
    typeMirror.isSubtypeOfType("java.io.Serializable") -> "($typeName) intent.getSerializableExtra(\"$keyName\")"
    else -> throw Error("Illegal field type" + typeName)
}