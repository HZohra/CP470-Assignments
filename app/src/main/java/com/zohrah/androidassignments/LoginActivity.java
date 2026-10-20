
package com.zohrah.androidassignments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "LoginActivity";
    private static final String PREFS_NAME = "LoginPreferences";
    private static final String EMAIL_KEY = "DefaultEmail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(ACTIVITY_NAME, "onCreate() called");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

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

        // Get references to the login fields and button
        EditText emailField = findViewById(R.id.editTextLogin);
        EditText passwordField = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.loginButton);

        // Open SharedPreferences
        SharedPreferences preferences =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Retrieve the last saved email
        String savedEmail = preferences.getString(
                EMAIL_KEY,
                "email@domain.com"
        );

        emailField.setText(savedEmail);

        // Handle Login button
        loginButton.setOnClickListener(v -> {

            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString();

            // Validate email address
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                emailField.setError(
                        getString(R.string.invalid_email)
                );

                emailField.requestFocus();
                return;
            }

            // Validate password
            if (password.trim().isEmpty()) {

                passwordField.setError(
                        getString(R.string.empty_password)
                );

                passwordField.requestFocus();
                return;
            }

            // Save the email address for the next app launch
            preferences.edit()
                    .putString(EMAIL_KEY, email)
                    .apply();

            // Open MainActivity
            Intent intent = new Intent(
                    LoginActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
        });
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

        Log.i(
                ACTIVITY_NAME,
                "onSaveInstanceState() called"
        );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i(
                ACTIVITY_NAME,
                "onRestoreInstanceState() called"
        );
    }
}
