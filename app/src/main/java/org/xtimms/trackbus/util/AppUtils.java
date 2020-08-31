package org.xtimms.trackbus.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.xtimms.trackbus.Constant;
import org.xtimms.trackbus.R;

public class AppUtils {

    public static boolean checkAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return context.getString(R.string.about_version) + " " + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDbVersion(Context context) {
        try {
            String version = String.valueOf(Constant.DB_VERSION);
            return context.getString(R.string.db_version) + " " + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
