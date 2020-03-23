package org.xtimms.ridebus.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.xtimms.ridebus.Constant.DB_VERSION;

@Database(entities = {
        City.class, Transport.class, TypeDay.class, KindRoute.class, Route.class, Stop.class,
        RouteStops.class, Schedule.class, ClassTransport.class}, version = 1)
public abstract class ScheduleDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "ridebus.db";
    private static final String DB_VERSION_KEY = ScheduleDatabase.class.getSimpleName().concat("DB_VERSION");

    public static void copyDatabase(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);

        //If the database already exists, return // Проверка стала не нужна, после реализации
        if (dbPath.exists()) {                // метода checkDatabaseExist()
            context.deleteDatabase(DATABASE_NAME);
            //Log.d("Activity", "db Path Exists");
            //return;
        }

        // Make sure we have a path to the file
        boolean s = dbPath.getParentFile().mkdirs();

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
        } catch (IOException e) {
            //Log.d("Activity", "Failed to open file", e);
            e.printStackTrace();
        }
    }

    public static boolean checkDatabaseExist(Context context) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(DB_VERSION_KEY, Context.MODE_PRIVATE);

        if (sharedPreferences == null) return false;

        return sharedPreferences.getInt(DB_VERSION_KEY, 0) == DB_VERSION;
    }

    public static void setDbVersion(Context context, int dbVersion) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                DB_VERSION_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(DB_VERSION_KEY, dbVersion).apply();
    }

    public abstract ScheduleDao ScheduleDao();
}