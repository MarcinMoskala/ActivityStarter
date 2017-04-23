package activitystarter.compiler.processing

import activitystarter.Arg
import activitystarter.Optional
import activitystarter.compiler.error.Errors
import activitystarter.compiler.error.error
import activitystarter.compiler.model.classbinding.KnownClassType
import activitystarter.compiler.model.param.ArgumentModel
import activitystarter.compiler.model.param.FieldAccessor
import activitystarter.compiler.model.param.ParamType
import activitystarter.compiler.utils.getElementType
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

class ArgumentFactory(val enclosingElement: TypeElement) {

    fun create(element: Element, packageName: String, knownClassType: KnownClassType): ArgumentModel? {
        val elementType: TypeMirror = getElementType(element)
        val paramType: ParamType? = ParamType.fromType(elementType)
        val error = getFieldError(element, knownClassType, paramType)
        if (error != null) {
            showProcessingError(element, error)
            return null
        }
        paramType!!
        val name: String = element.simpleName.toString()
        val keyFromAnnotation = element.getAnnotation(Arg::class.java)?.key
        val defaultKey = "$packageName.${name}StarterKey"
        val key: String = if (keyFromAnnotation.isNullOrBlank()) defaultKey else keyFromAnnotation!!
        val typeName: TypeName = TypeName.get(elementType)
        val isOptional: Boolean = element.getAnnotation(Optional::class.java) != null
        val accessor: FieldAccessor = FieldAccessor(element)
        return ArgumentModel(name, key, paramType, typeName, isOptional, accessor)
    }

    private fun getFieldError(element: Element, knownClassType: KnownClassType, paramTypeNullable: ParamType?) = when {
        enclosingElement.kind != ElementKind.CLASS -> Errors.notAClass
        enclosingElement.modifiers.contains(Modifier.PRIVATE) -> Errors.privateClass
        paramTypeNullable == null -> Errors.notSupportedType
        !FieldAccessor(element).isAccessible() -> Errors.inaccessibleField
        paramTypeNullable.typeUsedBySupertype() && knownClassType == KnownClassType.BroadcastReceiver -> Errors.notBasicTypeInReceiver
        else -> null
    }

    private fun showProcessingError(element: Element, text: String) {
        error(enclosingElement, "@%s %s $text (%s)", Arg::class.java.simpleName, enclosingElement.qualifiedName, element.simpleName)
    }
}