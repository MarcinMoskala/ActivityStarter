package com.example.activitystarter;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ClickableExampleTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void defaultDataActivityTest() throws InterruptedException {
        onView(withId(R.id.student_grade)).perform(scrollTo(), replaceText("A"), closeSoftKeyboard());

        onView(withId(R.id.show_data_button)).perform(scrollTo(), click());

        onView(withId(R.id.student_name)).check(matches(withText("No name provided")));
        onView(withId(R.id.student_id)).check(matches(withText("-1")));
        onView(withId(R.id.student_grade)).check(matches(withText("A")));
        onView(withId(R.id.student_is_passing)).check(matches(not(isChecked())));
    }

    @Test
    public void dataActivityTest() throws InterruptedException {
        onView(withId(R.id.student_name)).perform(scrollTo(), replaceText("Marcin"), closeSoftKeyboard());
        onView(withId(R.id.student_id)).perform(scrollTo(), replaceText("123"), closeSoftKeyboard());
        onView(withId(R.id.student_grade)).perform(scrollTo(), replaceText("A"), closeSoftKeyboard());
        onView(withId(R.id.student_is_passing)).perform(scrollTo(), click());

        onView(withId(R.id.show_data_button)).perform(scrollTo(), click());

        onView(withId(R.id.student_name)).check(matches(withText("Marcin")));
        onView(withId(R.id.student_id)).check(matches(withText("123")));
        onView(withId(R.id.student_grade)).check(matches(withText("A")));
        onView(withId(R.id.student_is_passing)).check(matches(isChecked()));
    }
}
