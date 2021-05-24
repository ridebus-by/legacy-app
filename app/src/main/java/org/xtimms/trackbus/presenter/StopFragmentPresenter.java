package org.xtimms.trackbus.presenter;

import android.os.AsyncTask;

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
    
    private static class GetBusStopsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<View> mFragmentWeakReference;
        private List<Stop> mStopList;
        private Exception mException;

        private GetBusStopsAsyncTask(View context) {
            super();
            mFragmentWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mStopList = ModelFactory.getModel().getAllStops();
            } catch (Exception e) {
                mException = e;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (mException == null) {
                mFragmentWeakReference.get().setAdapter((ArrayList<Stop>) mStopList);
            } else {
                return;
            }
        }
    }

}
