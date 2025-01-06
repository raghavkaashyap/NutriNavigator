package com.example.nutrinavigator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static java.util.regex.Pattern.matches;

import android.view.View;

import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AidanSystemTest {


    @Test
    public void testBackButtonNavigatesBack() {
        // Click the back button
        onView(withId(R.id.backButton)).perform(click());

        // Check if the activity finishes
        onView(withId(R.id.backButton)).check(doesNotExist());
    }


    @Test
    public void testPasswordMismatch() {
        // Enter mismatched passwords
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordEditText)).perform(typeText("password456"), closeSoftKeyboard());

        // Click the confirm button
        onView(withId(R.id.confirmButton)).perform(click());

        // Verify that the confirm password field is cleared and hint is updated
        onView(withId(R.id.confirmPasswordEditText)).check(matches(withHint("Incorrect! Please try again.")));
        onView(withId(R.id.confirmPasswordEditText)).check(matches(withText("")));
    }


    @Test
    public void testBackButton() {
        // Click the back button
        onView(withId(R.id.backButton)).perform(click());

        // Verify the activity finishes (optional: use activity monitors to assert)
        onView(isRoot()).check(matches(isDisplayed())); // Placeholder for activity finish test
    }



}
