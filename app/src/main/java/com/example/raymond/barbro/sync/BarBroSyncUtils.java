package com.example.raymond.barbro.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.raymond.barbro.data.BarBroContract;

/**
 * Created by alex on 1/5/17.
 */

public class BarBroSyncUtils {

    private static boolean sInitialized;


    synchronized public static void initialize(@NonNull final Context context) {

        if (sInitialized) return;


        sInitialized = true;


        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground( Void... voids ) {

                Uri forecastQueryUri = BarBroContract.BarBroEntry.CONTENT_URI;

                /* Here, we perform the query to check to see if we have any weather data */
                Cursor cursor = context.getContentResolver().query(
                        forecastQueryUri,
                        null,
                        null,
                        null,
                        null);

                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                /* Make sure to close the Cursor to avoid memory leaks! */
                cursor.close();
                return null;
            }
        }.execute();
    }

    public static void startImmediateSync(@NonNull final Context context){

        Intent syncIntent = new Intent(context, BarBroSyncIntentService.class);
        context.startService(syncIntent);
    }
}
