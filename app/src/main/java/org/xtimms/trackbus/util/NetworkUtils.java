package org.xtimms.trackbus.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        return isNetworkAvailable(context, true);
    }

    public static boolean isNetworkAvailable(Context context, boolean allowMetered) {
        final ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        final NetworkInfo network = manager.getActiveNetworkInfo();
        return network != null && network.isConnected() && (allowMetered || isNotMetered(network));
    }

    private static boolean isNotMetered(NetworkInfo networkInfo) {
        if(networkInfo.isRoaming()) return false;
        final int type = networkInfo.getType();
        return type == ConnectivityManager.TYPE_WIFI
                || type == ConnectivityManager.TYPE_WIMAX
                || type == ConnectivityManager.TYPE_ETHERNET;
    }

}
