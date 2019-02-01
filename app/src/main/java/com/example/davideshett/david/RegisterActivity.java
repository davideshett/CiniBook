package com.example.davideshett.david;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_confirm_pass_field;
    private Button   reg_btn;
    private Button   reg_login_btn;
    private ProgressBar reg_progress;
    private  FirebaseAuth.AuthStateListener mAuthStateListener;
     String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser()!= null){
                    sendToVerificationActivity();
                }

            }
        };

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();

        reg_email_field = findViewById(R.id.reg_email);
        reg_pass_field = findViewById(R.id.reg_password);
        reg_confirm_pass_field = findViewById(R.id.confirm_password);
        reg_btn = findViewById(R.id.reg_button);
        reg_login_btn = findViewById(R.id.account_already);
        reg_progress = findViewById(R.id.regProgress);


        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                 email = reg_email_field.getText().toString();
                 password = reg_pass_field.getText().toString();
                String confirm_pass =  reg_confirm_pass_field.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm_pass)){

                    if (password.equals(confirm_pass)){

                        reg_progress.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){
                                    //sendToMain();
                                    sendEmailVerification();
                                }else {

                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, errorMessage,Toast.LENGTH_LONG).show();


                                }



                                reg_progress.setVisibility(View.INVISIBLE);
                            }
                        });


                    } else {

                        Toast.makeText(RegisterActivity.this, "Passwords don't match",Toast.LENGTH_LONG).show();
                    }


                }

            }
        });




    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

   private void sendToVerificationActivity() {

        Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
        startActivity(intent);
        finish();
    }



   public void sendEmailVerification(){

       FirebaseAuth auth = FirebaseAuth.getInstance();
       FirebaseUser user = auth.getCurrentUser();

       if (user!= null){

           user.sendEmailVerification()
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                               intent.putExtra("email",email);
                               startActivity(intent);
                               finish();

                           }
                       }
                   });

       }


   }


}
