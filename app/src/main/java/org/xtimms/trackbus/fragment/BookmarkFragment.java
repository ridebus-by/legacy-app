package org.xtimms.trackbus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.App;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.BookmarksActivity;
import org.xtimms.trackbus.activity.StopsActivity;
import org.xtimms.trackbus.activity.TimelineActivity;
import org.xtimms.trackbus.adapter.BookmarkAdapter;
import org.xtimms.trackbus.model.DatabaseObject;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.model.Stop;
import org.xtimms.trackbus.presenter.BookmarkFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class BookmarkFragment extends AppBaseFragment {
    private final static int SEARCH_ACTIVITY_REQUEST = 1;
    private final static String BOOKMARKS_PREFERENCES = "bookmarksPreferences";
    private ArrayList<DatabaseObject> mBookmarks;
    private BookmarkAdapter mBookmarkAdapter;
    private int mPosition;
    private BookmarkFragmentPresenter mPresenter;

    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = new BookmarkFragmentPresenter(this, BOOKMARKS_PREFERENCES);
        mBookmarks = mPresenter.loadBookmarks();
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_favorite);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mBookmarkAdapter = new BookmarkAdapter(mBookmarks);
        mBookmarkAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mBookmarkAdapter);
        recyclerView.invalidate();
        mBookmarkAdapter.setOnItemClickListener((parent, v, position, id) -> {
            mPosition = position;
            DatabaseObject object = mBookmarkAdapter.getBookmarks().get(position);

            if (object.isEmpty()) {
                Intent intent = BookmarksActivity.newIntent(App.getInstance().getAppContext());
                startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST);
            } else {
                if (object instanceof Route) {
                    Route route = (Route) object;
                    Intent intent = TimelineActivity.newIntent(getActivity(), route);
                    startActivity(intent);
                }

                if (object instanceof Stop) {
                    Stop stop = (Stop) object;
                    Intent intent = StopsActivity.newIntent(getActivity(), stop);
                    startActivity(intent);
                }
            }

        });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = mBookmarkAdapter.getPosition();

        DatabaseObject object = mBookmarkAdapter.getBookmarks().get(position);

        if (object.isEmpty()) return false;

        switch (item.getItemId()) {
            case R.id.bookmarks_context_menu_change:
                mPosition = position;
                Intent intent = BookmarksActivity.newIntent(App.getInstance().getAppContext());
                startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST);
                return true;
            case R.id.bookmarks_context_menu_delete:
                mBookmarkAdapter.changeBookmarks(position, new Stop());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        mPresenter.saveBookmarks(mBookmarkAdapter.getBookmarks());
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEARCH_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    DatabaseObject message = (DatabaseObject) bundle.getSerializable(BookmarksActivity.REQUEST_RESULT);
                    mBookmarkAdapter.changeBookmarks(mPosition, message);
                }

            }
        }
    }
}
