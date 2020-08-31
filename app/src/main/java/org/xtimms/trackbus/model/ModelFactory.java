package org.xtimms.trackbus.model;

import org.xtimms.trackbus.App;

public class ModelFactory {
    public static ScheduleDao getModel() {
        return App.getInstance().getDatabase().ScheduleDao();
    }
}
