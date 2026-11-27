
package com.zohrah.androidassignments;

import android.app.Activity;
import android.content.Context;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "ChatWindow";
    public static final int MESSAGE_DETAILS_REQUEST = 20;

    private ListView listView;
    private EditText textInput;
    private Button sendButton;

    private final ArrayList<String> messages = new ArrayList<>();

    private ChatAdapter messageAdapter;
    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    // Assignment 3 Part 3
    private Cursor cursor;
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_window);

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

        listView = findViewById(R.id.chatListView);
        textInput = findViewById(R.id.chatTextInput);
        sendButton = findViewById(R.id.sendButton);

        // Check whether the two-pane layout is active
        isTablet = findViewById(
                R.id.messageFragmentContainer
        ) != null;

        Log.i(ACTIVITY_NAME,
                "Two-pane layout: " + isTablet);

        // Remove a restored tablet fragment when returning to phone layout
        if (!isTablet) {
            Fragment oldFragment = getSupportFragmentManager()
                    .findFragmentById(R.id.messageFragmentContainer);

            if (oldFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(oldFragment)
                        .commitNow();
            }
        }

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        // Open the existing SQLite database
        try {
            dbHelper = new ChatDatabaseHelper(this);
            db = dbHelper.getWritableDatabase();

            Log.i(ACTIVITY_NAME,
                    "Database opened successfully");

            loadMessages();

        } catch (SQLiteException e) {
            Log.e(ACTIVITY_NAME, "Database error", e);

            Toast.makeText(
                    this,
                    R.string.database_open_error,
                    Toast.LENGTH_LONG
            ).show();

            sendButton.setEnabled(false);
        }

        // Save new messages
        sendButton.setOnClickListener(v -> saveMessage());

        // Display details of the selected message
        listView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    if (id < 0) {
                        return;
                    }

                    String selectedMessage =
                            messageAdapter.getItem(position);

                    showMessageDetails(selectedMessage, id);
                }
        );
    }

    // Save a new message in SQLite
    private void saveMessage() {

        String message = textInput.getText().toString();

        if (!ChatMessageUtils.isValidMessage(message)) {
            return;
        }

        String cleanedMessage =
                ChatMessageUtils.cleanMessage(message);

        if (db == null || !db.isOpen()) {
            Toast.makeText(
                    this,
                    R.string.database_unavailable,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        ContentValues values = new ContentValues();

        values.put(
                ChatDatabaseHelper.KEY_MESSAGE,
                cleanedMessage
        );

        try {
            long id = db.insertOrThrow(
                    ChatDatabaseHelper.TABLE_NAME,
                    null,
                    values
            );

            Log.i(ACTIVITY_NAME,
                    "Saved SQL MESSAGE: " + cleanedMessage
                            + ", ID=" + id);

            // Refresh messages and their database IDs
            loadMessages();

            listView.smoothScrollToPosition(
                    messages.size() - 1
            );

            textInput.setText("");

        } catch (SQLiteException e) {

            Log.e(ACTIVITY_NAME,
                    "Failed to save message", e);

            Toast.makeText(
                    this,
                    R.string.message_save_error,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Load saved messages and retain the cursor
    private void loadMessages() {

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        cursor = db.query(
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
        );

        Log.i(ACTIVITY_NAME,
                "Cursor's column count = "
                        + cursor.getColumnCount());

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME,
                    "Cursor column " + i + " = "
                            + cursor.getColumnName(i));
        }

        int messageColumn = cursor.getColumnIndexOrThrow(
                ChatDatabaseHelper.KEY_MESSAGE
        );

        messages.clear();

        while (cursor.moveToNext()) {

            String message = cursor.getString(messageColumn);

            messages.add(message);

            Log.i(ACTIVITY_NAME,
                    "SQL MESSAGE: " + message);
        }

        messageAdapter.notifyDataSetChanged();

        Log.i(ACTIVITY_NAME,
                "Loaded " + messages.size()
                        + " messages from SQLite");
    }

    // Open message details based on the current layout
    private void showMessageDetails(String message, long id) {

        Log.i(ACTIVITY_NAME,
                "Selected message ID: " + id);

        if (isTablet) {

            // Display the fragment beside the ListView
            MessageFragment fragment =
                    MessageFragment.newInstance(message, id);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.messageFragmentContainer,
                            fragment
                    )
                    .commit();

        } else {

            // Open a separate activity on a phone
            Intent intent = new Intent(
                    this,
                    MessageDetails.class
            );

            Bundle args = new Bundle();

            args.putString(
                    MessageFragment.ARG_MESSAGE,
                    message
            );

            args.putLong(
                    MessageFragment.ARG_MESSAGE_ID,
                    id
            );

            intent.putExtras(args);

            startActivityForResult(
                    intent,
                    MESSAGE_DETAILS_REQUEST
            );
        }
    }

    // Delete a message using its SQLite database ID
    public void deleteMessage(long messageId) {

        if (db == null || !db.isOpen() || messageId <= 0) {

            Toast.makeText(
                    this,
                    R.string.message_delete_error,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        try {

            int deletedRows = db.delete(
                    ChatDatabaseHelper.TABLE_NAME,
                    ChatDatabaseHelper.KEY_ID + " = ?",
                    new String[]{String.valueOf(messageId)}
            );

            if (deletedRows > 0) {

                Log.i(ACTIVITY_NAME,
                        "Deleted message ID: " + messageId);

                // Refresh the ListView and database cursor
                loadMessages();

                // Remove the fragment from the two-pane layout
                if (isTablet) {

                    Fragment fragment = getSupportFragmentManager()
                            .findFragmentById(
                                    R.id.messageFragmentContainer
                            );

                    if (fragment != null) {

                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(fragment)
                                .commit();
                    }
                }

                Toast.makeText(
                        this,
                        R.string.message_deleted,
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        this,
                        R.string.message_delete_error,
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (SQLiteException e) {

            Log.e(ACTIVITY_NAME,
                    "Failed to delete message", e);

            Toast.makeText(
                    this,
                    R.string.message_delete_error,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Receive the message ID from the phone details activity
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == MESSAGE_DETAILS_REQUEST
                && resultCode == Activity.RESULT_OK
                && data != null) {

            long messageId = data.getLongExtra(
                    MessageFragment.ARG_MESSAGE_ID,
                    -1
            );

            if (messageId > 0) {
                deleteMessage(messageId);
            }
        }
    }


    @Override
    protected void onDestroy() {

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (dbHelper != null) {
            dbHelper.close();

            Log.i(ACTIVITY_NAME,
                    "Database closed in onDestroy");
        }

        super.onDestroy();
    }

    // Custom adapter for chat messages
    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public String getItem(int position) {
            return messages.get(position);
        }

        // Return the actual SQLite database ID
        @Override
        public long getItemId(int position) {

            if (cursor != null
                    && !cursor.isClosed()
                    && cursor.moveToPosition(position)) {

                int idColumn = cursor.getColumnIndexOrThrow(
                        ChatDatabaseHelper.KEY_ID
                );

                return cursor.getLong(idColumn);
            }

            return -1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent
        ) {

            LayoutInflater inflater =
                    ChatWindow.this.getLayoutInflater();

            View result;

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

            TextView messageText =
                    result.findViewById(R.id.message_text);

            messageText.setText(getItem(position));

            return result;
        }
    }
}
