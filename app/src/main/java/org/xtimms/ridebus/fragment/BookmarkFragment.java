package org.xtimms.ridebus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.ridebus.R;
import org.xtimms.ridebus.activity.BookmarkActivity;
import org.xtimms.ridebus.activity.StopsActivity;
import org.xtimms.ridebus.activity.StopsTimeLineActivity;
import org.xtimms.ridebus.adapter.BookmarkAdapter;
import org.xtimms.ridebus.model.DatabaseObject;
import org.xtimms.ridebus.model.Route;
import org.xtimms.ridebus.model.Stop;
import org.xtimms.ridebus.presenter.BookmarkFragmentPresenter;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class BookmarkFragment extends Fragment {
    private final static int SEARCH_ACTIVITY_REQUEST = 1;
    private final static String BOOKMARKS_PREFERENCES = "bookmarksPreferences";
    private List<DatabaseObject> mBookmarks;
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
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
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
                Intent intent = BookmarkActivity.newIntent(getContext());
                startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST);
            } else {
                if (object instanceof Route) {
                    Route route = (Route) object;
                    Intent intent = StopsTimeLineActivity.newIntent(getActivity(), route);
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
                Intent intent = BookmarkActivity.newIntent(getContext());
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
                    DatabaseObject message = (DatabaseObject) bundle.getSerializable(BookmarkActivity.REQUEST_RESULT);
                    mBookmarkAdapter.changeBookmarks(mPosition, message);
                }

            }
        }
    }
}
