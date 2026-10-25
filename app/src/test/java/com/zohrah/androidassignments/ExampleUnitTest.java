package com.zohrah.androidassignments;

import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void validMessage_isAccepted() {
        assertTrue(
                ChatMessageUtils.isValidMessage("Hello")
        );
    }

    @Test
    public void emptyMessage_isRejected() {
        assertFalse(
                ChatMessageUtils.isValidMessage("")
        );
    }

    @Test
    public void spacesOnlyMessage_isRejected() {
        assertFalse(
                ChatMessageUtils.isValidMessage("   ")
        );
    }

    @Test
    public void nullMessage_isRejected() {
        assertFalse(
                ChatMessageUtils.isValidMessage(null)
        );
    }

    @Test
    public void messageSpaces_areRemoved() {
        assertEquals(
                "Hello",
                ChatMessageUtils.cleanMessage("  Hello  ")
        );
    }

    @Test
    public void nullMessage_becomesEmptyString() {
        assertEquals(
                "",
                ChatMessageUtils.cleanMessage(null)
        );
    }

    @Test
    public void sendingMessage_addsToList() {
        ArrayList<String> messages = new ArrayList<>();

        boolean added = ChatMessageUtils.addMessage(
                messages, "Hello"
        );

        assertTrue(added);
        assertEquals(1, messages.size());
        assertEquals("Hello", messages.get(0));
    }

    @Test
    public void sendingEmptyMessage_doesNotAdd() {
        ArrayList<String> messages = new ArrayList<>();

        boolean added = ChatMessageUtils.addMessage(
                messages, "   "
        );

        assertFalse(added);
        assertEquals(0, messages.size());
    }

    @Test
    public void messages_keepCorrectOrder() {
        ArrayList<String> messages = new ArrayList<>();

        ChatMessageUtils.addMessage(messages, "First");
        ChatMessageUtils.addMessage(messages, "Second");

        assertEquals("First", messages.get(0));
        assertEquals("Second", messages.get(1));
    }

    @Test
    public void evenPosition_isIncoming() {
        assertTrue(
                ChatMessageUtils.isIncomingMessage(0)
        );

        assertTrue(
                ChatMessageUtils.isIncomingMessage(2)
        );
    }

    @Test
    public void oddPosition_isOutgoing() {
        assertFalse(
                ChatMessageUtils.isIncomingMessage(1)
        );

        assertFalse(
                ChatMessageUtils.isIncomingMessage(3)
        );
    }
}
