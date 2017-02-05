package activitystarter.compiler

import activitystarter.Arg
import activitystarter.compiler.FieldVeryfyResult.Accessible
import com.google.auto.common.MoreElements.getPackage
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier.*
import javax.lang.model.element.TypeElement

internal class ClassBinding(enclosingElement: TypeElement) {

    private val targetTypeName = getTargetTypeName(enclosingElement)
    private val bindingClassName = getBindingClassName(enclosingElement)
    private val argumentBindings: List<ArgumentBinding> = enclosingElement.enclosedElements
            .filter { it.getAnnotation(Arg::class.java) != null }
            .map(::ArgumentBinding)

    private fun getBindingClassName(enclosingElement: TypeElement): ClassName {
        val packageName = getPackage(enclosingElement).qualifiedName.toString()
        val className = enclosingElement.qualifiedName.toString().substring(packageName.length + 1)
        return ClassName.get(packageName, className + "Starter")
    }

    private fun getTargetTypeName(enclosingElement: TypeElement) = TypeName.get(enclosingElement.asType())
            .let { if (it is ParameterizedTypeName) it.rawType else it }

    fun brewJava(): JavaFile {
        return JavaFile.builder(bindingClassName.packageName(), createActivityStarterSpec())
                .addFileComment("Generated code from ActivityStarter. Do not modify!")
                .build()
    }

    private fun createActivityStarterSpec(): TypeSpec {
        val result = TypeSpec
                .classBuilder(bindingClassName.simpleName())
                .addModifiers(PUBLIC, FINAL)
                .addMethod(createFillFieldsMethod())

        for (variant in argumentBindings.createSublists { it.isOptional }) {
            result.addMethod(createGetIntentMethod(variant))
            result.addMethod(createStartActivityMethod(variant))
            result.addMethod(createStartActivityMethodWithFlags(variant))
        }

        return result.build()
    }

    private fun createGetIntentMethod(variant: List<ArgumentBinding>): MethodSpec {
        val builder = MethodSpec.methodBuilder("getIntent")
                .addAnnotation(UI_THREAD)
                .addParameter(CONTEXT, "context")
                .returns(INTENT)
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)

        variant.forEach { arg -> builder.addParameter(arg.type, arg.name) }
        builder.addStatement("\$T intent = new Intent(context, \$T.class)", INTENT, targetTypeName)
        variant.forEach { arg -> builder.addStatement("intent.putExtra(\"" + arg.name + "Arg\", " + arg.name + ")") }
        builder.addStatement("return intent")
        return builder.build()
    }

    private fun createStartActivityMethod(variant: List<ArgumentBinding>): MethodSpec {
        val builder = MethodSpec.methodBuilder("start")
                .addAnnotation(UI_THREAD)
                .addParameter(CONTEXT, "context")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)

        variant.forEach { builder.addParameter(it.type, it.name) }
        addGetIntentStatement(builder, variant)
        builder.addStatement("context.startActivity(intent)")
        return builder.build()
    }

    private fun createStartActivityMethodWithFlags(variant: List<ArgumentBinding>): MethodSpec {
        val builder = MethodSpec.methodBuilder("startWithFlags")
                .addAnnotation(UI_THREAD)
                .addParameter(CONTEXT, "context")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)

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

    private fun createFillFieldsMethod(): MethodSpec {
        val builder = MethodSpec.methodBuilder("fill")
                .addAnnotation(UI_THREAD)
                .addParameter(targetTypeName, "activity")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)

        if (argumentBindings.isNotEmpty())
            builder.addStatement("Intent intent = activity.getIntent()")

        for (arg in argumentBindings) {
            val fieldName = arg.name
            val keyName = fieldName + "Arg"
            val settingType = arg.settingType

            val settingPart =
                    if (settingType == Accessible) fieldName + " = " + getIntentGetterFor(arg, keyName)
                    else getSetter(settingType, fieldName) + "(" + getIntentGetterFor(arg, keyName) + ")"

            builder.addStatement("if(intent.hasExtra(\"$keyName\")) activity.$settingPart")
        }

        return builder.build()
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

    companion object {

        private val UI_THREAD = ClassName.get("android.support.annotation", "UiThread")
        private val INTENT = ClassName.get("android.content", "Intent")
        private val CONTEXT = ClassName.get("android.content", "Context")
    }
}
