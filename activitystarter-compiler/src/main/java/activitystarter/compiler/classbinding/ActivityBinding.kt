package activitystarter.compiler.classbinding

import activitystarter.NonSavable
import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.BUNDLE
import activitystarter.compiler.doIf
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeName.BOOLEAN
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement

internal class ActivityBinding(element: TypeElement) : IntentBinding(element) {

    val savable: Boolean = element.getAnnotation(NonSavable::class.java) == null

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(targetTypeName, "activity")
            .addParameter(BUNDLE, "savedInstanceState")
            .doIf(argumentBindings.isNotEmpty()) { addFieldSettersCode() }
            .build()!!

    private fun MethodSpec.Builder.addFieldSettersCode() {
        if (savable) {
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
            .doIf(savable) { addField(createSavedField()) }

    private fun createSaveMethod(): MethodSpec = this
            .builderWithCreationBasicFieldsNoContext("save")
            .addParameter(targetTypeName, "activity")
            .addParameter(BUNDLE, "bundle")
            .doIf(savable) {
                addSaveBundleStatements("bundle", argumentBindings, { "activity.${it.accessor.getFieldValue()}" })
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