package org.xtimms.trackbus.activity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

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

public class ScheduleActivity extends AppBaseActivity implements ScheduleActivityPresenter.View {
    public static final String EXTRA_SCHEDULE_STOP = ScheduleActivity.class.getSimpleName() + "stop_id";
    private static final String EXTRA_SCHEDULE_ROUTE = ScheduleActivity.class.getSimpleName() + "route_id";
    private Route mRoute;
    private Stop mStop;

    private TextView mTextDate;
    private RecyclerView mRecyclerView;
    private ShareActionProvider mShareActionProvider;
    private boolean mShareProvIsNull = false;
    private BroadcastReceiver mBroadcastReceiver;
    private TextView mWeekDay;
    //private String mCurrentPhotoPath;


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

        setContentView(R.layout.activity_schedule);
        AppBarLayout mAppBarLayout = findViewById(R.id.app_bar_schedule_activity);
        Toolbar toolbar = findViewById(R.id.toolbar_schedule_activity);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setTitle(mStop.getStopTitle());
        }

        TextView routeNumber = findViewById(R.id.text_routenumber_scheduleactivity);
        TextView routeTitle = findViewById(R.id.text_routetitle_scheduleactivity);
        TextView stopTitle = findViewById(R.id.text_stoptitle_scheduleactivity);
        TextView stopMark = findViewById(R.id.text_stopmark_scheduleactivity);
        mTextDate = findViewById(R.id.text_date_scheduleactivity);

        routeNumber.setText(mRoute.getRouteNumber());
        routeTitle.setText(mRoute.getRouteTitle());
        stopTitle.setText(mStop.getStopTitle());
        stopMark.setText(mStop.getMark());
        mTextDate.setText(DateTime.getCurrentDate());

        //mTextDate.setOnClickListener(v -> {
        //    Calendar mcurrentDate = Calendar.getInstance();
        //    int year = mcurrentDate.get(Calendar.YEAR);
        //    int month = mcurrentDate.get(Calendar.MONTH);
        //    int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        //    DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
        //        Calendar newDate = Calendar.getInstance();
        //        newDate.set(year1, monthOfYear, dayOfMonth);
        //        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        //        //SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        //        mTextDate.setText(sdf.format(newDate.getTime()));

        //        String currentDate = mTextDate.getText().toString();
        //        ScheduleActivityPresenter presenter = new ScheduleActivityPresenter(this, mStop.getId(), mRoute.getId(), currentDate);
        //        presenter.dateChange();
        //    }, year, month, day);
        //    datePickerDialog.show();
        //});

        mRecyclerView = findViewById(R.id.recyclerview_schedule_activity);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.calendar:
                Calendar mcurrentDate = Calendar.getInstance();
                int year = mcurrentDate.get(Calendar.YEAR);
                int month = mcurrentDate.get(Calendar.MONTH);
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year1, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
                    //SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
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
        mRecyclerView.setAdapter(scheduleAdapter);
        scheduleAdapter.setOnItemClickListener((parent, v, position, id) -> {
            //Snackbar.make(v, "click", Snackbar.LENGTH_SHORT).show();
//            Intent intent = ScheduleActivity.newIntent(this,
//                    mStopActivityObjectList.get(position).getRoute(), mStop);
//            startActivity(intent);
        });

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
