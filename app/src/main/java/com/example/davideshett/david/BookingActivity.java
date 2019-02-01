package com.example.davideshett.david;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class BookingActivity extends AppCompatActivity {

    private Card card;
    private Charge charge;


    private EditText cardNumberField;
    private EditText expiryMonthField;
    private EditText expiryYearField;
    private EditText cvvField;
    private TextView emailTv, amountTv;

    private String userEmail, cardNumber, cvv,title,poster,runtime;
    private String first_name;
    private int expiryMonth, expiryYear, ticketNumber;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        PaystackSdk.initialize(getApplicationContext());


        Button payBtn = findViewById(R.id.pay_button);
        progressBar = findViewById(R.id.pay_progress);
        cardNumberField = findViewById(R.id.cardNumber);
        expiryMonthField = findViewById(R.id.expiryMonth);
        expiryYearField = findViewById(R.id.expiryYear);
        cvvField = findViewById(R.id.cvv);
        emailTv = findViewById(R.id.payEMAIL);
        amountTv = findViewById(R.id.payAMOUNT);


        userEmail = getIntent().getStringExtra("User_Email");
        title = getIntent().getStringExtra("title");
        poster = getIntent().getStringExtra("poster");
        runtime = getIntent().getStringExtra("runtime");
        emailTv.setText(userEmail);

        String payAmount = getIntent().getStringExtra("User_Pay");
         first_name = getIntent().getStringExtra("User_Fname");

        ticketNumber = Integer.parseInt(payAmount);
        DecimalFormat formatter = new DecimalFormat("#,###");
        amountTv.setText("₦" + formatter.format(ticketNumber*1000));
        payBtn.setText("Pay ₦" + formatter.format(ticketNumber*1000));




        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                try {

                    cardNumber = cardNumberField.getText().toString().trim();
                    expiryMonth = Integer.parseInt(expiryMonthField.getText().toString().trim());
                    expiryYear = Integer.parseInt(expiryYearField.getText().toString().trim());
                    cvv = cvvField.getText().toString().trim();

                    //String cardNumber = "4084084084084081";
                    //int expiryMonth = 11; //any month in the future
                    //int expiryYear = 18; // any year in the future
                    //String cvv = "408";
                    card = new Card(cardNumber, expiryMonth, expiryYear, cvv);

                    if (card.isValid()) {
                        Toast.makeText(BookingActivity.this, "Card is Valid", Toast.LENGTH_LONG).show();
                        performCharge();
                        Intent intent = new Intent(BookingActivity.this,TicketActivity.class);
                        intent.putExtra("title",title);
                        intent.putExtra("runtime",runtime);
                        intent.putExtra("poster",poster);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(BookingActivity.this, "Card not Valid", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });







    }



    private void performCharge() {
        progressBar.setVisibility(View.VISIBLE);
        //create a Charge object
        charge = new Charge();

        //set the card to charge
        charge.setCard(card);

        charge.getAdditionalParameters().put("First Name",first_name);




        charge.setEmail(userEmail); //dummy email address
        charge.setAmount(ticketNumber*100000); //test amount

        PaystackSdk.chargeCard(BookingActivity.this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                String paymentReference = transaction.getReference();
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(BookingActivity.this, "Transaction Successful! payment reference: "
                        + paymentReference, Toast.LENGTH_LONG).show();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                //handle error here
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;


        String cardNumber = cardNumberField.getText().toString();
        if (TextUtils.isEmpty(cardNumber)) {
            cardNumberField.setError("Card number required.");
            valid = false;
        } else {
            cardNumberField.setError(null);
        }


        String expiryMonth = expiryMonthField.getText().toString();
        if (TextUtils.isEmpty(expiryMonth)) {
            expiryMonthField.setError("expiry month Required.");
            valid = false;
        } else {
            expiryMonthField.setError(null);
        }

        String expiryYear = expiryYearField.getText().toString();
        if (TextUtils.isEmpty(expiryYear)) {
            expiryYearField.setError("expiry year required.");
            valid = false;
        } else {
            expiryYearField.setError(null);
        }

        String cvv = cvvField.getText().toString();
        if (TextUtils.isEmpty(cvv)) {
            cvvField.setError("cvv required.");
            valid = false;
        } else {
            cvvField.setError(null);
        }

        return valid;
    }



}
