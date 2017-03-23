package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.BUNDLE
import activitystarter.compiler.doIf
import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.TypeElement

internal class FragmentBinding(element: TypeElement) : ClassBinding(element) {

    override fun createFillFieldsMethod() = getBasicFillMethodBuilder()
            .addParameter(targetTypeName, "fragment")
            .doIf(argumentBindings.isNotEmpty()) { addStatement("\$T arguments = fragment.getArguments()", BUNDLE) }
            .addBundleSetters("arguments", "fragment", true)
            .build()!!

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetFragmentMethod(variant)
    )

    private fun createGetFragmentMethod(variant: List<ArgumentBinding>) = builderWithCreationBasicFieldsNoContext("newInstance")
            .addArgParameters(variant)
            .returns(targetTypeName)
            .addGetFragmentCode(variant)
            .build()

    private fun MethodSpec.Builder.addGetFragmentCode(variant: List<ArgumentBinding>) = this
            .addStatement("\$T fragment = new \$T()", targetTypeName, targetTypeName)
            .doIf(variant.isNotEmpty()) {
                addStatement("\$T args = new Bundle()", BUNDLE)
                addSaveBundleStatements("args", variant, { it.name })
                addStatement("fragment.setArguments(args)")
            }
            .addStatement("return fragment")
}
