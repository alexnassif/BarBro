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
                BarBroContract.BarBroEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_BARBRO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BarBroContract.BarBroEntry.TABLE_NAME);
        onCreate(db);
    }
}
