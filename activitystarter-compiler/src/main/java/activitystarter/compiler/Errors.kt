package activitystarter.compiler

object Errors {
    val notAClass = "fields may only be contained in classes."
    val privateClass = "fields may not be contained in private classes."
    val notSupportedType: String = "fields must extend from Serializable, Parcelable or beof type String, int, float, double, char or boolean."
    val inaccessibleField: String = "Inaccessable element."
    val notBasicTypeInReceiver: String = "On BroadcastReceiver only basic types are supported."
}