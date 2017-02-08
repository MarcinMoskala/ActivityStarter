package com.marcinmoskala.kotlinapp


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SerializableTest {

    @Rule @JvmField var activityTestRule = ActivityTestRule(MainActivity::class.java, false)

    @Test
    @Throws(InterruptedException::class)
    fun serializableTest() {
        onView(withId(R.id.showSerializableDataButton)).perform(scrollTo(), click())

        onView(withId(R.id.nameView)).check(matches(withText("Name: Marcin Moskala")))
        onView(withId(R.id.idView)).check(matches(withText("Id: 20")))
        onView(withId(R.id.gradeView)).check(matches(withText("Grade: A")))
    }
}
