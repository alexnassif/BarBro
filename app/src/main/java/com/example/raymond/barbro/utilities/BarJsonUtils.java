package com.example.raymond.barbro.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.os.Debug;
import android.util.Log;

import com.example.raymond.barbro.data.BarBroContract;
import com.example.raymond.barbro.data.Drink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by raymond on 12/11/16.
 */

public final class BarJsonUtils {
    public static Map<String, ContentValues> drinkMap = new Hashtable<String, ContentValues>();
    public static Drink[] getSimpleDrinkStringsFromJson(String drinkJsonStr)
            throws JSONException {
        final String RESULT = "result";
        final String INGREDIENTS = "ingredients";
        final String NAME = "name";
        final String ID = "id";

        Drink[] parsedDrinkData = null;

        JSONObject drinkJson = new JSONObject(drinkJsonStr);

        JSONArray drinkArray = drinkJson.getJSONArray(RESULT);

        parsedDrinkData = new Drink[drinkArray.length()];

        for(int i = 0; i < drinkArray.length(); i++){

            JSONObject drinkObject = drinkArray.getJSONObject(i);
            JSONArray ingredientsArray = drinkObject.getJSONArray(INGREDIENTS);
            ArrayList<String> ingList = new ArrayList<String>();
            for(int j = 0; j < ingredientsArray.length(); j++){
                JSONObject ingObject = ingredientsArray.getJSONObject(j);
                ingList.add(ingObject.getString("textPlain"));

            }
            String drinkName = drinkObject.getString(NAME);
            String id = drinkObject.getString(ID);
            //Drink drink = new Drink(drinkName, ingList, id);
            //parsedDrinkData[i] = drink;
        }

        return parsedDrinkData;
    }

    public static ContentValues[] getDrinkContentValuesFromJson(Context context, String drinkJsonStr)
            throws JSONException {

        final String RESULT = "result";
        final String INGREDIENTS = "ingredients";
        final String NAME = "name";
        final String ID = "id";
        final String VIDEO = "videos";
        final String TASTES = "tastes";
        final String DESCRIPTION = "descriptionPlain";

        JSONObject drinkJson = new JSONObject(drinkJsonStr);

        JSONArray drinkArray = drinkJson.getJSONArray(RESULT);

        ContentValues[] parsedDrinkData = new ContentValues[drinkArray.length()];

        for(int i = 0; i < drinkArray.length(); i++){

            JSONObject drinkObject = drinkArray.getJSONObject(i);
            String drinkName = drinkObject.getString(NAME);
            if(!drinkMap.containsKey(drinkName)){
                JSONArray ingredientsArray = drinkObject.getJSONArray(INGREDIENTS);
                String ingList = "";
                ContentValues drinkValue = new ContentValues();
                for(int j = 0; j < ingredientsArray.length(); j++){
                    JSONObject ingObject = ingredientsArray.getJSONObject(j);
                    ingList += ingObject.getString("textPlain") + "\n";
                    String typeL = ingObject.getString("type");
                    switch (typeL){
                        case "vodka":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_VODKA, 1);
                            break;
                        case "gin":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_GIN, 1);
                            break;
                        case "rum":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_RUM, 1);
                            break;
                        case "tequila":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_TEQUILA, 1);
                            break;
                        case "brandy":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_BRANDY, 1);
                            break;
                        case "whisky":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_WHISKY, 1);
                            break;
                        default:
                            break;

                    }

                }
                JSONArray tastesArray = drinkObject.getJSONArray(TASTES);
                for(int j = 0; j < tastesArray.length(); j++){
                    JSONObject tObject = tastesArray.getJSONObject(j);
                    String typeT = tObject.getString("id");
                    switch (typeT){
                        case "sweet":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_SWEET, 1);
                            break;
                        case "fresh":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_FRESH, 1);
                            break;
                        case "fruity":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_FRUITY, 1);
                            break;
                        case "berry":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_BERRY, 1);
                            break;
                        case "sour":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_SOUR, 1);
                            break;
                        case "spicy":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_SPICY, 1);
                            break;
                        case "herb":
                            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_HERB, 1);
                            break;
                        default:
                            break;

                    }

                }
                JSONArray videoArray = drinkObject.getJSONArray(VIDEO);
                for(int k = 0; k < videoArray.length(); k++){
                    JSONObject vidObject = videoArray.getJSONObject(k);
                    String vid = vidObject.getString("type");
                    if (vid.equals("assets")) {
                        String videoType = vidObject.getString("video");
                        drinkValue.put(BarBroContract.BarBroEntry.COLUMN_VIDEO, videoType);
                    }

                }
                String description = drinkObject.getString(DESCRIPTION);
                String id = drinkObject.getString(ID);
                drinkValue.put(BarBroContract.BarBroEntry.COLUMN_DESCRIPTION, description);
                drinkValue.put(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME, drinkName);
                drinkValue.put(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS, ingList);
                drinkValue.put(BarBroContract.BarBroEntry.COLUMN_DRINK_PIC, id);
                parsedDrinkData[i] = drinkValue;
                drinkMap.put(drinkName, drinkValue);
            }
        }

        return parsedDrinkData;

    }
}
