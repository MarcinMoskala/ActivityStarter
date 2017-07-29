package activitystarter.compiler.processing

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import activitystarter.NonSavable
import activitystarter.compiler.error.Errors
import activitystarter.compiler.error.parsingError
import activitystarter.compiler.model.ProjectConfig
import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.classbinding.KnownClassType
import activitystarter.compiler.model.classbinding.KnownClassType.Companion.getByType
import activitystarter.compiler.model.param.ArgumentModel
import activitystarter.compiler.utils.getElementType
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

internal class ClassBindingFactory(val typeElement: TypeElement, val config: ProjectConfig) {

    fun create(): ClassModel? {
        val knownClassType: KnownClassType = getKnownClassType() ?: return null
        val targetTypeName = getTargetTypeName(typeElement)
        val bindingClassName = activitystarter.compiler.generation.getBindingClassName(typeElement)
        val packageName = bindingClassName.packageName()
        val argumentFactory = ArgumentFactory(typeElement, config)
        val enclosedElements = typeElement.enclosedElements
        val argumentFieldBindings = getArgumentFieldBindings(enclosedElements, argumentFactory, packageName, knownClassType)
        val argumentDelegateBindings = getArgumentDelegateBindings(enclosedElements, argumentFactory, packageName, knownClassType)
        val argumentBindings = argumentFieldBindings + argumentDelegateBindings
        val savable = typeElement.getAnnotation(NonSavable::class.java) == null
        val addStartForResult = typeElement.getAnnotation(MakeActivityStarter::class.java)?.includeStartForResult ?: false
        return ClassModel(knownClassType, targetTypeName, bindingClassName, packageName, argumentBindings, savable, addStartForResult)
    }

    private fun getKnownClassType(): KnownClassType? {
        val knownClassType: KnownClassType? = getByType(getElementType(typeElement))
        val error = getClassError(knownClassType)
        if (error != null) {
            parsingError<MakeActivityStarter>(error, typeElement, typeElement)
            return null
        }
        return knownClassType
    }

    private fun getArgumentFieldBindings(enclosedElements: List<Element>, argumentFactory: ArgumentFactory, packageName: String, knownClassType: KnownClassType): List<ArgumentModel> =
            enclosedElements
                    .filter { it.getAnnotation(Arg::class.java) != null }
                    .map { argumentFactory.create(it, packageName, knownClassType) }
                    .filterNotNull()

    private fun getArgumentDelegateBindings(enclosedElements: List<Element>, argumentFactory: ArgumentFactory, packageName: String?, knownClassType: KnownClassType): List<ArgumentModel> =
            enclosedElements
                    .map { it.simpleName.toString() }
                    .filter { it.endsWith("\$delegate") }
                    .map { argumentFactory.createFromDelegate(it.substringBefore('$'), enclosedElements, packageName, knownClassType) }
                    .filterNotNull()

    private fun getClassError(elementType: KnownClassType?) = when {
        elementType == null -> Errors.wrongClassType
        else -> null
    }

    private fun getTargetTypeName(enclosingElement: TypeElement) = TypeName
            .get(enclosingElement.asType())
            .let { if (it is ParameterizedTypeName) it.rawType else it }
}