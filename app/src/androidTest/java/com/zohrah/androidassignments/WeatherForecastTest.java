
package com.zohrah.androidassignments;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class WeatherForecastTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // Verify that the Weather Forecast button is displayed
    @Test
    public void weatherForecastButtonIsDisplayed() {
        onView(withId(R.id.weatherForecastButton))
                .check(matches(isDisplayed()));
    }

    // Verify that the Canadian cities dropdown is displayed
    @Test
    public void citySpinnerIsDisplayed() {
        onView(withId(R.id.citySpinner))
                .check(matches(isDisplayed()));
    }

    // Verify that Ottawa is available by default
    @Test
    public void defaultCityIsOttawa() {
        onView(withId(R.id.citySpinner))
                .check(matches(
                        withSpinnerText(containsString("Ottawa"))
                ));
    }

    // Verify navigation to the WeatherForecast activity
    @Test
    public void weatherForecastActivityLaunches() {
        onView(withId(R.id.weatherForecastButton))
                .perform(click());

        onView(withId(R.id.cityNameTextView))
                .check(matches(isDisplayed()));
    }

    // Verify that selecting Toronto updates the weather activity
    @Test
    public void selectedCityIsPassedCorrectly() {
        onView(withId(R.id.citySpinner))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)),
                is("Toronto")))
                .perform(click());

        onView(withId(R.id.weatherForecastButton))
                .perform(click());

        onView(withId(R.id.cityNameTextView))
                .check(matches(
                        withText(containsString("Toronto"))
                ));
    }

    // Verify that weather GUI components exist
    @Test
    public void weatherComponentsAreDisplayed() {
        onView(withId(R.id.weatherForecastButton))
                .perform(click());

        onView(withId(R.id.weatherImageView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.currentTemperatureTextView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.minTemperatureTextView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.maxTemperatureTextView))
                .check(matches(isDisplayed()));
    }
}
