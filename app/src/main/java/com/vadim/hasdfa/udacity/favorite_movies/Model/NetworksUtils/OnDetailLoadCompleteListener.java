package com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils;

import com.vadim.hasdfa.udacity.favorite_movies.Model.MovieDetail;

/**
 * Created by Raksha Vadim on 03.08.17, 18:30.
 */

public interface OnDetailLoadCompleteListener {
    void onLoad(MovieDetail detail);
}
