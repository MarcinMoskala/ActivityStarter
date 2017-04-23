package activitystarter.compiler.codegeneration

import activitystarter.compiler.model.classbinding.ClassBinding
import activitystarter.compiler.model.param.ArgumentBinding
import com.squareup.javapoet.MethodSpec

internal class BroadcastReceiverGeneration(classBinding: ClassBinding) : IntentBinding(classBinding) {

    override fun createFillFieldsMethod() = fillByIntentBinding("receiver")

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant)
    )
}