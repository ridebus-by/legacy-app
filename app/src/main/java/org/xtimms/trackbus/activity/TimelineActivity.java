package org.xtimms.trackbus.activity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import org.joda.time.LocalDate;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.adapter.TimelineAdapter;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.object.StopsActivityTimeLineObject;
import org.xtimms.trackbus.presenter.TimeLineActivityPresenter;
import org.xtimms.trackbus.util.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.galgtonold.jollydayandroid.Holiday;
import de.galgtonold.jollydayandroid.HolidayCalendar;
import de.galgtonold.jollydayandroid.HolidayManager;

public class TimelineActivity extends AppBaseActivity implements TimeLineActivityPresenter.View {

    public static final String EXTRA_ROUTE = TimelineActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TextView mWeekDay;
    private Route mRoute;
    private Toolbar toolbar;
    private ProgressBar mProgressBar;
    private boolean mAdapterIsSet = false;
    private TimelineAdapter mTimeLineAdapter;
    private BroadcastReceiver mBroadcastReceiver;

    public static Intent newIntent(Context context, Route route) {
        Intent intent = new Intent(context, TimelineActivity.class);
        intent.putExtra(EXTRA_ROUTE, route);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mRoute = (Route) getIntent().getSerializableExtra(TimelineActivity.EXTRA_ROUTE);

        mWeekDay = findViewById(R.id.text_date_timeline);
        mWeekDay.setText(DateTime.getCurrentDate());

        mProgressBar = findViewById(R.id.progressBar_activityTimeline);
        mProgressBar.setVisibility(View.VISIBLE);

        mRecyclerView = findViewById(R.id.recyclerview_timeline);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        getRecyclerViewData();

        toolbar = findViewById(R.id.toolbar_timeline_activity);
        toolbar.inflateMenu(R.menu.activity_stops_menu);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView textTitle = findViewById(R.id.text_title_activitytimeline);
        TextView textSubtitle = findViewById(R.id.text_subtitle_activitytimeline);

        textTitle.setText(getResources().getString(R.string.number) + mRoute.getRouteNumber());
        textSubtitle.setText(mRoute.getRouteTitle());

        boolean firstLoad = getSharedPreferences("PREFERENCE_TIMELINE", MODE_PRIVATE)
                .getBoolean("firstLoad", true);

        if (firstLoad) {

            initTapTargetView();

            getSharedPreferences("PREFERENCE_TIMELINE", MODE_PRIVATE).edit().putBoolean("firstLoad", false).apply();

        }
    }

    private void initTapTargetView() {
        TapTargetView.showFor(this, TapTarget.forToolbarMenuItem(toolbar, R.id.calendar, "Посмотреть расписание рабочего или выходного дня стало проще", "Нажмите на иконку и выберите день недели.")
                .cancelable(true), new TapTargetView.Listener() {

            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetView", "You dismissed me :(");
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_timeline_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        int x = item.getItemId();
        switch (x) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.calendar:
                Calendar mcurrentDate = Calendar.getInstance();
                int year = mcurrentDate.get(Calendar.YEAR);
                int month = mcurrentDate.get(Calendar.MONTH);
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TimelineActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year1, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
                    mWeekDay.setText(sdf.format(newDate.getTime()));
                    dateTimeChange();
                }, year, month, day);
                datePickerDialog.show();
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
    public void setAdapter(List<StopsActivityTimeLineObject> stopsActivityTimeLineObjects, String currentTime) {

        if (!mAdapterIsSet) {
            mProgressBar.setVisibility(View.INVISIBLE);
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

