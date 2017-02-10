package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.TypeElement

internal class ActivityBinding(element: TypeElement) : IntentBinding(element) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(targetTypeName, "activity")
            .doIf(argumentBindings.isNotEmpty()) { addStatement("Intent intent = activity.getIntent()") }
            .addSetters("activity")
            .build()

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant),
            createStartActivityMethod(variant),
            createStartActivityMethodWithFlags(variant)
    )

    private fun createStartActivityMethod(variant: List<ArgumentBinding>)
            = createGetIntentStarter("startActivity", variant)

    private fun createStartActivityMethodWithFlags(variant: List<ArgumentBinding>)
            = builderWitGetIntentWithFlags(variant)
            .addStatement("context.startActivity(intent)")
            .build()
}