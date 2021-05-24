package org.xtimms.trackbus.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.adapter.ScheduleAdapter;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.model.Stop;
import org.xtimms.trackbus.presenter.ScheduleActivityPresenter;
import org.xtimms.trackbus.util.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.xtimms.trackbus.R.*;

public class ScheduleActivity extends AppBaseActivity implements ScheduleActivityPresenter.View {
    public static final String EXTRA_SCHEDULE_STOP = ScheduleActivity.class.getSimpleName() + "stop_id";
    private static final String EXTRA_SCHEDULE_ROUTE = ScheduleActivity.class.getSimpleName() + "route_id";
    private Route mRoute;
    private Stop mStop;

    private RecyclerView mRecyclerView;
    private TextView mTextDate;
    private ProgressBar mProgressBar;
    private BroadcastReceiver mBroadcastReceiver;

    public static Intent newIntent(Context context, Route route, Stop stop) {
        Intent intent = new Intent(context, ScheduleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_SCHEDULE_ROUTE, route);
        bundle.putSerializable(EXTRA_SCHEDULE_STOP, stop);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mStop = (Stop) bundle.getSerializable(ScheduleActivity.EXTRA_SCHEDULE_STOP);
            mRoute = (Route) bundle.getSerializable(ScheduleActivity.EXTRA_SCHEDULE_ROUTE);
        }

        setContentView(layout.activity_schedule);
        Toolbar toolbar = findViewById(id.toolbar_schedule_activity);
        setSupportActionBar(toolbar);

        mProgressBar = findViewById(id.progress);
        mProgressBar.setVisibility(View.VISIBLE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView routeNumber = findViewById(id.text_route_number_schedule_activity);
        TextView routeTitle = findViewById(id.text_route_title_schedule_activity);
        TextView stopTitle = findViewById(id.text_stop_title_schedule_activity);
        TextView stopMark = findViewById(id.text_stop_mark_schedule_activity);
        mTextDate = findViewById(id.text_date_schedule_activity);

        routeNumber.setText(mRoute.getRouteNumber());
        routeTitle.setText(mRoute.getRouteTitle());
        stopTitle.setText(mStop.getStopTitle());
        stopMark.setText(mStop.getMark());
        mTextDate.setText(DateTime.getCurrentDate());

        mRecyclerView = findViewById(id.recyclerview_schedule_activity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        String currentDate = mTextDate.getText().toString();
        ScheduleActivityPresenter presenter = new ScheduleActivityPresenter(this, mStop.getId(), mRoute.getId(), currentDate);
        presenter.setAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_schedule_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int x = item.getItemId();
        switch (x) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case id.calendar:
                Calendar mcurrentDate = Calendar.getInstance();
                int year = mcurrentDate.get(Calendar.YEAR);
                int month = mcurrentDate.get(Calendar.MONTH);
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleActivity.this, style.Theme_AlertDialog, (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year1, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
                    mTextDate.setText(sdf.format(newDate.getTime()));

                    String currentDate = mTextDate.getText().toString();
                    ScheduleActivityPresenter presenter = new ScheduleActivityPresenter(this, mStop.getId(), mRoute.getId(), currentDate);
                    presenter.dateChange();
                }, year, month, day);
                datePickerDialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void setAdapter(Map<String, List<String>> scheduleMap, String closestTime) {
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(scheduleMap, closestTime);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setAdapter(scheduleAdapter);
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
        String currentDate = mTextDate.getText().toString();
        ScheduleActivityPresenter presenter = new ScheduleActivityPresenter(this, mStop.getId(), mRoute.getId(), currentDate);
        presenter.setAdapter();
    }

    private void dateTimeChange() {
        getRecyclerViewData();
    }

}
