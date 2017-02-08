package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.CONTEXT
import activitystarter.compiler.INTENT
import activitystarter.compiler.isSubtypeOfType
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement

internal class ActivityBinding(element: TypeElement) : IntentBinding(element) {

    override fun createFillFieldsMethod(): MethodSpec {
        val builder = getBasicFillMethodBuilder()
                .addParameter(targetTypeName, "activity")

        if (argumentBindings.isNotEmpty())
            builder.addStatement("Intent intent = activity.getIntent()")

        builder.addSetters("activity")
        return builder.build()
    }

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant),
            createStartActivityMethod(variant),
            createStartActivityMethodWithFlags(variant)
    )

    private fun createStartActivityMethod(variant: List<ArgumentBinding>)
            = createGetIntentStarter("startActivity", variant)

    private fun createStartActivityMethodWithFlags(variant: List<ArgumentBinding>): MethodSpec {
        val builder = builderWitGetIntentWithFlags(variant)
        builder.addStatement("context.startActivity(intent)")
        return builder.build()
    }
}