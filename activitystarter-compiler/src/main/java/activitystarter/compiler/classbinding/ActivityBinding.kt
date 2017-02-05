package activitystarter.compiler.classbinding

import activitystarter.compiler.*
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Modifier
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement

internal class ActivityBinding(element: TypeElement) : ClassBinding(element) {

    override fun createFillFieldsMethod(): MethodSpec {
        val builder = MethodSpec.methodBuilder("fill")
                .addParameter(targetTypeName, "activity")
                .addModifiers(PUBLIC, STATIC)

        if (argumentBindings.isNotEmpty())
            builder.addStatement("Intent intent = activity.getIntent()")

        for (arg in argumentBindings) {
            val fieldName = arg.name
            val keyName = getKey(fieldName)
            val settingType = arg.settingType

            val settingPart = setterFor(fieldName, settingType, getIntentGetterFor(arg, keyName))
            builder.addStatement("if(intent.hasExtra(\"$keyName\")) activity.$settingPart")
        }

        return builder.build()
    }

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant),
            createStartActivityMethod(variant),
            createStartActivityMethodWithFlags(variant)
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

    private fun createStartActivityMethod(variant: List<ArgumentBinding>): MethodSpec {
        val builder = MethodSpec.methodBuilder("start")
                .addParameter(CONTEXT, "context")
                .addModifiers(PUBLIC, STATIC)

        variant.forEach { builder.addParameter(it.type, it.name) }
        addGetIntentStatement(builder, variant)
        builder.addStatement("context.startActivity(intent)")
        return builder.build()
    }

    private fun createStartActivityMethodWithFlags(variant: List<ArgumentBinding>): MethodSpec {
        val builder = MethodSpec.methodBuilder("startWithFlags")
                .addParameter(CONTEXT, "context")
                .addModifiers(PUBLIC, STATIC)

        variant.forEach { builder.addParameter(it.type, it.name) }
        builder.addParameter(TypeName.INT, "flags")
        addGetIntentStatement(builder, variant)
        builder.addStatement("intent.addFlags(flags)")
        builder.addStatement("context.startActivity(intent)")
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
            isSubtypeOfType(arg.elementType, "android.os.Parcelable") ->
                "(${arg.type}) intent.getParcelableExtra(\"$keyName\")"
            isSubtypeOfType(arg.elementType, "java.io.Serializable") ->
                "(${arg.type}) intent.getSerializableExtra(\"$keyName\")"
            else -> throw Error("Illegal field type" + arg.type)
        }
    }
}