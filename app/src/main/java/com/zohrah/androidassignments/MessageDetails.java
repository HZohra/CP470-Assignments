
package com.zohrah.androidassignments;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message_details);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.messageFragmentContainer),
                (v, insets) -> {

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

        if (getSupportActionBar() != null) {
            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        // Create the fragment only on the first launch
        if (savedInstanceState == null) {

            Bundle args = getIntent().getExtras();

            if (args == null) {
                finish();
                return;
            }

            String message = args.getString(
                    MessageFragment.ARG_MESSAGE
            );

            long id = args.getLong(
                    MessageFragment.ARG_MESSAGE_ID,
                    -1
            );

            MessageFragment fragment =
                    MessageFragment.newInstance(message, id);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.messageFragmentContainer,
                            fragment
                    )
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
