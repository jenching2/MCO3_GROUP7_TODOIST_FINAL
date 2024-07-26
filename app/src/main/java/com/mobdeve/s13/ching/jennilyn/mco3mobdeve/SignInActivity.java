package com.mobdeve.s13.ching.jennilyn.mco3mobdeve;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText etFullNameSignIn, etPhoneSignIn, etPasswordSignIn;
    private Button btnSignIn;
    private TextView tvRegister;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        databaseHelper = new DatabaseHelper(this);

        etFullNameSignIn = findViewById(R.id.etFullNameSignIn);
        etPhoneSignIn = findViewById(R.id.etPhoneSignIn);
        etPasswordSignIn = findViewById(R.id.etPasswordSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvRegister = findViewById(R.id.tvRegister);

        btnSignIn.setOnClickListener(v -> onSignInClick());
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void onSignInClick() {
        String fullName = etFullNameSignIn.getText().toString();
        String phone = etPhoneSignIn.getText().toString();
        String password = etPasswordSignIn.getText().toString();

        if (fullName.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkUser(fullName, phone, password)) {
            Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
            intent.putExtra("USER_NAME", fullName); // Pass the user's name to DashboardActivity
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid sign-in credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
