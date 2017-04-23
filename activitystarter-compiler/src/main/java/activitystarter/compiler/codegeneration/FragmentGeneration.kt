package activitystarter.compiler.codegeneration

import activitystarter.compiler.model.classbinding.ClassBinding
import activitystarter.compiler.model.param.ArgumentBinding
import activitystarter.compiler.utils.BUNDLE
import activitystarter.compiler.utils.doIf
import com.squareup.javapoet.MethodSpec

internal class FragmentGeneration(classBinding: ClassBinding) : ClassGeneration(classBinding) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(classBinding.targetTypeName, "fragment")
            .doIf(classBinding.argumentBindings.isNotEmpty()) { addStatement("\$T arguments = fragment.getArguments()", BUNDLE) }
            .addBundleSetters("arguments", "fragment", true)
            .build()!!

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetFragmentMethod(variant)
    )

    private fun createGetFragmentMethod(variant: List<ArgumentBinding>) = builderWithCreationBasicFieldsNoContext("newInstance")
            .addArgParameters(variant)
            .returns(classBinding.targetTypeName)
            .addGetFragmentCode(variant)
            .build()

    private fun MethodSpec.Builder.addGetFragmentCode(variant: List<ArgumentBinding>) = this
            .addStatement("\$T fragment = new \$T()", classBinding.targetTypeName, classBinding.targetTypeName)
            .doIf(variant.isNotEmpty()) {
                addStatement("\$T args = new Bundle()", BUNDLE)
                addSaveBundleStatements("args", variant, { it.name })
                addStatement("fragment.setArguments(args)")
            }
            .addStatement("return fragment")
}
