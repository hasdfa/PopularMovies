package com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils;

import android.net.Uri;
import android.os.AsyncTask;

import com.vadim.hasdfa.udacity.popularmovies.Model.Movie;
import com.vadim.hasdfa.udacity.popularmovies.Model.MovieDetail;
import com.vadim.hasdfa.udacity.popularmovies.Model.Review;
import com.vadim.hasdfa.udacity.popularmovies.Model.Videos;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import static com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils.TheMoviewDBAPI.base;
import static com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils.TheMoviewDBAPI.key;


public class LoadDetailNetwork extends AsyncTask<Movie, Void, MovieDetail> {
    private LoadDetailNetwork() {}

    public static LoadDetailNetwork shared() {
        return new LoadDetailNetwork();
    }

    public static void load(Movie movie){
        LoadDetailNetwork.shared().execute(movie);
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
            URL url2 = new URL(Uri.parse(base + TheMoviewDBAPI.videos(movieDetail.getMovie().getId())).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .toString());


            HttpURLConnection[] connections = new HttpURLConnection[]{
                    (HttpsURLConnection) url1.openConnection(),
                    (HttpsURLConnection) url2.openConnection()
            };
            for (HttpURLConnection connection: connections) {
                try {
                    InputStream in = connection.getInputStream();

                    Scanner scanner = new Scanner(in);
                    scanner.useDelimiter("\\A");

                    boolean hasInput = scanner.hasNext();
                    if (hasInput) {
                        if (resultVideosJson.equals("")) {
                            resultVideosJson = scanner.next();
                        } else if (resultReviewJson.equals("")) {
                            resultReviewJson = scanner.next();
                        }
                    }
                } finally {
                    connection.disconnect();
                }
            }


            if (resultVideosJson != null) {
                JSONArray array = new JSONObject(resultVideosJson).getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Videos video = new Videos();
                    video.setId(obj.getString("id"));
                    video.setIso_639_1(obj.getString("iso_639_1"));
                    video.setIso_3166_1(obj.getString("iso_3166_1"));
                    video.setKey(obj.getString("key"));
                    video.setName(obj.getString("name"));
                    video.setSite(obj.getString("site"));
                    video.setSize(obj.getString("site"));
                    video.setType(obj.getString("type"));

                    movieDetail.getVideos().add(video);
                }
            }
            if (resultReviewJson != null) {
                JSONArray array = new JSONObject(resultReviewJson).getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Review review = new Review();
                    review.setId(obj.getString("id"));
                    review.setAuthor(obj.getString("author"));
                    review.setContent(obj.getString("content"));
                    review.setUrl(obj.getString("url"));

                    movieDetail.getReviews().add(review);
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

    }
}