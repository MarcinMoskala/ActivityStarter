package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.TypeElement

internal class BroadcastReceiverBinding(element: TypeElement) : IntentBinding(element) {

    override fun createFillFieldsMethod() = fillByIntentBinding("receiver")

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant)
    )
}