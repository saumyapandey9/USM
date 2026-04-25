package com.example.usm;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        RadioButton rbStudent = findViewById(R.id.checkBox3);
        RadioButton rbStaff = findViewById(R.id.checkBox4);
        RadioButton rbAdmin = findViewById(R.id.checkBoxAdmin);
        
        Button btn = findViewById(R.id.btnLogin);
        btn.setOnClickListener(v->{
            if (rbAdmin.isChecked()) {
                Intent intent = new Intent(MainActivity.this, MainActivity19.class); // Admin Login screen
                startActivity(intent);
            } else if (rbStaff.isChecked()) {
                Intent intent = new Intent(MainActivity.this, MainActivity18.class);
                startActivity(intent);
            } else if (rbStudent.isChecked()) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select role to Login", Toast.LENGTH_SHORT).show();
            }
        });

        Button registerBtn = findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(v->{
            showRoleSelectionPopup();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showRoleSelectionPopup() {
        String[] roles = {"STUDENT", "STAFF", "ADMIN"};
        new AlertDialog.Builder(this)
                .setTitle("Create Account as")
                .setItems(roles, (dialog, which) -> {
                    String selectedRole = roles[which];
                    Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                    intent.putExtra("SELECTED_ROLE", selectedRole);
                    startActivity(intent);
                })
                .show();
    }
}
