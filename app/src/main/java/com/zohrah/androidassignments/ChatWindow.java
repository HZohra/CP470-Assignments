package com.zohrah.androidassignments;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    // Chat interface components
    private ListView listView;
    private EditText textInput;
    private Button sendButton;

    // Stores all chat messages
    private final ArrayList<String> messages = new ArrayList<>();

    // Custom adapter
    private ChatAdapter messageAdapter;

    private static final String ACTIVITY_NAME = "ChatWindow";
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_window);

        // Handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        // Initialize chat interface components
        listView = findViewById(R.id.chatListView);
        textInput = findViewById(R.id.chatTextInput);
        sendButton = findViewById(R.id.sendButton);

        // Create adapter and connect it to ListView
        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);


// Assignment 3 - Open SQLite database
        try {
            ChatDatabaseHelper dbHelper =
                    new ChatDatabaseHelper(this);

            db = dbHelper.getWritableDatabase();

            Log.i(ACTIVITY_NAME, "Database opened successfully");

            // Retrieve all previously saved messages
            loadMessages();

        } catch (SQLiteException e) {
            Log.e(ACTIVITY_NAME, "Database error", e);

            Toast.makeText(
                    this,
                    "Unable to open chat database",
                    Toast.LENGTH_LONG
            ).show();

            sendButton.setEnabled(false);
        }




        // Handle Send button and save messages to SQLite
        sendButton.setOnClickListener(v -> {

            String message = textInput.getText().toString();

            // Reject empty or whitespace-only messages
            if (!ChatMessageUtils.isValidMessage(message)) {
                return;
            }

            String cleanedMessage =
                    ChatMessageUtils.cleanMessage(message);

            if (db == null || !db.isOpen()) {
                Toast.makeText(
                        this,
                        "Database unavailable",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // Prepare data for SQLite
            ContentValues values = new ContentValues();

            values.put(
                    ChatDatabaseHelper.KEY_MESSAGE,
                    cleanedMessage
            );

            try {
                // Save message in the database
                long id = db.insertOrThrow(
                        ChatDatabaseHelper.TABLE_NAME,
                        null,
                        values
                );

                // Add saved message to the ArrayList
                ChatMessageUtils.addMessage(
                        messages,
                        cleanedMessage
                );

                Log.i(
                        ACTIVITY_NAME,
                        "Saved SQL MESSAGE: " + cleanedMessage
                                + ", ID=" + id
                );

                // Refresh the ListView
                messageAdapter.notifyDataSetChanged();

                // Scroll to newest message
                listView.smoothScrollToPosition(
                        messages.size() - 1
                );

                // Clear the input
                textInput.setText("");

            } catch (SQLiteException e) {
                Log.e(ACTIVITY_NAME, "Failed to save message", e);

                Toast.makeText(
                        this,
                        "Message could not be saved",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

    }

    // Assignment 3 - Load saved messages from SQLite
    private void loadMessages() {

        try (Cursor cursor = db.query(
                ChatDatabaseHelper.TABLE_NAME,
                new String[]{
                        ChatDatabaseHelper.KEY_ID,
                        ChatDatabaseHelper.KEY_MESSAGE
                },
                null,
                null,
                null,
                null,
                ChatDatabaseHelper.KEY_ID + " ASC"
        )) {

            // Log the number of columns
            Log.i(
                    ACTIVITY_NAME,
                    "Cursor's column count = " +
                            cursor.getColumnCount()
            );

            // Log every column name
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.i(
                        ACTIVITY_NAME,
                        "Cursor column " + i + " = " +
                                cursor.getColumnName(i)
                );
            }

            // Find the MESSAGE column
            int messageColumn =
                    cursor.getColumnIndexOrThrow(
                            ChatDatabaseHelper.KEY_MESSAGE
                    );

            messages.clear();

            // Read and display each saved message
            while (cursor.moveToNext()) {

                String message =
                        cursor.getString(messageColumn);

                Log.i(
                        ACTIVITY_NAME,
                        "SQL MESSAGE: " + message
                );

                messages.add(message);
            }

            // Update the chat ListView
            messageAdapter.notifyDataSetChanged();

            if (!messages.isEmpty()) {
                listView.setSelection(messages.size() - 1);
            }

            Log.i(
                    ACTIVITY_NAME,
                    "Loaded " + messages.size() +
                            " messages from SQLite"
            );
        }
    }


    @Override
    protected void onDestroy() {

        if (db != null && db.isOpen()) {
            db.close();

            Log.i(
                    ACTIVITY_NAME,
                    "Database closed in onDestroy"
            );
        }

        super.onDestroy();

        Log.i(ACTIVITY_NAME, "onDestroy() called");
    }


    // Custom adapter for chat messages
    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        // Return number of messages
        @Override
        public int getCount() {
            return messages.size();
        }

        // Return message at specified position
        @Override
        public String getItem(int position) {
            return messages.get(position);
        }

        // Create a row for each message
        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent
        ) {

            LayoutInflater inflater =
                    ChatWindow.this.getLayoutInflater();

            View result;

            // Even positions: incoming
            // Odd positions: outgoing
            if (ChatMessageUtils.isIncomingMessage(position)) {

                result = inflater.inflate(
                        R.layout.chat_row_incoming,
                        parent,
                        false
                );

            } else {

                result = inflater.inflate(
                        R.layout.chat_row_outgoing,
                        parent,
                        false
                );
            }

            // Find the message TextView
            TextView messageText =
                    result.findViewById(R.id.message_text);

            // Display the message
            messageText.setText(getItem(position));

            return result;
        }
    }
}
