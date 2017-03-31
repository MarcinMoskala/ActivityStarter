package activitystarter.compiler.classbinding

import activitystarter.Arg
import activitystarter.NonSavable
import activitystarter.compiler.codegeneration.*
import activitystarter.compiler.param.ArgumentBinding
import activitystarter.compiler.param.ArgumentFactory
import activitystarter.compiler.utils.createSublists
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class ClassBinding(
        private val knownClassType: KnownClassType,
        val targetTypeName: TypeName,
        val bindingClassName: ClassName,
        val packageName: String,
        val argumentBindings: List<ArgumentBinding>,
        val argumentBindingVariants: List<List<ArgumentBinding>>,
        val savable: Boolean
) {

    internal fun getClasGeneration(): ClassGeneration = when (knownClassType) {
        KnownClassType.Activity -> ActivityGeneration(this)
        KnownClassType.Fragment -> FragmentGeneration(this)
        KnownClassType.Service -> ServiceGeneration(this)
        KnownClassType.BroadcastReceiver -> BroadcastReceiverGeneration(this)
    }
}