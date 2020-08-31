package org.xtimms.trackbus.object;

import org.xtimms.trackbus.model.Stop;

import java.util.List;

public class StopsActivityTimeLineObject {
    private final Stop mStop;
    private final List<String> mTimeList;

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
