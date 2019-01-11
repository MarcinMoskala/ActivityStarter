package com.marcinmoskala.kotlinapp

import android.os.RemoteException
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

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
