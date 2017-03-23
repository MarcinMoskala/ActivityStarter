package activitystarter.compiler.classbinding

import activitystarter.MakeActivityStarter
import activitystarter.NonSavable
import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.BUNDLE
import activitystarter.compiler.doIf
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

    val savable: Boolean = element.getAnnotation(NonSavable::class.java) == null

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(targetTypeName, "activity")
            .addParameter(BUNDLE, "savedInstanceState")
            .doIf(argumentBindings.isNotEmpty()) { addFieldSettersCode() }
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
            for (arg in argumentBindings) {
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
            .addParameter(targetTypeName, "activity")
            .addParameter(BUNDLE, "bundle")
            .doIf(savable) {
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