package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.BUNDLE
import activitystarter.compiler.isSubtypeOfType
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeName.*
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement

internal class FragmentBinding(element: TypeElement) : ClassBinding(element) {

    override fun createFillFieldsMethod() =
            getBasicFillMethodBuilder()
                    .addParameter(targetTypeName, "fragment")
                    .doIf(argumentBindings.isNotEmpty()) { addStatement("\$T arguments = fragment.getArguments()", BUNDLE) }
                    .addSetArgumentsStatements()
                    .build()

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant)
    )

    private fun MethodSpec.Builder.addSetArgumentsStatements() = apply {
        for (arg in argumentBindings) {
            val fieldName = arg.name
            val keyName = getKey(fieldName)
            val settingPart = arg.accessor.setToField(getArgumentGetterFor(arg, keyName))
            addStatement("if(arguments.containsKey(\"$keyName\")) fragment.$settingPart")
        }
    }

    private fun createGetIntentMethod(variant: List<ArgumentBinding>) =
            builderWithCreationBasicFieldsNoContext("newInstance")
                    .returns(targetTypeName)
                    .addArgParameters(variant)
                    .addStatement("\$T fragment = new \$T()", targetTypeName, targetTypeName)
                    .doIf(variant.isNotEmpty()) { addStatement("\$T args = new Bundle()", BUNDLE) }
                    .addSaveArgumentsStatements(variant)
                    .doIf(variant.isNotEmpty()) { addStatement("fragment.setArguments(args)") }
                    .addStatement("return fragment")
                    .build()

    private fun MethodSpec.Builder.addSaveArgumentsStatements(variant: List<ArgumentBinding>) = apply {
        variant.forEach { arg -> addStatement("args.${getArgumentSetterFor(arg)}(\"" + getKey(arg.name) + "\", " + arg.name + ")") }
    }

    private fun getArgumentGetterFor(arg: ArgumentBinding, keyName: String) = when (arg.type) {
        TypeName.get(String::class.java) -> "arguments.getString(\"$keyName\")"
        INT -> "arguments.getInt(\"$keyName\", -1)"
        FLOAT -> "arguments.getFloat(\"$keyName\", -1F)"
        BOOLEAN -> "arguments.getBoolean(\"$keyName\", false)"
        DOUBLE -> "arguments.getDouble(\"$keyName\", -1D)"
        CHAR -> "arguments.getChar(\"$keyName\", 'a')"
        else -> getArgumentGetterForNonTrival(arg, keyName)
    }

    private fun getArgumentGetterForNonTrival(arg: ArgumentBinding, keyName: String) = when {
        arg.elementType.isSubtypeOfType("android.os.Parcelable") -> "(${arg.type}) arguments.getParcelable(\"$keyName\")"
        arg.elementType.isSubtypeOfType("java.io.Serializable") -> "(${arg.type}) arguments.getSerializable(\"$keyName\")"
        else -> throw Error("Illegal field type" + arg.type)
    }

    private fun getArgumentSetterFor(arg: ArgumentBinding) = when (arg.type) {
        TypeName.get(String::class.java) -> "putString"
        INT -> "putInt"
        FLOAT -> "putFloat"
        BOOLEAN -> "putBoolean"
        DOUBLE -> "putDouble"
        CHAR -> "putChar"
        else -> getArgumentSetterForNonTrivial(arg)
    }

    private fun getArgumentSetterForNonTrivial(arg: ArgumentBinding) = when {
        arg.elementType.isSubtypeOfType("android.os.Parcelable") -> "putParcelable"
        arg.elementType.isSubtypeOfType("java.io.Serializable") -> "putSerializable"
        else -> throw Error("Illegal field type" + arg.type)
    }
}