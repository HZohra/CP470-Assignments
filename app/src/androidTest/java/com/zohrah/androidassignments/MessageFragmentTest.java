
package com.zohrah.androidassignments;

import android.content.pm.ActivityInfo;
import android.view.Gravity;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MessageFragmentTest {

    @Rule
    public ActivityScenarioRule<ChatWindow> activityRule =
            new ActivityScenarioRule<>(ChatWindow.class);

    // Create a unique message for each test
    private String sendTestMessage() {

        String message = "Test message " + System.nanoTime();

        onView(withId(R.id.chatTextInput))
                .perform(replaceText(message), closeSoftKeyboard());

        onView(withId(R.id.sendButton))
                .perform(click());

        return message;
    }

    // Select a message from the ListView
    private void selectMessage(String message) {

        onData(is(message))
                .inAdapterView(withId(R.id.chatListView))
                .perform(click());
    }

    // Verify that the chat interface is displayed
    @Test
    public void chatInterfaceIsDisplayed() {

        onView(withId(R.id.chatListView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.sendButton))
                .check(matches(isDisplayed()));
    }

    // Verify portrait message details and deletion
    @Test
    public void portraitMessageDetailsAndDeletion() {

        activityRule.getScenario().onActivity(activity ->
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                )
        );

        String message = sendTestMessage();

        selectMessage(message);

        onView(withId(R.id.messageDetailsText))
                .check(matches(withText(message)));

        onView(withId(R.id.messageIdText))
                .check(matches(isDisplayed()));

        onView(withId(R.id.deleteMessageButton))
                .check(matches(isEnabled()))
                .perform(click());

        onView(withId(R.id.chatListView))
                .check(matches(isDisplayed()));

        onView(withText(message))
                .check(doesNotExist());
    }

    // Verify the two-pane landscape layout and deletion
    @Test
    public void landscapeMessageDetailsAndDeletion() {

        activityRule.getScenario().onActivity(activity ->
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                )
        );

        String message = sendTestMessage();

        selectMessage(message);

        onView(withId(R.id.chatListView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.messageFragmentContainer))
                .check(matches(isDisplayed()));

        onView(withId(R.id.messageDetailsText))
                .check(matches(withText(message)));

        onView(withId(R.id.deleteMessageButton))
                .perform(click());

        onView(withText(message))
                .check(doesNotExist());

        onView(withId(R.id.messageDetailsText))
                .check(doesNotExist());
    }

    // Verify right alignment of the selected message
    @Test
    public void messageTextIsRightAligned() {

        activityRule.getScenario().onActivity(activity ->
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                )
        );

        String message = sendTestMessage();

        selectMessage(message);

        onView(withId(R.id.messageDetailsText))
                .check((view, exception) -> {

                    if (exception != null) {
                        throw exception;
                    }

                    TextView textView = (TextView) view;

                    assertEquals(
                            Gravity.RIGHT,
                            textView.getGravity()
                                    & Gravity.HORIZONTAL_GRAVITY_MASK
                    );
                });

        // Remove the temporary test message
        onView(withId(R.id.deleteMessageButton))
                .perform(click());
    }
}
