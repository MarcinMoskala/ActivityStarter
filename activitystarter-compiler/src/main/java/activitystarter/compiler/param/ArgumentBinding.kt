package activitystarter.compiler.param

import com.squareup.javapoet.TypeName
import javax.lang.model.type.TypeMirror

class ArgumentBinding(
    val elementType: TypeMirror,
    val name: String,
    val key: String,
    val paramType: ParamType,
    val typeName: TypeName,
    val isOptional: Boolean,
    val accessor: FieldAccessor
)