package activitystarter.compiler.model

import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.wrapping.ArgWrapper
import kotlin.reflect.KClass

data class ProjectModel (
        val converters: List<ConverterModel> = listOf(),
        val classesToProces: Set<ClassModel>
)