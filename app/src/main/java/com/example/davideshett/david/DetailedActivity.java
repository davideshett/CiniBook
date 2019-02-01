package com.example.davideshett.david;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.davideshett.david.model.Genre;
import com.example.davideshett.david.model.Movie;
import com.example.davideshett.david.model.Review;
import com.example.davideshett.david.model.Trailer;
import com.example.davideshett.david.rest.MoviesRepository;
import com.example.davideshett.david.rest.OnGetGenresCallback;
import com.example.davideshett.david.rest.OnGetMovieCallback;
import com.example.davideshett.david.rest.OnGetReviewsCallback;
import com.example.davideshett.david.rest.OnGetTrailersCallback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import co.paystack.android.PaystackSdk;
import co.paystack.android.model.Charge;

public class DetailedActivity extends AppCompatActivity {

    public static String MOVIE_ID = "movie_id";
    private TextView reviewsLabel;


    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    private ImageView movieBackdrop;
    private TextView movieTitle;
    private TextView movieGenres;
    private TextView movieOverview;
    private TextView movieOverviewLabel;
    private TextView movieReleaseDate;
    private RatingBar movieRating;
    private LinearLayout movieTrailers;
    private TextView runtime;
    private LinearLayout movieReviews;
    private TextView trailersLabel;
    private Button saveSeat;
    private Intent saveIntent;

    private MoviesRepository moviesRepository;
    private int movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        movieId = getIntent().getIntExtra(MOVIE_ID, movieId);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        moviesRepository = MoviesRepository.getInstance();
        saveIntent = new Intent(DetailedActivity.this, PayActivity1.class);

        setupToolbar();
        changeStatusBarColor();
        initUI();

        getMovie();

        saveSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(saveIntent);
                finish();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initUI() {
        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        movieTitle = findViewById(R.id.movieDetailsTitle);
        movieGenres = findViewById(R.id.movieDetailsGenres);
        movieOverview = findViewById(R.id.movieDetailsOverview);
        movieOverviewLabel = findViewById(R.id.summaryLabel);
        movieReleaseDate = findViewById(R.id.movieDetailsReleaseDate);
        movieRating = findViewById(R.id.movieDetailsRating);
        movieTrailers = findViewById(R.id.movieTrailers);
        movieReviews = findViewById(R.id.movieReviews);
        trailersLabel = findViewById(R.id.trailersLabel);
        runtime = findViewById(R.id.runtime);
        reviewsLabel = findViewById(R.id.reviewsLabel);
        saveSeat = findViewById(R.id.saveSeat);
    }

    private void getMovie() {
        moviesRepository.getMovie(movieId, new OnGetMovieCallback() {


            @Override
            public void onSuccess(com.example.davideshett.david.model.Movie movie) {

                String title = movie.getTitle().toString();
                String poster = movie.getPosterPath().toString();
                String runT = String.valueOf(movie.getRuntime()+ " Minutes");

                saveIntent.putExtra("title",title);
                saveIntent.putExtra("poster",poster);
                saveIntent.putExtra("runtime",runT);
                movieTitle.setText(title);


                movieOverviewLabel.setVisibility(View.VISIBLE);
                movieOverview.setText(movie.getOverview());
                movieRating.setVisibility(View.VISIBLE);
                movieRating.setRating(movie.getRating());
                runtime.setText(runT);
                getGenres(movie);
                movieReleaseDate.setText(movie.getReleaseDate());
                if (!isFinishing()) {
                    Picasso.with(DetailedActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .placeholder(R.color.colorPrimary)
                            .into(movieBackdrop);
                }
                getTrailers(movie);
                getReviews(movie);
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }



    private void getGenres(final Movie movie) {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });



    }

    private void getReviews(Movie movie) {
        moviesRepository.getReviews(movie.getId(), new OnGetReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                reviewsLabel.setVisibility(View.VISIBLE);
                movieReviews.removeAllViews();
                for (Review review : reviews) {
                    View parent = getLayoutInflater().inflate(R.layout.review, movieReviews, false);
                    TextView author = parent.findViewById(R.id.reviewAuthor);
                    TextView content = parent.findViewById(R.id.reviewContent);
                    author.setText(review.getAuthor());
                    content.setText(review.getContent());
                    movieReviews.addView(parent);
                }
            }

            @Override
            public void onError() {
                // Do nothing
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getTrailers(Movie movie) {
        moviesRepository.getTrailers(movie.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                trailersLabel.setVisibility(View.VISIBLE);
                movieTrailers.removeAllViews();
                for (final Trailer trailer : trailers) {
                    View parent = getLayoutInflater().inflate(R.layout.thumbnail_trailer, movieTrailers, false);
                    ImageView thumbnail = parent.findViewById(R.id.thumbnail);
                    thumbnail.requestLayout();
                    thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.getKey()));
                        }
                    });
                    Glide.with(DetailedActivity.this)
                            .load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                            .into(thumbnail);
                    movieTrailers.addView(parent);
                }
            }


            @Override
            public void onError() {
                // Do nothing
                trailersLabel.setVisibility(View.GONE);
            }


        });

    }




        private void showTrailer(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }




        private void changeStatusBarColor() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
    }


    private void showError() {
        Toast.makeText(DetailedActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
}

