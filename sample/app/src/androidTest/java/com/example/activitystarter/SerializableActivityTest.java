package com.example.activitystarter;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SerializableActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void serializableActivityTest() throws InterruptedException {
        onView(withId(R.id.show_serializable_data_button)).perform(scrollTo(), click());

        onView(withId(R.id.student_name)).check(matches(withText("Marcin Moskala")));
        onView(withId(R.id.student_id)).check(matches(withText("20")));
        onView(withId(R.id.student_grade)).check(matches(withText("A")));
    }
}
