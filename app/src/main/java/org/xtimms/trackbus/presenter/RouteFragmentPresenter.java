package org.xtimms.trackbus.presenter;

import android.os.AsyncTask;

import org.xtimms.trackbus.model.ModelFactory;
import org.xtimms.trackbus.model.Route;

import java.lang.ref.WeakReference;
import java.util.List;

public class RouteFragmentPresenter {
    private final View mView;

    public RouteFragmentPresenter(View view) {
        mView = view;
    }

    public void setAdapter() {
        new GetRoutesAsyncTask(mView).execute();
    }

    public interface View {
        void setAdapter(List<Route> routeList);
    }

    public static class GetRoutesAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<View> mFragmentWeakReference;
        private List<Route> mRouteList;

        private GetRoutesAsyncTask(View context) {
            super();
            mFragmentWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mRouteList = ModelFactory.getModel().getAllRoutesBus();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            mFragmentWeakReference.get().setAdapter(mRouteList);
        }
    }
}
