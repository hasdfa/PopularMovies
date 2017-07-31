package com.vadim.hasdfa.udacity.popularmovies.Model;

/**
 * Created by Raksha Vadim on 30.07.17, 14:23.
 */

// Shared user data
public class UserData {
    public static SortType sortType = SortType.popular;
    public static Movie movie;
    public enum SortType {
        topRated,
        popular,
        favorite
    }
}
