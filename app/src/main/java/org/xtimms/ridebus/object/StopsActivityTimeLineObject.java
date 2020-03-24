package org.xtimms.ridebus.object;

import org.xtimms.ridebus.model.Stop;

import java.util.List;

public class StopsActivityTimeLineObject {
    private Stop mStop;
    private List<String> mTimeList;

    public StopsActivityTimeLineObject(Stop stop, List<String> timeList) {
        mStop = stop;
        mTimeList = timeList;
    }

    public Stop getStop() {
        return mStop;
    }

    public List<String> getTimeList() {
        return mTimeList;
    }
}
