package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.BUNDLE
import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.TypeElement

internal class ActivityBinding(element: TypeElement) : IntentBinding(element) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(targetTypeName, "activity")
            .addParameter(BUNDLE, "savedInstanceState")
            .doIf(argumentBindings.isNotEmpty()) {
                addCode("if(savedInstanceState == null) {\n")
                doIf(argumentBindings.isNotEmpty()) { addStatement("Intent intent = activity.getIntent()") }
                addIntentSetters("activity")
                addCode("} else {\n")
                addBundleSetters("savedInstanceState", "activity")
                addCode("}\n")
            }
            .build()

    override fun createExtraMethods(): List<MethodSpec> = listOf(
            createSaveMethod()
    )

    private fun createSaveMethod(): MethodSpec =
            builderWithCreationBasicFieldsNoContext("save")
                    .addParameter(targetTypeName, "activity")
                    .addParameter(BUNDLE, "bundle")
                    .addSaveBundleStatements("bundle", argumentBindings, { "activity.${it.accessor.getFieldValue()}" })
                    .build()

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant),
            createStartActivityMethod(variant),
            createStartActivityMethodWithFlags(variant)
    )

    private fun createStartActivityMethod(variant: List<ArgumentBinding>) =
            createGetIntentStarter("startActivity", variant)

    private fun createStartActivityMethodWithFlags(variant: List<ArgumentBinding>) =
            builderWitGetIntentWithFlags(variant)
                    .addStatement("context.startActivity(intent)")
                    .build()
}