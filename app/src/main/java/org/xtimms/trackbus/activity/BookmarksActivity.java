package org.xtimms.trackbus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.adapter.BookmarksSearchAdapter;
import org.xtimms.trackbus.model.DatabaseObject;
import org.xtimms.trackbus.presenter.BookmarksActivityPresenter;

import java.util.ArrayList;

public class BookmarksActivity extends AppBaseActivity implements BookmarksActivityPresenter.View {
    //private final static String TAG = BookmarksActivity.class.getSimpleName();
    public final static String REQUEST_RESULT = "request_result";
    private BookmarksSearchAdapter mBookmarksSearchAdapter;
    private RecyclerView mRecyclerView;
    private boolean mAdapterIsSet = false;
    private ProgressBar mProgressBar;

    public static Intent newIntent(Context context) {
        return new Intent(context, BookmarksActivity.class);
    }

    @Override
    public void setAdapter(ArrayList<DatabaseObject> databaseObjects) {
        if (!mAdapterIsSet) {
            mProgressBar.setVisibility(View.GONE);
            mBookmarksSearchAdapter = new BookmarksSearchAdapter(databaseObjects);
            mRecyclerView.setAdapter(mBookmarksSearchAdapter);
            mBookmarksSearchAdapter.setOnItemClickListener((parent, v, position, id) -> {
                Intent intent = new Intent();
                Bundle bundleData = new Bundle();
                bundleData.putSerializable(REQUEST_RESULT, mBookmarksSearchAdapter.getStopsListFiltered().get(position));
                intent.putExtras(bundleData);
                setResult(RESULT_OK, intent);
                finish();
            });
        } else {
            mBookmarksSearchAdapter.dataChange(databaseObjects);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks_search);

        Toolbar toolbar = findViewById(R.id.toolbar_bookmarks_activity);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);

        mRecyclerView = findViewById(R.id.recyclerview_bookmarks_activity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookmarksActivityPresenter presenter = new BookmarksActivityPresenter(this);
        presenter.setAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView searchView = findViewById(R.id.search_view_bookmarks_activity);
        searchView.setQueryHint(getString(R.string.search_query_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mBookmarksSearchAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
