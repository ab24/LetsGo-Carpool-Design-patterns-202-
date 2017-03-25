package com.sjsu.ratingsandreviews;


public class RatingsAndReview {
    private double rating;
    private String review;

    private RatingsAndReview(double rating, String review) {
        this.rating = rating;
        this.review = review;
    }

    public static RatingsAndReview newRatingsAndReview(double rating, String review) {
        return new RatingsAndReview(rating, review);
    }

    public double getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}
