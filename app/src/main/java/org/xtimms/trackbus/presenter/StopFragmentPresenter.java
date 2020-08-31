package org.xtimms.trackbus.presenter;

import android.os.AsyncTask;

import org.xtimms.trackbus.model.ModelFactory;
import org.xtimms.trackbus.model.Stop;

import java.lang.ref.WeakReference;
import java.util.List;

public class StopFragmentPresenter {
    private final View mView;

    public StopFragmentPresenter(View view) {
        mView = view;
    }

    public void setAdapter() {
        new GetBusStopsAsyncTask(mView).execute();
    }

    public interface View {
        void setAdapter(List<Stop> stopList);
    }

    private static class GetBusStopsAsyncTask extends AsyncTask<Integer, Void, Void> {
        private final WeakReference<View> mFragmentWeakReference;
        private List<Stop> mStopList;

        private GetBusStopsAsyncTask(View context) {
            super();
            mFragmentWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mStopList = ModelFactory.getModel().getAllStops();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            mFragmentWeakReference.get().setAdapter(mStopList);
        }
    }

}
