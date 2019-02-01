package com.example.davideshett.david.rest;

import com.example.davideshett.david.model.Review;

import java.util.List;

public interface OnGetReviewsCallback {
    void onSuccess(List<Review> reviews);

    void onError();
}