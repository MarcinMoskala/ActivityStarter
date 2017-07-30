package com.marcinmoskala.kotlinapp

import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import org.junit.Assert
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class AllTypesActivityTest : FromIntentTest<AllTypesActivity>(AllTypesActivity::class.java) {

    private val string = "SomeString"
    private val i = 1000
    private val l: Long = 1000
    private val f = 1000f
    private val bool = true
    private val d = 1000.0
    private val c = 'Z'
    private val b: Byte = 10
    private val s: Short = 100
    private val cs = "CharSequence"

    private val stringArray = arrayOf("SomeString")
    private val iArray = intArrayOf(1000)
    private val lArray = longArrayOf(1000)
    private val fArray = floatArrayOf(1000f)
    private val boolArray = booleanArrayOf(true)
    private val dArray = doubleArrayOf(1000.0)
    private val cArray = charArrayOf('Z')
    private val bArray = byteArrayOf(10)
    private val sArray = shortArrayOf(100)
    private val csArray = arrayOf<CharSequence>("CharSequence")

    private val intList = arrayListOf(1, 2, 3)
    private val strList = arrayListOf("A", "B", "C", "D")
    private val csList = arrayListOf<CharSequence>("ALALA", "BABABA", "KAKAKA")

    private val sp = StudentParcelable(1, "AAA", 'A')
    private val ss = StudentSerializable(2, "Marcin", 'A', true)
    private val spList = ArrayList<StudentParcelable>()

    @Test
    @Throws(InterruptedException::class)
    fun defaultValuesTest() {
        val intent = AllTypesActivityStarter.getIntent(context, string, i, l, f, bool, d, c, b, s, cs, stringArray, iArray, lArray, fArray, boolArray, dArray, cArray, bArray, sArray, csArray, intList, strList, csList, sp, ss, spList)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity

        val a = activityTestRule.activity
        assertEquals(string, a.str)
        assertEquals(i.toLong(), a.i.toLong())
        assertEquals(l, a.l)
        assertEquals(f.toDouble(), a.f.toDouble(), 0.01)
        assertEquals(bool, a.bool)
        assertEquals(d, a.d, 0.01)
        assertEquals(c, a.c)
        assertEquals(b, a.b)
        assertEquals(s, a.s)
        assertArrayEquals(stringArray, a.stra)
        assertArrayEquals(iArray, a.ia)
        assertArrayEquals(lArray, a.la)
        assertArrayEquals(fArray, a.fa, 0.01f)
        assertArrayEquals(boolArray, a.boola)
        assertArrayEquals(dArray, a.da, 0.01)
        assertArrayEquals(cArray, a.ca)
        assertArrayEquals(bArray, a.ba)
        assertArrayEquals(sArray, a.sa)
        assertArrayEquals(csArray, a.csa)
        assertEquals(intList, a.intList)
        assertEquals(strList, a.strList)
        assertEquals(csList, a.csList)
        assertEquals(sp, a.sp)
        assertEquals(ss, a.ss)
        assertEquals(spList, a.spList)
    }
}
