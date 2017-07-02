package com.marcinmoskala.kotlinapp


import android.app.Activity
import android.content.Context
import android.os.RemoteException
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import android.test.suitebuilder.annotation.LargeTest

import org.junit.Rule
import org.junit.runner.RunWith

import android.support.test.InstrumentationRegistry.getInstrumentation

@LargeTest
@RunWith(AndroidJUnit4::class)
abstract class FromIntentTest<T : Activity> internal constructor(activityClass: Class<T>) {

    @Rule @JvmField
    var activityTestRule: ActivityTestRule<T> = ActivityTestRule(activityClass, false, false)

    internal var activity: T? = null

    internal val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Throws(RemoteException::class)
    internal fun makeRotation() {
        val device = UiDevice.getInstance(getInstrumentation())

        device.setOrientationLeft()
        device.setOrientationNatural()
        device.setOrientationRight()
        device.setOrientationNatural()
    }

    companion object {

        internal val exampleName = "Some Name"
        internal val exampleId = 2345654
        internal val exampleGrade = 'A'
        internal val examplePassing = true
        internal val exampleName2 = "Other Name"
        internal val exampleId2 = 7657
        internal val exampleGrade2 = 'B'
        internal val examplePassing2 = false
    }
}
