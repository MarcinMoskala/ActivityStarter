package activitystarter.compiler.codegeneration

import activitystarter.compiler.classbinding.ClassBinding
import activitystarter.compiler.param.ArgumentBinding
import activitystarter.compiler.utils.BUNDLE
import activitystarter.compiler.utils.doIf
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeName.BOOLEAN
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.STATIC

internal class ActivityGeneration(classBinding: ClassBinding) : IntentBinding(classBinding) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(classBinding.targetTypeName, "activity")
            .addParameter(BUNDLE, "savedInstanceState")
            .doIf(classBinding.argumentBindings.isNotEmpty()) { addFieldSettersCode() }
            .build()!!

    override fun TypeSpec.Builder.addExtraToClass() = this
            .addMethod(createSaveMethod())

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant),
            createStartActivityMethod(variant),
            createStartActivityMethodWithFlags(variant)
    )

    private fun MethodSpec.Builder.addFieldSettersCode() {
        addStatement("Intent intent = activity.getIntent()")
        if (savable) {
            for (arg in classBinding.argumentBindings) {
                val bundleName = "savedInstanceState"
                val bundlePredicate = getBundlePredicate(arg, bundleName)
                addCode("if($bundleName != null && $bundlePredicate) {\n")
                addBundleSetter(arg, bundleName, "activity", false)
                addCode("} else {\n")
                addIntentSetter(arg, "activity")
                addCode("}\n")
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
                addSaveBundleStatements("bundle", argumentBindings, { "activity.${it.accessor.getFieldValue()}" })
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
}