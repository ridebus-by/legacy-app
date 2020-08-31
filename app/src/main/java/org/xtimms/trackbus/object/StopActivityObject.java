package org.xtimms.trackbus.object;

import org.xtimms.trackbus.model.Route;

public class StopActivityObject {
    private final Route mRoute;
    private final String mClosestTime;
    private final String mRemainingTime;

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
