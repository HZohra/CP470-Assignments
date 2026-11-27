
package com.zohrah.androidassignments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherForecast extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "WeatherForecast";
    private static final String DEFAULT_CITY = "Ottawa";

    // API key supplied in the assignment instructions
    private static final String API_KEY =
            "79cecf493cb6e52d25bb7b7050ff723c";

    private static final String WEATHER_URL =
            "https://api.openweathermap.org/data/2.5/weather";

    private static final String ICON_URL =
            "https://openweathermap.org/img/wn/";

    private TextView currentTemperatureTextView;
    private TextView minTemperatureTextView;
    private TextView maxTemperatureTextView;
    private ImageView weatherImageView;
    private ProgressBar weatherProgressBar;

    private ForecastQuery forecastQuery;
    private String selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(ACTIVITY_NAME, "onCreate() called");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_forecast);

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

        // Initialize GUI components
        TextView cityNameTextView =
                findViewById(R.id.cityNameTextView);

        currentTemperatureTextView =
                findViewById(R.id.currentTemperatureTextView);

        minTemperatureTextView =
                findViewById(R.id.minTemperatureTextView);

        maxTemperatureTextView =
                findViewById(R.id.maxTemperatureTextView);

        weatherImageView =
                findViewById(R.id.weatherImageView);

        weatherProgressBar =
                findViewById(R.id.weatherProgressBar);

        // Receive selected city from MainActivity
        selectedCity = getIntent().getStringExtra(
                MainActivity.EXTRA_CITY_NAME
        );

        if (selectedCity == null || selectedCity.trim().isEmpty()) {
            selectedCity = DEFAULT_CITY;
        }

        cityNameTextView.setText(
                getString(R.string.weather_city, selectedCity)
        );

        Log.i(ACTIVITY_NAME, "Selected city: " + selectedCity);

        // Enable back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Display progress bar before starting the background task
        weatherProgressBar.setProgress(0);
        weatherProgressBar.setVisibility(View.VISIBLE);

        // Start retrieving weather information
        forecastQuery = new ForecastQuery();
        forecastQuery.execute(selectedCity);
    }

    // Check whether an image exists in internal storage
    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    // Background task required by Assignment 3 Part 2
    private class ForecastQuery
            extends AsyncTask<String, Integer, String> {

        private String currentTemperature;
        private String minTemperature;
        private String maxTemperature;
        private String iconName;

        private Bitmap weatherBitmap;
        private boolean successful;

        @Override
        protected String doInBackground(String... args) {

            HttpURLConnection connection = null;

            try {
                String city = URLEncoder.encode(
                        args[0] + ",ca", "UTF-8"
                );

                String queryUrl = WEATHER_URL
                        + "?q=" + city
                        + "&APPID=" + API_KEY
                        + "&mode=xml"
                        + "&units=metric";

                Log.i(ACTIVITY_NAME,
                        "Connecting to weather API for " + args[0]);

                // Connect to OpenWeatherMap
                URL url = new URL(queryUrl);

                connection =
                        (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.connect();

                int responseCode = connection.getResponseCode();

                Log.i(ACTIVITY_NAME,
                        "HTTP response code: " + responseCode);

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException(
                            "HTTP error: " + responseCode
                    );
                }

                // Parse XML weather information
                try (InputStream inputStream =
                             connection.getInputStream()) {

                    XmlPullParser parser = Xml.newPullParser();

                    parser.setFeature(
                            XmlPullParser.FEATURE_PROCESS_NAMESPACES,
                            false
                    );

                    parser.setInput(inputStream, null);

                    int eventType = parser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {

                        if (isCancelled()) {
                            return null;
                        }

                        if (eventType == XmlPullParser.START_TAG) {

                            String tagName = parser.getName();

                            if ("temperature".equals(tagName)) {

                                currentTemperature =
                                        parser.getAttributeValue(
                                                null, "value");

                                publishProgress(25);

                                minTemperature =
                                        parser.getAttributeValue(
                                                null, "min");

                                publishProgress(50);

                                maxTemperature =
                                        parser.getAttributeValue(
                                                null, "max");

                                publishProgress(75);

                                Log.i(ACTIVITY_NAME,
                                        "Current temperature: "
                                                + currentTemperature);

                                Log.i(ACTIVITY_NAME,
                                        "Minimum temperature: "
                                                + minTemperature);

                                Log.i(ACTIVITY_NAME,
                                        "Maximum temperature: "
                                                + maxTemperature);

                            } else if ("weather".equals(tagName)) {

                                iconName =
                                        parser.getAttributeValue(
                                                null, "icon");

                                Log.i(ACTIVITY_NAME,
                                        "Weather icon: " + iconName);
                            }
                        }

                        eventType = parser.next();
                    }
                }

                // Verify that the required XML attributes were found
                if (currentTemperature == null
                        || minTemperature == null
                        || maxTemperature == null
                        || iconName == null) {

                    throw new IOException(
                            "Incomplete weather information"
                    );
                }

                // Retrieve the weather image
                weatherBitmap = loadWeatherImage(iconName);

                publishProgress(100);

                successful = true;

                return "Success";

            } catch (Exception e) {

                Log.e(ACTIVITY_NAME,
                        "Failed to retrieve weather information", e);

                return "Error";

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        // Load an existing image or download and save a new image
        private Bitmap loadWeatherImage(String icon)
                throws IOException {

            if (!icon.matches("[0-9]{2}[dn]")) {
                throw new IOException("Invalid weather icon name");
            }

            String imageFile = icon + ".png";

            Log.i(ACTIVITY_NAME,
                    "Looking for image file: " + imageFile);

            // Check internal storage first
            if (fileExistance(imageFile)) {

                try (FileInputStream fis =
                             openFileInput(imageFile)) {

                    Bitmap cachedImage =
                            BitmapFactory.decodeStream(fis);

                    if (cachedImage != null) {

                        Log.i(ACTIVITY_NAME,
                                "Found image locally: " + imageFile);

                        return cachedImage;
                    }
                }

                Log.w(ACTIVITY_NAME,
                        "Cached image could not be decoded");
            }

            // Download image if it does not exist locally
            Log.i(ACTIVITY_NAME,
                    "Downloading image: " + imageFile);

            HttpURLConnection imageConnection = null;

            try {

                URL imageUrl = new URL(
                        ICON_URL + icon + "@2x.png"
                );

                imageConnection =
                        (HttpURLConnection) imageUrl.openConnection();

                imageConnection.setConnectTimeout(15000);
                imageConnection.setReadTimeout(15000);
                imageConnection.connect();

                if (imageConnection.getResponseCode()
                        != HttpURLConnection.HTTP_OK) {

                    throw new IOException(
                            "Unable to download weather image"
                    );
                }

                Bitmap image;

                try (InputStream imageStream =
                             imageConnection.getInputStream()) {

                    image = BitmapFactory.decodeStream(imageStream);
                }

                if (image == null) {
                    throw new IOException("Invalid weather image");
                }

                // Save image to private application storage
                try (FileOutputStream outputStream =
                             openFileOutput(
                                     imageFile,
                                     Context.MODE_PRIVATE
                             )) {

                    image.compress(
                            Bitmap.CompressFormat.PNG,
                            80,
                            outputStream
                    );

                    outputStream.flush();
                }

                Log.i(ACTIVITY_NAME,
                        "Image saved locally: " + imageFile);

                return image;

            } finally {

                if (imageConnection != null) {
                    imageConnection.disconnect();
                }
            }
        }

        // Update progress bar on the UI thread
        @Override
        protected void onProgressUpdate(Integer... value) {

            super.onProgressUpdate(value);

            weatherProgressBar.setVisibility(View.VISIBLE);
            weatherProgressBar.setProgress(value[0]);

            Log.i(ACTIVITY_NAME,
                    "Loading progress: " + value[0] + "%");
        }

        // Display the downloaded information
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (successful) {

                currentTemperatureTextView.setText(
                        getString(
                                R.string.current_temperature,
                                currentTemperature
                        )
                );

                minTemperatureTextView.setText(
                        getString(
                                R.string.min_temperature,
                                minTemperature
                        )
                );

                maxTemperatureTextView.setText(
                        getString(
                                R.string.max_temperature,
                                maxTemperature
                        )
                );

                weatherImageView.setImageBitmap(weatherBitmap);

                Log.i(ACTIVITY_NAME,
                        "Weather information displayed successfully");

            } else {

                Toast.makeText(
                        WeatherForecast.this,
                        R.string.weather_error,
                        Toast.LENGTH_LONG
                ).show();
            }

            // Hide progress bar after loading
            weatherProgressBar.setVisibility(View.INVISIBLE);

            Log.i(ACTIVITY_NAME, "Progress bar hidden");
        }
    }

    @Override
    protected void onDestroy() {

        if (forecastQuery != null) {
            forecastQuery.cancel(true);
        }

        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
