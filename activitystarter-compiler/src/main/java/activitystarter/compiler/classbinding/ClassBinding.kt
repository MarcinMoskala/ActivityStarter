package activitystarter.compiler.classbinding

import activitystarter.Arg
import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.CONTEXT
import activitystarter.compiler.createSublists
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.TypeElement

internal abstract class ClassBinding(enclosingElement: TypeElement) {

    protected val targetTypeName = getTargetTypeName(enclosingElement)
    protected val bindingClassName = getBindingClassName(enclosingElement)
    protected val argumentBindings: List<ArgumentBinding> = enclosingElement.enclosedElements
            .filter { it.getAnnotation(Arg::class.java) != null }
            .map(::ArgumentBinding)
    val variants = argumentBindings.createSublists { it.isOptional }
            .distinctBy { it.map { it.type } }

    fun brewJava() = JavaFile.builder(bindingClassName.packageName(), createActivityStarterSpec())
            .addFileComment("Generated code from ActivityStarter. Do not modify!")
            .build()

    abstract fun createFillFieldsMethod(): MethodSpec

    open fun TypeSpec.Builder.addExtraToClass(): TypeSpec.Builder = this

    abstract fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec>

    protected fun getKey(name: String) = name + "StarterKey"

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
        variant.forEach { arg -> addParameter(arg.type, arg.name) }
    }

    protected fun MethodSpec.Builder.addSaveBundleStatements(bundleName: String, variant: List<ArgumentBinding>, argumentGetByName: (ArgumentBinding) -> String) = apply {
        variant.forEach { arg -> addStatement("$bundleName.${getBundleSetterFor(arg)}(\"" + getKey(arg.name) + "\", " + argumentGetByName(arg) + ")") }
    }

    protected fun MethodSpec.Builder.addBundleSetters(bundleName: String, className: String) = apply {
        for (arg in argumentBindings) {
            val fieldName = arg.name
            val keyName = getKey(fieldName)
            val settingPart = arg.accessor.setToField(getBundleGetterFor(bundleName, arg, keyName))
            addStatement("if($bundleName.containsKey(\"$keyName\")) $className.$settingPart")
        }
    }

    private fun getTargetTypeName(enclosingElement: TypeElement) = TypeName
            .get(enclosingElement.asType())
            .let { if (it is ParameterizedTypeName) it.rawType else it }

    private fun createActivityStarterSpec() = TypeSpec
            .classBuilder(bindingClassName.simpleName())
            .addModifiers(PUBLIC, FINAL)
            .addClassMethods()
            .build()

    private fun TypeSpec.Builder.addClassMethods() = this
            .addMethod(createFillFieldsMethod())
            .addExtraToClass()
            .addMethods(variants.flatMap { variant -> createStarters(variant) })
}