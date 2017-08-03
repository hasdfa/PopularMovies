package com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils;

import com.vadim.hasdfa.udacity.favorite_movies.Model.Movie;

import java.util.ArrayList;

/**
 * Created by Raksha Vadim on 03.08.17, 18:30.
 */

public interface OnLoadCompleteListener {
    void onLoad(ArrayList<Movie> movies);
}
