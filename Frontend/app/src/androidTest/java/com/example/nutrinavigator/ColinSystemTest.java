package com.example.nutrinavigator;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
/*
@RunWith(AndroidJUnit4.class)
public class ColinSystemTest {

    @Rule
    public ActivityScenarioRule<RecipeSearch> activityRule =
            new ActivityScenarioRule<>(RecipeSearch.class);

    @Before
    public void setUp() throws Exception {
        Intents.init(); // Initializes Espresso Intents
    }

    @After
    public void tearDown() throws Exception {
        Intents.release(); // Releases Espresso Intents after the test
    }

    @Test
    public void testQueryUrlConstruction() throws InterruptedException {
        Espresso.onView(withId(R.id.editUploadUser)).perform(typeText("testUser"));
        Espresso.onView(withId(R.id.editIngredients)).perform(typeText("chicken"));
        Espresso.onView(withId(R.id.editCaloriesLow)).perform(typeText("100"));
        Espresso.onView(withId(R.id.editCaloriesHigh)).perform(typeText("500"));
        Espresso.onView(withId(R.id.editDateStart)).perform(typeText("2023-01-01"));
        Espresso.onView(withId(R.id.editDateEnd)).perform(typeText("2023-12-31"));
        Espresso.onView(withId(R.id.btn_search)).perform(click());

    }

    @Test
    public void testSearchResultsDisplay() throws InterruptedException {
        Espresso.onView(withId(R.id.editIngredients)).perform(typeText("chicken"));
        Espresso.onView(withId(R.id.btn_search)).perform(click());

        Espresso.onView(withId(R.id.rv))
                .check(ViewAssertions.matches(hasDescendant(withText("Chicken Salad"))))
                .check(ViewAssertions.matches(hasDescendant(withText("Grilled Chicken"))));
    }

    @Test
    public void testHandleEmptyResults() throws InterruptedException {
        Espresso.onView(withId(R.id.editIngredients)).perform(typeText("unknownIngredient"));
        Espresso.onView(withId(R.id.btn_search)).perform(click());

        Espresso.onView(allOf(withText("No recipes found")))
                .inRoot(ViewMatchers.isRoot()) // This searches the root of the view hierarchy, which includes Toasts
                .check(matches(isDisplayed()));
    }

    @Test
    public void testDietaryPreferencesIntegration() throws InterruptedException {
        Intent resultData = new Intent();
        resultData.putExtra("dietaryRestrictions", "vegan");
        resultData.putExtra("dietaryPreferences", "low-sugar");
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasComponent(DietaryPreferences.class.getName())).respondWith(result);

        Espresso.onView(withId(R.id.btn_dietaryPreferences)).perform(click());
        Espresso.onView(withId(R.id.btn_search)).perform(click());
    }

}
*/


@RunWith(AndroidJUnit4.class)
public class ColinSystemTest {

    @Rule
    public ActivityScenarioRule<RecipeSearch> activityRule =
            new ActivityScenarioRule<>(RecipeSearch.class);

    @Before
    public void setUp() throws Exception {
        Intents.init(); // Initializes Espresso Intents
    }

    @After
    public void tearDown() throws Exception {
        Intents.release(); // Releases Espresso Intents after the test
    }

    @Test
    public void testQueryUrlConstruction() throws InterruptedException {
        Espresso.onView(withId(R.id.editUploadUser)).perform(typeText("testUser"));
        Espresso.onView(withId(R.id.editIngredients)).perform(typeText("chicken"));
        Espresso.onView(withId(R.id.editCaloriesLow)).perform(typeText("100"));
        Espresso.onView(withId(R.id.editCaloriesHigh)).perform(typeText("500"));
        Espresso.onView(withId(R.id.editDateStart)).perform(typeText("2023-01-01"));
        Espresso.onView(withId(R.id.editDateEnd)).perform(typeText("2023-12-31"));
        Espresso.onView(withId(R.id.btn_search)).perform(click());
    }

    @Test
    public void testSearchResultsDisplay() throws InterruptedException {
        Espresso.onView(withId(R.id.editIngredients)).perform(typeText("chicken"));
        Espresso.onView(withId(R.id.btn_search)).perform(click());

        Espresso.onView(withId(R.id.rv))
                .check(ViewAssertions.matches(hasDescendant(withText("Chicken Salad"))))
                .check(ViewAssertions.matches(hasDescendant(withText("Grilled Chicken"))));
    }

    @Test
    public void testHandleEmptyResults() throws InterruptedException {
        Espresso.onView(withId(R.id.editIngredients)).perform(typeText("unknownIngredient"));
        Espresso.onView(withId(R.id.btn_search)).perform(click());

        Espresso.onView(withText("No recipes found"))
               // .inRoot(new ToastMatcher())
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testDietaryPreferencesIntegration() throws InterruptedException {
        Intent resultData = new Intent();
        resultData.putExtra("dietaryRestrictions", "VEGAN");
        resultData.putExtra("dietaryPreferences", "");
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasComponent(DietaryPreferences.class.getName())).respondWith(result);

        Espresso.onView(withId(R.id.btn_dietaryPreferences)).perform(click());
        Espresso.onView(withId(R.id.btn_search)).perform(click());
    }
}
