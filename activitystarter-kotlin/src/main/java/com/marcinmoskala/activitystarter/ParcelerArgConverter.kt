package com.marcinmoskala.activitystarter

import activitystarter.ActivityStarterNameConstruction
import android.app.Activity
import java.lang.reflect.Method
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> argExtra(default: T? = null) = ArgExtraDelegateFactory(default)

class ArgExtraDelegateFactory<T>(val default: T?) {

    operator fun provideDelegate(thisRef: Activity, prop: KProperty<*>): ReadWriteProperty<Any, T>
            = createDelegate(thisRef, thisRef.javaClass, prop.name)

    private fun createDelegate(thisRef: Any, javaClass: Class<*>, fieldName: String): ReadWriteProperty<Any, T> {
        val starterClass = getStarterClass(javaClass)
        val checkerName = ActivityStarterNameConstruction.getterFieldCheckerName(fieldName)
        val accessorName = ActivityStarterNameConstruction.getterFieldAccessorName(fieldName)
        val accessorMethod = getMethod(starterClass, accessorName, javaClass)
        val checkerMethod = getMethod(starterClass, checkerName, javaClass)
        return BoundToValueDelegate {
            val argFilled = checkerMethod.invokeMethod(thisRef) as Boolean
            val argValue: Any? = if (argFilled) accessorMethod.invokeMethod(thisRef) else null
            val argValueOrDefault = argValue ?: default
            argValueOrDefault as T
        }
    }
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

private class BoundToValueDelegate<T>(var initializer: (() -> T)?) : ReadWriteProperty<Any?, T> {

    var valueSet = false
    var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        synchronized(this) {
            if (valueSet) @Suppress("UNCHECKED_CAST") return value as T
            val typedValue = initializer?.invoke()
            setNewValue(typedValue)
            @Suppress("UNCHECKED_CAST") return valueSet as T
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        synchronized(this) {
            setNewValue(newValue)
        }
    }

    private fun setNewValue(typedValue: T?) {
        value = typedValue
        valueSet = true
        initializer = null
    }

    override fun toString(): String = if (valueSet) value.toString() else "Lazy value not initialized yet."
}