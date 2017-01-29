package com.example.raymond.barbro.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alex on 12/29/16.
 */

public class BarBroDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "barbro.db";
    private static final int DATABASE_VERSION = 1;

    public BarBroDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BARBRO_TABLE = "CREATE TABLE " +
                BarBroContract.BarBroEntry.TABLE_NAME + " (" +
                BarBroContract.BarBroEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BarBroContract.BarBroEntry.COLUMN_DRINK_NAME + " TEXT NOT NULL, " +
                BarBroContract.BarBroEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL, " +
                BarBroContract.BarBroEntry.COLUMN_DRINK_PIC + " TEXT, " +
                BarBroContract.BarBroEntry.COLUMN_FAVORITE + " INTEGER DEFAULT 0, " +
                BarBroContract.BarBroEntry.COLUMN_VODKA + " INTEGER DEFAULT 0, " +
                BarBroContract.BarBroEntry.COLUMN_GIN + " INTEGER DEFAULT 0, " +
                BarBroContract.BarBroEntry.COLUMN_RUM + " INTEGER DEFAULT 0, " +
                BarBroContract.BarBroEntry.COLUMN_TEQUILA + " INTEGER DEFAULT 0, " +
                BarBroContract.BarBroEntry.COLUMN_WHISKY + " INTEGER DEFAULT 0, " +
                BarBroContract.BarBroEntry.COLUMN_BRANDY + " INTEGER DEFAULT 0, " +
                BarBroContract.BarBroEntry.COLUMN_VIDEO + " TEXT " +
                ");";
        final String SQL_CREATE_BARBRO_FAVE_TABLE = "CREATE TABLE " +
                BarBroContract.FavoritesEntry.TABLE_NAME + " (" +
                BarBroContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BarBroContract.FavoritesEntry.COLUMN_DRINK_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY(" +
                BarBroContract.FavoritesEntry.COLUMN_DRINK_ID + ") REFERENCES drinks(_ID)" +
                ");";


        db.execSQL(SQL_CREATE_BARBRO_TABLE);
        db.execSQL(SQL_CREATE_BARBRO_FAVE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BarBroContract.BarBroEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BarBroContract.FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
