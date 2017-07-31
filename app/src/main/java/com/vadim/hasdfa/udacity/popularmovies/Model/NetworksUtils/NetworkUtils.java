package com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils;

import android.net.Uri;
import android.os.AsyncTask;

import com.vadim.hasdfa.udacity.popularmovies.Controllers.MainMoview.MoviesAdapter;
import com.vadim.hasdfa.udacity.popularmovies.Model.Movie;
import com.vadim.hasdfa.udacity.popularmovies.Model.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import static com.vadim.hasdfa.udacity.popularmovies.Model.NetworksUtils.TheMoviewDBAPI.*;

/**
 * Created by Raksha Vadim on 29.07.17, 21:53.
 */

public class NetworkUtils extends AsyncTask<URL, Void, ArrayList<Movie>>{
    MoviesAdapter mAdapter;

    public NetworkUtils(MoviesAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }


    public static boolean isLoading = false;

    public static int page = 1;
    public void incrementPage(){
        page++;
        loadMore();
    }
    public void loadMore() {
        String url = "https://google.com";
        if (UserData.sortType == UserData.SortType.popular) {
            url = Uri.parse(base + popular).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .appendQueryParameter("page", page + "")
                    .toString();
        } else if (UserData.sortType == UserData.SortType.topRated) {
            url = Uri.parse(base + topRated).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .appendQueryParameter("page", page + "")
                    .toString();
        }
        if (url.equals("https://google.com")) return;
        try {
            execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        isLoading = true;
    }

    @Override
    protected ArrayList<Movie> doInBackground(URL... urls) {
        ArrayList<Movie> result = new ArrayList<>();

        String resultJson = null;
        try {
            HttpURLConnection connection = (HttpsURLConnection) urls[0].openConnection();
            try {
                InputStream in = connection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    resultJson = scanner.next();
                }
            } finally {
                connection.disconnect();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (resultJson != null) {
            try {
                JSONObject main = new JSONObject(resultJson);
                JSONArray results = main.getJSONArray("results");
                for (int i = 0; i < results.length(); i++){
                    JSONObject obj = results.getJSONObject(i);
                    Movie m = new Movie();
                    m.setPosterPath(obj.getString("poster_path"));
                    m.setAdult(obj.getBoolean("adult"));
                    m.setOverview(obj.getString("overview"));
                    m.setReleaseDate(obj.getString("release_date"));
                    m.setId(obj.getInt("id"));
                    m.setOriginalTitle(obj.getString("original_title"));
                    m.setOriginalLanguage(obj.getString("original_language"));
                    m.setTitle(obj.getString("title"));
                    m.setBackdropPath(obj.getString("backdrop_path"));
                    m.setPopularity(obj.getInt("popularity"));
                    m.setVoteAverage(obj.getDouble("vote_count"));
                    m.setVideo(obj.getString("video"));
                    m.setVoteAverage(obj.getDouble("vote_average"));
                    result.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        mAdapter.notifyDataSetChanged(movies);
        isLoading = false;
    }
}
