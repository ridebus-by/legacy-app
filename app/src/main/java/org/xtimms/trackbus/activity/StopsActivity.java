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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.adapter.StopsActivityAdapter;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.model.Stop;
import org.xtimms.trackbus.object.StopActivityObject;
import org.xtimms.trackbus.presenter.StopsActivityPresenter;
import org.xtimms.trackbus.util.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static org.xtimms.trackbus.R.*;

public class StopsActivity extends AppBaseActivity implements StopsActivityPresenter.View {
    private static final String EXTRA_STOP = StopsActivity.class.getSimpleName() + "_stopID";
    private static final String EXTRA_ROUTE = StopsActivity.class.getSimpleName() + "_routeID";
    private Stop mStop;
    private TextView mWeekDay;
    //private TextView mTimeText;
    private RecyclerView mRecyclerView;
    private boolean mAdapterIsSet = false;
    private StopsActivityAdapter mStopsActivityAdapter;
    private BroadcastReceiver mBroadcastReceiver;
    private ProgressBar mProgressBar;

    public static Intent newIntent(Context context, Stop stop) {
        Intent intent = new Intent(context, StopsActivity.class);
        intent.putExtra(EXTRA_STOP, stop);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_stops);

        mStop = (Stop) getIntent().getSerializableExtra(StopsActivity.EXTRA_STOP);
        Route mRoute = (Route) getIntent().getSerializableExtra(StopsActivity.EXTRA_ROUTE);

        SubtitleCollapsingToolbarLayout collapsingToolbarLayout = findViewById(id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mStop.getTitle());
        collapsingToolbarLayout.setSubtitle(mStop.getMark());

        TextView title = findViewById(id.title);
        TextView subtitle = findViewById(id.subtitle);

        title.setText(mStop.getTitle());
        subtitle.setText(mStop.getMark());

        //mTimeText = findViewById(R.id.text_time_activitystop);
        //mTimeText.setText(DateTime.getCurrentTime());

        mProgressBar = findViewById(id.progress);
        mProgressBar.setVisibility(View.VISIBLE);

        mWeekDay = findViewById(id.text_weekday_activity_stop);
        mWeekDay.setText(DateTime.getCurrentDate());

        mRecyclerView = findViewById(id.recyclerview_stops_activity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getRecyclerViewData();

        Toolbar toolbar = findViewById(id.toolbar_stop_activity);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.report, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        int x = item.getItemId();
        switch (x) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
            case id.calendar:
                Calendar mcurrentDate = Calendar.getInstance();
                int year = mcurrentDate.get(Calendar.YEAR);
                int month = mcurrentDate.get(Calendar.MONTH);
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(StopsActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year1, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
                    mWeekDay.setText(sdf.format(newDate.getTime()));
                    dateTimeChange();
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

    private void getRecyclerViewData() {
        StopsActivityPresenter presenter = new StopsActivityPresenter(
                this, DateTime.getCurrentTime(), mWeekDay.getText().toString(), mStop.getId());
        presenter.setAdapter();
    }

    private void dateTimeChange() {
        getRecyclerViewData();
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

    @Override
    public void setAdapter(ArrayList<StopActivityObject> stopActivityObjectList) {
        if (!mAdapterIsSet) {
            mProgressBar.setVisibility(View.GONE);
            mStopsActivityAdapter = new StopsActivityAdapter(stopActivityObjectList);
            mRecyclerView.setAdapter(mStopsActivityAdapter);
            mAdapterIsSet = true;
            mStopsActivityAdapter.notifyDataSetChanged();

            mStopsActivityAdapter.setOnItemClickListener((parent, v, position, id) -> {
                // Snackbar.make(v, stopActivityObjectList.get(position).getRoute().getRouteTitle(), Snackbar.LENGTH_SHORT).show();
                Intent intent = ScheduleActivity.newIntent(this,
                        stopActivityObjectList.get(position).getRoute(), mStop);
                startActivity(intent);
            });
        } else {
            mStopsActivityAdapter.dataChange(stopActivityObjectList);
        }

    }

}
