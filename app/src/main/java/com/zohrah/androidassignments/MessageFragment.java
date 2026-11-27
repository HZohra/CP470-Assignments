
package com.zohrah.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {

    public static final String ARG_MESSAGE = "message";
    public static final String ARG_MESSAGE_ID = "messageId";

    private long messageId;

    public MessageFragment() {
    }

    // Create a fragment with the selected message and ID
    public static MessageFragment newInstance(
            String message, long id) {

        MessageFragment fragment = new MessageFragment();

        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        args.putLong(ARG_MESSAGE_ID, id);

        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_message,
                container,
                false
        );

        TextView messageText =
                view.findViewById(R.id.messageDetailsText);

        TextView idText =
                view.findViewById(R.id.messageIdText);

        Button deleteButton =
                view.findViewById(R.id.deleteMessageButton);

        // Retrieve the selected message and database ID
        Bundle args = requireArguments();

        String message = args.getString(ARG_MESSAGE);
        messageId = args.getLong(ARG_MESSAGE_ID, -1);

        messageText.setText(message);

        idText.setText(
                getString(R.string.message_id, messageId)
        );

        // Enable deletion only when the ID is valid
        deleteButton.setEnabled(messageId > 0);

        deleteButton.setOnClickListener(v -> {

            if (messageId <= 0) {
                return;
            }

            Activity activity = requireActivity();

            if (activity instanceof ChatWindow) {

                // Tablet or landscape: delete directly
                ChatWindow chatWindow =
                        (ChatWindow) activity;

                chatWindow.deleteMessage(messageId);

            } else if (activity instanceof MessageDetails) {

                // Phone: return the ID to ChatWindow
                Intent result = new Intent();

                result.putExtra(
                        ARG_MESSAGE_ID,
                        messageId
                );

                activity.setResult(
                        Activity.RESULT_OK,
                        result
                );

                activity.finish();
            }
        });

        return view;
    }
}
