package com.vadim.hasdfa.udacity.popularmovies.Model;

import java.util.ArrayList;

/**
 * Created by Raksha Vadim on 30.07.17, 15:27.
 */

public class MovieDetail {
    private Movie movie;
    private ArrayList<SecondItem> secondItems;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public ArrayList<SecondItem> getSecondItems() {
        return secondItems;
    }

    public void setSecondItems(ArrayList<SecondItem> secondItems) {
        this.secondItems = secondItems;
    }
}
