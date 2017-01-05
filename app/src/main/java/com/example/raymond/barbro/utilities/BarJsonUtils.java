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

/**
 * Created by raymond on 12/11/16.
 */

public final class BarJsonUtils {

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

        JSONObject drinkJson = new JSONObject(drinkJsonStr);

        JSONArray drinkArray = drinkJson.getJSONArray(RESULT);

        ContentValues[] parsedDrinkData = new ContentValues[drinkArray.length()];

        for(int i = 0; i < drinkArray.length(); i++){

            JSONObject drinkObject = drinkArray.getJSONObject(i);
            JSONArray ingredientsArray = drinkObject.getJSONArray(INGREDIENTS);
            String ingList = "";
            for(int j = 0; j < ingredientsArray.length(); j++){
                JSONObject ingObject = ingredientsArray.getJSONObject(j);
                ingList += ingObject.getString("textPlain") + "\n";

            }
            String drinkName = drinkObject.getString(NAME);
            String id = drinkObject.getString(ID);

            ContentValues drinkValue = new ContentValues();
            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME, drinkName);
            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS, ingList);
            drinkValue.put(BarBroContract.BarBroEntry.COLUMN_DRINK_PIC, id);
            parsedDrinkData[i] = drinkValue;
        }

        return parsedDrinkData;

    }
}
