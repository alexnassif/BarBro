package com.example.raymond.barbro.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by alex on 12/29/16.
 */

public class BarBroContentProvider extends ContentProvider {

    private BarBroDbHelper dbHelper;
    public static final int DRINKS = 100;
    public static final int DRINKS_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BarBroContract.AUTHORITY, BarBroContract.PATH_DRINKS, DRINKS);
        uriMatcher.addURI(BarBroContract.AUTHORITY, BarBroContract.PATH_DRINKS + "/#", DRINKS_WITH_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new BarBroDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
