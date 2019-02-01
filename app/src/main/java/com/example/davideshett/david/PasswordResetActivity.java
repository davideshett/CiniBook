package com.example.davideshett.david;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText inputEmail;

    private Button btnReset,back;

    private FirebaseAuth auth;
    private TextView resetText;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        inputEmail = findViewById(R.id.reset_email);
        btnReset = findViewById(R.id.verify3);
        back = findViewById(R.id.resetContinue);
        progressBar = findViewById(R.id.resetProqress);
        auth = FirebaseAuth.getInstance();
        resetText = findViewById(R.id.resetText);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordResetActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.sendPasswordResetEmail(email)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    resetText.setText("We have sent you instructions to reset your password! tap continue after reset.");
                                    resetText.setVisibility(View.VISIBLE);
                                    back.setVisibility(View.VISIBLE);

                                } else {
                                    resetText.setText("Failed to send reset email!");
                                    resetText.setVisibility(View.VISIBLE);
                                    }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });

    }
}
