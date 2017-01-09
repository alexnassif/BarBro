package com.example.raymond.barbro.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by alex on 12/29/16.
 */

public class BarBroContentProvider extends ContentProvider {

    private BarBroDbHelper dbHelper;
    public static final int DRINKS = 100;
    public static final int FAVORITES = 200;
    public static final int DRINKS_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BarBroContract.AUTHORITY, BarBroContract.PATH_DRINKS, DRINKS);
        uriMatcher.addURI(BarBroContract.AUTHORITY, BarBroContract.PATH_FAVES, FAVORITES);
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

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match){
            case DRINKS:
                retCursor = db.query(BarBroContract.BarBroEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITES:
                retCursor = db.query(BarBroContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Uri doesn't match " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri retUri;

        switch (match){
            case DRINKS:{
                long id = db.insert(BarBroContract.BarBroEntry.TABLE_NAME, null, values);
                if(id > 0)
                    retUri = ContentUris.withAppendedId(uri, id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;}
            case FAVORITES:{
                long id = db.insert(BarBroContract.FavoritesEntry.TABLE_NAME, null, values);
                if(id > 0)
                    retUri = ContentUris.withAppendedId(uri, id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;}
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values){
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int retInt;

        switch(match){
            case DRINKS:
                db.beginTransaction();
                int rowsInserted = 0;

                try{

                    for(ContentValues value: values){
                        long _id = db.insert(BarBroContract.BarBroEntry.TABLE_NAME, null, value);
                        if(_id != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();

                }
                finally {
                    db.endTransaction();
                }
                if(rowsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);

        }

    }
}
