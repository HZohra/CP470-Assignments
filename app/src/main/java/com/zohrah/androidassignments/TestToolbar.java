package com.zohrah.androidassignments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.zohrah.androidassignments.databinding.ActivityTestToolbarBinding;

public class TestToolbar extends AppCompatActivity {

    // PART 4: Message displayed by toolbar Item 1
    private String snackbarMessage;

    // Activity binding for toolbar and floating action button
    private ActivityTestToolbarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the Snackbar message using the current language
        snackbarMessage = getString(R.string.toolbar_default_snackbar);

        // Existing edge-to-edge window insets
        ViewCompat.setOnApplyWindowInsetsListener(
                binding.main, (v, insets) -> {

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

        // Connect the custom toolbar
        setSupportActionBar(binding.toolbar);

        // PART 4: Floating Action Button with translated Snackbar
        binding.fab.setOnClickListener(
                view -> Snackbar.make(
                                view,
                                R.string.toolbar_fab_snackbar,
                                Snackbar.LENGTH_LONG
                        )
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null)
                        .show()
        );
    }

    // Display the three toolbar icons and About menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Handle toolbar menu selections
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        // Item 1: Display Snackbar
        if (itemId == R.id.action_item1) {

            Snackbar.make(
                    binding.getRoot(),
                    snackbarMessage,
                    Snackbar.LENGTH_LONG
            ).show();

            return true;
        }

        // Item 2: Confirmation dialog
        else if (itemId == R.id.action_item2) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.toolbar_dialog_title)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        finish();
                    })
                    .setNegativeButton(R.string.no, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

            return true;
        }

        // Item 3: Open custom message dialog
        else if (itemId == R.id.action_item3) {

            showChangeMessageDialog();
            return true;
        }

        // About: Display application information
        else if (itemId == R.id.action_about) {

            Toast.makeText(
                    this,
                    R.string.toolbar_about_message,
                    Toast.LENGTH_LONG
            ).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Custom dialog for changing the Snackbar message
    private void showChangeMessageDialog() {

        View dialogView = getLayoutInflater().inflate(
                R.layout.dialog_change_message,
                null
        );

        EditText newMessageEditText =
                dialogView.findViewById(R.id.newMessageEditText);

        // Display the current Snackbar message
        newMessageEditText.setText(snackbarMessage);

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton(R.string.toolbar_ok, (dialog, which) -> {

                    String newMessage = newMessageEditText
                            .getText()
                            .toString()
                            .trim();

                    if (!newMessage.isEmpty()) {

                        snackbarMessage = newMessage;

                        Toast.makeText(
                                TestToolbar.this,
                                R.string.toolbar_message_updated,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton(R.string.toolbar_cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
