package activitystarter.compiler.generation

import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ArgumentModel
import activitystarter.compiler.utils.CONTEXT
import activitystarter.compiler.utils.STRING
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier
import javax.lang.model.element.Modifier.*

internal abstract class ClassGeneration(val classModel: ClassModel) {

    fun brewJava() = JavaFile.builder(classModel.packageName, createStarterSpec())
            .addFileComment("Generated code from ActivityStarter. Do not modify!")
            .build()!!

    abstract fun createFillFieldsMethod(): MethodSpec

    open fun TypeSpec.Builder.addExtraToClass(): TypeSpec.Builder = this

    abstract fun createStarters(variant: List<ArgumentModel>): List<MethodSpec>

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

    protected fun MethodSpec.Builder.addArgParameters(variant: List<ArgumentModel>) = apply {
        variant.forEach { arg -> addParameter(arg.typeName, arg.name) }
    }

    protected fun MethodSpec.Builder.addSaveBundleStatements(bundleName: String, variant: List<ArgumentModel>, argumentGetByName: (ArgumentModel) -> String) = apply {
        variant.forEach { arg ->
            val value = arg.addWrapper { argumentGetByName(arg) }
            addStatement("$bundleName.${getBundleSetterFor(arg.saveParamType)}(${arg.fieldName}, $value)")
        }
    }

    protected fun MethodSpec.Builder.addBundleSetters(bundleName: String, className: String, checkIfSet: Boolean) = apply {
        classModel.argumentModels.forEach { arg -> addBundleSetter(arg, bundleName, className, checkIfSet) }
    }

    protected fun MethodSpec.Builder.addBundleSetter(arg: ArgumentModel, bundleName: String, className: String, checkIfSet: Boolean) {
        val fieldName = arg.fieldName
        val bundleValue = (if (arg.paramType.typeUsedBySupertype()) "(\$T) " else "") +
                arg.addUnwrapper { getBundleGetter(bundleName, arg.saveParamType, fieldName) }
        val bundleValueSetter = arg.accessor.makeSetter(bundleValue)
        if (checkIfSet) addCode("if($bundleName != null && ${getBundlePredicate(bundleName, fieldName)}) ")
        addStatement("$className.$bundleValueSetter", arg.typeName)
    }

    protected fun getBundlePredicate(bundleName: String, key: String) = "$bundleName.containsKey($key)"

    private fun createStarterSpec() = TypeSpec
            .classBuilder(classModel.bindingClassName.simpleName())
            .addModifiers(PUBLIC, FINAL)
            .addKeyFields()
            .addClassMethods()
            .build()

    private fun TypeSpec.Builder.addKeyFields(): TypeSpec.Builder {
        for (arg in classModel.argumentModels) {
            val fieldSpec = FieldSpec
                    .builder(STRING, arg.fieldName, STATIC, FINAL, PRIVATE)
                    .initializer("\"${arg.key}\"")
                    .build()
            addField(fieldSpec)
        }
        return this
    }

    private fun TypeSpec.Builder.addClassMethods() = this
            .addMethod(createFillFieldsMethod())
            .addExtraToClass()
            .addMethods(classModel.argumentModelVariants.flatMap { variant -> createStarters(variant) })
}
