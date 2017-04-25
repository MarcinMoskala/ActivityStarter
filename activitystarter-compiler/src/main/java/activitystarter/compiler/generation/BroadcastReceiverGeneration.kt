package activitystarter.compiler.generation

import activitystarter.compiler.model.ProjectModel
import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ArgumentModel
import com.squareup.javapoet.MethodSpec

internal class BroadcastReceiverGeneration(projectModel: ProjectModel, classModel: ClassModel) : IntentBinding(projectModel, classModel) {

    override fun createFillFieldsMethod() = fillByIntentBinding("receiver")

    override fun createStarters(variant: List<ArgumentModel>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant)
    )
}