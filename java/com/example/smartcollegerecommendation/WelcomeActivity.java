package com.example.smartcollegerecommendation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button btnStart = findViewById(R.id.btnStart);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            auth.signOut(); // safe (won’t affect later)
        }

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class))
        );

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class))
        );

        btnStart.setOnClickListener(v -> {
            if (auth.getCurrentUser() != null) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            } else {
                Toast.makeText(WelcomeActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
            }
        });
    }
}