package com.example.usm;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class MainActivity15 extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main15);

        db = new DatabaseHelper(this);
        LinearLayout listContainer = findViewById(R.id.ll_all_bookings_list);
        TextView noBookingsMsg = findViewById(R.id.tv_no_bookings_staff);

        loadAllBookings(listContainer, noBookingsMsg);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadAllBookings(LinearLayout container, TextView noMsg) {
        container.removeAllViews();
        Cursor cursor = db.getAllStudentBookings();
        
        if (cursor != null && cursor.getCount() > 0) {
            noMsg.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                String sport = cursor.getString(cursor.getColumnIndexOrThrow("sport"));
                String playground = cursor.getString(cursor.getColumnIndexOrThrow("playground"));
                String slot = cursor.getString(cursor.getColumnIndexOrThrow("slot"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("booking_date"));
                String studentName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String studentEmail = cursor.getString(cursor.getColumnIndexOrThrow("user_email"));

                addBookingCard(container, studentName, studentEmail, sport, playground, slot, date);
            }
            cursor.close();
        } else {
            noMsg.setVisibility(View.VISIBLE);
        }
    }

    private void addBookingCard(LinearLayout container, String name, String email, String sport, String playground, String slot, String date) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 24);
        card.setLayoutParams(lp);
        card.setCardElevation(4f);
        card.setRadius(16f);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(32, 32, 32, 32);
        inner.setBackgroundColor(android.graphics.Color.WHITE);

        TextView tvHeader = new TextView(this);
        tvHeader.setText(sport.toUpperCase() + " - " + playground);
        tvHeader.setTextColor(android.graphics.Color.parseColor("#1A237E"));
        tvHeader.setTextSize(18);
        tvHeader.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvDetails = new TextView(this);
        tvDetails.setText("Student: " + name + "\nEmail: " + email + "\nDate: " + date + "\nSlot: " + slot);
        tvDetails.setTextSize(14);
        tvDetails.setPadding(0, 8, 0, 0);

        inner.addView(tvHeader);
        inner.addView(tvDetails);
        card.addView(inner);
        container.addView(card);
    }
}
