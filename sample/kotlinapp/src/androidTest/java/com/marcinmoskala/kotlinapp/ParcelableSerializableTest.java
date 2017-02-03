package com.marcinmoskala.kotlinapp;


import android.support.test.espresso.ViewInteraction;
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
public class ParcelableSerializableTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void parcelableSerializableTest() {
        ViewInteraction appCompatButton = onView(allOf(withId(R.id.showParcelableDataButton), withText("Show data from parceleble on new Activity")));

        appCompatButton.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.nameView), withText("Name: Marcin"),
                        childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class), 0), 0),
                        isDisplayed()));
        textView.check(matches(withText("Name: Marcin")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.idView), withText("Id: 10"),
                        childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class), 0), 1),
                        isDisplayed()));
        textView2.check(matches(withText("Id: 10")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.gradeView), withText("Grade: A"),
                        childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class), 0), 2),
                        isDisplayed()));
        textView3.check(matches(withText("Grade: A")));

        pressBack();

        ViewInteraction appCompatButton2 = onView(allOf(withId(R.id.showSerializableDataButton), withText("Show data from serializable on new Activity")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.nameView), withText("Name: Marcin Moskala"),
                        childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class), 0), 0),
                        isDisplayed()));
        textView4.check(matches(withText("Name: Marcin Moskala")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.idView), withText("Id: 20"),
                        childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class), 0), 1),
                        isDisplayed()));
        textView5.check(matches(withText("Id: 20")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.gradeView), withText("Grade: A"),
                        childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class), 0), 2),
                        isDisplayed()));
        textView6.check(matches(withText("Grade: A")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
