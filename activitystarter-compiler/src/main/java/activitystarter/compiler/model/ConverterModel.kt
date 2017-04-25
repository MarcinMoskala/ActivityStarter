package activitystarter.compiler.model

import activitystarter.compiler.model.param.ParamType
import activitystarter.compiler.utils.isSubtypeOfType
import java.lang.reflect.Type
import javax.lang.model.type.TypeMirror


data class ConverterModel(
        val className: String,
        val typeFrom: TypeMirror,
        val typeTo: TypeMirror
) {

    val toParamType: ParamType?
        get() = ParamType.fromType(typeTo)

    fun canApplyTo(type: TypeMirror): Boolean {
        return ParamType.fromType(type) == ParamType.fromType(typeTo) || type.isSubtypeOfType(typeFrom.toString())
    }
}