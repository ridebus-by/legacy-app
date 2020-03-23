package org.xtimms.ridebus.util;

import org.xtimms.ridebus.model.Route;
import org.xtimms.ridebus.model.Stop;

import java.io.Serializable;

public class SharePreferenceObject implements Serializable {
    private Stop mStop;
    private Route mRoute;

    public SharePreferenceObject(Route route) {
        mRoute = route;
    }

    public SharePreferenceObject(Stop stop) {
        mStop = stop;
    }

    public Stop getStop() {
        return mStop;
    }

    public void setStop(Stop stop) {
        mStop = stop;
    }

    public Route getRoute() {
        return mRoute;
    }

    public void setRoute(Route route) {
        mRoute = route;
    }
}
