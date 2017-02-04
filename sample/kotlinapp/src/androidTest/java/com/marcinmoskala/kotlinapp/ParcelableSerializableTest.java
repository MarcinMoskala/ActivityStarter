package com.marcinmoskala.kotlinapp;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ParcelableSerializableTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void parcelableTest() throws InterruptedException {
        mActivityTestRule.getActivity().startParcelableActivity();

        onView(withId(R.id.nameView)).check(matches(withText("Name: Marcin")));
        onView(withId(R.id.idView)).check(matches(withText("Id: 10")));
        onView(withId(R.id.gradeView)).check(matches(withText("Grade: A")));
    }

    @Test
    public void serializableTest() throws InterruptedException {
        mActivityTestRule.getActivity().startSerializableActivity();

        onView(withId(R.id.nameView)).check(matches(withText("Name: Marcin Moskala")));
        onView(withId(R.id.idView)).check(matches(withText("Id: 20")));
        onView(withId(R.id.gradeView)).check(matches(withText("Grade: A")));
    }
}
