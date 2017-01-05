package com.example.raymond.barbro.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.utilities.BarJsonUtils;
import com.example.raymond.barbro.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by alex on 1/5/17.
 */

public class BarBroSyncTask {

    synchronized public static void syncBarBro(Context context){

        try{
            URL allDrinkUrl = NetworkUtils.buildAllDrinksUrl();
            String jsonDrinkVal = NetworkUtils.getResponseFromHttpUrl(allDrinkUrl);
            ContentValues[] drinkValues = BarJsonUtils.getDrinkContentValuesFromJson(context, jsonDrinkVal);
            if(drinkValues != null && drinkValues.length != 0){
                ContentResolver BarBroResolver = context.getContentResolver();
                BarBroResolver.bulkInsert(BarBroContract.BarBroEntry.CONTENT_URI, drinkValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
