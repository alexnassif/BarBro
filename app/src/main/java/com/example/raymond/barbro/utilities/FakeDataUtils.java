package com.example.raymond.barbro.utilities;

/**
 * Created by raymond on 1/2/17.
 */
import android.content.ContentValues;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import com.example.raymond.barbro.data.BarBroContract;


public class FakeDataUtils {


    private static ContentValues createDrinkContentValues(int i) {
        ContentValues testDrinkValues = new ContentValues();
        testDrinkValues.put(BarBroContract.BarBroEntry.COLUMN_DRINK_NAME, "Alex Test Tini " + i);
        testDrinkValues.put(BarBroContract.BarBroEntry.COLUMN_INGREDIENTS, "Vodka\n"+"Gin\n"+"juice\n");
        testDrinkValues.put(BarBroContract.BarBroEntry.COLUMN_DRINK_PIC, "absolut-cosmopolitan");

        return testDrinkValues;
    }

    /**
     * Creates random weather data for 7 days starting today
     * @param context
     */
    public static void insertFakeData(Context context) {
        //Get today's normalized date
        List<ContentValues> fakeValues = new ArrayList<ContentValues>();
        //loop over 7 days starting today onwards
        for(int i=0; i<7; i++) {
            fakeValues.add(FakeDataUtils.createDrinkContentValues(i));
        }
        // Bulk Insert our new weather data into Sunshine's Database
        context.getContentResolver().bulkInsert(
                BarBroContract.BarBroEntry.CONTENT_URI,
                fakeValues.toArray(new ContentValues[7]));
    }
}
