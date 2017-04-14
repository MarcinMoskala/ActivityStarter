package activitystarter.compiler.param

import activitystarter.compiler.utils.camelCaseToUppercaseUnderscore
import com.squareup.javapoet.TypeName
import javax.lang.model.type.TypeMirror

class ArgumentBinding(
    val name: String,
    val key: String,
    val paramType: ParamType,
    val typeName: TypeName,
    val isOptional: Boolean,
    val accessor: FieldAccessor
) {
    val fieldName: String by lazy { camelCaseToUppercaseUnderscore(name) + "_KEY" }
}