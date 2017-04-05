package activitystarter.compiler.codegeneration

import activitystarter.compiler.classbinding.ClassBinding
import activitystarter.compiler.param.ArgumentBinding
import activitystarter.compiler.utils.CONTEXT
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PUBLIC

internal abstract class ClassGeneration(val classBinding: ClassBinding) {

    fun brewJava() = JavaFile.builder(classBinding.packageName, createStarterSpec())
            .addFileComment("Generated code from ActivityStarter. Do not modify!")
            .build()

    abstract fun createFillFieldsMethod(): MethodSpec

    open fun TypeSpec.Builder.addExtraToClass(): TypeSpec.Builder = this

    abstract fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec>

    protected fun getBasicFillMethodBuilder(fillProperCall: String = "ActivityStarter.fill(this)"): MethodSpec.Builder = MethodSpec
            .methodBuilder("fill")
            .addJavadoc("This is method used to fill fields. Use it by calling $fillProperCall.")
            .addModifiers(PUBLIC, Modifier.STATIC)

    protected fun builderWithCreationBasicFields(name: String) =
            builderWithCreationBasicFieldsNoContext(name)
                    .addParameter(CONTEXT, "context")

    protected fun builderWithCreationBasicFieldsNoContext(name: String) =
            MethodSpec.methodBuilder(name)
                    .addModifiers(PUBLIC, Modifier.STATIC)

    protected fun MethodSpec.Builder.addArgParameters(variant: List<ArgumentBinding>) = apply {
        variant.forEach { arg -> addParameter(arg.typeName, arg.name) }
    }

    protected fun MethodSpec.Builder.addSaveBundleStatements(bundleName: String, variant: List<ArgumentBinding>, argumentGetByName: (ArgumentBinding) -> String) = apply {
        variant.forEach { arg ->
            val bundleSetter = getBundleSetterFor(arg.paramType)
            addStatement("$bundleName.$bundleSetter(\"" + arg.key + "\", " + argumentGetByName(arg) + ")")
        }
    }

    protected fun MethodSpec.Builder.addBundleSetters(bundleName: String, className: String, checkIfSet: Boolean) = apply {
        classBinding.argumentBindings.forEach { arg -> addBundleSetter(arg, bundleName, className, checkIfSet) }
    }

    protected fun MethodSpec.Builder.addBundleSetter(arg: ArgumentBinding, bundleName: String, className: String, checkIfSet: Boolean) {
        val keyName = arg.key
        val
                bundleGetter = getBundleGetter(bundleName, arg.paramType, arg.typeName, keyName)
        val settingPart = arg.accessor.setToField(bundleGetter)
        if (checkIfSet) addCode("if(${getBundlePredicate(bundleName, keyName)}) ")
        addStatement("$className.$settingPart")
    }

    protected fun getBundlePredicate(bundleName: String, keyName: String) = "$bundleName.containsKey(\"$keyName\")"

    private fun createStarterSpec() = TypeSpec
            .classBuilder(classBinding.bindingClassName.simpleName())
            .addModifiers(PUBLIC, FINAL)
            .addClassMethods()
            .build()

    private fun TypeSpec.Builder.addClassMethods() = this
            .addMethod(createFillFieldsMethod())
            .addExtraToClass()
            .addMethods(classBinding.argumentBindingVariants.flatMap { variant -> createStarters(variant) })
}