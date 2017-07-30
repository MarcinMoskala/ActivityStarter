package com.marcinmoskala.activitystarter

import activitystarter.ActivityStarterNameConstruction
import android.app.Activity
import java.lang.reflect.Method
import kotlin.reflect.KProperty

fun <T> argExtra(default: T? = null) = ArgExtraDelegateFactory(default)

class ArgExtraDelegateFactory<T>(val default: T?) {

    operator fun provideDelegate(thisRef: Activity, prop: KProperty<*>): Lazy<T> {
        val targetClass = thisRef.javaClass
        val starterClass = getStarterClass(targetClass)
        val fieldName = prop.name
        val checkerName = ActivityStarterNameConstruction.getterFieldCheckerName(fieldName)
        val accessorName = ActivityStarterNameConstruction.getterFieldAccessorName(fieldName)
        val accessorMethod = getMethod(starterClass, accessorName, thisRef.javaClass)
        val checkerMethod = getMethod(starterClass, checkerName, thisRef.javaClass)
        return lazy {
            val argFilled = checkerMethod.invokeMethod(thisRef) as Boolean
            val argValue: Any? = if (argFilled) accessorMethod.invokeMethod(thisRef) else null
            val argValueOrDefault = argValue ?: default
            argValueOrDefault as T
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
}