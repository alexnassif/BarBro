package com.example.raymond.barbro.data;

import android.provider.BaseColumns;

/**
 * Created by alex on 12/29/16.
 */

public class BarBroContract {

    public static final class BarBroEntry implements BaseColumns {

        public static final String TABLE_NAME = "drinks";
        public static final String COLUMN_DRINK_NAME = "drinkName";
        public static final String COLUMN_INGREDIENTS = "ingredients";

    }
}
