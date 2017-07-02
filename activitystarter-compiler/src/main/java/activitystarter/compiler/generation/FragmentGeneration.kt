package activitystarter.compiler.generation

import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ArgumentModel
import activitystarter.compiler.utils.BUNDLE
import activitystarter.compiler.utils.doIf
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec

internal class FragmentGeneration(classModel: ClassModel) : ClassGeneration(classModel) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(classModel.targetTypeName, "fragment")
            .addParameter(BUNDLE, "savedInstanceState")
            .doIf(classModel.argumentModels.isNotEmpty()) {
                addFieldSettersCode()
            }
            .build()!!

    private fun MethodSpec.Builder.addFieldSettersCode() {
        addStatement("\$T arguments = fragment.getArguments()", BUNDLE)
        if (classModel.savable) {
            for (arg in classModel.argumentModels) {
                val bundleName = "savedInstanceState"
                val bundlePredicate = getBundlePredicate(bundleName, arg.fieldName)
                addCode("if($bundleName != null && $bundlePredicate) {\n")
                addBundleSetter(arg, bundleName, "fragment", false)
                addCode("} else ")
                addBundleSetter(arg, "arguments", "fragment", true)
            }
        } else {
            addBundleSetters("arguments", "fragment", true)
        }
    }
    override fun createStarters(variant: List<ArgumentModel>): List<MethodSpec> = listOf(
            createGetFragmentMethod(variant)
    )

    override fun TypeSpec.Builder.addExtraToClass() = this
            .addMethod(createSaveMethod())

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

    private fun createSaveMethod(): MethodSpec = this
            .builderWithCreationBasicFieldsNoContext("save")
            .addParameter(classModel.targetTypeName, "fragment")
            .addParameter(BUNDLE, "bundle")
            .doIf(classModel.savable) {
                addSaveBundleStatements("bundle", classModel.argumentModels, { "fragment.${it.accessor.getFieldValue()}" })
            }
            .build()
}
