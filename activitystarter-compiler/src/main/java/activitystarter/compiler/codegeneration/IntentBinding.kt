package activitystarter.compiler.codegeneration

import activitystarter.compiler.classbinding.ClassBinding
import activitystarter.compiler.param.ArgumentBinding
import activitystarter.compiler.utils.INTENT
import com.squareup.javapoet.MethodSpec

internal abstract class IntentBinding(classBinding: ClassBinding) : ClassGeneration(classBinding) {

    protected fun fillByIntentBinding(targetName: String) = getBasicFillMethodBuilder("ActivityStarter.fill(this, intent)")
            .addParameter(classBinding.targetTypeName, targetName)
            .addParameter(INTENT, "intent")
            .addIntentSetters(targetName)
            .build()!!

    protected fun createGetIntentMethod(variant: List<ArgumentBinding>) = builderWithCreationBasicFields("getIntent")
            .addArgParameters(variant)
            .returns(INTENT)
            .addStatement("\$T intent = new Intent(context, \$T.class)", INTENT, classBinding.targetTypeName)
            .addPutExtraStatement(variant)
            .addStatement("return intent")
            .build()!!

    private fun MethodSpec.Builder.addPutExtraStatement(variant: List<ArgumentBinding>) = apply {
        variant.forEach { arg ->
            val putArgumentToIntentMethodName = getPutArgumentToIntentMethodName(arg.paramType)
            addStatement("intent.$putArgumentToIntentMethodName(\"" + arg.key + "\", " + arg.name + ")")
        }
    }

    protected fun MethodSpec.Builder.addIntentSetters(targetParameterName: String) = apply {
        classBinding.argumentBindings.forEach { arg -> addIntentSetter(arg, targetParameterName) }
    }

    protected fun MethodSpec.Builder.addIntentSetter(arg: ArgumentBinding, targetParameterName: String) {
        val keyName = arg.key
        val settingPart = arg.accessor.setToField(getIntentGetterFor(arg, keyName))
        addStatement("if(intent.hasExtra(\"$keyName\")) $targetParameterName.$settingPart")
    }

    protected fun createGetIntentStarter(starterFunc: String, variant: List<ArgumentBinding>) = builderWithCreationBasicFields("start")
            .addArgParameters(variant)
            .addGetIntentStatement(variant)
            .addStatement("context.$starterFunc(intent)")
            .build()

    protected fun MethodSpec.Builder.addGetIntentStatement(variant: List<ArgumentBinding>) = apply {
        if (variant.isEmpty())
            addStatement("\$T intent = getIntent(context)", INTENT)
        else {
            val intentArguments = variant.joinToString(separator = ", ", transform = { it.name })
            addStatement("\$T intent = getIntent(context, $intentArguments)", INTENT)
        }
    }
}
