package org.xtimms.trackbus.presenter;

import android.os.AsyncTask;

import org.xtimms.trackbus.model.ModelFactory;
import org.xtimms.trackbus.model.Route;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ExpressFragmentPresenter {
    private final View mView;

    public ExpressFragmentPresenter(View view) {
        mView = view;
    }

    public void setAdapter() {
        new GetSuburbAsyncTask(mView).execute();
    }

    public interface View {
        void setAdapter(ArrayList<Route> routeList);
    }

    public static class GetSuburbAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<View> mFragmentWeakReference;
        private List<Route> mRouteList;

        private GetSuburbAsyncTask(View context) {
            super();
            mFragmentWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mRouteList = ModelFactory.getModel().getAllRoutesExpress();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            mFragmentWeakReference.get().setAdapter((ArrayList<Route>) mRouteList);
        }
    }
}
