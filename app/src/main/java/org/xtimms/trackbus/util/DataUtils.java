package org.xtimms.trackbus.util;

import org.xtimms.trackbus.BuildConfig;
import org.xtimms.trackbus.model.MyApps;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    public static List<MyApps> getMyAppsData() {
        List<MyApps> lists = new ArrayList();

        MyApps model0 = new MyApps();
        model0.setName("Kitsune - Читалка манги");
        model0.setDescription("Удобная и многофункциональная читалка манги");
        model0.setPackageName("org.xtimms.kitsune");
        model0.setGooglePlayUrl("https://play.google.com/store/apps/details?id=org.xtimms.kitsune");
        model0.setImageUrl("kitsune");
        if (!model0.getPackageName().equals(BuildConfig.APPLICATION_ID))
            lists.add(model0);

        return lists;
    }
}