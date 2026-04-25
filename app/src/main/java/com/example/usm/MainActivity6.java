package com.example.usm;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.HashSet;
import java.util.Set;

public class MainActivity6 extends AppCompatActivity {

    DatabaseHelper db;
    String userEmail;
    String sportName;
    String selectedDate;
    String targetPlayground;
    boolean showOnlyFree;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main6);

        db = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        sportName = getIntent().getStringExtra("SPORT_NAME");
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        targetPlayground = getIntent().getStringExtra("PLAYGROUND_NAME");
        showOnlyFree = getIntent().getBooleanExtra("SHOW_ONLY_FREE", false);
        
        TextView tvSport = findViewById(R.id.tv_selected_sport);
        tvSport.setText((sportName != null ? sportName : "Sports") + " - " + (selectedDate != null ? selectedDate : ""));

        container = findViewById(R.id.ll_playgrounds_container);

        String[] slots = {
            "09:00 AM - 10:00 AM", "10:00 AM - 11:00 AM", "11:00 AM - 12:00 PM",
            "12:00 PM - 01:00 PM", "01:00 PM - 02:00 PM", "02:00 PM - 03:00 PM",
            "03:00 PM - 04:00 PM", "04:00 PM - 05:00 PM", "05:00 PM - 06:00 PM"
        };

        if (targetPlayground != null) {
            addPlaygroundSection(targetPlayground, slots);
        } else {
            String[] playgrounds = {"Playground 1", "Playground 2", "Playground 3"};
            for (String playground : playgrounds) {
                addPlaygroundSection(playground, slots);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addPlaygroundSection(String playgroundName, String[] slots) {
        Set<String> bookedSlots = new HashSet<>();
        Set<String> maintenanceSlots = new HashSet<>();
        
        SQLiteDatabase readableDb = db.getReadableDatabase();
        Cursor cursor = readableDb.rawQuery("SELECT slot, is_maintenance FROM bookings WHERE sport=? AND playground=? AND booking_date=?", 
                new String[]{sportName, playgroundName, selectedDate});
        
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String slot = cursor.getString(0);
                int isMaint = cursor.getInt(1);
                if (isMaint == 1) maintenanceSlots.add(slot);
                else bookedSlots.add(slot);
            }
            cursor.close();
        }

        TextView tvHeader = new TextView(this);
        tvHeader.setText(playgroundName + (showOnlyFree ? " (Available Slots)" : ""));
        tvHeader.setTextSize(22);
        tvHeader.setPadding(0, 32, 0, 16);
        tvHeader.setTextColor(android.graphics.Color.parseColor("#1A237E"));
        tvHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        container.addView(tvHeader);

        for (String slotTime : slots) {
            boolean isMaintenance = maintenanceSlots.contains(slotTime);
            boolean isBooked = bookedSlots.contains(slotTime);

            if (showOnlyFree && (isMaintenance || isBooked)) continue;

            MaterialButton btn = new MaterialButton(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 140);
            lp.setMargins(0, 0, 0, 16);
            btn.setLayoutParams(lp);
            btn.setText(slotTime);
            btn.setCornerRadius(24);
            
            if (isMaintenance) {
                btn.setText(slotTime + " (Maintenance)");
                btn.setEnabled(false);
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FFF3E0")));
                btn.setTextColor(android.graphics.Color.parseColor("#EF6C00"));
            } else if (isBooked) {
                btn.setText(slotTime + " (Booked)");
                btn.setEnabled(false);
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FFEBEE")));
                btn.setTextColor(android.graphics.Color.parseColor("#C62828"));
            } else {
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#E8F5E9")));
                btn.setTextColor(android.graphics.Color.parseColor("#2E7D32"));
                
                btn.setOnClickListener(v -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Confirm Booking")
                            .setMessage("Book " + sportName + " at " + playgroundName + " for " + slotTime + " on " + selectedDate + "?")
                            .setPositiveButton("Yes, book", (dialog, which) -> {
                                if (userEmail == null) {
                                    Toast.makeText(this, "Session error. Please login.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (db.addBooking(userEmail, sportName, playgroundName, slotTime, selectedDate, 0, "")) {
                                    Toast.makeText(this, "Booked Successfully for " + selectedDate, Toast.LENGTH_SHORT).show();
                                    btn.setEnabled(false);
                                    btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FFEBEE")));
                                    btn.setTextColor(android.graphics.Color.parseColor("#C62828"));
                                    btn.setText(slotTime + " (Booked)");
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                });
            }
            container.addView(btn);
        }
    }
}
