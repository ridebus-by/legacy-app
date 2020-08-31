package org.xtimms.trackbus.presenter;

import android.os.AsyncTask;

import org.xtimms.trackbus.model.ModelFactory;
import org.xtimms.trackbus.model.Route;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BottomSheetPresenter {
    private final View mView;
    private final String mRouteNumber;

    public BottomSheetPresenter(View view, String routeNumber) {
        mView = view;
        mRouteNumber = routeNumber;
    }

    public void setAdapter() {
        new GetRoutesAsyncTask(mView).execute(mRouteNumber);
    }

    public interface View {
        void setAdapter(List<Route> routes);
    }

    private static class GetRoutesAsyncTask extends AsyncTask<String, Void, Void> {
        private final WeakReference<View> mBottomSheetFragmentWeakReference;
        private final List<Route> mRoutesBottomSheet = new ArrayList<>();

        private GetRoutesAsyncTask(View context) {
            super();
            mBottomSheetFragmentWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            String routeNumber = params[0];
            List<Route> routeList;

            routeList = ModelFactory.getModel().getAllRoutesBus();

            for (Route route : routeList) {
                if (route.getRouteNumber().equals(routeNumber)) {
                    mRoutesBottomSheet.add(route);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            mBottomSheetFragmentWeakReference.get().setAdapter(mRoutesBottomSheet);
        }
    }
}
