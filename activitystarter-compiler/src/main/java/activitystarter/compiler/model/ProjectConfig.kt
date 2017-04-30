package activitystarter.compiler.model

import javax.lang.model.type.TypeMirror

data class ProjectConfig(val converters: List<ConverterModel> = listOf()) {

    fun converterFor(type: TypeMirror): ConverterModel? {
        return converters.firstOrNull { it.canWrap(type) }
    }
}