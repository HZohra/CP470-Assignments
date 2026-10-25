package com.zohrah.androidassignments;

import java.util.List;

public class ChatMessageUtils {

    // Checks whether a message contains text
    public static boolean isValidMessage(String message) {
        return message != null && !message.trim().isEmpty();
    }

    // Removes unnecessary spaces
    public static String cleanMessage(String message) {
        if (message == null) {
            return "";
        }
        return message.trim();
    }

    // Adds a valid message to the list
    public static boolean addMessage(
            List<String> messages,
            String message
    ) {
        if (!isValidMessage(message)) {
            return false;
        }

        messages.add(cleanMessage(message));
        return true;
    }

    // Determines which chat row layout to use
    public static boolean isIncomingMessage(int position) {
        return position % 2 == 0;
    }
}
