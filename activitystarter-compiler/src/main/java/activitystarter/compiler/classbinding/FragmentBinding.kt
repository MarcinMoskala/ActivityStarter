package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.BUNDLE
import activitystarter.compiler.isSubtypeOfType
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeName.INT
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement

internal class FragmentBinding(element: TypeElement) : ClassBinding(element) {

    override fun createFillFieldsMethod(): MethodSpec {
        val builder = MethodSpec.methodBuilder("fill")
                .addJavadoc("This is method used to fill Fragment fields. Use it by calling ActivityStarter.fill(this).")
                .addParameter(targetTypeName, "fragment")
                .addModifiers(PUBLIC, STATIC)

        if (argumentBindings.isNotEmpty())
            builder.addStatement("\$T arguments = fragment.getArguments()", BUNDLE)

        for (arg in argumentBindings) {
            val fieldName = arg.name
            val keyName = getKey(fieldName)
            val settingPart = setterFor(fieldName, arg.settingType, getArgumentGetterFor(arg, keyName))
            builder.addStatement("if(arguments.containsKey(\"$keyName\")) fragment.$settingPart")
        }

        return builder.build()
    }

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant)
    )

    private fun createGetIntentMethod(variant: List<ArgumentBinding>): MethodSpec {
        val builder = MethodSpec.methodBuilder("newInstance")
                .returns(targetTypeName)
                .addModifiers(PUBLIC, STATIC)

        variant.forEach { arg -> builder.addParameter(arg.type, arg.name) }
        builder.addStatement("\$T fragment = new \$T()", targetTypeName, targetTypeName)
        if(variant.isNotEmpty()) builder.addStatement("\$T args = new Bundle()", BUNDLE)
        variant.forEach { arg -> builder.addStatement("args.${getArgumentSetterFor(arg)}(\"" + getKey(arg.name) + "\", " + arg.name + ")") }
        if(variant.isNotEmpty()) builder.addStatement("fragment.setArguments(args)")
        builder.addStatement("return fragment")
        return builder.build()
    }

    private fun getArgumentGetterFor(arg: ArgumentBinding, keyName: String) = when (arg.type) {
        TypeName.get(String::class.java) -> "arguments.getString(\"$keyName\")"
        INT -> "arguments.getInt(\"$keyName\", -1)"
        TypeName.FLOAT -> "arguments.getFloat(\"$keyName\", -1F)"
        TypeName.BOOLEAN -> "arguments.getBoolean(\"$keyName\", false)"
        TypeName.DOUBLE -> "arguments.getDouble(\"$keyName\", -1D)"
        TypeName.CHAR -> "arguments.getChar(\"$keyName\", 'a')"
        else -> when {
            arg.elementType.isSubtypeOfType("android.os.Parcelable") -> "(${arg.type}) arguments.getParcelable(\"$keyName\")"
            arg.elementType.isSubtypeOfType("java.io.Serializable") -> "(${arg.type}) arguments.getSerializable(\"$keyName\")"
            else -> throw Error("Illegal field type" + arg.type)
        }
    }

    private fun getArgumentSetterFor(arg: ArgumentBinding) = when (arg.type) {
        TypeName.get(String::class.java) -> "putString"
        INT -> "putInt"
        TypeName.FLOAT -> "putFloat"
        TypeName.BOOLEAN -> "putBoolean"
        TypeName.DOUBLE -> "putDouble"
        TypeName.CHAR -> "putChar"
        else -> when {
            arg.elementType.isSubtypeOfType("android.os.Parcelable") -> "putParcelable"
            arg.elementType.isSubtypeOfType("java.io.Serializable") -> "putSerializable"
            else -> throw Error("Illegal field type" + arg.type)
        }
    }
}