package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.CONTEXT
import activitystarter.compiler.INTENT
import activitystarter.compiler.isSubtypeOfType
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement

internal class ServiceBinding(element: TypeElement) : ClassBinding(element) {

    override fun createFillFieldsMethod(): MethodSpec {
        val builder = MethodSpec.methodBuilder("fill")
                .addParameter(targetTypeName, "service")
                .addParameter(INTENT, "intent")
                .addModifiers(PUBLIC, STATIC)

        for (arg in argumentBindings) {
            val fieldName = arg.name
            val keyName = getKey(fieldName)
            val settingType = arg.settingType

            val settingPart = setterFor(fieldName, settingType, getIntentGetterFor(arg, keyName))
            builder.addStatement("if(intent.hasExtra(\"$keyName\")) service.$settingPart")
        }

        return builder.build()
    }

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant),
            createStartServiceMethod(variant)
    )

    private fun createGetIntentMethod(variant: List<ArgumentBinding>): MethodSpec {
        val builder = MethodSpec.methodBuilder("getIntent")
                .addParameter(CONTEXT, "context")
                .returns(INTENT)
                .addModifiers(PUBLIC, STATIC)

        variant.forEach { arg -> builder.addParameter(arg.type, arg.name) }
        builder.addStatement("\$T intent = new Intent(context, \$T.class)", INTENT, targetTypeName)
        variant.forEach { arg -> builder.addStatement("intent.putExtra(\"" + getKey(arg.name) + "\", " + arg.name + ")") }
        builder.addStatement("return intent")
        return builder.build()
    }

    private fun createStartServiceMethod(variant: List<ArgumentBinding>): MethodSpec {
        val builder = MethodSpec.methodBuilder("start")
                .addParameter(CONTEXT, "context")
                .addModifiers(PUBLIC, STATIC)

        variant.forEach { builder.addParameter(it.type, it.name) }
        addGetIntentStatement(builder, variant)
        builder.addStatement("context.startService(intent)")
        return builder.build()
    }

    private fun addGetIntentStatement(builder: MethodSpec.Builder, variant: List<ArgumentBinding>) {
        if (variant.isEmpty())
            builder.addStatement("\$T intent = getIntent(context)", INTENT)
        else {
            val intentArguments = variant.joinToString(separator = ", ", transform = { it.name })
            builder.addStatement("\$T intent = getIntent(context, $intentArguments)", INTENT)
        }
    }

    private fun getIntentGetterFor(arg: ArgumentBinding, keyName: String) = when (arg.type) {
        TypeName.get(String::class.java) -> "intent.getStringExtra(\"$keyName\")"
        TypeName.INT -> "intent.getIntExtra(\"$keyName\", -1)"
        TypeName.FLOAT -> "intent.getFloatExtra(\"$keyName\", -1F)"
        TypeName.BOOLEAN -> "intent.getBooleanExtra(\"$keyName\", false)"
        TypeName.DOUBLE -> "intent.getDoubleExtra(\"$keyName\", -1D)"
        TypeName.CHAR -> "intent.getCharExtra(\"$keyName\", 'a')"
        else -> when {
            arg.elementType.isSubtypeOfType("android.os.Parcelable") ->
                "(${arg.type}) intent.getParcelableExtra(\"$keyName\")"
            arg.elementType.isSubtypeOfType("java.io.Serializable") ->
                "(${arg.type}) intent.getSerializableExtra(\"$keyName\")"
            else -> throw Error("Illegal field type" + arg.type)
        }
    }
}