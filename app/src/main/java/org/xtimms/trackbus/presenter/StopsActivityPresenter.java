package org.xtimms.trackbus.presenter;

import android.os.AsyncTask;

import org.xtimms.trackbus.util.ConstantUtils;
import org.xtimms.trackbus.model.ModelFactory;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.object.StopActivityObject;
import org.xtimms.trackbus.util.DateTime;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StopsActivityPresenter {
    private final View mView;
    private final String mCurrentTime;
    private final String mCurrentDate;
    private final int mStopId;

    public StopsActivityPresenter(View view, String currentTime, String currentDate, int stopId) {
        mView = view;
        mCurrentTime = currentTime;
        mCurrentDate = currentDate;
        mStopId = stopId;
    }

    public void setAdapter() {
        new ActivityObjectsListAsyncTask(mView, mCurrentTime, mCurrentDate, mStopId).execute();
    }

    public interface View {
        void setAdapter(ArrayList<StopActivityObject> stopActivityObjectList);
    }

    private static class ActivityObjectsListAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        private final WeakReference<View> activityWeakReference;
        private final ArrayList<StopActivityObject> mStopActivityObjectList = new ArrayList<>();
        private final String mCurrentTime;
        private final String mCurrentDate;
        private final int mStopId;
        private final DateTime mDateTime = new DateTime();
        private Exception mException;

        ActivityObjectsListAsyncTask(View context, String currentTime, String currentDate, int stopId) {
            super();
            activityWeakReference = new WeakReference<>(context);
            mCurrentTime = currentTime;
            mCurrentDate = currentDate;
            mStopId = stopId;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {

            List<Route> routeList = ModelFactory.getModel().getRoutesByStop(mStopId);

            for (Route route : routeList) {

                int routeId = route.getId();

                List<Integer> typeDayList = ModelFactory.getModel().getTypeDay(routeId, mStopId);
                List<String> timeList;

                try {
                    timeList = ModelFactory.getModel().getTimeOnStop(mDateTime
                            .getTypeDay(mCurrentDate, typeDayList), routeId, mStopId);
                } catch (ParseException e) {
                    mException = e;
                    e.printStackTrace();
                    return true;
                }

                if ((timeList == null) || (timeList.isEmpty())) {
                    mStopActivityObjectList.add(new StopActivityObject(routeId, route, ConstantUtils.TIME_EMPTY, ConstantUtils.TIME_EMPTY));
                    continue;
                }

                DateTime.ResultTime resultTime;

                try {
                    resultTime = mDateTime.getRemainingClosestTime(timeList, mCurrentTime);
                } catch (ParseException e) {
                    mException = e;
                    e.printStackTrace();
                    return true;
                }

                if (resultTime == null) continue;

                mStopActivityObjectList.add(new StopActivityObject(routeId, route, resultTime.getClosestTime(), resultTime.getRemainingTime()));

                Collections.sort(mStopActivityObjectList, (o1, o2) -> {

                    if (o1.getRemainingTime().equals(ConstantUtils.TIME_EMPTY)
                            && !o2.getRemainingTime().equals(ConstantUtils.TIME_EMPTY)) {
                        return 1;
                    }

                    if (o2.getRemainingTime().equals(ConstantUtils.TIME_EMPTY)
                            && !o1.getRemainingTime().equals(ConstantUtils.TIME_EMPTY)) {
                        return -1;
                    }

                    if (o1.getRemainingTime().equals(ConstantUtils.TIME_EMPTY)
                            && o2.getRemainingTime().equals(ConstantUtils.TIME_EMPTY)) {
                        return 0;
                    }

                    return Integer.valueOf(o1.getRemainingTime())
                            .compareTo(Integer.valueOf(o2.getRemainingTime()));

                });
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (mException == null) {
                activityWeakReference.get().setAdapter(mStopActivityObjectList);
            } else {
                return;
            }
        }
    }
}
