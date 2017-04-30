package activitystarter.compiler.generation

import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ArgumentModel
import activitystarter.compiler.utils.ACTIVITY
import activitystarter.compiler.utils.BUNDLE
import activitystarter.compiler.utils.addIf
import activitystarter.compiler.utils.doIf
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec

internal class ActivityGeneration(classModel: ClassModel) : IntentBinding(classModel) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(classModel.targetTypeName, "activity")
            .addParameter(BUNDLE, "savedInstanceState")
            .doIf(classModel.argumentModels.isNotEmpty()) {
                addFieldSettersCode()
            }
            .build()!!

    override fun TypeSpec.Builder.addExtraToClass() = this
            .addMethod(createSaveMethod())

    override fun createStarters(variant: List<ArgumentModel>): List<MethodSpec> = listOfNotNull(
            createGetIntentMethod(variant),
            createStartActivityMethod(variant),
            createStartActivityMethodWithFlags(variant)
    ).addIf(classModel.includeStartForResult,
            createStartActivityForResultMethod(variant),
            createStartActivityForResultMethodWithFlags(variant)
    )

    private fun MethodSpec.Builder.addFieldSettersCode() {
        addStatement("Intent intent = activity.getIntent()")
        if (classModel.savable) {
            for (arg in classModel.argumentModels) {
                val bundleName = "savedInstanceState"
                val bundlePredicate = getBundlePredicate(bundleName, arg.fieldName)
                addCode("if($bundleName != null && $bundlePredicate) {\n")
                addBundleSetter(arg, bundleName, "activity", false)
                addCode("} else ")
                addIntentSetter(arg, "activity")
            }
        } else {
            addIntentSetters("activity")
        }
    }

    private fun createSaveMethod(): MethodSpec = this
            .builderWithCreationBasicFieldsNoContext("save")
            .addParameter(classModel.targetTypeName, "activity")
            .addParameter(BUNDLE, "bundle")
            .doIf(classModel.savable) {
                addSaveBundleStatements("bundle", classModel.argumentModels, { "activity.${it.accessor.getFieldValue()}" })
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
}