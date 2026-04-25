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

public class MainActivity8 extends AppCompatActivity {

    DatabaseHelper db;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main8);

        db = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        LinearLayout listContainer = findViewById(R.id.ll_bookings_list);
        TextView noBookingsMsg = findViewById(R.id.tv_no_bookings);

        if (userEmail != null) {
            Cursor cursor = db.getUserBookings(userEmail);
            if (cursor.getCount() == 0) {
                noBookingsMsg.setVisibility(View.VISIBLE);
            } else {
                noBookingsMsg.setVisibility(View.GONE);
                while (cursor.moveToNext()) {
                    String sport = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SPORT));
                    String playground = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PLAYGROUND));
                    String slot = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SLOT));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATE));

                    addBookingCard(listContainer, sport, playground, slot, date);
                }
            }
            cursor.close();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addBookingCard(LinearLayout container, String sport, String playground, String slot, String date) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 32);
        card.setLayoutParams(layoutParams);
        card.setCardElevation(8f);
        card.setRadius(32f);

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setPadding(48, 48, 48, 48);
        innerLayout.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));

        TextView tvSport = new TextView(this);
        tvSport.setText(sport.toUpperCase());
        tvSport.setTextColor(android.graphics.Color.parseColor("#1A237E"));
        tvSport.setTextSize(18);
        tvSport.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvPlayground = new TextView(this);
        tvPlayground.setText(playground);
        tvPlayground.setTextSize(16);
        tvPlayground.setPadding(0, 8, 0, 8);

        TextView tvSlot = new TextView(this);
        tvSlot.setText("Date: " + date + "\nTime: " + slot);
        tvSlot.setTextColor(android.graphics.Color.parseColor("#546E7A"));
        tvSlot.setTextSize(14);

        innerLayout.addView(tvSport);
        innerLayout.addView(tvPlayground);
        innerLayout.addView(tvSlot);
        card.addView(innerLayout);
        container.addView(card);
    }
}
