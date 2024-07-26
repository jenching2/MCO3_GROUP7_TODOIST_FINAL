package com.mobdeve.s13.ching.jennilyn.mco3mobdeve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);  // Ensure this matches the layout file name

        etFullName = findViewById(R.id.etFullNameSignIn);
        etPhone = findViewById(R.id.etPhoneSignIn);
        etPassword = findViewById(R.id.etPasswordSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInClick(v);
            }
        });
    }

    public void onSignInClick(View view) {
        String fullName = etFullName.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        if (fullName.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform login logic here (e.g., authenticate with a server or check credentials)

        // Navigate to the task dashboard if login is successful
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class); // Assuming TaskActivity is your dashboard activity
        startActivity(intent);
        finish();
    }

    public void onRegisterClick(View view) {
        // Navigate to the Register Activity
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
