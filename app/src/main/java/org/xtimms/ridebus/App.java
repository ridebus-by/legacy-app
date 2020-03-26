package org.xtimms.ridebus;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.room.Room;

import org.xtimms.ridebus.model.ScheduleDatabase;

import java.util.Locale;

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

    public static void setLanguage(Resources res, String lang) {
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = TextUtils.isEmpty(lang) ? Locale.getDefault() : new Locale(lang);
        res.updateConfiguration(conf, dm);
    }

    public ScheduleDatabase getDatabase() {
        return mDatabase;
    }

    public Context getAppContext() {
        return getApplicationContext();
    }

    public void restart() {
        final Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        assert intent != null;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}

