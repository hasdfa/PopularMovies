package com.vadim.hasdfa.udacity.popularmovies.Model.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Raksha Vadim on 30.07.17, 22:14.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Movies.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS \"FavoriteMovies\" (\n" +
                "  \"_id\" integer PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"movie_id\" integer NOT NULL,\n" +
                "  \"title\" text NOT NULL,\n" +
                "  \"poster_url\" text,\n" +
                "  \"overview\" text,\n" +
                "  \"date\" text\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}
