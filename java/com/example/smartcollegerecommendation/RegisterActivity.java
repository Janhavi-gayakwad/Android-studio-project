package com.example.smartcollegerecommendation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password;
    Button register;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPass = password.getText().toString().trim();

            if(userEmail.isEmpty() || userPass.isEmpty()){
                Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            if(userPass.length() < 6){
                Toast.makeText(this, "Password must be 6+ chars", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(this, "Registered Successfully 🎉", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();

                        } else {
                            Toast.makeText(this,
                                    "Error: "+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}