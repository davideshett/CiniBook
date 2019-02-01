package com.example.davideshett.david;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class TicketActivity extends AppCompatActivity {

    private String runtime,title,poster;
    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";

    private ImageView imageView;
    private TextView titleTv,runtimeTv;
    private TextView genreTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        title = getIntent().getStringExtra("title");
        poster = getIntent().getStringExtra("poster");
        runtime = getIntent().getStringExtra("runtime");

        imageView = findViewById(R.id.imv);
        runtimeTv = findViewById(R.id.dur);
        titleTv = findViewById(R.id.mvName);


        runtimeTv.setText(runtime);
        titleTv.setText(title);


        Picasso.with(TicketActivity.this)
                .load(IMAGE_BASE_URL + poster)
                .placeholder(R.color.colorPrimary)
                .into(imageView);

    }
}
