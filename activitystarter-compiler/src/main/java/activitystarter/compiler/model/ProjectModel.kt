package activitystarter.compiler.model

import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ParamType
import activitystarter.wrapping.ArgConverter
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

data class ProjectModel (
        val converters: List<ConverterModel> = listOf(),
        val classesToProces: Set<ClassModel>
) {

    fun converterFor(type: ParamType): ConverterModel? {
        return converters.firstOrNull { it.canApplyTo(type) }
    }
}