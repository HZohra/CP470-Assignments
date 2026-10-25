package com.zohrah.androidassignments;

import android.content.Context;
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

        // Handle Send button
        sendButton.setOnClickListener(v -> {

            String message = textInput.getText().toString();

            // Validate, clean, and add the message
            boolean messageAdded =
                    ChatMessageUtils.addMessage(messages, message);

            // Only update the screen when a valid message is added
            if (messageAdded) {

                // Refresh the ListView
                messageAdapter.notifyDataSetChanged();

                // Scroll to the newest message
                listView.smoothScrollToPosition(
                        messages.size() - 1
                );

                // Clear the input field
                textInput.setText("");
            }
        });
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
