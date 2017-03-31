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

    private fun MethodSpec.Builder.addFieldSettersCode() {
        if (classBinding.savable) {
            addCode("if(savedInstanceState == null || !saved) {\n")
            addFieldSettersFromIntent()
            addCode("} else {\n")
            addBundleSetters("savedInstanceState", "activity")
            addCode("}\n")
        } else {
            addFieldSettersFromIntent()
        }
    }

    private fun MethodSpec.Builder.addFieldSettersFromIntent() {
        addStatement("Intent intent = activity.getIntent()")
        addIntentSetters("activity")
    }

    override fun TypeSpec.Builder.addExtraToClass() = this
            .addMethod(createSaveMethod())
            .doIf(classBinding.savable) { addField(createSavedField()) }

    private fun createSaveMethod(): MethodSpec = this
            .builderWithCreationBasicFieldsNoContext("save")
            .addParameter(classBinding.targetTypeName, "activity")
            .addParameter(BUNDLE, "bundle")
            .doIf(classBinding.savable) {
                addSaveBundleStatements("bundle", classBinding.argumentBindings, { "activity.${it.accessor.getFieldValue()}" })
                addStatement("saved = true")
            }
            .build()

    private fun createSavedField() = FieldSpec.builder(BOOLEAN, "saved", PRIVATE, STATIC)
            .initializer("false")
            .build()

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant),
            createStartActivityMethod(variant),
            createStartActivityMethodWithFlags(variant)
    )

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