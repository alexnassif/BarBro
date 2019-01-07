package com.alexnassif.mobile.barbro.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Drink.class, MyDrink.class, History.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "barbro";
    private static AppDatabase sInstance;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.

            database.execSQL("DROP TABLE drinks");
        }
    };
    public static AppDatabase getsInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract FavoritesDao favoritesDao();
    public abstract MyDrinksDao myDrinksDao();


}
