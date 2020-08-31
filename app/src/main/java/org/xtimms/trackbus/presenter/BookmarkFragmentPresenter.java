package org.xtimms.trackbus.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xtimms.trackbus.Constant;
import org.xtimms.trackbus.fragment.BookmarkFragment;
import org.xtimms.trackbus.model.DatabaseObject;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.model.Stop;
import org.xtimms.trackbus.util.SharePreferenceObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkFragmentPresenter {

    private final String BOOKMARKS_PREFERENCES;

    private final BookmarkFragment mView;

    public BookmarkFragmentPresenter(BookmarkFragment view, String BOOKMARKS_PREFERENCES) {
        this.BOOKMARKS_PREFERENCES = BOOKMARKS_PREFERENCES;
        mView = view;
    }

    public void saveBookmarks(List<DatabaseObject> databaseObjects) {
        List<SharePreferenceObject> sharePreferenceObjectList = new ArrayList<>();

        for (DatabaseObject object : databaseObjects) {

            if (object instanceof Stop) {
                Stop stop = (Stop) object;
                sharePreferenceObjectList.add(new SharePreferenceObject(stop));
            }

            if (object instanceof Route) {
                Route route = (Route) object;
                sharePreferenceObjectList.add(new SharePreferenceObject(route));
            }
        }

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mView.getContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        Type type = new TypeToken<List<SharePreferenceObject>>() {
        }.getType();

        String json = new Gson().toJson(sharePreferenceObjectList, type);

        prefsEditor.putString(BOOKMARKS_PREFERENCES, json);
        prefsEditor.apply();
    }

    public List<DatabaseObject> loadBookmarks() {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mView.getContext());

        String json = appSharedPrefs.getString(BOOKMARKS_PREFERENCES, Constant.EMPTY_STRING);

        if (!json.isEmpty()) {
            Type type = new TypeToken<List<SharePreferenceObject>>() {
            }.getType();
            List<SharePreferenceObject> sharePreferenceObjectList = new Gson().fromJson(json, type);
            List<DatabaseObject> bookmarks = new ArrayList<>();

            for (SharePreferenceObject object : sharePreferenceObjectList) {
                Stop stop = object.getStop();
                Route route = object.getRoute();

                if (stop != null) bookmarks.add(stop);
                if (route != null) bookmarks.add(route);
            }

            return bookmarks;
        }

        return new ArrayList<>(getEmptyObjects());
    }

    private List<Stop> getEmptyObjects() {
        List<Stop> stops = new ArrayList<>();

        for (int i = 0; i < Constant.NUMBER_BOOKMARKS; i++) {
            stops.add(new Stop());
        }

        return stops;
    }

}
