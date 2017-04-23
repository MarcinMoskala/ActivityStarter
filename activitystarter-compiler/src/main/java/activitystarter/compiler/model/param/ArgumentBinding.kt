package activitystarter.compiler.model.param

import activitystarter.compiler.utils.camelCaseToUppercaseUnderscore
import activitystarter.wrapping.ArgWrapper
import com.squareup.javapoet.TypeName
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ArgumentBinding(
        val name: String,
        val key: String,
        val paramType: ParamType,
        val typeName: TypeName,
        val isOptional: Boolean,
        val accessor: FieldAccessor,
        val converter: Class<out ArgWrapper<*, *>>? = null
) {
    val fieldName: String by lazy { camelCaseToUppercaseUnderscore(name) + "_KEY" }
    val genericTypes: Pair<Type, Type>? by lazy {
        converter?.javaClass
                ?.genericInterfaces
                ?.filterIsInstance<ParameterizedType>()
                ?.map { it.actualTypeArguments }
                ?.filter { it.size == 2 }
                ?.map { all -> all[0] to all[1] }
                ?.firstOrNull()
    }
}