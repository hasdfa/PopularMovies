package com.vadim.hasdfa.udacity.popularmovies.Model.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vadim.hasdfa.udacity.popularmovies.Model.Movie;

import java.util.ArrayList;

public class MovieDBController {
    private boolean isUpdateBegin = false;

    private DBHelper helper;
    private SQLiteDatabase database;
    private Cursor cursor;

    public MovieDBController beginDataBaseQuery(Context context){
        isUpdateBegin = true;
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public MovieDBController getAllItems(ArrayList<Movie> movieDBItems) throws Exception {
        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
        movieDBItems = new ArrayList<>();
        cursor = database.query("SELECT * FROM FavoriteMovies;", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Movie m = new Movie();
                m.setId(cursor.getInt(cursor.getColumnIndex("movie_id")));
                m.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                m.setPosterPath(cursor.getString(cursor.getColumnIndex("poster_url")));
                m.setOverview(cursor.getString(cursor.getColumnIndex("overview")));
                m.setReleaseDate(cursor.getString(cursor.getColumnIndex("date")));

                movieDBItems.add(m);
            } while (cursor.moveToNext());
        }
        return this;
    }

    public MovieDBController putItem(int movieId, String title, String posterUrl, String overview, String date) throws Exception {
        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
        database
                .execSQL("INSERT OR REPLACE INTO FavoriteMovies (movie_id, title, poster_url, overview, date)\n" +
                        "VALUES (\""+movieId+"\",\""+title+"\",\""+posterUrl+"\",\""+overview+"\",\""+date+"\");");
        return this;
    }

    public MovieDBController endDataBaseQuery() {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = null;

        if (helper != null){
            helper.close();
        }
        helper = null;

        if (database != null && database.isOpen()) {
            database.close();
        }
        database = null;
        isUpdateBegin = false;
        return this;
    }
}
