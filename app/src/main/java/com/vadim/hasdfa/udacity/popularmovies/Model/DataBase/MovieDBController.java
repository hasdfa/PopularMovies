package com.vadim.hasdfa.udacity.popularmovies.Model.DataBase;

import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.vadim.hasdfa.udacity.popularmovies.Model.Movie;

import java.util.ArrayList;

public class MovieDBController {
    public static final String TABLE_NAME = "FavoriteMovies";
    public static final String AUTHORITY = "com.vadim.hasdfa.udacity.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String MOVIES_PATH = "movies";
    public static final Uri CONTENT_URI =  BASE_CONTENT_URI
            .buildUpon()
            .appendPath(MOVIES_PATH)
            .build();

    public static final int MOVIES = 500;
    public static final int MOVIES_WITH_ID = 501;
    public static final int MOVIES_WITH_mID = 502;
    public static final UriMatcher sUriMathcer = buildUriMathcer();
    private static UriMatcher buildUriMathcer(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, MOVIES_PATH, MOVIES);
        matcher.addURI(AUTHORITY, MOVIES_PATH + "/#", MOVIES_WITH_ID);
        matcher.addURI(AUTHORITY, MOVIES_PATH + "/m", MOVIES_WITH_mID);
        return matcher;
    }

    private boolean isUpdateBegin = false;

    private DBHelper helper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private Context context;

    private MovieDBController(){}
    public static MovieDBController shared(){
        return new MovieDBController();
    }


//    public MovieDBController beginDataBaseQuery(Context context){
//        isUpdateBegin = true;
//        helper = new DBHelper(context);
//        database = helper.getWritableDatabase();
////        helper.onUpgrade(database, 7, 8);
//        return this;
//    }
//
//    public MovieDBController getAllItems(ArrayList<Movie> movies) throws Exception {
//        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
//        cursor = database.rawQuery("SELECT * FROM FavoriteMovies;", null);
//        getFromDB(movies, cursor);
//        return this;
//    }
//
//    public MovieDBController getAllFavoriteItems(ArrayList<Movie> movies) throws Exception {
//        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
//        cursor = database.rawQuery("SELECT * FROM FavoriteMovies WHERE isFavorite=1;", null);
//        getFromDB(movies, cursor);
//        return this;
//    }
//
//    public MovieDBController getItemById(int movie_id, ArrayList<Movie> movies) throws Exception {
//        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
//        cursor = database.rawQuery("SELECT * FROM FavoriteMovies WHERE movie_id = "+movie_id+";", null);
//        getFromDB(movies, cursor);
//        return this;
//    }

//    public Cursor queryDatabase(String[] projection, String selection, String[] selectionArgs, String sortOrder){
//        return database
//                .query(TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder);
//    }

    public void getFromDB(ArrayList<Movie> movies, Cursor cursor){
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

//    public MovieDBController putItem(Movie m) throws Exception {
//        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
//        int favorite = m.isFavorite() ? 1 : 0;
//        database
//                .execSQL("INSERT OR REPLACE INTO FavoriteMovies (movie_id, title, isFavorite, poster_url, overview, date, blur_poster_url, rate)\n" +
//                        "VALUES (\""+m.getId()+"\",\""+m.getTitle()+"\",\""+favorite+"\",\""+m.getPosterPath()+"\",\""+m.getOverview()+"\",\""+m.getReleaseDate()+"\",\""+m.getBackdropPath()+"\",\""+m.getVoteAverage()+"\");");
//        return this;
//    }
//
//    public MovieDBController updateItem(int movie_id, boolean isFavorite) throws Exception {
//        if (!isUpdateBegin) throw new Exception("DataBase is not begin updated");
//        int favorite = isFavorite ? 1 : 0;
//        database
//                .execSQL("UPDATE FavoriteMovies \n" +
//                        "SET isFavorite=\"" + favorite + "\"\n" +
//                        "WHERE movie_id=\""+movie_id+"\";");
//        return this;
//    }
//
//    public MovieDBController endDataBaseQuery() {
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        cursor = null;
//
//        if (helper != null){
//            helper.close();
//        }
//        helper = null;
//
//        if (database != null && database.isOpen()) {
//            database.close();
//        }
//        database = null;
//        isUpdateBegin = false;
//        return this;
//    }
//
//
//    public Context getContext() {
//        return context;
//    }
//
//    public void setContext(Context context) {
//        this.context = context;
//    }
}
