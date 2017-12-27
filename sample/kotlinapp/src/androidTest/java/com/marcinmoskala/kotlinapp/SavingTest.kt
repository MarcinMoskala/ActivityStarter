package com.marcinmoskala.kotlinapp


import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.marcinmoskala.kotlinapp.savetest.SavingActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SavingTest {

    @Rule @JvmField var activityTestRule = ActivityTestRule(SavingActivity::class.java)

    @Test
    fun activityIsSavingChangesTest() {
        onView(withId(R.id.iView)).check(matches(withText("" + SavingActivity.DEFAULT_I)))
        onView(withId(R.id.strView)).check(matches(withText("" + SavingActivity.DEFAULT_STR)))
        onView(withId(R.id.bView)).check(matches(withText("" + SavingActivity.DEFAULT_B)))

        val activity = activityTestRule.activity
        activity.i = SavingActivity.NEW_I
        activity.str = SavingActivity.NEW_STR
        activity.b = SavingActivity.NEW_B
        Thread.sleep(500)

        rotateScreen()
        Thread.sleep(500)

        rotateScreen()
        Thread.sleep(500)

        onView(withId(R.id.iView)).check(matches(withText("" + SavingActivity.NEW_I)))
        onView(withId(R.id.strView)).check(matches(withText("" + SavingActivity.NEW_STR)))
        onView(withId(R.id.bView)).check(matches(withText("" + SavingActivity.NEW_B)))
    }

    private fun rotateScreen() {
        val context = InstrumentationRegistry.getTargetContext()
        val orientation = context.resources.configuration.orientation
        activityTestRule.activity.requestedOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT)
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        else
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}
