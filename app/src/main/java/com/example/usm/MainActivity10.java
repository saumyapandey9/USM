package com.example.usm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity10 extends AppCompatActivity {

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main10);

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        Button btn1 = findViewById(R.id.btnBasketball1);
        Button btn2 = findViewById(R.id.btnBasketball2);
        Button btn3 = findViewById(R.id.btnBasketball3);

        btn1.setOnClickListener(v -> openSlots("Basketball Court 1"));
        btn2.setOnClickListener(v -> openSlots("Basketball Court 2"));
        btn3.setOnClickListener(v -> openSlots("Basketball Court 3"));

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
        intent.putExtra("SPORT_NAME", "Basketball");
        startActivity(intent);
    }
}
