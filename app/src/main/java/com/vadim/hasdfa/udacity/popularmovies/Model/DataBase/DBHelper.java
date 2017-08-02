package com.vadim.hasdfa.udacity.popularmovies.Model.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Raksha Vadim on 30.07.17, 22:14.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Movies.db";
    private static int DATABASE_VERSION = 9;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS \"FavoriteMovies\" (\n" +
                "  \"_id\" integer PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"movie_id\" integer NOT NULL,\n" +
                "  \"title\" text NOT NULL,\n" +
                "  \"isFavorite\" integer NOT NULL default 0,\n"+
                "  \"poster_url\" text NOT NULL,\n" +
                "  \"blur_poster_url\" text NOT NULL,\n" +
                "  \"overview\" text NOT NULL,\n" +
                "  \"rate\" text NOT NULL,\n" +
                "  \"date\" text NOT NULL\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS FavoriteMovies");
        onCreate(db);
    }
}
