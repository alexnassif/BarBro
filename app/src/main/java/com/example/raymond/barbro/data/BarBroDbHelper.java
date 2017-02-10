package com.example.raymond.barbro.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by alex on 12/29/16.
 */

public class BarBroDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "barbro.db";
    private static final int DATABASE_VERSION = 1;
    private Context c;
    String db_out_path;
    public BarBroDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c = context;
        db_out_path = context.getDatabasePath(DATABASE_NAME).toString();
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
        final String SQL_CREATE_BARBRO_MYDRINK_TABLE = "CREATE TABLE " +
                BarBroContract.MyDrinkEntry.TABLE_NAME + " (" +
                BarBroContract.MyDrinkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_NAME + " TEXT NOT NULL, " +
                BarBroContract.MyDrinkEntry.COLUMN_MYINGREDIENTS + " TEXT NOT NULL, " +
                BarBroContract.MyDrinkEntry.COLUMN_MYDRINK_PIC + " TEXT " +
                ");";


        db.execSQL(SQL_CREATE_BARBRO_TABLE);
        db.execSQL(SQL_CREATE_BARBRO_MYDRINK_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BarBroContract.BarBroEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BarBroContract.MyDrinkEntry.TABLE_NAME);
        onCreate(db);
    }
}
