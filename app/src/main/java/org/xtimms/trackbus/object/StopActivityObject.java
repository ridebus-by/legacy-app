package org.xtimms.trackbus.object;

import org.xtimms.trackbus.model.Route;

public class StopActivityObject {

    private final int mId;
    private final Route mRoute;
    private final String mClosestTime;
    private final String mRemainingTime;

    public StopActivityObject(int id, Route route, String closestTime, String remainingTime) {
        mId = id;
        mRoute = route;
        mClosestTime = closestTime;
        mRemainingTime = remainingTime;
    }

    public int getId() {
        return mId;
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
