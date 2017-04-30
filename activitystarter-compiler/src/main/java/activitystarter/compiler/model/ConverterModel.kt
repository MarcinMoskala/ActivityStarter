package activitystarter.compiler.model

import activitystarter.compiler.model.param.ParamType
import activitystarter.compiler.utils.isSubtypeOfType
import javax.lang.model.type.TypeMirror


data class ConverterModel(
        val className: String,
        val typeFrom: TypeMirror,
        val typeTo: TypeMirror
) {
    val toParamType: ParamType?
        get() = ParamType.fromType(typeTo)

    val fromParamType: ParamType?
        get() = ParamType.fromType(typeFrom)

    fun canWrap(type: TypeMirror): Boolean {
        val paramType = ParamType.fromType(type)
        return paramType == fromParamType &&
                if(paramType.typeUsedBySupertype()) type.isSubtypeOfType(typeFrom.toString()) else true
    }
}