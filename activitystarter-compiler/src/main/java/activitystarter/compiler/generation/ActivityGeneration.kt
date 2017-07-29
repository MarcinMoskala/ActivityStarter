package activitystarter.compiler.generation

import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ArgumentModel
import activitystarter.compiler.utils.*
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec

internal class ActivityGeneration(classModel: ClassModel) : IntentBinding(classModel) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(classModel.targetTypeName, "activity")
            .doIf(classModel.argumentModels.isNotEmpty()) { addFieldSettersCode() }
            .build()!!

    override fun createStarters(variant: List<ArgumentModel>): List<MethodSpec> = listOfNotNull(
            createGetIntentMethod(variant),
            createStartActivityMethod(variant),
            createStartActivityMethodWithFlags(variant)
    ).addIf(classModel.includeStartForResult,
            createStartActivityForResultMethod(variant),
            createStartActivityForResultMethodWithFlags(variant)
    )

    override fun TypeSpec.Builder.addExtraToClass() = this
            .addMethod(createSaveMethod())
            .addNoSettersAccessors()

    private fun MethodSpec.Builder.addFieldSettersCode() {
        addStatement("\$T intent = activity.getIntent()", INTENT)
        addIntentSetters("activity")
    }

    private fun createSaveMethod(): MethodSpec = this
            .builderWithCreationBasicFieldsNoContext("save")
            .addParameter(classModel.targetTypeName, "activity")
            .doIf(classModel.savable) {
                addStatement("\$T bundle = new Bundle()", BUNDLE)
                addSaveBundleStatements("bundle", classModel.argumentModels, { "activity.${it.accessor.makeGetter()}" })
                addStatement("activity.getIntent().putExtras(bundle)")
            }
            .build()

    private fun createStartActivityMethod(variant: List<ArgumentModel>) =
            createGetIntentStarter("startActivity", variant)

    private fun createStartActivityMethodWithFlags(variant: List<ArgumentModel>) = builderWithCreationBasicFields("startWithFlags")
            .addArgParameters(variant)
            .addParameter(TypeName.INT, "flags")
            .addGetIntentStatement(variant)
            .addStatement("intent.addFlags(flags)")
            .addStatement("context.startActivity(intent)")
            .build()

    private fun createStartActivityForResultMethod(variant: List<ArgumentModel>) = builderWithCreationBasicFieldsNoContext("startForResult")
            .addParameter(ACTIVITY, "context")
            .addArgParameters(variant)
            .addParameter(TypeName.INT, "result")
            .addGetIntentStatement(variant)
            .addStatement("context.startActivityForResult(intent, result)")
            .build()

    private fun createStartActivityForResultMethodWithFlags(variant: List<ArgumentModel>) = builderWithCreationBasicFieldsNoContext("startWithFlagsForResult")
            .addParameter(ACTIVITY, "context")
            .addArgParameters(variant)
            .addParameter(TypeName.INT, "result")
            .addParameter(TypeName.INT, "flags")
            .addGetIntentStatement(variant)
            .addStatement("intent.addFlags(flags)")
            .addStatement("context.startActivityForResult(intent, result)")
            .build()

    private fun TypeSpec.Builder.addNoSettersAccessors(): TypeSpec.Builder = apply {
        classModel.argumentModels.filter { it.noSetter }.forEach { arg ->
            addMethod(buildCheckValueMethod(arg))
            addMethod(buildGetValueMethod(arg))
        }
    }

    private fun buildCheckValueMethod(arg: ArgumentModel): MethodSpec? = builderWithCreationBasicFieldsNoContext(arg.checkerName)
            .addParameter(classModel.targetTypeName, "activity")
            .returns(TypeName.BOOLEAN)
            .addStatement("\$T intent = activity.getIntent()", INTENT)
            .addStatement("return intent.hasExtra(${arg.keyFieldName})")
            .build()

    private fun buildGetValueMethod(arg: ArgumentModel): MethodSpec? = builderWithCreationBasicFieldsNoContext(arg.accessorName)
            .addParameter(classModel.targetTypeName, "activity")
            .returns(arg.typeName)
            .buildGetValueMethodBody(arg)
            .build()

    private fun MethodSpec.Builder.buildGetValueMethodBody(arg: ArgumentModel) = apply {
        val keyFieldName = arg.keyFieldName
        val possiblyWrappedValue = getIntentGetterFor(arg.saveParamType, keyFieldName)
        val valueToSet = (if (arg.paramType.typeUsedBySupertype()) "(\$T) " else "") + arg.addUnwrapper { possiblyWrappedValue }
        addStatement("\$T intent = activity.getIntent()", INTENT)
        addStatement("return $valueToSet", arg.typeName)
    }
}