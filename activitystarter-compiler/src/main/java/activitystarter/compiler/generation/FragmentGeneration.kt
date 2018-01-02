package activitystarter.compiler.generation

import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ArgumentModel
import activitystarter.compiler.model.param.FieldAccessType
import activitystarter.compiler.utils.BUNDLE
import activitystarter.compiler.utils.doIf
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec

internal class FragmentGeneration(classModel: ClassModel) : ClassGeneration(classModel) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(classModel.targetTypeName, "fragment")
            .addParameter(BUNDLE, "savedInstanceState")
            .doIf(classModel.argumentModels.isNotEmpty()) {
                addFieldSettersCode()
            }
            .build()!!

    override fun createStarters(variant: List<ArgumentModel>): List<MethodSpec> = listOf(
            createGetFragmentMethod(variant)
    )

    override fun TypeSpec.Builder.addExtraToClass() = this
            .addMethod(createSaveMethod())
            .addNoSettersAccessors()

    private fun MethodSpec.Builder.addFieldSettersCode() {
        if (classModel.savable) {
            val bundleName = "savedInstanceState"
            val settableArgs = classModel.argumentModels.filter { it.accessor.isSettable() }
            val (fromGetterAccessors, byPropertyAccessors) = settableArgs.partition { it.accessor.fromGetter }
            if(byPropertyAccessors.isNotEmpty()) {
                addStatement("\$T arguments = fragment.getArguments()", BUNDLE)
                byPropertyAccessors.forEach { arg -> addArgumentAndSavedStateSetters(arg, bundleName) }
            }
            fromGetterAccessors.forEach { arg -> addSavedStateSetters(arg, bundleName) }
        } else {
            addStatement("\$T arguments = fragment.getArguments()", BUNDLE)
            addBundleSetters("arguments", "fragment", true)
        }
    }

    private fun MethodSpec.Builder.addArgumentAndSavedStateSetters(arg: ArgumentModel, bundleName: String) {
        val bundlePredicate = getBundlePredicate(bundleName, arg.keyFieldName)
        addCode("if($bundleName != null && $bundlePredicate) {\n")
        addBundleSetter(arg, bundleName, "fragment", false)
        addCode("} else {")
        addBundleSetter(arg, "arguments", "fragment", true)
        addCode("}")
    }

    private fun MethodSpec.Builder.addSavedStateSetters(arg: ArgumentModel, bundleName: String) {
        val bundlePredicate = getBundlePredicate(bundleName, arg.keyFieldName)
        addCode("if($bundleName != null && $bundlePredicate) {\n")
        addBundleSetter(arg, bundleName, "fragment", false)
        addCode("}")
    }

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
            .doIf(classModel.savable && classModel.argumentModels.isNotEmpty()) {
                addSaveBundleStatements("bundle", classModel.argumentModels, { it.accessor.makeGetter()?.let { "fragment.$it" } })
            }
            .build()

    private fun TypeSpec.Builder.addNoSettersAccessors(): TypeSpec.Builder = apply {
        classModel.argumentModels.filter { it.accessor.fromGetter }.forEach { arg ->
            addMethod(buildCheckValueMethod(arg))
            addMethod(buildGetValueMethod(arg))
        }
    }

    private fun buildCheckValueMethod(arg: ArgumentModel): MethodSpec? = builderWithCreationBasicFieldsNoContext(arg.checkerName)
            .addParameter(classModel.targetTypeName, "fragment")
            .returns(TypeName.BOOLEAN)
            .addStatement("\$T bundle = fragment.getArguments()", BUNDLE)
            .addStatement("return bundle != null && bundle.containsKey(${arg.keyFieldName})")
            .build()

    private fun buildGetValueMethod(arg: ArgumentModel): MethodSpec? = builderWithCreationBasicFieldsNoContext(arg.accessorName)
            .addParameter(classModel.targetTypeName, "fragment")
            .returns(arg.typeName)
            .buildGetValueMethodBody(arg)
            .build()

    private fun MethodSpec.Builder.buildGetValueMethodBody(arg: ArgumentModel) = apply {
        addStatement("\$T arguments = fragment.getArguments()", BUNDLE)
        val fieldName = arg.keyFieldName
        val bundleValue = (if (arg.paramType.typeUsedBySupertype()) "(\$T) " else "") +
                arg.addUnwrapper { getBundleGetter("arguments", arg.saveParamType, fieldName) }
        addStatement("return $bundleValue", arg.typeName)
    }
}
