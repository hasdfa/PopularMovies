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

    private MovieDBController(){}
    public static MovieDBController shared(){
        return new MovieDBController();
    }

    public MovieDBController beginDataBaseQuery(Context context){
        isUpdateBegin = true;
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
//        helper.onUpgrade(database, 7, 8);
        return this;
    }

    public MovieDBController getAllItems(ArrayList<Movie> movies) throws Exception {
        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
        cursor = database.rawQuery("SELECT * FROM FavoriteMovies;", null);
        getFromDB(movies);
        return this;
    }

    public MovieDBController getAllFavoriteItems(ArrayList<Movie> movies) throws Exception {
        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
        cursor = database.rawQuery("SELECT * FROM FavoriteMovies WHERE isFavorite=1;", null);
        getFromDB(movies);
        return this;
    }

    public MovieDBController getItemById(int movie_id, ArrayList<Movie> movies) throws Exception {
        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
        cursor = database.rawQuery("SELECT * FROM FavoriteMovies WHERE movie_id = "+movie_id+";", null);
        getFromDB(movies);
        return this;
    }

    private void getFromDB(ArrayList<Movie> movies){
        if (cursor.moveToFirst()) {
            do {
                Movie m = new Movie();
                m.setId(cursor.getInt(cursor.getColumnIndex("movie_id")));
                m.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                m.setPosterPath(cursor.getString(cursor.getColumnIndex("poster_url")));
                m.setOverview(cursor.getString(cursor.getColumnIndex("overview")));
                m.setReleaseDate(cursor.getString(cursor.getColumnIndex("date")));
                m.setBackdropPath(cursor.getString(cursor.getColumnIndex("blur_poster_url")));
                m.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex("rate"))));
                m.setFavorite(
                        cursor.getInt(cursor.getColumnIndex("isFavorite")) != 0
                );

                movies.add(m);
            } while (cursor.moveToNext());
        }
    }

    public MovieDBController putItem(Movie m) throws Exception {
        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
        int favorite = m.isFavorite() ? 1 : 0;
        database
                .execSQL("INSERT OR REPLACE INTO FavoriteMovies (movie_id, title, isFavorite, poster_url, overview, date, blur_poster_url, rate)\n" +
                        "VALUES (\""+m.getId()+"\",\""+m.getTitle()+"\",\""+favorite+"\",\""+m.getPosterPath()+"\",\""+m.getOverview()+"\",\""+m.getReleaseDate()+"\",\""+m.getBackdropPath()+"\",\""+m.getVoteAverage()+"\");");
        return this;
    }

    public MovieDBController updateItem(int movie_id, boolean isFavorite) throws Exception {
        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
        int favorite = isFavorite ? 1 : 0;
        database
                .execSQL("UPDATE FavoriteMovies \n" +
                        "SET isFavorite=\"" + favorite + "\"\n" +
                        "WHERE movie_id=\""+movie_id+"\";");
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
