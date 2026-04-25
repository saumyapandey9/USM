package com.example.usm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity14 extends AppCompatActivity {

    String staffEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main14);

        staffEmail = getIntent().getStringExtra("STAFF_EMAIL");

        // Section: Account
        Button btnProfile = findViewById(R.id.btn_staff_profile);
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity14.this, MainActivity24.class);
            intent.putExtra("USER_EMAIL", staffEmail);
            startActivity(intent);
        });

        // Section: Management
        Button btnSeeAllBookings = findViewById(R.id.btn_see_all_bookings);
        btnSeeAllBookings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity14.this, MainActivity15.class);
            startActivity(intent);
        });

        Button btnSeeMaintenanceBookings = findViewById(R.id.btn_see_maintenance_bookings);
        btnSeeMaintenanceBookings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity14.this, MainActivity17.class);
            startActivity(intent);
        });

        // Section: Actions
        Button btnBookMaintenance = findViewById(R.id.btn_book_maintenance);
        btnBookMaintenance.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity14.this, MainActivity16.class);
            intent.putExtra("STAFF_EMAIL", staffEmail);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
