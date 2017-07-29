package activitystarter.compiler.error

import activitystarter.compiler.model.param.ParamType

object Errors {
    val notAClass = "fields may only be contained in classes."
    val privateClass = "fields may not be contained in private classes."
    val notSupportedType: String = "Type is not supported. Supported types are: ${ParamType.supportedTypes}"
    val inaccessibleField: String = "Inaccessable element."
    val notBasicTypeInReceiver: String = "On BroadcastReceiver only basic types are supported."
    val wrongClassType: String = "Is in wroing typeName. It needs to be Activity, Froagment, Service or BroadcastReceiver."
    val noSetter: String = "Trying to set Inaccessible field"
    val moreThenOneConfig: String = "There cannot be more then one ActivityStarter config (Annotation @ActivityStarterConfig)"
}