package com.example.activitystarter;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.activitystarter.savetest.NonSavingActivity;
import com.example.activitystarter.savetest.SavingActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NonSavingTest {

    @Rule
    public ActivityTestRule<NonSavingActivity> activityTestRule = new ActivityTestRule<>(NonSavingActivity.class);

    @Test
    public void startTest() throws InterruptedException {
        onView(withId(R.id.i)).check(matches(withText("" + SavingActivity.DEFAULT_I)));
        onView(withId(R.id.str)).check(matches(withText("" + SavingActivity.DEFAULT_STR)));
        onView(withId(R.id.b)).check(matches(withText("" + SavingActivity.DEFAULT_B)));

        final NonSavingActivity activity = activityTestRule.getActivity();
        activity.i = SavingActivity.NEW_I;
        activity.str = SavingActivity.NEW_STR;
        activity.b = SavingActivity.NEW_B;

        rotateScreen();
        rotateScreen();

        Thread.sleep(500);

        onView(withId(R.id.i)).check(matches(withText("" + SavingActivity.DEFAULT_I)));
        onView(withId(R.id.str)).check(matches(withText("" + SavingActivity.DEFAULT_STR)));
        onView(withId(R.id.b)).check(matches(withText("" + SavingActivity.DEFAULT_B)));
    }

    private void rotateScreen() {
        Context context = InstrumentationRegistry.getTargetContext();
        int orientation = context.getResources().getConfiguration().orientation;
        activityTestRule.getActivity()
                .setRequestedOrientation(
                        (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                );
    }
}
