package activitystarter.compiler.classbinding

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import activitystarter.NonSavable
import activitystarter.compiler.codegeneration.getBindingClassName
import activitystarter.compiler.error.Errors
import activitystarter.compiler.error.parsingError
import activitystarter.compiler.param.ArgumentFactory
import activitystarter.compiler.utils.createSublists
import activitystarter.compiler.utils.getElementType
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

internal class ClassBindingFactory(val typeElement: TypeElement) {

    fun create(): ClassBinding? {
        val knownClassType: KnownClassType? = KnownClassType.getByType(getElementType(typeElement))
        val error = getClassError(knownClassType)
        if(error != null) {
            parsingError<MakeActivityStarter>(error, typeElement, typeElement)
            return null
        }
        knownClassType!!
        val targetTypeName = getTargetTypeName(typeElement)
        val bindingClassName = getBindingClassName(typeElement)
        val packageName = bindingClassName.packageName()
        val argumentFactory = ArgumentFactory(typeElement)
        val argumentBindings = typeElement.enclosedElements
                .filter { it.getAnnotation(Arg::class.java) != null }
                .map { argumentFactory.parseArgument(it, packageName, knownClassType) }
                .filterNotNull()
        val argumentBindingVariants = argumentBindings.createSublists { it.isOptional }
                .distinctBy { it.map { it.typeName } }
        val savable = typeElement.getAnnotation(NonSavable::class.java) == null

        return ClassBinding(knownClassType, targetTypeName, bindingClassName, packageName, argumentBindings, argumentBindingVariants, savable)
    }

    private fun getClassError(elementType: KnownClassType?) = when {
        elementType == null -> Errors.wrongClassType
        else -> null
    }

    private fun getTargetTypeName(enclosingElement: TypeElement) = TypeName
            .get(enclosingElement.asType())
            .let { if (it is ParameterizedTypeName) it.rawType else it }
}