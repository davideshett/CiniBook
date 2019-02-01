package com.example.davideshett.david.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.davideshett.david.DetailedActivity;
import com.example.davideshett.david.R;
import com.example.davideshett.david.model.Genre;
import com.example.davideshett.david.model.Movie;
import com.example.davideshett.david.rest.OnMoviesClickCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;
    private List<Genre> allGenres;
    private String custonLink = "https://image.tmdb.org/t/p/w500/";
    private OnMoviesClickCallback callback;

    public MoviesAdapter(List<Movie> movies, List<Genre> allGenres, OnMoviesClickCallback callback, Context context) {
        this.callback = callback;
        this.movies = movies;
        this.context = context;
        this.allGenres = allGenres;
    }


    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {


        //  Picasso.with(context).load(custonLink+movies.get(position).getPosterPath()).into(holder.moviePoster);
        holder.bind(movies.get(position));

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout moviesLayout;
        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;
        TextView showdays;
        ImageView moviePoster;
        Movie movie;


        public MovieViewHolder(View itemView) {
            super(itemView);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);
            moviePoster = itemView.findViewById(R.id.item_movie_poster);
            showdays = itemView.findViewById(R.id.showingDays);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });


        }

        public void bind(Movie movie) {
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            genres.setText(getGenres(movie.getGenreIds()));
            if (movies.get(getAdapterPosition()).getRating()>=6 && getGenres(movie.getGenreIds()).contains("Action")){
                showdays.setText(R.string.movie_days2);
            }else
                showdays.setText(R.string.movie_days);

            if (movies.get(getAdapterPosition()).getRating()<6 && getGenres(movie.getGenreIds()).contains("Science Fiction"))
                showdays.setText(R.string.Science_fiction);
            this.movie = movie;
            Picasso.with(context).load(custonLink + movie.getPosterPath()).fit().into(moviePoster);

        }

        private String getGenres(List<Integer> genreIds) {

            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (Genre genre : allGenres) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }







        /*private void getnames() {
            for (Genre genre : allGenres) {
                if (genre.getName().contains("Action")) {
                    showdays.setText(R.string.movie_days2);
                } else
                    showdays.setText(R.string.movie_days);
            }


        }*/


    }
}