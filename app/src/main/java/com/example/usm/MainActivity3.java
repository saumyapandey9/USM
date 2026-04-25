package com.example.usm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity3 extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);

        db = new DatabaseHelper(this);

        EditText etName = findViewById(R.id.editTextName);
        EditText etEmail = findViewById(R.id.editTextTextEmailAddress4);
        EditText etPassword = findViewById(R.id.editTextTextPassword3);
        RadioGroup rgRole = findViewById(R.id.rgRole);
        RadioButton rbStudent = findViewById(R.id.rbStudentReg);
        RadioButton rbStaff = findViewById(R.id.rbStaffReg);
        RadioButton rbAdmin = findViewById(R.id.rbAdminReg);

        // Get the role passed from the first screen
        String roleFromIntent = getIntent().getStringExtra("SELECTED_ROLE");
        if (roleFromIntent != null) {
            if (roleFromIntent.equals("STUDENT")) {
                rbStudent.setChecked(true);
            } else if (roleFromIntent.equals("STAFF")) {
                rbStaff.setChecked(true);
            } else if (roleFromIntent.equals("ADMIN")) {
                rbAdmin.setChecked(true);
            }
        }

        Button registerBtn = findViewById(R.id.button);
        registerBtn.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            int selectedRoleId = rgRole.getCheckedRadioButtonId();
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedRoleId == -1) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String role;
            if (selectedRoleId == R.id.rbStudentReg) {
                role = "STUDENT";
            } else if (selectedRoleId == R.id.rbStaffReg) {
                role = "STAFF";
            } else {
                role = "ADMIN";
            }

            if (db.registerUser(name, email, password, role)) {
                Toast.makeText(MainActivity3.this, "Account successfully registered! Please login.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity3.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration failed or email already exists", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
