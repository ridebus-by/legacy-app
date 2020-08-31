package org.xtimms.trackbus.presenter;

import android.content.Context;
import android.os.AsyncTask;

import org.xtimms.trackbus.App;
import org.xtimms.trackbus.Constant;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.model.ModelFactory;
import org.xtimms.trackbus.util.DateTime;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class ScheduleActivityPresenter {
    private final View mView;
    private final int mStopId;
    private final int mRouteId;
    private final String mCurrentDate;

    public ScheduleActivityPresenter(View view, int stopId, int routeId, String currentDate) {
        mView = view;
        mStopId = stopId;
        mRouteId = routeId;
        mCurrentDate = currentDate;
    }

    public void setAdapter() {
        new GetScheduleAsyncTask(mView).execute(String.valueOf(mStopId),
                String.valueOf(mRouteId), mCurrentDate);
    }

    public void dateChange() {
        setAdapter();
    }

    public interface View {
        void setAdapter(Map<String, List<String>> scheduleMap, String closestTime);
    }

    public static class GetScheduleAsyncTask extends AsyncTask<String, Void, Void> {
        private final WeakReference<View> mViewWeakReference;
        private List<String> mTimeList;
        private final List<String> mErrorMessage = new ArrayList<>();
        private String mClosestTime;
        private final Map<String, List<String>> mMap = new TreeMap<>((o1, o2) -> {

            if (o1.equals("00"))
                o1 = "24";
            if (o2.equals("00")) o2 = "24";
            if (o1.equals("01")) o1 = "25";
            if (o2.equals("01")) o2 = "25";

            return o1.compareTo(o2);
        });

        private GetScheduleAsyncTask(View context) {
            super();
            mViewWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            int stopId = Integer.parseInt(params[0]);
            int routeId = Integer.parseInt(params[1]);
            String currentDate = params[2];

            DateTime dateTime = new DateTime();
            String currentTime = DateTime.getCurrentTime();

            List<Integer> typeDayList = ModelFactory.getModel().getTypeDay(routeId, stopId);
            try {
                mTimeList = ModelFactory.getModel().getTimeOnStop(dateTime.getTypeDay(currentDate, typeDayList), routeId, stopId);

                if (mTimeList.isEmpty()) {
                    Context appContext = App.getInstance().getAppContext();
                    mErrorMessage.add(appContext.getString(R.string.string_mapError));
                    mMap.put("!", mErrorMessage);
                    return null;
                }

                DateTime.ResultTime resultTime = dateTime.getRemainingClosestTime(mTimeList, currentTime);
                mClosestTime = resultTime.getClosestTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < mTimeList.size(); i++) {
                List<String> mins = new ArrayList<>();
                StringTokenizer tokenizer = new StringTokenizer(mTimeList.get(i), Constant.TIME_DELIM);
                String hours = tokenizer.nextToken();
                String minutes = tokenizer.nextToken();

                if (mMap.containsKey(hours)) {
                    continue;
                }

                mins.add(minutes);

                for (int j = i + 1; j < mTimeList.size(); j++) {
                    StringTokenizer token = new StringTokenizer(mTimeList.get(j), Constant.TIME_DELIM);

                    if (token.nextToken().equals(hours)) {
                        mins.add(token.nextToken());
                    }
                }

                mMap.put(hours, mins);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            mViewWeakReference.get().setAdapter(mMap, mClosestTime);
        }
    }
}
