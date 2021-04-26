package org.xtimms.trackbus.presenter;

import android.os.AsyncTask;
import android.util.Log;

import org.xtimms.trackbus.model.ModelFactory;
import org.xtimms.trackbus.model.Stop;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
        void setAdapter(ArrayList<Stop> stopList);
    }


    private static class GetBusStopsAsyncTask extends AsyncTask<Void, Void, String> {
        private final WeakReference<View> mFragmentWeakReference;
        private List<Stop> mStopList;

        private GetBusStopsAsyncTask(View context) {
            super();
            mFragmentWeakReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            mStopList = ModelFactory.getModel().getAllStops();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d("ERROR", "OnPostExecute not working...");
                return;
            } else {
                mFragmentWeakReference.get().setAdapter((ArrayList<Stop>) mStopList);
                Log.d("SUCCESS", "Yay! Adapter working!");
            }
        }
    }

}
