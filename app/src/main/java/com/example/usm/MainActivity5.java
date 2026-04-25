package com.example.usm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class MainActivity5 extends AppCompatActivity {

    DatabaseHelper db;
    String userEmail;
    String sportName;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main5);

        db = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        sportName = getIntent().getStringExtra("SPORT_NAME");
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");

        TextView tvSubtitle = findViewById(R.id.tv_sport_subtitle);
        tvSubtitle.setText((sportName != null ? sportName : "Sport") + " - " + (selectedDate != null ? selectedDate : ""));

        setupPlaygroundCard(R.id.cardPlayground1, R.id.tv_free1, R.id.tv_booked1, "Playground 1");
        setupPlaygroundCard(R.id.cardPlayground2, R.id.tv_free2, R.id.tv_booked2, "Playground 2");
        setupPlaygroundCard(R.id.cardPlayground3, R.id.tv_free3, R.id.tv_booked3, "Playground 3");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupPlaygroundCard(int cardId, int freeTvId, int bookedTvId, String playgroundName) {
        MaterialCardView card = findViewById(cardId);
        TextView tvFree = findViewById(freeTvId);
        TextView tvBooked = findViewById(bookedTvId);

        int totalSlots = 9;
        int bookedCount = db.getBookedCount(sportName, playgroundName, selectedDate);
        int freeCount = totalSlots - bookedCount;

        tvFree.setText("Free: " + freeCount);
        tvBooked.setText("Booked: " + bookedCount);

        // Update: Click on "Free" specifically if you want that logic, 
        // but for now card click opens all or filtered based on previous logic.
        card.setOnClickListener(v -> openSlots(playgroundName, false));
        tvFree.setOnClickListener(v -> openSlots(playgroundName, true));
    }

    private void openSlots(String playgroundName, boolean onlyFree) {
        Intent intent = new Intent(this, MainActivity6.class);
        intent.putExtra("PLAYGROUND_NAME", playgroundName);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("SPORT_NAME", sportName);
        intent.putExtra("SELECTED_DATE", selectedDate);
        intent.putExtra("SHOW_ONLY_FREE", onlyFree);
        startActivity(intent);
    }
}
