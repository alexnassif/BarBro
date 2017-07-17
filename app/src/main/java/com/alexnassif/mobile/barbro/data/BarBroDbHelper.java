package com.alexnassif.mobile.barbro.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.crash.FirebaseCrash;

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
    private final Context c;
    private SQLiteDatabase myDataBase;
    String db_out_path;
    String DB_PATH = null;

    public BarBroDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c = context;
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            SQLiteDatabase db = this.getReadableDatabase();
            if (db.isOpen()) {
                db.close();
            }

            try {

                copyDataBase();

            } catch (IOException e) {
                FirebaseCrash.report(new Exception(e));
                throw new Error("Error copying database");

            }
        }

    }

    //    *//**//**//**//**//**//**//**//**
//     * Check if the database already exist to avoid re-copying the file each time you open the application.
//     *
//     * @return true if it exists, false if it doesn't
//     *//**//**//**//**//**//**//**//*
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = c.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /* @Override
     public void onCreate(SQLiteDatabase db) {
         final String SQL_CREATE_BARBRO_TABLE = "CREATE TABLE " +
                 BarBroContract.BarBroEntry.TABLE_NAME + " (" +
                 BarBroContract.BarBroEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 BarBroContract.BarBroEntry.COLUMN_DRINK_NAME + " TEXT NOT NULL, " +
                 BarBroContract.BarBroEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL, " +
                 BarBroContract.BarBroEntry.COLUMN_DRINK_PIC + " TEXT, " +
                 BarBroContract.BarBroEntry.COLUMN_DESCRIPTION + " TEXT, " +
                 BarBroContract.BarBroEntry.COLUMN_BERRY + " INTEGER DEFAULT 0, " +
                 BarBroContract.BarBroEntry.COLUMN_FRESH + " INTEGER DEFAULT 0, " +
                 BarBroContract.BarBroEntry.COLUMN_FRUITY + " INTEGER DEFAULT 0, " +
                 BarBroContract.BarBroEntry.COLUMN_HERB + " INTEGER DEFAULT 0, " +
                 BarBroContract.BarBroEntry.COLUMN_SOUR + " INTEGER DEFAULT 0, " +
                 BarBroContract.BarBroEntry.COLUMN_SPICY + " INTEGER DEFAULT 0, " +
                 BarBroContract.BarBroEntry.COLUMN_SWEET + " INTEGER DEFAULT 0, " +
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
         final String SQL_CREATE_BARBRO_HISTORY_TABLE = "CREATE TABLE " +
                 BarBroContract.HistoryEntry.TABLE_NAME + " (" +
                 BarBroContract.HistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 BarBroContract.HistoryEntry.COLUMN_HISTORYID + " TEXT NOT NULL, " +
                 BarBroContract.HistoryEntry.COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                 "FOREIGN KEY(" + BarBroContract.HistoryEntry.COLUMN_HISTORYID +
                 ") REFERENCES " + BarBroContract.BarBroEntry.TABLE_NAME +
                 "(" + BarBroContract.BarBroEntry._ID + ") " +
                 ");";


         db.execSQL(SQL_CREATE_BARBRO_TABLE);
         db.execSQL(SQL_CREATE_BARBRO_MYDRINK_TABLE);
         db.execSQL(SQL_CREATE_BARBRO_HISTORY_TABLE);

     }*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BarBroContract.BarBroEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BarBroContract.MyDrinkEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BarBroContract.HistoryEntry.TABLE_NAME);
        onCreate(db);
    }
}
