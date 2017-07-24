package com.marcinmoskala.activitystarter

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

inline fun <reified T> argExtra(default: T? = null) = ArgExtraDelegateFactory(T::class.java, default)

/* Currently not supported features:
 * - Custom keys
 */
class ArgExtraDelegateFactory<T>(val clazz: Class<T>, val default: T?) {

    operator fun provideDelegate(thisRef: Activity, prop: KProperty<*>): Lazy<T> {
        val key = getKey(thisRef::class.java, prop)
        val getter = makeIntentGetter(key, prop.returnType, default)
        return lazy { (getter(thisRef.intent) ?: default) as T }
    }

    private fun makeIntentGetter(key: String, type: KType, default: T?): (Intent) -> Any? = when (clazz.canonicalName) {
        "java.lang.String" -> { intent: Intent -> intent.getStringExtra(key) }
        "java.lang.Integer" -> { intent: Intent -> intent.getIntExtra(key, default as? Int ?: -1) }
        "java.lang.Long" -> { intent: Intent -> intent.getLongExtra(key, default as? Long ?: -1) }
        "java.lang.Float" -> { intent: Intent -> intent.getFloatExtra(key, default as? Float ?: -1F) }
        "java.lang.Boolean" -> { intent: Intent -> intent.getBooleanExtra(key, default as? Boolean ?: false) }
        "java.lang.Double" -> { intent: Intent -> intent.getDoubleExtra(key, default as? Double ?: -1.0) }
        "java.lang.Character" -> { intent: Intent -> intent.getCharExtra(key, default as? Char ?: '\u0000') }
        "java.lang.Byte" -> { intent: Intent -> intent.getByteExtra(key, default as? Byte ?: -1) }
        "java.lang.Short" -> { intent: Intent -> intent.getShortExtra(key, default as? Short ?: -1) }
        "java.lang.CharSequence" -> { intent: Intent -> intent.getCharSequenceExtra(key) }

        "java.lang.String[]" -> { intent: Intent -> intent.getStringArrayExtra(key) }
        "int[]" -> { intent: Intent -> intent.getIntArrayExtra(key) }
        "long[]" -> { intent: Intent -> intent.getLongArrayExtra(key) }
        "float[]" -> { intent: Intent -> intent.getFloatArrayExtra(key) }
        "boolean[]" -> { intent: Intent -> intent.getBooleanArrayExtra(key) }
        "double[]" -> { intent: Intent -> intent.getDoubleArrayExtra(key) }
        "char[]" -> { intent: Intent -> intent.getCharArrayExtra(key) }
        "byte[]" -> { intent: Intent -> intent.getByteArrayExtra(key) }
        "short[]" -> { intent: Intent -> intent.getShortArrayExtra(key) }
        "java.lang.CharSequence[]" -> { intent: Intent -> intent.getCharSequenceArrayExtra(key) }

        "java.util.ArrayList" -> when (type.javaType.toString()) {
            "java.util.ArrayList<java.lang.Integer>" -> { intent: Intent -> intent.getIntegerArrayListExtra(key) }
            "java.util.ArrayList<java.lang.String>" -> { intent: Intent -> intent.getStringArrayListExtra(key) }
            "java.util.ArrayList<java.lang.CharSequence>" -> { intent: Intent -> intent.getCharSequenceArrayListExtra(key) }
            else -> (throw TypeNotSupportedError()) as (Intent) -> Any?
        }

        else -> when {
            Parcelable::class.java.isAssignableFrom(clazz) -> { intent: Intent -> intent.getParcelableExtra<Parcelable>(key) }
            Serializable::class.java.isAssignableFrom(clazz) -> { intent: Intent -> intent.getSerializableExtra(key) }
            else -> (throw TypeNotSupportedError()) as (Intent) -> Any?
        }
    }
}

private fun getKey(contextRef: Class<*>, property: KProperty<*>) = "${contextRef.canonicalName}.${property.name}StarterKey"

class TypeNotSupportedError : Error("Type not supported")