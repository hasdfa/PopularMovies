package com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.vadim.hasdfa.udacity.favorite_movies.Model.Movie;
import com.vadim.hasdfa.udacity.favorite_movies.Model.MovieDetail;
import com.vadim.hasdfa.udacity.favorite_movies.Model.SecondItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import static com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.TheMoviewDBAPI.base;
import static com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.TheMoviewDBAPI.key;


public class LoadDetailNetwork extends AsyncTask<Movie, Void, MovieDetail> {
    private LoadDetailNetwork() {}

    public static LoadDetailNetwork shared(){
        return new LoadDetailNetwork();
    }

    private OnDetailLoadCompleteListener mListener;
    public void load(Movie movie, OnDetailLoadCompleteListener mListener){
        this.mListener = mListener;
        execute(movie);
    }

    @Override
    protected MovieDetail doInBackground(Movie... movies) {
        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setMovie(movies[0]);

        String resultVideosJson = "";
        String resultReviewJson = "";
        try {
            URL url1 = new URL(Uri.parse(base + TheMoviewDBAPI.videos(movieDetail.getMovie().getId())).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .toString());
            URL url2 = new URL(Uri.parse(base + TheMoviewDBAPI.reviews(movieDetail.getMovie().getId())).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .toString());


            HttpURLConnection connection1 = (HttpsURLConnection) url1.openConnection();
            HttpURLConnection connection2 = (HttpsURLConnection) url2.openConnection();
            try {
                InputStream in = connection1.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    resultVideosJson = scanner.next();
                }
            } finally {
                connection1.disconnect();
            }
            try {
                InputStream in = connection2.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    resultReviewJson = scanner.next();
                }
            } finally {
                connection2.disconnect();
            }


            movieDetail.setSecondItems(new ArrayList<SecondItem>());
            if (resultVideosJson != null) {
                JSONArray array = new JSONObject(resultVideosJson).getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    SecondItem secondItem = new SecondItem();
                    secondItem.setId(obj.getString("id"));
                    secondItem.setIso_639_1(obj.getString("iso_639_1"));
                    secondItem.setIso_3166_1(obj.getString("iso_3166_1"));
                    secondItem.setKey(obj.getString("key"));
                    secondItem.setName(obj.getString("name"));
                    secondItem.setSite(obj.getString("site"));
                    secondItem.setSize(obj.getString("site"));
                    secondItem.setType(obj.getString("type"));

                    secondItem.itemType = 0;
                    movieDetail.getSecondItems().add(secondItem);
                }
            }
            movieDetail.getSecondItems().add(SecondItem.ad());

            if (resultReviewJson != null) {
                Log.d("myLog", resultReviewJson);
                JSONArray array = new JSONObject(resultReviewJson).getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    SecondItem review = new SecondItem();
                    review.setId(obj.getString("id"));
                    review.setAuthor(obj.getString("author"));
                    review.setContent(obj.getString("content"));
                    review.setUrl(obj.getString("url"));

                    review.itemType = 1;
                    movieDetail.getSecondItems().add(review);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieDetail;
    }

    @Override
    protected void onPostExecute(MovieDetail movieDetail) {
        super.onPostExecute(movieDetail);
        if (mListener != null) {
            mListener.onLoad(movieDetail);
        }
    }
}
