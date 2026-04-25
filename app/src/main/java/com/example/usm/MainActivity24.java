package com.example.usm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity24 extends AppCompatActivity {

    DatabaseHelper db;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main24);

        db = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        TextView tvName = findViewById(R.id.tv_profile_name);
        TextView tvEmail = findViewById(R.id.tv_profile_email);
        TextView tvRole = findViewById(R.id.tv_profile_role);
        TextView tvId = findViewById(R.id.tv_profile_id);

        if (userEmail != null) {
            Cursor cursor = db.getUserDetails(userEmail);
            if (cursor != null && cursor.moveToFirst()) {
                tvName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                tvEmail.setText(userEmail);
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                tvRole.setText(role);
                tvId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
                cursor.close();
            }
        }

        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity24.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
