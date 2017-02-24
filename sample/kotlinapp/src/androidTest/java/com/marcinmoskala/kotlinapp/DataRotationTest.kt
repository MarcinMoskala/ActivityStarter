package com.marcinmoskala.kotlinapp


import android.content.Intent
import android.os.RemoteException
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

@LargeTest
@RunWith(AndroidJUnit4::class)
class DataRotationTest : FromIntentTest<StudentDataActivity>(StudentDataActivity::class.java) {

    @Test
    @Throws(InterruptedException::class, RemoteException::class)
    fun defaultRotationTest() {
        val intent = StudentDataActivityStarter.getIntent(context, FromIntentTest.exampleName, FromIntentTest.exampleId, FromIntentTest.exampleGrade, FromIntentTest.examplePassing)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity

        assertEquals(activity!!.name, FromIntentTest.exampleName)
        assertEquals(activity!!.id.toLong(), FromIntentTest.exampleId.toLong())
        assertEquals(activity!!.grade.toLong(), FromIntentTest.exampleGrade.toLong())
        Assert.assertEquals(activity!!.passing, FromIntentTest.examplePassing)

        makeRotation()

        assertEquals(activity!!.name, FromIntentTest.exampleName)
        assertEquals(activity!!.id.toLong(), FromIntentTest.exampleId.toLong())
        assertEquals(activity!!.grade.toLong(), FromIntentTest.exampleGrade.toLong())
        Assert.assertEquals(activity!!.passing, FromIntentTest.examplePassing)
        assertNotEquals(activity!!.name, FromIntentTest.exampleName2)
        assertNotEquals(activity!!.id.toLong(), FromIntentTest.exampleId2.toLong())
        assertNotEquals(activity!!.grade.toLong(), FromIntentTest.exampleGrade2.toLong())
        Assert.assertNotEquals(activity!!.passing, FromIntentTest.examplePassing2)
    }

    @Test
    @Throws(InterruptedException::class, RemoteException::class)
    fun changesTest() {
        val intent = StudentDataActivityStarter.getIntent(context, FromIntentTest.exampleName, FromIntentTest.exampleId, FromIntentTest.exampleGrade, FromIntentTest.examplePassing2)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity

        activity!!.name = FromIntentTest.exampleName2
        activity!!.id = FromIntentTest.exampleId2
        activity!!.grade = FromIntentTest.exampleGrade2
        activity!!.passing = FromIntentTest.examplePassing2

        makeRotation()

        assertNotEquals(activity!!.name, FromIntentTest.exampleName)
        assertNotEquals(activity!!.id.toLong(), FromIntentTest.exampleId.toLong())
        assertNotEquals(activity!!.grade.toLong(), FromIntentTest.exampleGrade.toLong())
        Assert.assertNotEquals(activity!!.passing, FromIntentTest.examplePassing)
        assertEquals(activity!!.name, FromIntentTest.exampleName2)
        assertEquals(activity!!.id.toLong(), FromIntentTest.exampleId2.toLong())
        assertEquals(activity!!.grade.toLong(), FromIntentTest.exampleGrade2.toLong())
        Assert.assertEquals(activity!!.passing, FromIntentTest.examplePassing2)
    }
}
