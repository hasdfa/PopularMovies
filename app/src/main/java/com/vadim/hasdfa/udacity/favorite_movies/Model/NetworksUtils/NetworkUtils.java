package com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.vadim.hasdfa.udacity.favorite_movies.Model.Movie;
import com.vadim.hasdfa.udacity.favorite_movies.Model.UserData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import static com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.TheMoviewDBAPI.base;
import static com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.TheMoviewDBAPI.key;
import static com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.TheMoviewDBAPI.popular;
import static com.vadim.hasdfa.udacity.favorite_movies.Model.NetworksUtils.TheMoviewDBAPI.topRated;

/**
 * Created by Raksha Vadim on 29.07.17, 21:53.
 */

public class NetworkUtils extends AsyncTask<URL, Void, ArrayList<Movie>>{
    private OnLoadCompleteListener mListener;

    public NetworkUtils(OnLoadCompleteListener mListener) {
        this.mListener = mListener;
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
        if (url.equals("https://google.com")) { Log.d("myLog", "GOOGLE"); return;}
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
        Log.d("myLog", "START_LOADING");
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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        Log.d("myLog", resultJson);

        if (resultJson != null || !resultJson.isEmpty()) {
            try {
                JSONObject main = new JSONObject(resultJson);
                JSONArray results = main.getJSONArray("results");
                int ad1 = results.length()/3;
                int ad2 = (results.length()/3)*2;
                for (int index = 0; index < results.length()+3; index++){
                    if (index == ad1 || index == ad2 || index == results.length()+2) {
                        result.add(Movie.ad());
                        continue;
                    }
                    int i = index;
                    if (index > ad1) {
                        i--;
                    }
                    if (index > ad2) {
                        i--;
                    }
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
        if (mListener != null) {
            mListener.onLoad(movies);
        }
        isLoading = false;
        Log.d("myLog", "END_LOADING");
    }
}
