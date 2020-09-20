package org.xtimms.trackbus.util;

import android.view.View;

import org.xtimms.trackbus.App;
import org.xtimms.trackbus.R;

public class ColorUtils {

    public static void setBackgroundColor(int transportId, View view) {
        if (transportId == TransportId.BUS.getIdInDatabase()) {
            view.setBackgroundColor(App.getInstance().getAppContext().getResources()
                    .getColor(R.color.blue_bus));
        }
        if (transportId == TransportId.TRAM.getIdInDatabase()) {
            view.setBackgroundColor(App.getInstance().getAppContext().getResources()
                    .getColor(R.color.red_tram));
        }
        if (transportId == TransportId.EXPRESS.getIdInDatabase()) {
            view.setBackgroundColor(App.getInstance().getAppContext().getResources()
                    .getColor(R.color.green_express));
        }
        if (transportId == TransportId.MINIBUS.getIdInDatabase()) {
            view.setBackgroundColor(App.getInstance().getAppContext().getResources()
                    .getColor(R.color.orange_minibus));
        }
    }

    public static void setBackgroundCircle(int transportId, View view) {
        if (transportId == TransportId.BUS.getIdInDatabase()) {
            view.setBackground(App.getInstance().getAppContext().getResources()
                    .getDrawable(R.drawable.circle_bus));
        }
        if (transportId == TransportId.TRAM.getIdInDatabase()) {
            view.setBackground(App.getInstance().getAppContext().getResources()
                    .getDrawable(R.drawable.circle_tram));
        }
        if (transportId == TransportId.EXPRESS.getIdInDatabase()) {
            view.setBackground(App.getInstance().getAppContext().getResources()
                    .getDrawable(R.drawable.circle_express));
        }
        if (transportId == TransportId.MINIBUS.getIdInDatabase()) {
            view.setBackground(App.getInstance().getAppContext().getResources()
                    .getDrawable(R.drawable.circle_minibus));
        }
    }

}
