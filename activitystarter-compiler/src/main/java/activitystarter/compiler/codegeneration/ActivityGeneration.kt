package activitystarter.compiler.codegeneration

import activitystarter.compiler.classbinding.ClassBinding
import activitystarter.compiler.param.ArgumentBinding
import activitystarter.compiler.utils.*
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec

internal class ActivityGeneration(classBinding: ClassBinding) : IntentBinding(classBinding) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(classBinding.targetTypeName, "activity")
            .addParameter(BUNDLE, "savedInstanceState")
            .doIf(classBinding.argumentBindings.isNotEmpty()) { addFieldSettersCode() }
            .build()!!

    override fun TypeSpec.Builder.addExtraToClass() = this
            .addMethod(createSaveMethod())

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOfNotNull(
            createGetIntentMethod(variant),
            createStartActivityMethod(variant),
            createStartActivityMethodWithFlags(variant)
    ).addIf(classBinding.includeStartForResult,
            createStartActivityForResultMethod(variant),
            createStartActivityForResultMethodWithFlags(variant)
    )

    private fun MethodSpec.Builder.addFieldSettersCode() {
        addStatement("Intent intent = activity.getIntent()")
        if (classBinding.savable) {
            for (arg in classBinding.argumentBindings) {
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
            .addParameter(classBinding.targetTypeName, "activity")
            .addParameter(BUNDLE, "bundle")
            .doIf(classBinding.savable) {
                addSaveBundleStatements("bundle", classBinding.argumentBindings, { "activity.${it.accessor.getFieldValue()}" })
            }
            .build()

    private fun createStartActivityMethod(variant: List<ArgumentBinding>) =
            createGetIntentStarter("startActivity", variant)

    private fun createStartActivityMethodWithFlags(variant: List<ArgumentBinding>) = builderWithCreationBasicFields("startWithFlags")
            .addArgParameters(variant)
            .addParameter(TypeName.INT, "flags")
            .addGetIntentStatement(variant)
            .addStatement("intent.addFlags(flags)")
            .addStatement("context.startActivity(intent)")
            .build()

    private fun createStartActivityForResultMethod(variant: List<ArgumentBinding>) = builderWithCreationBasicFieldsNoContext("startForResult")
            .addParameter(ACTIVITY, "context")
            .addArgParameters(variant)
            .addParameter(TypeName.INT, "result")
            .addGetIntentStatement(variant)
            .addStatement("context.startActivityForResult(intent, result)")
            .build()

    private fun createStartActivityForResultMethodWithFlags(variant: List<ArgumentBinding>) = builderWithCreationBasicFieldsNoContext("startWithFlagsForResult")
            .addParameter(ACTIVITY, "context")
            .addArgParameters(variant)
            .addParameter(TypeName.INT, "result")
            .addParameter(TypeName.INT, "flags")
            .addGetIntentStatement(variant)
            .addStatement("intent.addFlags(flags)")
            .addStatement("context.startActivityForResult(intent, result)")
            .build()
}