package com.example.davideshett.david;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.davideshett.david.adapter.MoviesAdapter;
import com.example.davideshett.david.model.Genre;
import com.example.davideshett.david.model.Movie;
import com.example.davideshett.david.rest.MoviesRepository;
import com.example.davideshett.david.rest.OnGetGenresCallback;
import com.example.davideshett.david.rest.OnGetMoviesCallback;
import com.example.davideshett.david.rest.OnMoviesClickCallback;
import java.util.List;



public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();




    private String API_KEY = "78672b9eec5df84f1a4f9ae81fa31d59";
    private static final String LANGUAGE = "en-US";


    ShimmerRecyclerView moviesList;
    private MoviesAdapter adapter;
    private MoviesRepository moviesRepository;


    // List<Movie> movies;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, null);

        moviesRepository = MoviesRepository.getInstance();

        moviesList = view.findViewById(R.id.recycler);
        moviesList.setLayoutManager(new LinearLayoutManager(moviesList.getContext()));
        getGenres();





        return view;


    }
    private void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                getMovies(genres);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getMovies(final List<Genre> genres) {
        moviesRepository.getMovies(new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                adapter = new MoviesAdapter(movies, genres,callback, getContext());
                moviesList.setAdapter(adapter);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }






    private void showError() {
        Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {

            Intent intent = new Intent(getActivity(),DetailedActivity.class);
            intent.putExtra(DetailedActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };

}