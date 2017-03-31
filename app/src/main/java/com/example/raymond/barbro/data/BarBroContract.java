package com.example.raymond.barbro.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alex on 12/29/16.
 */

public class BarBroContract {

    public static final String AUTHORITY = "com.example.raymond.barbro";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_DRINKS = "drinks";
    public static final String PATH_MYDRINKS = "mydrinks";
    public static final String PATH_HISTORY = "history";

    public static final class BarBroEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DRINKS).build();
        public static final String TABLE_NAME = "drinks";
        public static final String COLUMN_DRINK_NAME = "drinkName";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_DRINK_PIC = "pic";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_VODKA = "vodka";
        public static final String COLUMN_GIN = "gin";
        public static final String COLUMN_RUM = "rum";
        public static final String COLUMN_TEQUILA = "tequila";
        public static final String COLUMN_WHISKY = "whisky";
        public static final String COLUMN_BRANDY = "brandy";
    }

    public static final class MyDrinkEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MYDRINKS).build();
        public static final String TABLE_NAME = "mydrinks";
        public static final String COLUMN_MYDRINK_NAME = "name";
        public static final String COLUMN_MYINGREDIENTS = "ingredients";
        public static final String COLUMN_MYDRINK_PIC = "pic";

    }

    public static final class HistoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HISTORY).build();
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_HISTORYID = "idh";
        public static final String COLUMN_DATE = "date";

    }
}
