package org.xtimms.trackbus;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import org.xtimms.trackbus.model.ScheduleDatabase;
import org.xtimms.trackbus.task.DatabaseUpdateCheckingTask;
import org.xtimms.trackbus.util.ConstantUtils;
import org.xtimms.trackbus.util.LogUtils;
import org.xtimms.trackbus.util.NetworkUtils;

import java.util.Locale;
import java.util.Objects;

public class App extends Application {
    private static App instance;
    //public static final String TAG = App.class.getSimpleName();
    private static ScheduleDatabase mDatabase;

    public static App getInstance() {
        return instance;
    }

    @NonNull
    public static App from(Activity activity) {
        return (App) activity.getApplication();
    }

    public static void setLanguage(Resources res, String lang) {
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = TextUtils.isEmpty(lang) ? Locale.getDefault() : new Locale(lang);
        res.updateConfiguration(conf, dm);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LogUtils.init(this);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        updateDatabase();

        if (!ScheduleDatabase.checkDatabaseExist(getApplicationContext())) {
            ScheduleDatabase.copyDatabase(getApplicationContext(), ScheduleDatabase.DATABASE_NAME);
        }

        //mDatabase = Room.databaseBuilder(getApplicationContext(), ScheduleDatabase.class, ScheduleDatabase.DATABASE_NAME).allowMainThreadQueries().build();
        mDatabase = Room.databaseBuilder(getApplicationContext(), ScheduleDatabase.class, ScheduleDatabase.DATABASE_NAME).build();
        ScheduleDatabase.setDbVersion(getApplicationContext(), ConstantUtils.DB_VERSION);
        setLanguage(getResources(), PreferenceManager.getDefaultSharedPreferences(this).getString("lang", ""));
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
        Objects.requireNonNull(intent).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateDatabase() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            SharedPreferences sharedPreferences = getSharedPreferences(ScheduleDatabase.DB_VERSION_KEY, Context.MODE_PRIVATE);
            try {
                if (sharedPreferences.getInt(ScheduleDatabase.DB_VERSION_KEY, 0) != ConstantUtils.getDbVersion()) {
                    final DatabaseUpdateCheckingTask task = new DatabaseUpdateCheckingTask(this);
                    task.execute("https://rumblur.hrebeni.uk/ridebus/trackbus.db");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

