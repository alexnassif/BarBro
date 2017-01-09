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
    public static final String PATH_FAVES = "favorites";

    public static final class BarBroEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DRINKS).build();
        public static final String TABLE_NAME = "drinks";
        public static final String COLUMN_DRINK_NAME = "drinkName";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_DRINK_PIC = "pic";
        public static final String COLUMN_FAVORITE = "favorite";

    }

    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVES).build();
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_DRINK_ID = "drink_id";

    }
}
