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

public class MainActivity23 extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main23);

        db = new DatabaseHelper(this);
        LinearLayout listContainer = findViewById(R.id.ll_deleted_list);
        TextView noDeletedMsg = findViewById(R.id.tv_no_deleted);

        Cursor cursor = db.getDeletedUsers();
        if (cursor != null && cursor.getCount() > 0) {
            noDeletedMsg.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("del_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("del_email"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("del_role"));
                String reason = cursor.getString(cursor.getColumnIndexOrThrow("del_reason"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("del_date"));
                
                addDeletedUserCard(listContainer, name, email, role, reason, date);
            }
            cursor.close();
        } else {
            noDeletedMsg.setVisibility(View.VISIBLE);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addDeletedUserCard(LinearLayout container, String name, String email, String role, String reason, String date) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(layoutParams);
        card.setCardElevation(4f);
        card.setRadius(16f);

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setPadding(32, 32, 32, 32);

        TextView tvName = new TextView(this);
        tvName.setText(name + " (" + role + ")");
        tvName.setTextSize(18);
        tvName.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvEmail = new TextView(this);
        tvEmail.setText(email);
        tvEmail.setTextSize(14);

        TextView tvReason = new TextView(this);
        tvReason.setText("Reason: " + reason);
        tvReason.setTextSize(14);
        tvReason.setPadding(0, 8, 0, 8);
        tvReason.setTypeface(null, android.graphics.Typeface.ITALIC);

        TextView tvDate = new TextView(this);
        tvDate.setText("Deleted on: " + date);
        tvDate.setTextSize(12);
        tvDate.setTextColor(android.graphics.Color.GRAY);

        innerLayout.addView(tvName);
        innerLayout.addView(tvEmail);
        innerLayout.addView(tvReason);
        innerLayout.addView(tvDate);
        card.addView(innerLayout);
        container.addView(card);
    }
}
