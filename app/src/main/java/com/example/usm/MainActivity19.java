package com.example.usm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity19 extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main19);

        db = new DatabaseHelper(this);

        EditText etEmail = findViewById(R.id.editAdminEmail);
        EditText etPassword = findViewById(R.id.editAdminPassword);
        MaterialButton btnSignIn = findViewById(R.id.btnAdminSignIn);

        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkUser(email, password, "ADMIN")) {
                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity19.this, MainActivity20.class); // To Admin Dashboard
                intent.putExtra("ADMIN_EMAIL", email);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
