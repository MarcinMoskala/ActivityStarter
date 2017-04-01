package activitystarter.compiler.param

import activitystarter.Arg
import activitystarter.Optional
import activitystarter.compiler.utils.getElementType
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
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