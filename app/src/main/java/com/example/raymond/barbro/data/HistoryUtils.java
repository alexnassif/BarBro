package com.example.raymond.barbro.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by raymond on 3/28/17.
 */

public class HistoryUtils {

    synchronized public static void addToHistory(@NonNull final Context context, final Integer drink){

        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground( Void... voids ) {
                String stringId = Integer.toString(drink);
                Uri uri = BarBroContract.HistoryEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                /* Here, we perform the query to check to see if we have any weather data */
                Cursor cursor = context.getContentResolver().query(
                        uri,
                        null,
                        null,
                        null,
                        null);

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int drinkId = cursor.getColumnIndex(BarBroContract.HistoryEntry._ID);
                    int historyId = cursor.getColumnIndex(BarBroContract.HistoryEntry.COLUMN_HISTORYID);

                    String hid = cursor.getString(historyId);
                    String d_id = cursor.getString(drinkId);
                    String date = getDateTime();
                    Log.d("date tag", date);
                    ContentValues values = new ContentValues();
                    values.put(BarBroContract.HistoryEntry.COLUMN_DATE, date);
                    Uri newuri = BarBroContract.HistoryEntry.CONTENT_URI;
                    newuri = newuri.buildUpon().appendPath(d_id).build();
                    context.getContentResolver().update(newuri, values, null, null);


                }
                else if(cursor.getCount() == 0){

                    Uri newuri = BarBroContract.HistoryEntry.CONTENT_URI;
                    Log.d("insert history tag", newuri.toString());
                    ContentValues values = new ContentValues();
                    values.put(BarBroContract.HistoryEntry.COLUMN_HISTORYID, drink);
                    values.put(BarBroContract.HistoryEntry.COLUMN_DATE, getDateTime());
                    context.getContentResolver().insert(newuri, values);
                }


                /* Make sure to close the Cursor to avoid memory leaks! */
                cursor.close();
                return null;
            }
        }.execute();
    }

    private static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
