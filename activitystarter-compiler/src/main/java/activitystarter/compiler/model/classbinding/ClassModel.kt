package activitystarter.compiler.model.classbinding

import activitystarter.compiler.generation.*
import activitystarter.compiler.model.classbinding.KnownClassType.*
import activitystarter.compiler.model.param.ArgumentModel
import activitystarter.compiler.utils.createSublists
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

class ClassModel(
        private val knownClassType: KnownClassType,
        val targetTypeName: TypeName,
        val bindingClassName: ClassName,
        val packageName: String,
        val argumentModels: List<ArgumentModel>,
        val savable: Boolean,
        val includeStartForResult: Boolean
) {

    val argumentModelVariants: List<List<ArgumentModel>>
        get() = argumentModels
                .createSublists { it.isOptional }
                .distinctBy { it.map { it.typeName } }

    internal fun getClasGeneration(): ClassGeneration = when (knownClassType) {
        Activity -> ActivityGeneration(this)
        Fragment -> FragmentGeneration(this)
        Service -> ServiceGeneration(this)
        BroadcastReceiver -> BroadcastReceiverGeneration(this)
    }
}