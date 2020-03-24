package org.xtimms.ridebus.object;

import org.xtimms.ridebus.model.Route;

public class StopActivityObject {
    private Route mRoute;
    private String mClosestTime;
    private String mRemainingTime;

    public StopActivityObject(Route route, String closestTime, String remainingTime) {
        mRoute = route;
        mClosestTime = closestTime;
        mRemainingTime = remainingTime;
    }

    public Route getRoute() {
        return mRoute;
    }

    public String getClosestTime() {
        return mClosestTime;
    }

    public String getRemainingTime() {
        return mRemainingTime;
    }
}
