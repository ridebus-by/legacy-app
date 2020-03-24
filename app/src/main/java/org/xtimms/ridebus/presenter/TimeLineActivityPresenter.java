package org.xtimms.ridebus.presenter;

import android.os.AsyncTask;

import org.xtimms.ridebus.App;
import org.xtimms.ridebus.model.ModelFactory;
import org.xtimms.ridebus.model.Stop;
import org.xtimms.ridebus.object.StopsActivityTimeLineObject;
import org.xtimms.ridebus.util.DateTime;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TimeLineActivityPresenter {
    private View mView;
    private String mCurrentTime;
    private String mCurrentDate;
    private int mRouteId;

    public TimeLineActivityPresenter(View view, String currentTime, String currentDate, int routeId) {
        mView = view;
        mCurrentTime = currentTime;
        mCurrentDate = currentDate;
        mRouteId = routeId;
    }

    public void setAdapter() {
        new ActivityObjectsListAsyncTask(mView, mCurrentTime, mCurrentDate, mRouteId).execute();
    }

    public interface View {
        void setAdapter(List<StopsActivityTimeLineObject> stopsActivityTimeLineObjects, String currentTime);
    }

    private static class ActivityObjectsListAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        private WeakReference<View> activityWeakReference;
        private List<StopsActivityTimeLineObject> mStopsActivityTimeLineObjects = new ArrayList<>();
        private String mCurrentTime;
        private String mCurrentDate;
        private int mRouteId;
        private DateTime mDateTime = new DateTime();

        ActivityObjectsListAsyncTask(View context, String currentTime, String currentDate, int routeId) {
            super();
            activityWeakReference = new WeakReference<>(context);
            mCurrentTime = currentTime;
            mCurrentDate = currentDate;
            mRouteId = routeId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {

            List<Stop> stopList = App.getInstance().getDatabase().ScheduleDao().getStops(mRouteId);

            for (Stop stop : stopList) {

                int stopId = stop.getId();
                List<Integer> typeDayList = ModelFactory.getModel().getTypeDay(mRouteId, stopId);
                List<String> timeList = null;

                try {
                    timeList = ModelFactory.getModel().getTimeOnStop(mDateTime
                            .getTypeDay(mCurrentDate, typeDayList), mRouteId, stopId);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mStopsActivityTimeLineObjects.add(new StopsActivityTimeLineObject(stop, timeList));
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            activityWeakReference.get().setAdapter(mStopsActivityTimeLineObjects, mCurrentTime);
        }
    }
}
