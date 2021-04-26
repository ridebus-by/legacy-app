package org.xtimms.trackbus.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.adapter.TimelineAdapter;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.object.StopsActivityTimeLineObject;
import org.xtimms.trackbus.presenter.TimeLineActivityPresenter;
import org.xtimms.trackbus.util.ConstantUtils;
import org.xtimms.trackbus.util.DateTime;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppBaseActivity implements TimeLineActivityPresenter.View {

    public static final String EXTRA_ROUTE = TimelineActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TextView mWeekDay;
    private Route mRoute;
    private ProgressBar mProgressBar;
    private boolean mAdapterIsSet = false;
    private TimelineAdapter mTimeLineAdapter;
    private BroadcastReceiver mBroadcastReceiver;

    public static Intent newIntent(Context context, Route route) {
        Intent intent = new Intent(context, TimelineActivity.class);
        intent.putExtra(EXTRA_ROUTE, route);
        return intent;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mRoute = (Route) getIntent().getSerializableExtra(TimelineActivity.EXTRA_ROUTE);

        mWeekDay = findViewById(R.id.text_date_timeline);
        mWeekDay.setText(DateTime.getCurrentDate());

        mProgressBar = findViewById(R.id.progressBar_activityTimeline);
        mProgressBar.setVisibility(View.VISIBLE);

        SubtitleCollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.number) + mRoute.getRouteNumber());
        collapsingToolbarLayout.setSubtitle(mRoute.getRouteTitle());

        TextView title = findViewById(R.id.title);
        TextView subtitle = findViewById(R.id.subtitle);

        title.setText(getResources().getString(R.string.number) + mRoute.getRouteNumber());
        subtitle.setText(mRoute.getRouteTitle());

        mRecyclerView = findViewById(R.id.recyclerview_timeline);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        getRecyclerViewData();

        Toolbar toolbar = findViewById(R.id.toolbar_timeline_activity);
        toolbar.inflateMenu(R.menu.report);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        TextView textNumber = findViewById(R.id.number);
        textNumber.setText(mRoute.getRouteNumber());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.report, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        //Menu
        int x = item.getItemId();
        switch (x) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.report:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(ConstantUtils.EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_report_intent));
                //intent.putExtra(Intent.EXTRA_TEXT, "Hi,");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(TimelineActivity.this, getString(R.string.about_not_found_email), Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        runTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
    }

    private void getRecyclerViewData() {
        TimeLineActivityPresenter presenter = new TimeLineActivityPresenter(this,
                DateTime.getCurrentTime(), mWeekDay.getText().toString(), mRoute.getId());
        presenter.setAdapter();
    }

    private void runTimer() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null) {
                    if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                        dateTimeChange();
                    }
                }
            }
        };
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    private void stopTimer() {
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }

    private void dateTimeChange() {
        getRecyclerViewData();
    }

    @Override
    public void setAdapter(ArrayList<StopsActivityTimeLineObject> stopsActivityTimeLineObjects, String currentTime) {

        if (!mAdapterIsSet) {
            mProgressBar.setVisibility(View.GONE);
            mTimeLineAdapter = new TimelineAdapter(stopsActivityTimeLineObjects, currentTime);
            mRecyclerView.setAdapter(mTimeLineAdapter);
            mAdapterIsSet = true;

            mTimeLineAdapter.setOnItemClickListener((parent, view, position, id) -> {
                //Snackbar.make(view, mStopsActivityTimeLineObjects.get(position).getStop().getStopTitle(), Snackbar.LENGTH_SHORT).show();
                Intent intent = ScheduleActivity.newIntent(this, mRoute,
                        stopsActivityTimeLineObjects.get(position).getStop());
                startActivity(intent);
            });
        } else {
            mTimeLineAdapter.dataChange(stopsActivityTimeLineObjects, currentTime);
        }

    }

}

