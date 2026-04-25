package com.example.usm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity16 extends AppCompatActivity {

    private String selectedSport = "";
    private String selectedPlayground = "";
    private String selectedDate = "";
    private String selectedSlot = "";
    private String maintenanceReason = "";
    DatabaseHelper db;
    String staffEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main16);

        db = new DatabaseHelper(this);
        staffEmail = getIntent().getStringExtra("STAFF_EMAIL");

        Button btnSelect = findViewById(R.id.btn_select_maintenance_sport);
        btnSelect.setOnClickListener(v -> showSportSelectionDialog());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showSportSelectionDialog() {
        String[] sports = {"Cricket", "Badminton", "Football", "Basketball", "Volleyball", "Tennis"};
        new AlertDialog.Builder(this)
                .setTitle("Select Sport")
                .setItems(sports, (dialog, which) -> {
                    selectedSport = sports[which];
                    showPlaygroundSelectionDialog();
                })
                .show();
    }

    private void showPlaygroundSelectionDialog() {
        String[] playgrounds = {"Playground 1", "Playground 2", "Playground 3"};
        new AlertDialog.Builder(this)
                .setTitle("Select Playground for " + selectedSport)
                .setItems(playgrounds, (dialog, which) -> {
                    selectedPlayground = playgrounds[which];
                    showDatePicker();
                })
                .show();
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    askForMaintenanceConfirmation();
                }, year, month, day);
        
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setTitle("Select Date for Maintenance");
        datePickerDialog.show();
    }

    private void askForMaintenanceConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Maintenance")
                .setMessage("Book maintenance for " + selectedSport + " - " + selectedPlayground + " on " + selectedDate + "?")
                .setPositiveButton("Yes", (dialog, which) -> showSlotSelectionDialog())
                .setNegativeButton("No", null)
                .show();
    }

    private void showSlotSelectionDialog() {
        String[] slots = {"09:00 AM - 10:00 AM", "10:00 AM - 11:00 AM", "11:00 AM - 12:00 PM", "12:00 PM - 01:00 PM", "01:00 PM - 02:00 PM", "02:00 PM - 03:00 PM", "03:00 PM - 04:00 PM", "04:00 PM - 05:00 PM", "05:00 PM - 06:00 PM"};
        new AlertDialog.Builder(this)
                .setTitle("Select Slot for Maintenance (" + selectedDate + ")")
                .setItems(slots, (dialog, which) -> {
                    selectedSlot = slots[which];
                    showReasonInputDialog();
                })
                .show();
    }

    private void showReasonInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Maintenance Reason");
        builder.setMessage("Please enter the reason for maintenance:");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Next", (dialog, which) -> {
            maintenanceReason = input.getText().toString();
            if (maintenanceReason.trim().isEmpty()) {
                Toast.makeText(this, "Reason is required", Toast.LENGTH_SHORT).show();
                showReasonInputDialog();
            } else {
                confirmFinalBooking();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void confirmFinalBooking() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Maintenance Booking")
                .setMessage("Confirm maintenance booking for " + selectedSport + " (" + selectedPlayground + ") on " + selectedDate + " at " + selectedSlot + "?\n\nReason: " + maintenanceReason)
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (db.addBooking(staffEmail != null ? staffEmail : "Staff", selectedSport, selectedPlayground, selectedSlot, selectedDate, 1, maintenanceReason)) {
                        Toast.makeText(this, "Maintenance Booked Successfully!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to book maintenance.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
