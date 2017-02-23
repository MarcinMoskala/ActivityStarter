package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.INTENT
import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.TypeElement

internal abstract class IntentBinding(element: TypeElement) : ClassBinding(element) {

    protected fun fillByIntentBinding(targetName: String) = getBasicFillMethodBuilder("ActivityStarter.fill(this, intent)")
            .addParameter(targetTypeName, targetName)
            .addParameter(INTENT, "intent")
            .addIntentSetters(targetName)
            .build()!!

    protected fun createGetIntentMethod(variant: List<ArgumentBinding>) = builderWithCreationBasicFields("getIntent")
            .addArgParameters(variant)
            .returns(INTENT)
            .addStatement("\$T intent = new Intent(context, \$T.class)", INTENT, targetTypeName)
            .addPutExtraStatement(variant)
            .addStatement("return intent")
            .build()

    private fun MethodSpec.Builder.addPutExtraStatement(variant: List<ArgumentBinding>) = apply {
        variant.forEach { arg -> addStatement("intent.putExtra(\"" + arg.key + "\", " + arg.name + ")") }
    }

    protected fun MethodSpec.Builder.addIntentSetters(targetParameterName: String) = apply {
        for (arg in argumentBindings) {
            val keyName = arg.key
            val settingPart = arg.accessor.setToField(getIntentGetterFor(arg, keyName))
            addStatement("if(intent.hasExtra(\"$keyName\")) $targetParameterName.$settingPart")
        }
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
