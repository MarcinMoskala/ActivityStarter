package activitystarter.compiler.classbinding

import activitystarter.compiler.ArgumentBinding
import activitystarter.compiler.CONTEXT
import activitystarter.compiler.INTENT
import activitystarter.compiler.isSubtypeOfType
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement

internal abstract class IntentBinding(element: TypeElement) : ClassBinding(element) {

    protected fun fillByIntentBinding(targetName: String) =
            getBasicFillMethodBuilder("ActivityStarter.fill(this, intent)")
                    .addParameter(targetTypeName, targetName)
                    .addParameter(INTENT, "intent")
                    .addSetters(targetName)
                    .build()!!

    protected fun createGetIntentMethod(variant: List<ArgumentBinding>) =
            builderWithCreationBasicFields("getIntent")
                    .addArgParameters(variant)
                    .returns(INTENT)
                    .addStatement("\$T intent = new Intent(context, \$T.class)", INTENT, targetTypeName)
                    .addPutExtraStatement(variant)
                    .addStatement("return intent")
                    .build()

    private fun MethodSpec.Builder.addPutExtraStatement(variant: List<ArgumentBinding>) = apply {
        variant.forEach { arg -> addStatement("intent.putExtra(\"" + getKey(arg.name) + "\", " + arg.name + ")") }
    }

    protected fun MethodSpec.Builder.addSetters(targetParameterName: String) = apply {
        for (arg in argumentBindings) {
            val keyName = getKey(arg.name)
            val settingPart = arg.accessor.setToField(getIntentGetterFor(arg, keyName))
            addStatement("if(intent.hasExtra(\"$keyName\")) $targetParameterName.$settingPart")
        }
    }

    protected fun createGetIntentStarter(starterFunc: String, variant: List<ArgumentBinding>)
            = builderWithCreationBasicFields("start")
            .addArgParameters(variant)
            .addGetIntentStatement(variant)
            .addStatement("context.$starterFunc(intent)")
            .build()

    protected fun builderWitGetIntentWithFlags(variant: List<ArgumentBinding>)
            = builderWithCreationBasicFields("startWithFlags")
            .addArgParameters(variant)
            .addParameter(TypeName.INT, "flags")
            .addGetIntentStatement(variant)
            .addStatement("intent.addFlags(flags)")

    protected fun MethodSpec.Builder.addGetIntentStatement(variant: List<ArgumentBinding>) = apply {
        if (variant.isEmpty())
            addStatement("\$T intent = getIntent(context)", INTENT)
        else {
            val intentArguments = variant.joinToString(separator = ", ", transform = { it.name })
            addStatement("\$T intent = getIntent(context, $intentArguments)", INTENT)
        }
    }

    protected fun getIntentGetterFor(arg: ArgumentBinding, keyName: String) = when (arg.type) {
        TypeName.get(String::class.java) -> "intent.getStringExtra(\"$keyName\")"
        TypeName.INT -> "intent.getIntExtra(\"$keyName\", -1)"
        TypeName.FLOAT -> "intent.getFloatExtra(\"$keyName\", -1F)"
        TypeName.BOOLEAN -> "intent.getBooleanExtra(\"$keyName\", false)"
        TypeName.DOUBLE -> "intent.getDoubleExtra(\"$keyName\", -1D)"
        TypeName.CHAR -> "intent.getCharExtra(\"$keyName\", 'a')"
        else -> getIntentGetterForNotTrival(arg, keyName)
    }

    private fun getIntentGetterForNotTrival(arg: ArgumentBinding, keyName: String) = when {
        arg.elementType.isSubtypeOfType("android.os.Parcelable") -> "(${arg.type}) intent.getParcelableExtra(\"$keyName\")"
        arg.elementType.isSubtypeOfType("java.io.Serializable") -> "(${arg.type}) intent.getSerializableExtra(\"$keyName\")"
        else -> throw Error("Illegal field type" + arg.type)
    }
}
