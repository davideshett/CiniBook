package com.example.davideshett.david.rest;

import com.example.davideshett.david.model.Genre;

import java.util.List;

public interface OnGetGenresCallback {

    void onSuccess(List<Genre> genres);

    void onError();

}
