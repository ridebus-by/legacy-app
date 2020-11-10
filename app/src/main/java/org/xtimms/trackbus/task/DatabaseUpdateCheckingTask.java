package org.xtimms.trackbus.task;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.xtimms.trackbus.backend.DatabaseBackend;
import org.xtimms.trackbus.bus.BusEvent;
import org.xtimms.trackbus.bus.DatabaseUpdateAvailableEvent;
import org.xtimms.trackbus.bus.DatabaseUpdateNotAvailableEvent;
import org.xtimms.trackbus.util.PreferencesUtils;

public class DatabaseUpdateCheckingTask extends AsyncTask<Void, Void, BusEvent> {

    private final Context context;

    public static void execute(@NonNull Context context) {
        new DatabaseUpdateCheckingTask(context).execute();
    }

    private DatabaseUpdateCheckingTask(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected BusEvent doInBackground(Void... voids) {
        String localDatabaseVersion = getLocalDatabaseVersion();
        String serverDatabaseVersion = getServerDatabaseVersion();

        if (StringUtils.isBlank(serverDatabaseVersion)) {
            return new DatabaseUpdateNotAvailableEvent();
        }

        if (StringUtils.isBlank(localDatabaseVersion)) {
            return new DatabaseUpdateAvailableEvent();
        }

        if (!localDatabaseVersion.equals(serverDatabaseVersion)) {
            return new DatabaseUpdateAvailableEvent();
        }

        return new DatabaseUpdateNotAvailableEvent();
    }

    private String getLocalDatabaseVersion() {
        return PreferencesUtils.of(context).getDatabaseVersion();
    }

    private String getServerDatabaseVersion() {
        return DatabaseBackend.with(context).getDatabaseVersion();
    }

    @Override
    protected void onPostExecute(BusEvent busEvent) {
        super.onPostExecute(busEvent);
    }
}
