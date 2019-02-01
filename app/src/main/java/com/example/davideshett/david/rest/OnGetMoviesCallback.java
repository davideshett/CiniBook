package com.example.davideshett.david.rest;

import com.example.davideshett.david.model.Movie;

import java.util.List;

public interface OnGetMoviesCallback {
    void onSuccess(List<Movie> movies);

    void onError();
}
