package com.marcinmoskala.kotlinapp;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SimpleDataPassingTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void simpleDataPassingTest() throws InterruptedException {
        onView(withId(R.id.studentNameView)).perform(scrollTo(), replaceText("Marcin"), closeSoftKeyboard());
        onView(withId(R.id.studentIdView)).perform(scrollTo(), replaceText("123"), closeSoftKeyboard());
        onView(withId(R.id.studentGradeView)).perform(scrollTo(), replaceText("A"), closeSoftKeyboard());
        onView(withId(R.id.studentIsPassingView)).perform(scrollTo(), click());

        mActivityTestRule.getActivity().startDetailsActivity();

        onView(withId(R.id.nameView)).check(matches(withText("Name: Marcin")));
        onView(withId(R.id.idView)).check(matches(withText("Id: 123")));
        onView(withId(R.id.gradeView)).check(matches(withText("Grade: A")));
        onView(withId(R.id.isPassingView)).check(matches(withText("Passing status: true")));
    }
}
