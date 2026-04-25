package com.example.usm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity7 extends AppCompatActivity {

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main7);

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        findViewById(R.id.btn_open_profile).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity7.this, MainActivity24.class);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });

        findViewById(R.id.btn_choose_sports).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity7.this, MainActivity4.class);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });

        findViewById(R.id.btn_my_bookings).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity7.this, MainActivity8.class);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });

        findViewById(R.id.btn_feedback).setOnClickListener(v -> {
            Toast.makeText(this, "Feedback feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_settings).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity7.this, MainActivity25.class);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
