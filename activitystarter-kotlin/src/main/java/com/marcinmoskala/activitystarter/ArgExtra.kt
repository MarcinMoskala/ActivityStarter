package com.marcinmoskala.activitystarter

import activitystarter.ActivityStarterNameConstruction
import activitystarter.Arg
import android.app.Activity
import android.app.Fragment
import java.lang.reflect.Method
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

fun <T> Activity.argExtra(default: T? = null) = BoundToArgValueDelegateProvider(default)

fun <T> Fragment.argExtra(default: T? = null) = BoundToArgValueDelegateProvider(default)

fun <T> android.support.v4.app.Fragment.argExtra(default: T? = null) = BoundToArgValueDelegateProvider(default)

class BoundToArgValueDelegateProvider<T>(val default: T? = null) {

    operator fun provideDelegate(
            thisRef: Any?,
            prop: KProperty<*>
    ): ReadWriteProperty<Any, T> {
        val annotation = try {
            prop.getter.findAnnotation<Arg>()
        } catch (e: Error) {
            return BoundToValueDelegate(default)
        }
        when {
            annotation == null -> throw Error(ErrorMessages.noAnnotation)
            annotation.optional && !prop.returnType.isMarkedNullable && default == null -> throw Error(ErrorMessages.optionalValueNeeded)
        }
        return BoundToValueDelegate(default)
    }
}

internal object ErrorMessages {
    const val noAnnotation = "Element getter must be annotated with Arg"
    const val optionalValueNeeded = "Arguments that are optional and have not-nullable type must have defaut value specified"
}

private class BoundToValueDelegate<T>(var default: T?) : ReadWriteProperty<Any, T> {

    var valueSet = false
    var value: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        synchronized(this) {
            if (valueSet) @Suppress("UNCHECKED_CAST") return value as T
            val typedValue = getValueFromStarter(thisRef, thisRef.javaClass, property.name, default)
            setNewValue(typedValue)
            @Suppress("UNCHECKED_CAST") return typedValue as T
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, newValue: T) {
        synchronized(this) {
            setNewValue(newValue)
        }
    }

    private fun setNewValue(typedValue: T?) {
        value = typedValue
        valueSet = true
        default = null
    }

    override fun toString(): String = if (valueSet) value.toString() else "Lazy value not initialized yet."
}

// This function is invoked only once for one lifetime of Activity for each property. Later value is cached
private fun <T> getValueFromStarter(thisRef: Any, javaClass: Class<*>, fieldName: String, default: T?): T {
    val starterClass = getStarterClass(javaClass)
    val checkerName = ActivityStarterNameConstruction.getterFieldCheckerName(fieldName)
    val accessorName = ActivityStarterNameConstruction.getterFieldAccessorName(fieldName)
    val checkerMethod = getMethod(starterClass, checkerName, javaClass)
    val accessorMethod = getMethod(starterClass, accessorName, javaClass)
    val argFilled = checkerMethod.invokeMethod(thisRef) as Boolean
    val argValue: Any? = if (argFilled) accessorMethod.invokeMethod(thisRef) else null
    val argValueOrDefault = argValue ?: default
    return argValueOrDefault as T
}

private fun getStarterClass(targetClass: Class<*>): Class<*> {
    val clsName = targetClass.name
    val starterName = clsName + "Starter"
    return Class.forName(starterName)
}

private fun getMethod(starterClass: Class<*>, methodName: String, vararg classes: Class<*>): Method {
    try {
        return starterClass.getMethod(methodName, *classes)
    } catch (e: NoSuchMethodException) {
        throw RuntimeException("Unable to find " + methodName + " method for " + starterClass.name, e)
    }
}

private fun Method.invokeMethod(vararg args: Any) = try {
    this.invoke(null, *args)
} catch (e: IllegalAccessException) {
    throw RuntimeException("Unable to invoke " + this + "because of illegal access", e)
} catch (e: IllegalArgumentException) {
    throw RuntimeException("Unable to invoke $this because of illegal argument", e)
}