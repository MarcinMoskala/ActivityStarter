package com.marcinmoskala.kotlinapp


import android.content.Intent
import android.os.RemoteException
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.assertEquals

@LargeTest
@RunWith(AndroidJUnit4::class)
class ParcelableActivityTest : FromIntentTest<StudentParcelableActivity>(StudentParcelableActivity::class.java) {

    @Test
    @Throws(InterruptedException::class, RemoteException::class)
    fun defaultParcelableTest() {
        val studentParcelable = StudentParcelable(FromIntentTest.exampleId, FromIntentTest.exampleName, FromIntentTest.exampleGrade)
        val intent = StudentParcelableActivityStarter.getIntent(context, studentParcelable)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity

        assertEquals(activity!!.student!!.name, FromIntentTest.exampleName)
        assertEquals(activity!!.student!!.id.toLong(), FromIntentTest.exampleId.toLong())
        assertEquals(activity!!.student!!.grade.toLong(), FromIntentTest.exampleGrade.toLong())
    }
}
