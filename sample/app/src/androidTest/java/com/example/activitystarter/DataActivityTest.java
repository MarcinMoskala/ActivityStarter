package com.example.activitystarter;


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
public class DataActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void defaultDataActivityTest() throws InterruptedException {
        onView(withId(R.id.student_grade)).perform(scrollTo(), replaceText("A"), closeSoftKeyboard());

        activityTestRule.getActivity().performClick(R.id.show_data_button);

        onView(withId(R.id.name_view)).check(matches(withText("Name: No name provided")));
        onView(withId(R.id.id_view)).check(matches(withText("Id: -1")));
        onView(withId(R.id.grade_view)).check(matches(withText("Grade: A")));
        onView(withId(R.id.is_passing_view)).check(matches(withText("Passing status: false")));
    }

    @Test
    public void dataActivityTest() throws InterruptedException {
        onView(withId(R.id.student_name)).perform(scrollTo(), replaceText("Marcin"), closeSoftKeyboard());
        onView(withId(R.id.student_id)).perform(scrollTo(), replaceText("123"), closeSoftKeyboard());
        onView(withId(R.id.student_grade)).perform(scrollTo(), replaceText("A"), closeSoftKeyboard());
        onView(withId(R.id.student_is_passing)).perform(scrollTo(), click());

        activityTestRule.getActivity().performClick(R.id.show_data_button);

        onView(withId(R.id.name_view)).check(matches(withText("Name: Marcin")));
        onView(withId(R.id.id_view)).check(matches(withText("Id: 123")));
        onView(withId(R.id.grade_view)).check(matches(withText("Grade: A")));
        onView(withId(R.id.is_passing_view)).check(matches(withText("Passing status: true")));
    }
}
