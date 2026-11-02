package com.zohrah.androidassignments;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class TestToolbarInstrumentedTest {

    // TEST 1: MainActivity can open TestToolbar
    @Test
    public void mainActivity_opensTestToolbar() {

        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            onView(withId(R.id.testToolbarButton))
                    .perform(click());

            onView(withId(R.id.toolbar))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 2: Toolbar contains all three actions and About
    @Test
    public void toolbar_containsFourMenuItems() {

        try (ActivityScenario<TestToolbar> scenario =
                     ActivityScenario.launch(TestToolbar.class)) {

            scenario.onActivity(activity -> {

                Toolbar toolbar =
                        activity.findViewById(R.id.toolbar);

                assertNotNull(toolbar);

                Menu menu = toolbar.getMenu();

                assertNotNull(menu.findItem(R.id.action_item1));
                assertNotNull(menu.findItem(R.id.action_item2));
                assertNotNull(menu.findItem(R.id.action_item3));
                assertNotNull(menu.findItem(R.id.action_about));
            });
        }
    }

    // TEST 3: Item 1 displays the default Snackbar
    @Test
    public void item1_displaysSnackbar() {

        try (ActivityScenario<TestToolbar> scenario =
                     ActivityScenario.launch(TestToolbar.class)) {

            onView(withId(R.id.action_item1))
                    .perform(click());

            onView(withText("You selected Item 1"))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 4: Item 2 opens confirmation dialog
    // Selecting No keeps the activity open
    @Test
    public void item2_noKeepsActivityOpen() {

        try (ActivityScenario<TestToolbar> scenario =
                     ActivityScenario.launch(TestToolbar.class)) {

            onView(withId(R.id.action_item2))
                    .perform(click());

            onView(withText("Do you want to go back?"))
                    .check(matches(isDisplayed()));

            onView(withText("No"))
                    .perform(click());

            onView(withId(R.id.toolbar))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 5: Item 2 -> Yes returns to MainActivity
    @Test
    public void item2_yesReturnsToMainActivity() {

        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            onView(withId(R.id.testToolbarButton))
                    .perform(click());

            onView(withId(R.id.action_item2))
                    .perform(click());

            onView(withText("Yes"))
                    .perform(click());

            onView(withId(R.id.testToolbarButton))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 6: Item 3 opens the custom message dialog
    @Test
    public void item3_opensCustomDialog() {

        try (ActivityScenario<TestToolbar> scenario =
                     ActivityScenario.launch(TestToolbar.class)) {

            onView(withId(R.id.action_item3))
                    .perform(click());

            onView(withId(R.id.newMessageEditText))
                    .check(matches(isDisplayed()));

            onView(withText("Change Snackbar Message"))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 7: Updating the message changes Item 1 Snackbar
    @Test
    public void item3_updatesSnackbarMessage() {

        try (ActivityScenario<TestToolbar> scenario =
                     ActivityScenario.launch(TestToolbar.class)) {

            onView(withId(R.id.action_item3))
                    .perform(click());

            onView(withId(R.id.newMessageEditText))
                    .perform(
                            replaceText("Hello from CP470!"),
                            closeSoftKeyboard()
                    );

            onView(withText("OK"))
                    .perform(click());

            onView(withId(R.id.action_item1))
                    .perform(click());

            onView(withText("Hello from CP470!"))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 8: About appears in the overflow menu
    @Test
    public void aboutOption_isAvailable() {

        try (ActivityScenario<TestToolbar> scenario =
                     ActivityScenario.launch(TestToolbar.class)) {

            onView(withId(R.id.toolbar))
                    .check(matches(isDisplayed()));

            scenario.onActivity(activity -> {

                Toolbar toolbar =
                        activity.findViewById(R.id.toolbar);

                MenuItem aboutItem =
                        toolbar.getMenu().findItem(R.id.action_about);

                assertNotNull(aboutItem);
            });
        }
    }
}
