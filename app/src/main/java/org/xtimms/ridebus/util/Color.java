package org.xtimms.ridebus.util;

import android.content.Context;
import android.view.View;

import org.xtimms.ridebus.App;
import org.xtimms.ridebus.R;

public class Color {

    public static void setBackgroundColor(int transportId, View view, Context context) {
        if (transportId == TransportId.BUS.getIdInDatabase()) {
            view.setBackgroundColor(App.getInstance().getAppContext().getResources()
                    .getColor(R.color.colorPrimary));
        }
        if (transportId == TransportId.TRAM.getIdInDatabase()) {
            view.setBackgroundColor(App.getInstance().getAppContext().getResources()
                    .getColor(R.color.colorPrimary));
        }
        if (transportId == TransportId.EXPRESS.getIdInDatabase()) {
            view.setBackgroundColor(App.getInstance().getAppContext().getResources()
                    .getColor(R.color.colorPrimary));
        }
        if (transportId == TransportId.MINIBUS.getIdInDatabase()) {
            view.setBackgroundColor(App.getInstance().getAppContext().getResources()
                    .getColor(R.color.colorPrimary));
        }
    }

    public static void setBackgroundCircle(int transportId, View view, Context context) {
        if (transportId == TransportId.BUS.getIdInDatabase()) {
            view.setBackground(App.getInstance().getAppContext().getResources()
                    .getDrawable(R.drawable.circle));
        }
        if (transportId == TransportId.TRAM.getIdInDatabase()) {
            view.setBackground(App.getInstance().getAppContext().getResources()
                    .getDrawable(R.drawable.circle));
        }
        if (transportId == TransportId.EXPRESS.getIdInDatabase()) {
            view.setBackground(App.getInstance().getAppContext().getResources()
                    .getDrawable(R.drawable.circle));
        }
        if (transportId == TransportId.MINIBUS.getIdInDatabase()) {
            view.setBackground(App.getInstance().getAppContext().getResources()
                    .getDrawable(R.drawable.circle));
        }
    }

}
