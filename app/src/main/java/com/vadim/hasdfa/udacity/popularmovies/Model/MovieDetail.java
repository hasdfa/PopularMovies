package com.vadim.hasdfa.udacity.popularmovies.Model;

import java.util.ArrayList;

/**
 * Created by Raksha Vadim on 30.07.17, 15:27.
 */

public class MovieDetail {
    private Movie movie;
    private ArrayList<Review> reviews;
    private ArrayList<Videos> videos;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<Videos> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Videos> videos) {
        this.videos = videos;
    }
}
