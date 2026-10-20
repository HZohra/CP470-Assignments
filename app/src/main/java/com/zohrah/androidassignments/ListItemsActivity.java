
package com.zohrah.androidassignments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListItemsActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "ListItemsActivity";

    private static final int CAMERA_REQUEST_CODE = 20;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 21;

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(ACTIVITY_NAME, "onCreate() called");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_items);

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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // CAMERA FUNCTIONALITY
        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(v -> checkCameraPermission());

        // SWITCH FUNCTIONALITY
        Switch switchButton = findViewById(R.id.switch1);

        switchButton.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {
                        print(getString(R.string.switch_on));
                    } else {
                        Toast.makeText(
                                ListItemsActivity.this,
                                getString(R.string.switch_off),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );

        // CHECKBOX FUNCTIONALITY
        CheckBox checkBox = findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (!isChecked) {
                        return;
                    }

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(ListItemsActivity.this);

                    builder.setTitle(R.string.dialog_title)
                            .setMessage(R.string.dialog_message)
                            .setPositiveButton(
                                    R.string.yes,
                                    (dialog, which) -> {

                                        Log.i(
                                                ACTIVITY_NAME,
                                                "User selected Yes - finishing activity"
                                        );

                                        Intent resultIntent = new Intent();

                                        resultIntent.putExtra(
                                                "Response",
                                                getString(R.string.response_message)
                                        );

                                        setResult(
                                                Activity.RESULT_OK,
                                                resultIntent
                                        );

                                        finish();
                                    }
                            )
                            .setNegativeButton(
                                    R.string.no,
                                    (dialog, which) -> {
                                        checkBox.setChecked(false);
                                        dialog.dismiss();
                                    }
                            )
                            .setOnCancelListener(
                                    dialog -> checkBox.setChecked(false)
                            )
                            .show();
                }
        );
    }

    // CHECK CAMERA PERMISSION
    private void checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {

            openCamera();

        } else {

            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE
            );
        }
    }

    // HANDLE CAMERA PERMISSION RESULT
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(ACTIVITY_NAME, "Camera permission granted");
                openCamera();

            } else {

                Log.i(ACTIVITY_NAME, "Camera permission denied");
                print(getString(R.string.camera_denied));
            }
        }
    }

    // REUSABLE PRINT METHOD
    private void print(String message) {
        Toast.makeText(
                ListItemsActivity.this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    // OPEN CAMERA
    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {

            startActivityForResult(intent, CAMERA_REQUEST_CODE);

        } catch (android.content.ActivityNotFoundException e) {

            Log.e(
                    ACTIVITY_NAME,
                    "No camera application available",
                    e
            );

            print(getString(R.string.camera_unavailable));

        } catch (SecurityException e) {

            Log.e(
                    ACTIVITY_NAME,
                    "Camera access denied",
                    e
            );

            print(getString(R.string.camera_denied));
        }
    }

    // RECEIVE PHOTO FROM CAMERA
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE
                && resultCode == Activity.RESULT_OK
                && data != null) {

            Bundle extras = data.getExtras();

            if (extras != null) {

                Object imageData = extras.get("data");

                if (imageData instanceof Bitmap) {

                    Bitmap photo = (Bitmap) imageData;

                    imageButton.setScaleType(
                            ImageView.ScaleType.CENTER_CROP
                    );

                    imageButton.setImageBitmap(photo);

                    Log.i(ACTIVITY_NAME, "Camera image updated");
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

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
