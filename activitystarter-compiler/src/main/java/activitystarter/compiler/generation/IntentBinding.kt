package activitystarter.compiler.generation

import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ArgumentModel
import activitystarter.compiler.utils.INTENT
import com.squareup.javapoet.MethodSpec

internal abstract class IntentBinding(classModel: ClassModel) : ClassGeneration(classModel) {

    protected fun fillByIntentBinding(targetName: String) = getBasicFillMethodBuilder("ActivityStarter.fill(this, intent)")
            .addParameter(classModel.targetTypeName, targetName)
            .addParameter(INTENT, "intent")
            .addStatement("if(intent == null) return")
            .addIntentSetters(targetName)
            .build()!!

    protected fun createGetIntentMethod(variant: List<ArgumentModel>) = builderWithCreationBasicFields("getIntent")
            .addArgParameters(variant)
            .returns(INTENT)
            .addStatement("\$T intent = new Intent(context, \$T.class)", INTENT, classModel.targetTypeName)
            .addPutExtraStatement(variant)
            .addStatement("return intent")
            .build()!!

    private fun MethodSpec.Builder.addPutExtraStatement(variant: List<ArgumentModel>) = apply {
        variant.forEach { arg ->
            val putArgumentToIntentMethodName = getPutArgumentToIntentMethodName(arg.saveParamType)
            val wrappedValue = arg.addWrapper { arg.name }
            addStatement("intent.$putArgumentToIntentMethodName(${arg.fieldName}, $wrappedValue)")
        }
    }

    protected fun MethodSpec.Builder.addIntentSetters(targetParameterName: String) = apply {
        classModel.argumentModels.forEach { arg -> addIntentSetter(arg, targetParameterName) }
    }

    protected fun MethodSpec.Builder.addIntentSetter(arg: ArgumentModel, targetParameterName: String) {
        val fieldName = arg.fieldName
        val possiblyWrappedValue = getIntentGetterFor(arg.saveParamType, fieldName)
        val valueToSet = (if (arg.paramType.typeUsedBySupertype()) "(\$T) " else "") +
                        arg.addUnwrapper { possiblyWrappedValue }
        val settingPart = arg.accessor.makeSetter(valueToSet)
        addStatement("if(intent.hasExtra($fieldName)) \n $targetParameterName.$settingPart", arg.typeName)
    }

    protected fun createGetIntentStarter(starterFunc: String, variant: List<ArgumentModel>) = builderWithCreationBasicFields("start")
            .addArgParameters(variant)
            .addGetIntentStatement(variant)
            .addStatement("context.$starterFunc(intent)")
            .build()

    protected fun MethodSpec.Builder.addGetIntentStatement(variant: List<ArgumentModel>) = apply {
        if (variant.isEmpty())
            addStatement("\$T intent = getIntent(context)", INTENT)
        else {
            val intentArguments = variant.joinToString(separator = ", ", transform = { it.name })
            addStatement("\$T intent = getIntent(context, $intentArguments)", INTENT)
        }
    }
}

