
package com.zohrah.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "MainActivity";
    private static final int REQUEST_CODE = 10;

    // Assignment 3 Part 2: Intent key for selected city
    public static final String EXTRA_CITY_NAME = "cityName";

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

        // PART 1 & 2: Open ListItemsActivity
        Button listItemsButton = findViewById(R.id.button);

        listItemsButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ListItemsActivity.class
            );

            startActivityForResult(intent, REQUEST_CODE);
        });

        // PART 3: Open ChatWindow
        Button startChatButton = findViewById(R.id.startChatButton);

        startChatButton.setOnClickListener(v -> {

            Log.i(ACTIVITY_NAME, "User clicked Start Chat");

            Intent intent = new Intent(
                    MainActivity.this,
                    ChatWindow.class
            );

            startActivity(intent);
        });

        // PART 4: Open TestToolbar
        Button testToolbarButton = findViewById(R.id.testToolbarButton);

        testToolbarButton.setOnClickListener(v -> {

            Log.i(ACTIVITY_NAME, "User clicked Test Toolbar");

            Intent intent = new Intent(
                    MainActivity.this,
                    TestToolbar.class
            );

            startActivity(intent);
        });

        // Assignment 3 Part 2: Configure weather controls
        setupWeatherForecast();
    }

    // Configure Canadian cities dropdown and weather button
    private void setupWeatherForecast() {

        Spinner citySpinner = findViewById(R.id.citySpinner);

        // Load Canadian cities from arrays.xml
        ArrayAdapter<CharSequence> cityAdapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.canadian_cities,
                        android.R.layout.simple_spinner_item
                );

        // Set the appearance of dropdown items
        cityAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        citySpinner.setAdapter(cityAdapter);

        Button weatherForecastButton =
                findViewById(R.id.weatherForecastButton);

        weatherForecastButton.setOnClickListener(v -> {

            // Retrieve the selected Canadian city
            String selectedCity =
                    citySpinner.getSelectedItem().toString();

            Log.i(
                    ACTIVITY_NAME,
                    "Weather Forecast selected for: " + selectedCity
            );

            // Launch WeatherForecast with selected city
            Intent intent = new Intent(
                    MainActivity.this,
                    WeatherForecast.class
            );

            intent.putExtra(EXTRA_CITY_NAME, selectedCity);

            startActivity(intent);
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
