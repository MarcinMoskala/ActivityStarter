package activitystarter.compiler.classbinding

import activitystarter.Arg
import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.createSublists
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

    protected fun getKey(name: String) = name + "StarterKey"

    protected fun getBasicFillMethodBuilder(fillProperCall: String = "ActivityStarter.fill(this)"): MethodSpec.Builder =
            MethodSpec.methodBuilder("fill")
                    .addJavadoc("This is method used to fill fields. Use it by calling $fillProperCall.")
                    .addModifiers(PUBLIC, Modifier.STATIC)

    private fun getBindingClassName(enclosingElement: TypeElement): ClassName {
        val packageName = getPackage(enclosingElement).qualifiedName.toString()
        val className = enclosingElement.qualifiedName.toString().substring(packageName.length + 1)
        return ClassName.get(packageName, className + "Starter")
    }

    private fun getTargetTypeName(enclosingElement: TypeElement) = TypeName.get(enclosingElement.asType())
            .let { if (it is ParameterizedTypeName) it.rawType else it }

    fun brewJava(): JavaFile {
        return JavaFile.builder(bindingClassName.packageName(), createActivityStarterSpec())
                .addFileComment("Generated code from ActivityStarter. Do not modify!")
                .build()
    }

    private fun createActivityStarterSpec(): TypeSpec {
        val result = TypeSpec
                .classBuilder(bindingClassName.simpleName())
                .addModifiers(PUBLIC, FINAL)
                .addMethod(createFillFieldsMethod())

        for (variant in variants) {
            result.addMethods(createStarters(variant))
        }

        return result.build()
    }

    abstract fun createFillFieldsMethod(): MethodSpec
    abstract fun createStarters(variant: List<ArgumentBinding>): List<MethodSpec>
}
