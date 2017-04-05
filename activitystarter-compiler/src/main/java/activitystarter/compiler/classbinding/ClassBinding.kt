package activitystarter.compiler.classbinding

import activitystarter.compiler.codegeneration.*
import activitystarter.compiler.param.ArgumentBinding
import activitystarter.compiler.utils.createSublists
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

class ClassBinding(
        private val knownClassType: KnownClassType,
        val targetTypeName: TypeName,
        val bindingClassName: ClassName,
        val packageName: String,
        val argumentBindings: List<ArgumentBinding>,
        val savable: Boolean,
        val includeStartForResult: Boolean
) {

    val argumentBindingVariants: List<List<ArgumentBinding>>
        get() = argumentBindings
                .createSublists { it.isOptional }
                .distinctBy { it.map { it.typeName } }

    internal fun getClasGeneration(): ClassGeneration = when (knownClassType) {
        KnownClassType.Activity -> ActivityGeneration(this)
        KnownClassType.Fragment -> FragmentGeneration(this)
        KnownClassType.Service -> ServiceGeneration(this)
        KnownClassType.BroadcastReceiver -> BroadcastReceiverGeneration(this)
    }
}