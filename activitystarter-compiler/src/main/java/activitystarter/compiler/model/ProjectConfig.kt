package activitystarter.compiler.model

import activitystarter.compiler.model.param.ParamType

data class ProjectConfig(val converters: List<ConverterModel> = listOf()) {

    fun converterFor(type: ParamType): ConverterModel? {
        return converters.firstOrNull { it.canWrap(type) }
    }
}