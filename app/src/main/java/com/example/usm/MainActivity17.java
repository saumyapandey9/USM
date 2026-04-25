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

public class MainActivity17 extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main17);

        db = new DatabaseHelper(this);
        LinearLayout listContainer = findViewById(R.id.ll_maint_list);
        TextView noBookingMsg = findViewById(R.id.tv_no_maint_booking);

        loadMaintenanceBookings(listContainer, noBookingMsg);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadMaintenanceBookings(LinearLayout container, TextView noMsg) {
        container.removeAllViews();
        Cursor cursor = db.getMaintenanceBookings();
        
        if (cursor != null && cursor.getCount() > 0) {
            noMsg.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                String sport = cursor.getString(cursor.getColumnIndexOrThrow("sport"));
                String playground = cursor.getString(cursor.getColumnIndexOrThrow("playground"));
                String slot = cursor.getString(cursor.getColumnIndexOrThrow("slot"));
                String reason = cursor.getString(cursor.getColumnIndexOrThrow("reason"));

                addMaintenanceCard(container, sport, playground, slot, reason);
            }
            cursor.close();
        } else {
            noMsg.setVisibility(View.VISIBLE);
        }
    }

    private void addMaintenanceCard(LinearLayout container, String sport, String playground, String slot, String reason) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 24);
        card.setLayoutParams(lp);
        card.setCardElevation(4f);
        card.setRadius(16f);
        card.setStrokeColor(android.graphics.Color.parseColor("#FF9800"));
        card.setStrokeWidth(2);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(32, 32, 32, 32);
        inner.setBackgroundColor(android.graphics.Color.parseColor("#FFF3E0"));

        TextView tvHeader = new TextView(this);
        tvHeader.setText(sport.toUpperCase() + " - " + playground);
        tvHeader.setTextColor(android.graphics.Color.parseColor("#E65100"));
        tvHeader.setTextSize(18);
        tvHeader.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvReason = new TextView(this);
        tvReason.setText("Reason: " + reason);
        tvReason.setTextSize(16);
        tvReason.setPadding(0, 8, 0, 8);
        tvReason.setTextColor(android.graphics.Color.BLACK);

        TextView tvSlot = new TextView(this);
        tvSlot.setText("Time: " + slot);
        tvSlot.setTextSize(14);
        tvSlot.setTextColor(android.graphics.Color.parseColor("#455A64"));

        inner.addView(tvHeader);
        inner.addView(tvReason);
        inner.addView(tvSlot);
        card.addView(inner);
        container.addView(card);
    }
}
