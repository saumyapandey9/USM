package com.example.usm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity11 extends AppCompatActivity {

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main11);

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        Button btn1 = findViewById(R.id.btnFootball1);
        Button btn2 = findViewById(R.id.btnFootball2);

        btn1.setOnClickListener(v -> openSlots("Football Turf 1"));
        btn2.setOnClickListener(v -> openSlots("Football Turf 2"));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void openSlots(String playgroundName) {
        Intent intent = new Intent(this, MainActivity6.class);
        intent.putExtra("PLAYGROUND_NAME", playgroundName);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("SPORT_NAME", "Football");
        startActivity(intent);
    }
}
