package org.xtimms.trackbus.presenter;

import android.os.AsyncTask;
import android.util.Log;

import org.xtimms.trackbus.App;
import org.xtimms.trackbus.model.ModelFactory;
import org.xtimms.trackbus.model.Stop;
import org.xtimms.trackbus.object.StopsActivityTimeLineObject;
import org.xtimms.trackbus.util.DateTime;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TimeLineActivityPresenter {
    private final View mView;
    private final String mCurrentTime;
    private final String mCurrentDate;
    private final int mRouteId;

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
        private final WeakReference<View> activityWeakReference;
        private final List<StopsActivityTimeLineObject> mStopsActivityTimeLineObjects = new ArrayList<>();
        private final String mCurrentTime;
        private final String mCurrentDate;
        private final int mRouteId;
        private final DateTime mDateTime = new DateTime();

        ActivityObjectsListAsyncTask(View context, String currentTime, String currentDate, int routeId) {
            super();
            activityWeakReference = new WeakReference<>(context);
            mCurrentTime = currentTime;
            mCurrentDate = currentDate;
            mRouteId = routeId;
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
        protected void onPostExecute(Boolean result) {
            if (result == null) {
                Log.d("ERROR", "OnPostExecute not working...");
                return;
            } else {
                activityWeakReference.get().setAdapter(mStopsActivityTimeLineObjects, mCurrentTime);
                Log.d("SUCCESS", "Yay! Adapter working!");
            }
        }
    }
}
