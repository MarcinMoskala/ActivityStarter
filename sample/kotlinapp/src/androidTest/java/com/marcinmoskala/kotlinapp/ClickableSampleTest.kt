package com.marcinmoskala.kotlinapp


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
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
class ClickableSampleTest {

    @Rule @JvmField var activityTestRule = ActivityTestRule(MainActivity::class.java, true)

    @Test
    @Throws(InterruptedException::class)
    fun defaultValuesTest() {
        onView(withId(R.id.studentGradeView)).perform(scrollTo(), replaceText("A"), closeSoftKeyboard())

        onView(withId(R.id.showDataButton)).perform(scrollTo(), click())

        onView(withId(R.id.gradeView)).check(matches(withText("Grade: A")))
        onView(withId(R.id.nameView)).check(matches(withText("Name: No name provided")))
        onView(withId(R.id.idView)).check(matches(withText("Id: -1")))
    }

    @Test
    @Throws(InterruptedException::class)
    fun parcelableTest() {
        onView(withId(R.id.showParcelableDataButton)).perform(scrollTo(), click())

        onView(withId(R.id.nameView)).check(matches(withText("Name: Marcin")))
        onView(withId(R.id.idView)).check(matches(withText("Id: 10")))
        onView(withId(R.id.gradeView)).check(matches(withText("Grade: A")))
    }

    @Test
    fun simpleDataPassingTest() {
        onView(withId(R.id.studentNameView)).perform(scrollTo(), replaceText("Marcin"), closeSoftKeyboard())
        onView(withId(R.id.studentIdView)).perform(scrollTo(), replaceText("123"), closeSoftKeyboard())
        onView(withId(R.id.studentGradeView)).perform(scrollTo(), replaceText("A"), closeSoftKeyboard())
        onView(withId(R.id.studentIsPassingView)).perform(scrollTo(), click())

        onView(withId(R.id.showDataButton)).perform(scrollTo(), click())

        onView(withId(R.id.nameView)).check(matches(withText("Name: Marcin")))
        onView(withId(R.id.idView)).check(matches(withText("Id: 123")))
        onView(withId(R.id.gradeView)).check(matches(withText("Grade: A")))
        onView(withId(R.id.isPassingView)).check(matches(withText("Passing status: true")))
    }
}
