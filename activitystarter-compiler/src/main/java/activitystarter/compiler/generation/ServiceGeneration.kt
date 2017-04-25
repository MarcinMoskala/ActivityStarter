package activitystarter.compiler.generation

import activitystarter.compiler.model.ProjectModel
import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ArgumentModel
import com.squareup.javapoet.MethodSpec

internal class ServiceGeneration(projectModel: ProjectModel, classModel: ClassModel) : IntentBinding(projectModel, classModel) {

    override fun createFillFieldsMethod(): MethodSpec = fillByIntentBinding("service")

    override fun createStarters(variant: List<ArgumentModel>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant),
            createStartServiceMethod(variant)
    )

    private fun createStartServiceMethod(variant: List<ArgumentModel>) =
            createGetIntentStarter("startService", variant)
}