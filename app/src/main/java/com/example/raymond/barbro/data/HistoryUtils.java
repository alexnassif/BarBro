package com.example.raymond.barbro.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
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

                if (null != cursor && cursor.getCount() == 1) {
                    int drinkId = cursor.getColumnIndex(BarBroContract.HistoryEntry._ID);
                    int historyId = cursor.getColumnIndex(BarBroContract.HistoryEntry.COLUMN_HISTORYID);
                    cursor.moveToFirst();
                    String hid = cursor.getString(historyId);
                    String d_id = cursor.getString(drinkId);
                    if(Integer.parseInt(hid) == drink){
                        ContentValues values = new ContentValues();
                        values.put(BarBroContract.HistoryEntry.COLUMN_DATE, getDateTime());
                        Uri newuri = BarBroContract.HistoryEntry.CONTENT_URI;
                        newuri = newuri.buildUpon().appendPath(d_id).build();
                        context.getContentResolver().update(newuri, values, null, null);
                    }

                }
                else{
                    Uri newuri = BarBroContract.HistoryEntry.CONTENT_URI;
                    ContentValues values = new ContentValues();
                    values.put(BarBroContract.HistoryEntry.COLUMN_HISTORYID, drink);
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
