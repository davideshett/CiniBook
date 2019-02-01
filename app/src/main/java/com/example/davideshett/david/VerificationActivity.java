package com.example.davideshett.david;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {

    private TextView verificationText;
    private TextView statusText;
    private Button verificationStatus;
    private Button continueBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressBar verificationProgress;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        verificationText = findViewById(R.id.mailText);
        statusText = findViewById(R.id.status);
        verificationStatus = findViewById(R.id.verify);
        continueBtn = findViewById(R.id.verified);
        verificationProgress = findViewById(R.id.verificationProgress);


        mAuth = FirebaseAuth.getInstance();
        continueBtn.setVisibility(View.INVISIBLE);
        verificationProgress.setVisibility(View.INVISIBLE);



        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                //FirebaseAuth.getInstance().signOut();
            }
        });

        verificationStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificationProgress.setVisibility(View.VISIBLE);

                 mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         if (mAuth.getCurrentUser().isEmailVerified()){
                             verificationProgress.setVisibility(View.INVISIBLE);
                             statusText.setText("Email Verified!!");
                             statusText.setTextColor(Color.parseColor("#0D7639"));
                             continueBtn.setVisibility(View.VISIBLE);

                         }else {
                             verificationProgress.setVisibility(View.INVISIBLE);
                             statusText.setTextColor(Color.parseColor("#BE4347"));
                             statusText.setText("Email not verified, Try again");

                         }
                     }
                 });
            }
        });


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {



            }
        };

        email = getIntent().getStringExtra("email");
        verificationText.setText("We have sent an email to " + email + ". Kindly verify account.");
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


}
