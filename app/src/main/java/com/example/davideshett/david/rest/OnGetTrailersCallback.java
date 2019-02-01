package com.example.davideshett.david.rest;

import com.example.davideshett.david.model.Trailer;

import java.util.List;

public interface OnGetTrailersCallback {
    void onSuccess(List<Trailer> trailers);

    void onError();
}