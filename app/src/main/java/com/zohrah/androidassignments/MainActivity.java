
package com.zohrah.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "MainActivity";
    private static final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(ACTIVITY_NAME, "onCreate() called");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

        // BUTTON: Opens ListItemsActivity
        Button listItemsButton = findViewById(R.id.button);

        listItemsButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ListItemsActivity.class
            );

            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    // RECEIVE RESULT FROM LISTITEMSACTIVITY
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            Log.i(
                    ACTIVITY_NAME,
                    "Returned to MainActivity.onActivityResult"
            );

            if (resultCode == Activity.RESULT_OK && data != null) {

                String messagePassed = data.getStringExtra("Response");

                if (messagePassed != null) {

                    // Display the message using the selected language
                    Toast.makeText(
                            MainActivity.this,
                            getString(
                                    R.string.returned_message,
                                    messagePassed
                            ),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        }
    }

    // ACTIVITY LIFECYCLE METHODS

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(ACTIVITY_NAME, "onSaveInstanceState() called");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(ACTIVITY_NAME, "onRestoreInstanceState() called");
    }
}
