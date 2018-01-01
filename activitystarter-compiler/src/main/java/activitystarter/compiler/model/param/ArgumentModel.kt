package activitystarter.compiler.model.param

import activitystarter.ActivityStarterNameConstruction
import activitystarter.compiler.generation.ConverterGeneration
import activitystarter.compiler.generation.ParcelerGeneration
import activitystarter.compiler.model.ConverterModel
import activitystarter.compiler.utils.camelCaseToUppercaseUnderscore
import com.squareup.javapoet.TypeName

class ArgumentModel(
        val name: String,
        val key: String,
        val paramType: ParamType,
        val typeName: TypeName,
        val saveParamType: ParamType,
        val isOptional: Boolean,
        val accessor: FieldAccessor,
        private val converter: ConverterModel?,
        val parceler: Boolean
) {
    val keyFieldName: String by lazy { camelCaseToUppercaseUnderscore(name) + "_KEY" }
    val accessorName = ActivityStarterNameConstruction.getterFieldAccessorName(name)
    val checkerName = ActivityStarterNameConstruction.getterFieldCheckerName(name)

    fun addUnwrapper(body: () -> String) = when {
        parceler -> ParcelerGeneration().unwrap(body)
        converter != null -> ConverterGeneration(converter).unwrap(body)
        else -> body()
    }

    fun addWrapper(body: () -> String) = when {
        parceler -> ParcelerGeneration().wrap(body)
        converter != null -> ConverterGeneration(converter).wrap(body)
        else -> body()
    }
}