package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.BUNDLE
import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.TypeElement

internal class FragmentBinding(element: TypeElement) : ClassBinding(element) {

    override fun createFillFieldsMethod() =
            getBasicFillMethodBuilder()
                    .addParameter(targetTypeName, "fragment")
                    .doIf(argumentBindings.isNotEmpty()) { addStatement("\$T arguments = fragment.getArguments()", BUNDLE) }
                    .addBundleSetters("arguments", "fragment")
                    .build()!!

    override fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec> = listOf(
            createGetIntentMethod(variant)
    )

    private fun createGetIntentMethod(variant: List<ArgumentBinding>) =
            builderWithCreationBasicFieldsNoContext("newInstance")
                    .addArgParameters(variant)
                    .returns(targetTypeName)
                    .addStatement("\$T fragment = new \$T()", targetTypeName, targetTypeName)
                    .doIf(variant.isNotEmpty()) {
                        addStatement("\$T args = new Bundle()", BUNDLE)
                        addSaveBundleStatements("args", variant, { it.name })
                        addStatement("fragment.setArguments(args)")
                    }
                    .addStatement("return fragment")
                    .build()
}