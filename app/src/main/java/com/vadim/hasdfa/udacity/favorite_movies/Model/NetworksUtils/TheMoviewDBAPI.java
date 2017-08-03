package com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils;

/**
 * Created by Raksha Vadim on 29.07.17, 21:12.
 */

public class TheMoviewDBAPI {
    public static String base = "https://api.themoviedb.org/3";
    public static String baseImage = "http://image.tmdb.org/t/p/w154";
    public static String baseBack = "https://image.tmdb.org/t/p/w1400_and_h450_bestv2";

    public static String key = "85c2e118721d84b2de72e43e7fdb18ba";

    public static String topRated = "/movie/top_rated";
    public static String popular = "/movie/popular";

    public static String movie(int movie_id){
        return "/movie/"+movie_id;
    }
    public static String videos(int movie_id) { return "/movie/"+movie_id+"/videos"; }
    public static String reviews(int movie_id) { return "/movie/"+movie_id+"/reviews"; }
}