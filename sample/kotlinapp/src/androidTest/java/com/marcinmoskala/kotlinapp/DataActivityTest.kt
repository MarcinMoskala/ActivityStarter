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
class DataActivityTest : FromIntentTest<StudentDataActivity>(StudentDataActivity::class.java) {

    @Test
    @Throws(InterruptedException::class, RemoteException::class)
    fun setAllDataActivityTest() {
        val intent = StudentDataActivityStarter.getIntent(context, FromIntentTest.exampleName, FromIntentTest.exampleId, FromIntentTest.exampleGrade, FromIntentTest.examplePassing)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity

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
    fun defaultValuesDataActivityTestNameId() {
        val intent = StudentDataActivityStarter.getIntent(context, FromIntentTest.exampleGrade, FromIntentTest.examplePassing)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity

        Assert.assertEquals(activity!!.name, StudentDataActivity.defaultName)
        Assert.assertEquals(activity!!.id, StudentDataActivity.defaultId)
        assertEquals(activity!!.grade.toLong(), FromIntentTest.exampleGrade.toLong())
        Assert.assertEquals(activity!!.passing, FromIntentTest.examplePassing)
    }

    @Test
    @Throws(InterruptedException::class, RemoteException::class)
    fun defaultValuesDataActivityTestNameOnly() {
        val intent = StudentDataActivityStarter.getIntent(context, FromIntentTest.exampleId, FromIntentTest.exampleGrade, FromIntentTest.examplePassing)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity

        Assert.assertEquals(activity!!.name, StudentDataActivity.defaultName)
        assertEquals(activity!!.id.toLong(), FromIntentTest.exampleId.toLong())
        assertEquals(activity!!.grade.toLong(), FromIntentTest.exampleGrade.toLong())
        Assert.assertEquals(activity!!.passing, FromIntentTest.examplePassing)
    }

    @Test
    @Throws(InterruptedException::class, RemoteException::class)
    fun defaultValuesDataActivityTestIdOnly() {
        val intent = StudentDataActivityStarter.getIntent(context, FromIntentTest.exampleName, FromIntentTest.exampleGrade, FromIntentTest.examplePassing)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity

        assertEquals(activity!!.name, FromIntentTest.exampleName)
        Assert.assertEquals(activity!!.id, StudentDataActivity.defaultId)
        assertEquals(activity!!.grade.toLong(), FromIntentTest.exampleGrade.toLong())
        Assert.assertEquals(activity!!.passing, FromIntentTest.examplePassing)
    }
}
