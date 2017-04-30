package activitystarter.compiler.helpers

import activitystarter.compiler.issubtype.B
import activitystarter.compiler.issubtype.E
import activitystarter.compiler.issubtype.F
import android.accounts.Account
import com.google.testing.compile.CompilationRule
import java.awt.Color
import java.util.*
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

inline fun <reified T: Any> CompilationRule.getElement(): TypeElement {
    val javaClass = T::class.java
    return getElement(javaClass)
}

fun <T : Any> CompilationRule.getElement(javaClass: Class<T>): TypeElement {
    val canonicalName = javaClass.canonicalName
    return elements.getTypeElement(canonicalName)
}

inline fun <reified T: Any> CompilationRule.getMirror(): TypeMirror {
    return types.getDeclaredType(getElement<T>())
}

val CompilationRule.intTypeMirror: TypeMirror
    get() = types.getPrimitiveType(TypeKind.INT)

val CompilationRule.stringTypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<String>())

val CompilationRule.charSequenceTypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<CharSequence>())

val CompilationRule.longTypeMirror: TypeMirror
    get() = types.getPrimitiveType(TypeKind.LONG)

val CompilationRule.boolTypeMirror: TypeMirror
    get() = types.getPrimitiveType(TypeKind.BOOLEAN)

val CompilationRule.charTypeMirror: TypeMirror
    get() = types.getPrimitiveType(TypeKind.CHAR)

val CompilationRule.byteTypeMirror: TypeMirror
    get() = types.getPrimitiveType(TypeKind.BYTE)

val CompilationRule.shortTypeMirror: TypeMirror
    get() = types.getPrimitiveType(TypeKind.SHORT)

val CompilationRule.floatTypeMirror: TypeMirror
    get() = types.getPrimitiveType(TypeKind.FLOAT)

val CompilationRule.doubleTypeMirror: TypeMirror
    get() = types.getPrimitiveType(TypeKind.DOUBLE)

val CompilationRule.stringArrayTypeMirror: TypeMirror
    get() = types.getArrayType(stringTypeMirror)

val CompilationRule.intArrayTypeMirror: TypeMirror
    get() = types.getArrayType(intTypeMirror)

val CompilationRule.boolArrayTypeMirror: TypeMirror
    get() = types.getArrayType(boolTypeMirror)

val CompilationRule.byteArrayTypeMirror: TypeMirror
    get() = types.getArrayType(byteTypeMirror)

val CompilationRule.shortArrayTypeMirror: TypeMirror
    get() = types.getArrayType(shortTypeMirror)

val CompilationRule.charArrayTypeMirror: TypeMirror
    get() = types.getArrayType(charTypeMirror)

val CompilationRule.longArrayTypeMirror: TypeMirror
    get() = types.getArrayType(longTypeMirror)

val CompilationRule.floatArrayTypeMirror: TypeMirror
    get() = types.getArrayType(floatTypeMirror)

val CompilationRule.doubleArrayTypeMirror: TypeMirror
    get() = types.getArrayType(doubleTypeMirror)

val CompilationRule.charSequenceArrayTypeMirror: TypeMirror
    get() = types.getArrayType(charSequenceTypeMirror)

val CompilationRule.integerArrayListTypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<ArrayList<Int>>(), getElement<Int>().asType())

val CompilationRule.stringArrayListTypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<ArrayList<String>>(), getElement<String>().asType())

val CompilationRule.charSequenceArrayListTypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<ArrayList<CharSequence>>(), getElement<CharSequence>().asType())

val CompilationRule.subtypeOfParcelableTypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<Account>())

val CompilationRule.subtypeOfSerializableTypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<Color>())

val CompilationRule.parcelableSubtypeArrayListTypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<ArrayList<Account>>(), getElement<Account>().asType())

val CompilationRule.interfaceBTypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<B>())

val CompilationRule.interfaceETypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<E>())

val CompilationRule.objectFTypeMirror: TypeMirror
    get() = types.getDeclaredType(getElement<F>())
