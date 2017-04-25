package activitystarter.compiler.model.param

import activitystarter.compiler.generation.ConverterGeneration
import activitystarter.compiler.model.ConverterModel
import activitystarter.compiler.utils.camelCaseToUppercaseUnderscore
import com.squareup.javapoet.TypeName

class ArgumentModel(
        val name: String,
        val key: String,
        val paramType: ParamType,
        val typeName: TypeName,
        val saveParamType: ParamType,
        val saveTypeName: TypeName,
        val isOptional: Boolean,
        val accessor: FieldAccessor,
        private val converter: ConverterModel?
) {
    val fieldName: String by lazy { camelCaseToUppercaseUnderscore(name) + "_KEY" }

    fun addUnwrapper(body: () -> String): String {
        converter ?: return body()
        return ConverterGeneration(converter).unwrap(body)
    }

    fun addWrapper(body: () -> String): String {
        converter ?: return body()
        return ConverterGeneration(converter).wrap(body)
    }
}