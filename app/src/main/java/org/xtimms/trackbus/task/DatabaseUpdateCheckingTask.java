package org.xtimms.trackbus.task;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.util.ConstantUtils;
import org.xtimms.trackbus.util.ThemeUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DatabaseUpdateCheckingTask extends AsyncTask<String, Integer, String> {

    private final Context mContext;
    private PowerManager.WakeLock mWakeLock;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mUpdatingBuilder;
    private NotificationCompat.Builder mUpdatedBuilder;
    private NotificationChannel updatingNotificationChannel;
    private NotificationChannel updatedNotificationChannel;
    private static final int UPDATING_NOTIFICATION_ID = 1;
    private static final int UPDATED_NOTIFICATION_ID = 2;
    private static final String UPDATING_NOTIFICATION_CHANNEL_ID = "1";
    private static final String UPDATED_NOTIFICATION_CHANNEL_ID = "2";

    public DatabaseUpdateCheckingTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(mContext.getDatabasePath("trackbus.db"));

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                mNotifyManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

                updatingNotificationChannel = new NotificationChannel(UPDATING_NOTIFICATION_CHANNEL_ID, mContext.getResources().getString(R.string.updating_database), NotificationManager.IMPORTANCE_DEFAULT);
                updatedNotificationChannel = new NotificationChannel(UPDATED_NOTIFICATION_CHANNEL_ID, mContext.getResources().getString(R.string.database_updated_successfully), NotificationManager.IMPORTANCE_HIGH);

                // Configure the notification channel.
                updatingNotificationChannel.setDescription("Уведомление, сообщающее об обновлении расписания, если есть новая версия на сервере.");
                updatedNotificationChannel.setDescription("Уведомление, сообщающее об успешном обновлении расписания.");
                mNotifyManager.createNotificationChannel(updatingNotificationChannel);
                mNotifyManager.createNotificationChannel(updatedNotificationChannel);

                mUpdatingBuilder = new NotificationCompat.Builder(mContext, UPDATING_NOTIFICATION_CHANNEL_ID)
                        .setColor(ThemeUtils.getThemeAttrColor(mContext, R.attr.colorPrimary))
                        .setOnlyAlertOnce(true)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_baseline_bus_24)
                        .setContentTitle(mContext.getResources().getString(R.string.updating_database));

                mUpdatedBuilder = new NotificationCompat.Builder(mContext, UPDATED_NOTIFICATION_CHANNEL_ID)
                        .setColor(ThemeUtils.getThemeAttrColor(mContext, R.attr.colorPrimary))
                        .setOnlyAlertOnce(true)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_baseline_check_24)
                        .setContentTitle(mContext.getResources().getString(R.string.database_updated_successfully))
                        .setContentText(mContext.getString(R.string.current_version) + ConstantUtils.getDbVersion());

                mNotifyManager.notify(UPDATING_NOTIFICATION_ID, mUpdatingBuilder.build());
            }
            else
            {
                mUpdatingBuilder = new NotificationCompat.Builder(mContext, "updating");
                mUpdatedBuilder = new NotificationCompat.Builder(mContext, "updated");

                mUpdatingBuilder.setColor(ThemeUtils.getThemeAttrColor(mContext, R.attr.colorPrimary))
                        .setOnlyAlertOnce(true)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_baseline_bus_24)
                        .setContentTitle(mContext.getResources().getString(R.string.updating_database));

                mUpdatedBuilder.setColor(ThemeUtils.getThemeAttrColor(mContext, R.attr.colorPrimary))
                        .setOnlyAlertOnce(true)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_baseline_check_24)
                        .setContentTitle(mContext.getResources().getString(R.string.database_updated_successfully))
                        .setContentText(mContext.getString(R.string.current_version) + ConstantUtils.getDbVersion())
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyManager.notify(1, mUpdatingBuilder.build());
            }
        }
        catch(Exception e)
        {
            System.out.println("Null Pointer ");
        }
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire(10*60*1000L /*10 minutes*/);
        /*mProgressDialog.show();*/
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mNotifyManager.cancel(UPDATING_NOTIFICATION_ID);
        mNotifyManager.notify(UPDATED_NOTIFICATION_ID, mUpdatedBuilder.build());
        if (result != null) {
            Log.d("ERROR", "Download error: " + result);
        } else {
            Log.d("SUCCESS", "File downloaded");
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mUpdatingBuilder.setProgress(100, values[0], false);
        // Displays the progress bar on notification
        mNotifyManager.notify(UPDATING_NOTIFICATION_ID, mUpdatingBuilder.build());
    }

}