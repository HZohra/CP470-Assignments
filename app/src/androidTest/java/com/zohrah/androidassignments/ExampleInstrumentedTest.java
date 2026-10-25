package com.zohrah.androidassignments;

import android.content.Context;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;


import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    // TEST 1: Check application package
    @Test
    public void useAppContext() {
        Context appContext =
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext();

        assertEquals(
                "com.zohrah.androidassignments",
                appContext.getPackageName()
        );
    }

    // TEST 2: Check Login screen
    @Test
    public void loginScreen_displaysFields() {
        try (ActivityScenario<LoginActivity> scenario =
                     ActivityScenario.launch(LoginActivity.class)) {

            onView(withId(R.id.editTextLogin))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.editTextPassword))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.loginButton))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 3: Verify valid login
    @Test
    public void validLogin_opensMainActivity() {
        try (ActivityScenario<LoginActivity> scenario =
                     ActivityScenario.launch(LoginActivity.class)) {

            onView(withId(R.id.editTextLogin))
                    .perform(replaceText("student@example.com"));

            onView(withId(R.id.editTextPassword))
                    .perform(
                            replaceText("password123"),
                            closeSoftKeyboard()
                    );

            onView(withId(R.id.loginButton))
                    .perform(click());

            onView(withId(R.id.startChatButton))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 4: Verify Start Chat button
    @Test
    public void mainActivity_displaysStartChat() {
        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            onView(withId(R.id.startChatButton))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 5: Open ChatWindow
    @Test
    public void startChat_opensChatWindow() {
        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            onView(withId(R.id.startChatButton))
                    .perform(click());

            onView(withId(R.id.chatListView))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.sendButton))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 6: Send a chat message
    @Test
    public void sendMessage_displaysMessage() {
        try (ActivityScenario<ChatWindow> scenario =
                     ActivityScenario.launch(ChatWindow.class)) {

            onView(withId(R.id.chatTextInput))
                    .perform(
                            replaceText("Hello World"),
                            closeSoftKeyboard()
                    );

            onView(withId(R.id.sendButton))
                    .perform(click());

            onView(withText("Hello World"))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 7: Verify EditText clears
    @Test
    public void sendMessage_clearsInput() {
        try (ActivityScenario<ChatWindow> scenario =
                     ActivityScenario.launch(ChatWindow.class)) {

            onView(withId(R.id.chatTextInput))
                    .perform(
                            replaceText("Test message"),
                            closeSoftKeyboard()
                    );

            onView(withId(R.id.sendButton))
                    .perform(click());

            onView(withId(R.id.chatTextInput))
                    .check(matches(withText("")));
        }
    }

    // TEST 8: Multiple chat messages
    @Test
    public void multipleMessages_areDisplayed() {
        try (ActivityScenario<ChatWindow> scenario =
                     ActivityScenario.launch(ChatWindow.class)) {

            onView(withId(R.id.chatTextInput))
                    .perform(
                            replaceText("First message"),
                            closeSoftKeyboard()
                    );

            onView(withId(R.id.sendButton))
                    .perform(click());

            onView(withId(R.id.chatTextInput))
                    .perform(
                            replaceText("Second message"),
                            closeSoftKeyboard()
                    );

            onView(withId(R.id.sendButton))
                    .perform(click());

            onView(withText("First message"))
                    .check(matches(isDisplayed()));

            onView(withText("Second message"))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 9: Empty messages are rejected
    @Test
    public void emptyMessage_isNotAdded() {
        try (ActivityScenario<ChatWindow> scenario =
                     ActivityScenario.launch(ChatWindow.class)) {

            onView(withId(R.id.sendButton))
                    .perform(click());

            scenario.onActivity(activity -> {
                ListView list =
                        activity.findViewById(R.id.chatListView);

                assertEquals(0, list.getAdapter().getCount());
            });
        }
    }

    // TEST 10: Back navigation from ChatWindow
    @Test
    public void backButton_returnsToMainActivity() {
        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            onView(withId(R.id.startChatButton))
                    .perform(click());

            onView(withId(R.id.chatListView))
                    .check(matches(isDisplayed()));

            pressBack();

            onView(withId(R.id.startChatButton))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 11: MainActivity opens ListItemsActivity
    @Test
    public void mainActivity_opensListItemsActivity() {
        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            onView(withId(R.id.button))
                    .perform(click());

            onView(withId(R.id.imageButton))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.switch1))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 12: ListItemsActivity controls exist
    @Test
    public void listItems_displaysControls() {
        try (ActivityScenario<ListItemsActivity> scenario =
                     ActivityScenario.launch(ListItemsActivity.class)) {

            onView(withId(R.id.imageButton))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.switch1))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.checkBox))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 13: Switch can be turned on
    @Test
    public void listItems_switchTurnsOn() {
        try (ActivityScenario<ListItemsActivity> scenario =
                     ActivityScenario.launch(ListItemsActivity.class)) {

            onView(withId(R.id.switch1))
                    .perform(click());

            onView(withId(R.id.switch1))
                    .check(matches(isChecked()));
        }
    }

    // TEST 14: Switch can be turned off
    @Test
    public void listItems_switchTurnsOff() {
        try (ActivityScenario<ListItemsActivity> scenario =
                     ActivityScenario.launch(ListItemsActivity.class)) {

            onView(withId(R.id.switch1))
                    .perform(click());

            onView(withId(R.id.switch1))
                    .perform(click());

            onView(withId(R.id.switch1))
                    .check(matches(isNotChecked()));
        }
    }

    // TEST 15: Checkbox displays confirmation dialog
    @Test
    public void listItems_checkboxOpensDialog() {
        try (ActivityScenario<ListItemsActivity> scenario =
                     ActivityScenario.launch(ListItemsActivity.class)) {

            onView(withId(R.id.checkBox))
                    .perform(click());

            onView(withText(R.string.dialog_message))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 16: Selecting No resets checkbox
    @Test
    public void listItems_dialogNoResetsCheckbox() {
        try (ActivityScenario<ListItemsActivity> scenario =
                     ActivityScenario.launch(ListItemsActivity.class)) {

            onView(withId(R.id.checkBox))
                    .perform(click());

            onView(withText(R.string.no))
                    .perform(click());

            onView(withId(R.id.checkBox))
                    .check(matches(isNotChecked()));
        }
    }

    // TEST 17: Selecting Yes returns to MainActivity
    @Test
    public void listItems_dialogYesReturnsToMain() {
        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            onView(withId(R.id.button))
                    .perform(click());

            onView(withId(R.id.checkBox))
                    .perform(click());

            onView(withText(R.string.yes))
                    .perform(click());

            onView(withId(R.id.startChatButton))
                    .check(matches(isDisplayed()));
        }
    }

    // TEST 18: Back navigation from ListItemsActivity
    @Test
    public void listItems_backReturnsToMainActivity() {
        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            onView(withId(R.id.button))
                    .perform(click());

            onView(withId(R.id.imageButton))
                    .check(matches(isDisplayed()));

            pressBack();

            onView(withId(R.id.startChatButton))
                    .check(matches(isDisplayed()));
        }
    }


    // TEST 19: Invalid email should show an error
    @Test
    public void login_invalidEmailShowsError() {
        try (ActivityScenario<LoginActivity> scenario =
                     ActivityScenario.launch(LoginActivity.class)) {

            onView(withId(R.id.editTextLogin))
                    .perform(replaceText("invalid-email"));

            onView(withId(R.id.editTextPassword))
                    .perform(
                            replaceText("password123"),
                            closeSoftKeyboard()
                    );

            onView(withId(R.id.loginButton))
                    .perform(click());

            onView(withId(R.id.editTextLogin))
                    .check(matches(
                            hasErrorText(
                                    scenarioString(R.string.invalid_email)
                            )
                    ));
        }
    }

    // TEST 20: Empty password should show an error
    @Test
    public void login_emptyPasswordShowsError() {
        try (ActivityScenario<LoginActivity> scenario =
                     ActivityScenario.launch(LoginActivity.class)) {

            onView(withId(R.id.editTextLogin))
                    .perform(replaceText("student@example.com"));

            onView(withId(R.id.editTextPassword))
                    .perform(
                            replaceText(""),
                            closeSoftKeyboard()
                    );

            onView(withId(R.id.loginButton))
                    .perform(click());

            onView(withId(R.id.editTextPassword))
                    .check(matches(
                            hasErrorText(
                                    scenarioString(R.string.empty_password)
                            )
                    ));
        }
    }

    // Get the expected error text in the current app language
    private String scenarioString(int stringId) {
        return InstrumentationRegistry
                .getInstrumentation()
                .getTargetContext()
                .getString(stringId);
    }

}
