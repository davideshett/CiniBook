package com.example.davideshett.david.rest;

import com.example.davideshett.david.model.Movie;

public interface OnGetMovieCallback {

    void onSuccess(Movie movie);

    void onError();
}
