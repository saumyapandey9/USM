package com.example.usm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Calendar;

public class MainActivity4 extends AppCompatActivity {

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        findViewById(R.id.button3).setOnClickListener(v -> showDatePicker("Cricket"));
        findViewById(R.id.button24).setOnClickListener(v -> showDatePicker("Badminton"));
        findViewById(R.id.button22).setOnClickListener(v -> showDatePicker("Football"));
        findViewById(R.id.button21).setOnClickListener(v -> showDatePicker("Basketball"));
        findViewById(R.id.button23).setOnClickListener(v -> showDatePicker("Volleyball"));
        findViewById(R.id.button26).setOnClickListener(v -> showDatePicker("Tennis"));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDatePicker(String sport) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    openPlaygroundSelection(sport, selectedDate);
                }, year, month, day);
        
        // Prevent booking for past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void openPlaygroundSelection(String sport, String date) {
        Intent intent = new Intent(this, MainActivity5.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("SPORT_NAME", sport);
        intent.putExtra("SELECTED_DATE", date);
        startActivity(intent);
    }
}
