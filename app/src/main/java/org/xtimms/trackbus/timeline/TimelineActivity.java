package org.xtimms.trackbus.timeline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.AppBaseActivity;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.object.StopsActivityTimeLineObject;
import org.xtimms.trackbus.presenter.TimeLineActivityPresenter;
import org.xtimms.trackbus.timeline.info.Info;
import org.xtimms.trackbus.timeline.stops.Stops;
import org.xtimms.trackbus.util.DateTime;

import java.util.List;

public class TimelineActivity extends AppBaseActivity implements TimeLineActivityPresenter.View {

    public static final String EXTRA_ROUTE = TimelineActivity.class.getSimpleName();
    private BroadcastReceiver mBroadcastReceiver;

    private Route mRoute;

    public static Intent newIntent(Context context, Route route) {
        Intent intent = new Intent(context, TimelineActivity.class);
        intent.putExtra(EXTRA_ROUTE, route);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_newest);
        setSupportActionBar(R.id.toolbar);
        enableHomeAsUp();

        mRoute = (Route) getIntent().getSerializableExtra(TimelineActivity.EXTRA_ROUTE);

        TextView text = findViewById(R.id.text);
        text.setText("Маршрут");

        ViewPager mPager = findViewById(R.id.view_pager);
        TabLayout mTabs = findViewById(R.id.tab_layout);

        Stops stops;
        Info info;
        final PagesAdapter adapter = new PagesAdapter(
                info = new Info(mPager),
                stops = new Stops(mPager)
        );

        mPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mPager);

        info.updateContent(mRoute);
        stops.setData(mRoute);

    }

    @Override
    protected void onStart() {
        super.onStart();
        runTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runTimer();
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

    private void getRecyclerViewData() {
        TimeLineActivityPresenter presenter = new TimeLineActivityPresenter(this,
                DateTime.getCurrentTime(), DateTime.getCurrentDate(), mRoute.getId());
        presenter.setAdapter();
    }

    private void dateTimeChange() {
        getRecyclerViewData();
    }

    @Override
    public void setAdapter(List<StopsActivityTimeLineObject> stopsActivityTimeLineObjects, String currentTime) {

    }
}
