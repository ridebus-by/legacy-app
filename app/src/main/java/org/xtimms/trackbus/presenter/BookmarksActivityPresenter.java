package org.xtimms.trackbus.presenter;

import android.os.AsyncTask;

import org.xtimms.trackbus.model.DatabaseObject;
import org.xtimms.trackbus.model.ModelFactory;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.model.Stop;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BookmarksActivityPresenter {
    private final View mView;

    public BookmarksActivityPresenter(View view) {
        mView = view;
    }

    public void setAdapter() {
        new DatabaseObjectsListAsyncTask(mView).execute();
    }

    public interface View {
        void setAdapter(List<DatabaseObject> databaseObjects);
    }

    private static class DatabaseObjectsListAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        private final WeakReference<View> activityWeakReference;
        private List<DatabaseObject> mDatabaseObjects;

        DatabaseObjectsListAsyncTask(View context) {
            super();
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            List<Route> routeList = ModelFactory.getModel().getAllRoutes();
            List<Stop> stopList = ModelFactory.getModel().getAllStops();
            mDatabaseObjects = new ArrayList<>(routeList);
            mDatabaseObjects.addAll(stopList);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            activityWeakReference.get().setAdapter(mDatabaseObjects);
        }
    }

}
