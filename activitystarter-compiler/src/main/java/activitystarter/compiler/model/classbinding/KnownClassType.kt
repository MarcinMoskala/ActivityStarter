package activitystarter.compiler.model.classbinding

import activitystarter.compiler.utils.isSubtypeOfType
import javax.lang.model.type.TypeMirror

enum class KnownClassType(vararg val typeString: String) {
    Activity(ACTIVITY_TYPE),
    Fragment(FRAGMENT_TYPE, FRAGMENTv4_TYPE),
    Service(SERVICE_TYPE),
    BroadcastReceiver(BROADCAST_RECEIVER_TYPE);

    companion object {
        fun getByType(elementType: TypeMirror): KnownClassType? = values()
                .first { elementType.isSubtypeOfType(*it.typeString) }
    }
}

private const val ACTIVITY_TYPE = "android.app.Activity"
private const val FRAGMENT_TYPE = "android.app.Fragment"
private const val FRAGMENTv4_TYPE = "android.support.v4.app.Fragment"
private const val SERVICE_TYPE = "android.app.Service"
private const val BROADCAST_RECEIVER_TYPE = "android.content.BroadcastReceiver"
private const val SERIALIZABLE_TYPE = "java.io.Serializable"
private const val PARCELABLE_TYPE = "android.os.Parcelable"