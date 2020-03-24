package org.xtimms.ridebus.presenter;

import android.os.AsyncTask;

import org.xtimms.ridebus.Constant;
import org.xtimms.ridebus.model.ModelFactory;
import org.xtimms.ridebus.model.Route;
import org.xtimms.ridebus.object.StopActivityObject;
import org.xtimms.ridebus.util.DateTime;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StopActivityPresenter {
    private View mView;
    private String mCurrentTime;
    private String mCurrentDate;
    private int mStopId;

    public StopActivityPresenter(View view, String currentTime, String currentDate, int stopId) {
        mView = view;
        mCurrentTime = currentTime;
        mCurrentDate = currentDate;
        mStopId = stopId;
    }

    public void setAdapter() {
        new ActivityObjectsListAsyncTask(mView, mCurrentTime, mCurrentDate, mStopId).execute();
    }

    public interface View {
        void setAdapter(List<StopActivityObject> stopActivityObjectList);
    }

    private static class ActivityObjectsListAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        private WeakReference<View> activityWeakReference;
        private List<StopActivityObject> mStopActivityObjectList = new ArrayList<>();
        private String mCurrentTime;
        private String mCurrentDate;
        private int mStopId;
        private DateTime mDateTime = new DateTime();

        ActivityObjectsListAsyncTask(View context, String currentTime, String currentDate, int stopId) {
            super();
            activityWeakReference = new WeakReference<>(context);
            mCurrentTime = currentTime;
            mCurrentDate = currentDate;
            mStopId = stopId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {

            List<Route> routeList = ModelFactory.getModel().getRoutesByStop(mStopId);

            for (Route route : routeList) {

                int routeId = route.getId();
                List<Integer> typeDayList = ModelFactory.getModel().getTypeDay(routeId, mStopId);
                List<String> timeList = null;

                try {
                    timeList = ModelFactory.getModel().getTimeOnStop(mDateTime
                            .getTypeDay(mCurrentDate, typeDayList), routeId, mStopId);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if ((timeList == null) || (timeList.isEmpty())) {
                    mStopActivityObjectList.add(new StopActivityObject(route, Constant.TIME_EMPTY, Constant.TIME_EMPTY));
                    continue;
                }

                DateTime.ResultTime resultTime = null;

                try {
                    resultTime = mDateTime.getRemainingClosestTime(timeList, mCurrentTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (resultTime == null) continue;

                mStopActivityObjectList.add(new StopActivityObject(
                        route, resultTime.getClosestTime(), resultTime.getRemainingTime()));

                Collections.sort(mStopActivityObjectList, (o1, o2) -> {

                    if (o1.getRemainingTime().equals(Constant.TIME_EMPTY)
                            && !o2.getRemainingTime().equals(Constant.TIME_EMPTY)) {
                        return 1;
                    }

                    if (o2.getRemainingTime().equals(Constant.TIME_EMPTY)
                            && !o1.getRemainingTime().equals(Constant.TIME_EMPTY)) {
                        return -1;
                    }

                    if (o1.getRemainingTime().equals(Constant.TIME_EMPTY)
                            && o2.getRemainingTime().equals(Constant.TIME_EMPTY)) {
                        return 0;
                    }

                    return Integer.valueOf(o1.getRemainingTime())
                            .compareTo(Integer.valueOf(o2.getRemainingTime()));

                });
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            activityWeakReference.get().setAdapter(mStopActivityObjectList);
        }
    }
}
