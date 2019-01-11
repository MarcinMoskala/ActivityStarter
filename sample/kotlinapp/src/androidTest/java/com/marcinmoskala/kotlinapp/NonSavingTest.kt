package com.marcinmoskala.kotlinapp

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.marcinmoskala.kotlinapp.savetest.NonSavingActivity
import com.marcinmoskala.kotlinapp.savetest.SavingActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NonSavingTest {

    @Rule @JvmField var activityTestRule = ActivityTestRule(NonSavingActivity::class.java)

    @Test
    fun activityIsNotSavingChangesTest() {
        onView(withId(R.id.iView)).check(matches(withText("" + SavingActivity.DEFAULT_I)))
        onView(withId(R.id.strView)).check(matches(withText("" + SavingActivity.DEFAULT_STR)))
        onView(withId(R.id.bView)).check(matches(withText("" + SavingActivity.DEFAULT_B)))

        val activity = activityTestRule.activity
        activity.i = SavingActivity.NEW_I
        activity.str = SavingActivity.NEW_STR
        activity.b = SavingActivity.NEW_B

        rotateScreen()
        rotateScreen()

        Thread.sleep(500)

        onView(withId(R.id.iView)).check(matches(withText("" + SavingActivity.DEFAULT_I)))
        onView(withId(R.id.strView)).check(matches(withText("" + SavingActivity.DEFAULT_STR)))
        onView(withId(R.id.bView)).check(matches(withText("" + SavingActivity.DEFAULT_B)))
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
