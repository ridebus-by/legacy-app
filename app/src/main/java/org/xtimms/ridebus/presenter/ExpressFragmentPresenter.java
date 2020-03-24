package org.xtimms.ridebus.presenter;

import android.os.AsyncTask;

import org.xtimms.ridebus.model.ModelFactory;
import org.xtimms.ridebus.model.Route;

import java.lang.ref.WeakReference;
import java.util.List;

public class ExpressFragmentPresenter {
    private View mView;

    public ExpressFragmentPresenter(View view) {
        mView = view;
    }

    public void setAdapter() {
        new GetSuburbAsyncTask(mView).execute();
    }

    public interface View {
        void setAdapter(List<Route> routeList);
    }

    public static class GetSuburbAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<View> mFragmentWeakReference;
        private List<Route> mRouteList;

        private GetSuburbAsyncTask(View context) {
            super();
            mFragmentWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mRouteList = ModelFactory.getModel().getAllRoutesExpress();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            mFragmentWeakReference.get().setAdapter(mRouteList);
        }
    }
}
