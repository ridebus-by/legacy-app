package org.xtimms.trackbus.util;

import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.model.Stop;

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
