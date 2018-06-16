package com.alexnassif.mobile.barbro.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

import com.alexnassif.mobile.barbro.model.Drink;
import com.alexnassif.mobile.barbro.model.History;
import com.alexnassif.mobile.barbro.model.MyDrink;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@Database(entities = {Drink.class, History.class, MyDrink.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

        private static final String LOG_TAG = AppDatabase.class.getSimpleName();
        private static final Object LOCK = new Object();
        private static final String DATABASE_NAME = "barbro.db";
        private static AppDatabase sInstance;
        static final Migration MIGRATION_1_2 = new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        };

        public static AppDatabase getsInstance(Context context){

            if(sInstance == null){
                synchronized (LOCK){
                    Log.d(LOG_TAG, "Creating a new database instance");
                    copyAttachedDatabase(context.getApplicationContext(), DATABASE_NAME);
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            AppDatabase.DATABASE_NAME).addMigrations(MIGRATION_1_2)
                            .allowMainThreadQueries()
                            .build();
                }
            }

            Log.d(LOG_TAG, "getting database instance");
            return sInstance;
        }

    public abstract DrinkDao drinkDao();

    private static void copyAttachedDatabase(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);

        // If the database already exists, return
        if (dbPath.exists()) {

            Log.d("exists?", "db exists");
            return;
        }

        // Make sure we have a path to the file
        dbPath.getParentFile().mkdirs();

        // Try to copy database file
        try {
            final InputStream inputStream = context.getAssets().open(databaseName);
            final OutputStream output = new FileOutputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = inputStream.read(buffer, 0, 8192)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            inputStream.close();
        }
        catch (IOException e) {
            Log.d("failedopen", "Failed to open file", e);
            e.printStackTrace();
        }
    }


}
