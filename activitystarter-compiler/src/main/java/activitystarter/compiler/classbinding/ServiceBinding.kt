package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.TypeElement

internal class ServiceBinding(element: TypeElement) : IntentBinding(element) {

    override fun createFillFieldsMethod(): MethodSpec = fillByIntentBinding("service")

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant),
            createStartServiceMethod(variant)
    )

    private fun createStartServiceMethod(variant: List<ArgumentBinding>) =
            createGetIntentStarter("startService", variant)
}