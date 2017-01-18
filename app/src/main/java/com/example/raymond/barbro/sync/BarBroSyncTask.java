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

            URL vodkaDrinkUrl = NetworkUtils.buildUrl("vodka");
            URL rumDrinkUrl = NetworkUtils.buildUrl("rum");
            URL berriesDrinkUrl = NetworkUtils.buildUrl("berries");
            URL brandyDrinkUrl = NetworkUtils.buildUrl("brandy");
            URL ginDrinkUrl = NetworkUtils.buildUrl("gin");
            URL tequilaDrinkUrl = NetworkUtils.buildUrl("tequila");
            URL whiskyDrinkUrl = NetworkUtils.buildUrl("whisky");

            String jsonDrinkVal = NetworkUtils.getResponseFromHttpUrl(allDrinkUrl);
            String vodkaJsonDrinkVal = NetworkUtils.getResponseFromHttpUrl(vodkaDrinkUrl);
            String rumJsonDrinkVal = NetworkUtils.getResponseFromHttpUrl(rumDrinkUrl);
            String berriesJsonDrinkVal = NetworkUtils.getResponseFromHttpUrl(berriesDrinkUrl);
            String brandyJsonDrinkVal = NetworkUtils.getResponseFromHttpUrl(brandyDrinkUrl);
            String ginJsonDrinkVal = NetworkUtils.getResponseFromHttpUrl(ginDrinkUrl);
            String tequilaJsonDrinkVal = NetworkUtils.getResponseFromHttpUrl(tequilaDrinkUrl);
            String whiskyJsonDrinkVal = NetworkUtils.getResponseFromHttpUrl(whiskyDrinkUrl);

            ContentValues[] drinkValues = BarJsonUtils.getDrinkContentValuesFromJson(context, jsonDrinkVal);
            ContentValues[] vodkaValues = BarJsonUtils.getDrinkContentValuesFromJson(context, vodkaJsonDrinkVal);
            ContentValues[] rumValues = BarJsonUtils.getDrinkContentValuesFromJson(context, rumJsonDrinkVal);
            ContentValues[] berriesValues = BarJsonUtils.getDrinkContentValuesFromJson(context, berriesJsonDrinkVal);
            ContentValues[] brandyValues = BarJsonUtils.getDrinkContentValuesFromJson(context, brandyJsonDrinkVal);
            ContentValues[] ginValues = BarJsonUtils.getDrinkContentValuesFromJson(context, ginJsonDrinkVal);
            ContentValues[] tequilaValues = BarJsonUtils.getDrinkContentValuesFromJson(context, tequilaJsonDrinkVal);
            ContentValues[] whiskyValues = BarJsonUtils.getDrinkContentValuesFromJson(context, whiskyJsonDrinkVal);

            if(drinkValues != null && drinkValues.length != 0){
                ContentResolver BarBroResolver = context.getContentResolver();
                BarBroResolver.bulkInsert(BarBroContract.BarBroEntry.CONTENT_URI, drinkValues);
            }
            if(vodkaValues != null && vodkaValues.length != 0){
                ContentResolver BarBroResolver = context.getContentResolver();
                BarBroResolver.bulkInsert(BarBroContract.BarBroEntry.CONTENT_URI, vodkaValues);
            }
            if(rumValues != null && rumValues.length != 0){
                ContentResolver BarBroResolver = context.getContentResolver();
                BarBroResolver.bulkInsert(BarBroContract.BarBroEntry.CONTENT_URI, rumValues);
            }
            if(berriesValues != null && berriesValues.length != 0){
                ContentResolver BarBroResolver = context.getContentResolver();
                BarBroResolver.bulkInsert(BarBroContract.BarBroEntry.CONTENT_URI, berriesValues);
            }
            if(brandyValues != null && brandyValues.length != 0){
                ContentResolver BarBroResolver = context.getContentResolver();
                BarBroResolver.bulkInsert(BarBroContract.BarBroEntry.CONTENT_URI, brandyValues);
            }
            if(ginValues != null && ginValues.length != 0){
                ContentResolver BarBroResolver = context.getContentResolver();
                BarBroResolver.bulkInsert(BarBroContract.BarBroEntry.CONTENT_URI, ginValues);
            }
            if(tequilaValues != null && tequilaValues.length != 0){
                ContentResolver BarBroResolver = context.getContentResolver();
                BarBroResolver.bulkInsert(BarBroContract.BarBroEntry.CONTENT_URI, tequilaValues);
            }
            if(whiskyValues != null && whiskyValues.length != 0){
                ContentResolver BarBroResolver = context.getContentResolver();
                BarBroResolver.bulkInsert(BarBroContract.BarBroEntry.CONTENT_URI, whiskyValues);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
