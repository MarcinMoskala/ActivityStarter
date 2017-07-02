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
class SerializableActivityTest : FromIntentTest<StudentSerializableActivity>(StudentSerializableActivity::class.java) {

    @Test
    @Throws(InterruptedException::class, RemoteException::class)
    fun defaultParcelableTest() {
        val studentSerializable = StudentSerializable(FromIntentTest.exampleId, FromIntentTest.exampleName, FromIntentTest.exampleGrade, FromIntentTest.examplePassing)
        val intent = StudentSerializableActivityStarter.getIntent(context, studentSerializable)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity

        assertEquals(activity?.student?.name, FromIntentTest.exampleName)
        assertEquals(activity?.student?.id, FromIntentTest.exampleId)
        assertEquals(activity?.student?.grade, FromIntentTest.exampleGrade)
        assertEquals(activity?.student?.passing, FromIntentTest.examplePassing)
    }
}
