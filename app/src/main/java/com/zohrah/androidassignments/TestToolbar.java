
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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.zohrah.androidassignments.databinding.ActivityTestToolbarBinding;

public class TestToolbar extends AppCompatActivity {

    // Existing navigation configuration
    private AppBarConfiguration appBarConfiguration;

    // PART 4: Snackbar message that Item 3 can change
    private String snackbarMessage = "You selected Item 1";

    // Existing activity binding
    private ActivityTestToolbarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Existing view binding
        binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        // Existing toolbar setup
        setSupportActionBar(binding.toolbar);

        // Existing navigation setup
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(
                                R.id.nav_host_fragment_content_test_toolbar
                        );

        if (navHostFragment != null) {

            NavController navController =
                    navHostFragment.getNavController();

            appBarConfiguration =
                    new AppBarConfiguration.Builder(
                            navController.getGraph()
                    ).build();

            NavigationUI.setupActionBarWithNavController(
                    this,
                    navController,
                    appBarConfiguration
            );
        }

        // Existing Floating Action Button functionality
        binding.fab.setOnClickListener(
                view -> Snackbar.make(
                                view,
                                "Replace with your own action",
                                Snackbar.LENGTH_LONG
                        )
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null)
                        .show()
        );
    }

    // PART 4: Display toolbar menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.toolbar_menu,
                menu
        );

        return true;
    }

    // PART 4: Handle toolbar menu clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        // Item 1: Show Snackbar
        if (itemId == R.id.action_item1) {

            Snackbar.make(
                    binding.getRoot(),
                    snackbarMessage,
                    Snackbar.LENGTH_LONG
            ).show();

            return true;
        }

        // Item 2: Show confirmation dialog
        else if (itemId == R.id.action_item2) {

            new AlertDialog.Builder(this)
                    .setTitle("Finish Activity")
                    .setMessage("Do you want to go back?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

            return true;
        }

        // PART 4: Item 3 opens custom dialog
        else if (itemId == R.id.action_item3) {

            showChangeMessageDialog();

            return true;
        }

        // About: Show app information
        else if (itemId == R.id.action_about) {

            Toast.makeText(
                    this,
                    "Version 1.0, by Zohra Haidary",
                    Toast.LENGTH_LONG
            ).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // PART 4: Custom dialog for changing Snackbar message
    private void showChangeMessageDialog() {

        // Load the custom XML layout
        View dialogView = getLayoutInflater().inflate(
                R.layout.dialog_change_message,
                null
        );

        // Find the EditText inside the dialog
        EditText newMessageEditText =
                dialogView.findViewById(R.id.newMessageEditText);

        // Display current Snackbar message in the input field
        newMessageEditText.setText(snackbarMessage);

        // Create and display the dialog
        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {

                    // Read the new message entered by the user
                    String newMessage = newMessageEditText
                            .getText()
                            .toString()
                            .trim();

                    // Update Snackbar message if input is not empty
                    if (!newMessage.isEmpty()) {

                        snackbarMessage = newMessage;

                        Toast.makeText(
                                TestToolbar.this,
                                "Snackbar message updated",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    // Existing navigation method — unchanged
    @Override
    public boolean onSupportNavigateUp() {

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(
                                R.id.nav_host_fragment_content_test_toolbar
                        );

        boolean handled = false;

        if (navHostFragment != null) {

            NavController navController =
                    navHostFragment.getNavController();

            handled = NavigationUI.navigateUp(
                    navController,
                    appBarConfiguration
            );
        }

        return handled || super.onSupportNavigateUp();
    }
}
