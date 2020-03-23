package org.xtimms.ridebus;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.room.Room;

import org.xtimms.ridebus.model.ScheduleDatabase;

public class App extends Application {

    private static App instance;
    private static ScheduleDatabase mDatabase;

    public static App getInstance() {
        return instance;
    }

    @NonNull
    public static App from(Activity activity) {
        return (App) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (!ScheduleDatabase.checkDatabaseExist(getApplicationContext())) {
            ScheduleDatabase.copyDatabase(getApplicationContext(), ScheduleDatabase.DATABASE_NAME);
        }

        mDatabase = Room.databaseBuilder(getApplicationContext(), ScheduleDatabase.class, ScheduleDatabase.DATABASE_NAME).build();
        ScheduleDatabase.setDbVersion(getApplicationContext(), Constant.DB_VERSION);
    }

    public ScheduleDatabase getDatabase() {
        return mDatabase;
    }

    public Context getAppContext() {
        return getApplicationContext();
    }

}

