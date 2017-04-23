package activitystarter.compiler.model

import activitystarter.compiler.model.classbinding.ClassBinding
import activitystarter.wrapping.ArgWrapper
import kotlin.reflect.KClass

data class ProjectModel (
    val converters: List<KClass<out ArgWrapper<*, *>>> = listOf(),
    val classesToProcess: Set<ClassBinding>
)