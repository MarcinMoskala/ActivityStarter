package activitystarter.compiler.classbinding

import activitystarter.Arg
import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.CONTEXT
import activitystarter.compiler.createSublists
import activitystarter.compiler.isSubtypeOfType
import com.google.auto.common.MoreElements.getPackage
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

    open fun createExtraMethods(): List<MethodSpec> = listOf()

    abstract fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec>

    protected fun getKey(name: String) = name + "StarterKey"

    protected fun getBasicFillMethodBuilder(fillProperCall: String = "ActivityStarter.fill(this)"): MethodSpec.Builder =
            MethodSpec.methodBuilder("fill")
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

    protected fun MethodSpec.Builder.addSaveBundleStatements(bundleName: String, variant: List<ArgumentBinding>, argumentGetByName: (ArgumentBinding)->String) = apply {
        variant.forEach { arg -> addStatement("$bundleName.${getBundleSetterFor(arg)}(\"" + getKey(arg.name) + "\", " + argumentGetByName(arg) + ")") }
    }

    protected inline fun MethodSpec.Builder.doIf(check: Boolean, f: MethodSpec.Builder.() -> Unit) = apply {
        if (check) f()
    }

    protected fun MethodSpec.Builder.addBundleSetters(bundleName: String, className: String) = apply {
        for (arg in argumentBindings) {
            val fieldName = arg.name
            val keyName = getKey(fieldName)
            val settingPart = arg.accessor.setToField(getBundleGetterFor(bundleName, arg, keyName))
            addStatement("if($bundleName.containsKey(\"$keyName\")) $className.$settingPart")
        }
    }

    private fun getBundleGetterFor(bundleName: String, arg: ArgumentBinding, keyName: String) = when (arg.type) {
        TypeName.get(String::class.java) -> "$bundleName.getString(\"$keyName\")"
        TypeName.INT -> "$bundleName.getInt(\"$keyName\", -1)"
        TypeName.FLOAT -> "$bundleName.getFloat(\"$keyName\", -1F)"
        TypeName.BOOLEAN -> "$bundleName.getBoolean(\"$keyName\", false)"
        TypeName.DOUBLE -> "$bundleName.getDouble(\"$keyName\", -1D)"
        TypeName.CHAR -> "$bundleName.getChar(\"$keyName\", 'a')"
        else -> getBundleGetterForNonTrival(bundleName, arg, keyName)
    }

    protected fun getBundleSetterFor(arg: ArgumentBinding) = when (arg.type) {
        TypeName.get(String::class.java) -> "putString"
        TypeName.INT -> "putInt"
        TypeName.FLOAT -> "putFloat"
        TypeName.BOOLEAN -> "putBoolean"
        TypeName.DOUBLE -> "putDouble"
        TypeName.CHAR -> "putChar"
        else -> getBundleSetterForNonTrivial(arg)
    }

    private fun getBundleGetterForNonTrival(bundleName: String, arg: ArgumentBinding, keyName: String) = when {
        arg.elementType.isSubtypeOfType("android.os.Parcelable") -> "(${arg.type}) $bundleName.getParcelable(\"$keyName\")"
        arg.elementType.isSubtypeOfType("java.io.Serializable") -> "(${arg.type}) $bundleName.getSerializable(\"$keyName\")"
        else -> throw Error("Illegal field type" + arg.type)
    }

    private fun getBundleSetterForNonTrivial(arg: ArgumentBinding) = when {
        arg.elementType.isSubtypeOfType("android.os.Parcelable") -> "putParcelable"
        arg.elementType.isSubtypeOfType("java.io.Serializable") -> "putSerializable"
        else -> throw Error("Illegal field type" + arg.type)
    }

    private fun getBindingClassName(enclosingElement: TypeElement): ClassName {
        val packageName = getPackage(enclosingElement).qualifiedName.toString()
        val className = enclosingElement.qualifiedName.toString().substring(packageName.length + 1)
        return ClassName.get(packageName, className + "Starter")
    }

    private fun getTargetTypeName(enclosingElement: TypeElement) = TypeName.get(enclosingElement.asType())
            .let { if (it is ParameterizedTypeName) it.rawType else it }

    private fun createActivityStarterSpec(): TypeSpec {
        val result = TypeSpec
                .classBuilder(bindingClassName.simpleName())
                .addModifiers(PUBLIC, FINAL)
                .addMethod(createFillFieldsMethod())
                .addMethods(createExtraMethods())

        for (variant in variants) {
            result.addMethods(createStarters(variant))
        }

        return result.build()
    }
}
