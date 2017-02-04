package com.example.activitystarter;


import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ParcelableSerializableActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void parcelableActivityTest() throws InterruptedException {
        onView(withId(R.id.show_parcelable_data_button)).perform(scrollTo(), click());

        onView(withId(R.id.name_view)).check(matches(withText("Name: Marcin")));
        onView(withId(R.id.id_view)).check(matches(withText("Id: 10")));
        onView(withId(R.id.grade_view)).check(matches(withText("Grade: A")));
    }

    @Test
    public void serializableActivityTest() throws InterruptedException {
        onView(withId(R.id.show_serializable_data_button)).perform(scrollTo(), click());

        onView(withId(R.id.name_view)).check(matches(withText("Name: Marcin Moskala")));
        onView(withId(R.id.id_view)).check(matches(withText("Id: 20")));
        onView(withId(R.id.grade_view)).check(matches(withText("Grade: A")));
    }
}
