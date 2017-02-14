package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.BUNDLE
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeName.BOOLEAN
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement

internal class ActivityBinding(element: TypeElement) : IntentBinding(element) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(targetTypeName, "activity")
            .addParameter(BUNDLE, "savedInstanceState")
            .doIf(argumentBindings.isNotEmpty()) {
                addCode("if(savedInstanceState == null || !saved) {\n")
                doIf(argumentBindings.isNotEmpty()) { addStatement("Intent intent = activity.getIntent()") }
                addIntentSetters("activity")
                addCode("} else {\n")
                addBundleSetters("savedInstanceState", "activity")
                addCode("}\n")
            }
            .build()

    override fun TypeSpec.Builder.addExtraToClass() = this
            .addMethod(createSaveMethod())
            .addField(createSavedField())

    private fun createSaveMethod(): MethodSpec = this
            .builderWithCreationBasicFieldsNoContext("save")
            .addParameter(targetTypeName, "activity")
            .addParameter(BUNDLE, "bundle")
            .addSaveBundleStatements("bundle", argumentBindings, { "activity.${it.accessor.getFieldValue()}" })
            .addStatement("saved = true")
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