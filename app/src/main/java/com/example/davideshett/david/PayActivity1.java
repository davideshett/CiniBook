package com.example.davideshett.david;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import co.paystack.android.model.Card;

public class PayActivity1 extends AppCompatActivity {

    private EditText ticketNumber;
    private EditText email;
    private EditText fName,lName,pNumber;
    private TextView amount;
    private Button paynow;
    String number,title,poster,runtime;
    DecimalFormat formatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay1);
        ticketNumber = findViewById(R.id.number_tickets);
        amount = findViewById(R.id.Amount);
        paynow = findViewById(R.id.payNow);
        email = findViewById(R.id.card_email);
        fName = findViewById(R.id.firstNamePay);
        lName = findViewById(R.id.lastName);
        pNumber = findViewById(R.id.phone_number);

        title = getIntent().getStringExtra("title");
        poster = getIntent().getStringExtra("poster");
        runtime = getIntent().getStringExtra("runtime");

        ticketNumber.addTextChangedListener(passwordWatcher);
        amount.setVisibility(View.GONE);

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
               nextScreen();
            }






        });

    }

    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            amount.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {

            if (s.length() == 0) {
                amount.setVisibility(View.GONE);
            } else{
                number = ticketNumber.getText().toString();

                int ticketNumber = Integer.parseInt(number);
                formatter = new DecimalFormat("#,###");
                amount.setText("₦" + formatter.format(ticketNumber*1000));
            }


            }

    };


    public  void nextScreen(){
        Intent myintent = new Intent(PayActivity1.this, BookingActivity.class);
        myintent.putExtra("User_Email", email.getText().toString());
        myintent.putExtra("User_Fname", fName.getText().toString());
        myintent.putExtra("User_Lname", lName.getText().toString());
        myintent.putExtra("User_Pnumber", pNumber.getText().toString());
        myintent.putExtra("User_Pay",number);

        myintent.putExtra("title",title );
        myintent.putExtra("poster",poster );
        myintent.putExtra("runtime",runtime );

        startActivity(myintent);
    }

    private boolean validateForm() {
        boolean valid = true;


        String firstName = fName.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            fName.setError("First name Required.");
            valid = false;
        } else {
            fName.setError(null);
        }


        String lastName = lName.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            lName.setError("Last Name Required.");
            valid = false;
        } else {
            lName.setError(null);
        }

        String emailAddress = email.getText().toString();
        if (TextUtils.isEmpty(emailAddress)) {
            email.setError("email Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String pNumberr = pNumber.getText().toString();
        if (TextUtils.isEmpty(pNumberr)) {
            pNumber.setError("Cell number Required.");
            valid = false;
        } else {
            pNumber.setError(null);
        }

        String amount =  ticketNumber.getText().toString();
        if (TextUtils.isEmpty(amount)) {
            ticketNumber.setError("Number of tickets required");
        } else {

            ticketNumber.setError(null);
        }

        return valid;
    }



}
