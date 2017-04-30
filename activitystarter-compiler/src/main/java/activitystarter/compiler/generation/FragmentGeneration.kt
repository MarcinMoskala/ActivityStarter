package activitystarter.compiler.generation

import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ArgumentModel
import activitystarter.compiler.utils.BUNDLE
import activitystarter.compiler.utils.doIf
import com.squareup.javapoet.MethodSpec

internal class FragmentGeneration(classModel: ClassModel) : ClassGeneration(classModel) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(classModel.targetTypeName, "fragment")
            .doIf(classModel.argumentModels.isNotEmpty()) { addStatement("\$T arguments = fragment.getArguments()", BUNDLE) }
            .addBundleSetters("arguments", "fragment", true)
            .build()!!

    override fun createStarters(variant: List<ArgumentModel>): List<MethodSpec> = listOf(
            createGetFragmentMethod(variant)
    )

    private fun createGetFragmentMethod(variant: List<ArgumentModel>) = builderWithCreationBasicFieldsNoContext("newInstance")
            .addArgParameters(variant)
            .returns(classModel.targetTypeName)
            .addGetFragmentCode(variant)
            .build()

    private fun MethodSpec.Builder.addGetFragmentCode(variant: List<ArgumentModel>) = this
            .addStatement("\$T fragment = new \$T()", classModel.targetTypeName, classModel.targetTypeName)
            .doIf(variant.isNotEmpty()) {
                addStatement("\$T args = new Bundle()", BUNDLE)
                addSaveBundleStatements("args", variant, { it.name })
                addStatement("fragment.setArguments(args)")
            }
            .addStatement("return fragment")
}
