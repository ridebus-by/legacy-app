package org.xtimms.ridebus.model;

import org.xtimms.ridebus.App;

public class ModelFactory {
    public static ScheduleDao getModel() {
        return App.getInstance().getDatabase().ScheduleDao();
    }
}
