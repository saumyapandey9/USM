package com.example.usm;

import android.content.Intent;
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

public class MainActivity21 extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main21);

        db = new DatabaseHelper(this);
        listContainer = findViewById(R.id.ll_users_list);

        loadUsers();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers(); // Refresh list when returning from detail screen
    }

    private void loadUsers() {
        listContainer.removeAllViews();
        Cursor cursor = db.getAllUsers();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                
                addUserCard(name, email, role);
            }
            cursor.close();
        }
    }

    private void addUserCard(String name, String email, String role) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(layoutParams);
        card.setCardElevation(4f);
        card.setRadius(16f);
        card.setClickable(true);
        card.setFocusable(true);

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setPadding(32, 32, 32, 32);
        
        if ("STAFF".equals(role)) {
            innerLayout.setBackgroundColor(android.graphics.Color.parseColor("#E8F5E9"));
        } else if ("ADMIN".equals(role)) {
            innerLayout.setBackgroundColor(android.graphics.Color.parseColor("#FFF9C4"));
        } else {
            innerLayout.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
        }

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextSize(18);
        tvName.setTextColor(android.graphics.Color.BLACK);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvEmail = new TextView(this);
        tvEmail.setText(email);
        tvEmail.setTextSize(14);

        TextView tvRole = new TextView(this);
        tvRole.setText("Role: " + role);
        tvRole.setTextSize(12);
        tvRole.setTypeface(null, android.graphics.Typeface.ITALIC);

        innerLayout.addView(tvName);
        innerLayout.addView(tvEmail);
        innerLayout.addView(tvRole);
        card.addView(innerLayout);

        card.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity21.this, MainActivity22.class);
            intent.putExtra("USER_NAME", name);
            intent.putExtra("USER_EMAIL", email);
            intent.putExtra("USER_ROLE", role);
            startActivity(intent);
        });

        listContainer.addView(card);
    }
}
