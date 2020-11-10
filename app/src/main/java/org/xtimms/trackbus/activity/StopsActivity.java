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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.adapter.StopsActivityAdapter;
import org.xtimms.trackbus.model.Stop;
import org.xtimms.trackbus.object.StopActivityObject;
import org.xtimms.trackbus.presenter.StopsActivityPresenter;
import org.xtimms.trackbus.util.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StopsActivity extends AppBaseActivity implements StopsActivityPresenter.View {
    private static final String EXTRA_STOP = StopsActivity.class.getSimpleName();
    private Stop mStop;
    private TextView mWeekDay;
    //private TextView mTimeText;
    private RecyclerView mRecyclerView;
    private boolean mAdapterIsSet = false;
    private StopsActivityAdapter mStopsActivityAdapter;
    private BroadcastReceiver mBroadcastReceiver;
    private ProgressBar mProgressBar;
    private FloatingActionButton floatingActionButton;

    public static Intent newIntent(Context context, Stop stop) {
        Intent intent = new Intent(context, StopsActivity.class);
        intent.putExtra(EXTRA_STOP, stop);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        mStop = (Stop) getIntent().getSerializableExtra(StopsActivity.EXTRA_STOP);

        //mTimeText = findViewById(R.id.text_time_activitystop);
        //mTimeText.setText(DateTime.getCurrentTime());

        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);

        mWeekDay = findViewById(R.id.text_weekday_activitystop);
        mWeekDay.setText(DateTime.getCurrentDate());

        mRecyclerView = findViewById(R.id.recyclerview_stops_activity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        floatingActionButton = findViewById(R.id.fab);

        getRecyclerViewData();

        Toolbar toolbar = findViewById(R.id.toolbar_stopactivity);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView textTitle = findViewById(R.id.text_title_stopactivity);
        textTitle.setText(mStop.getStopTitle());
        TextView subtitle = findViewById(R.id.subtitle_stopactivity);
        subtitle.setText(mStop.getMark());

//        mTimeText.setOnClickListener(v -> {
//            Calendar mcurrentTime = Calendar.getInstance();
//            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//            int minute = mcurrentTime.get(Calendar.MINUTE);
//            TimePickerDialog mTimePicker;
//            mTimePicker = new TimePickerDialog(StopsActivity.this, (timePicker, selectedHour, selectedMinute) -> {
//                String newHourses = (selectedHour < 10) ? "0" + selectedHour : String.valueOf(selectedHour);
//                String newMinutes = (selectedMinute < 10) ? "0" + selectedMinute : String.valueOf(selectedMinute);
//                String newTime = newHourses + ":" + newMinutes;
//                mTimeText.setText(newTime);
//                dateTimeChange();
//            }, hour, minute, true);//Yes 24 hour time
//            mTimePicker.setTitle(getString(R.string.time_picker_title));
//            mTimePicker.show();
//
//        });
//
//        mTimeText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                dateTimeChange();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        //mWeekDay.setOnClickListener(v -> {
        //    Calendar mcurrentDate = Calendar.getInstance();
        //    int year = mcurrentDate.get(Calendar.YEAR);
        //    int month = mcurrentDate.get(Calendar.MONTH);
        //    int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        //    DatePickerDialog datePickerDialog = new DatePickerDialog(StopsActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
        //        Calendar newDate = Calendar.getInstance();
        //        newDate.set(year1, monthOfYear, dayOfMonth);
        //        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        //        //SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        //        mWeekDay.setText(sdf.format(newDate.getTime()));
        //        dateTimeChange();
        //    }, year, month, day);
        //    datePickerDialog.show();
        //});

        boolean firstLoad = getSharedPreferences("prefs", MODE_PRIVATE)
                .getBoolean("firstLoadStops", true);

        if (firstLoad) {

            initTapTargetView();

            getSharedPreferences("prefs", MODE_PRIVATE).edit().putBoolean("firstLoadStops", false).apply();

        } else {
            floatingActionButton.hide();
        }

    }

    private char[] convertToIndexList(List<Stop> list) {
        char[] chars = list.get(0).getStopTitle().toUpperCase().toCharArray();
        return chars;
    }

    private void initTapTargetView() {
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.fab), "Тут показывается транспорт, который проезжает данную остановку", "Также, здесь тоже был изменён интерфейс.")
                .cancelable(true)
                .drawShadow(true)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                floatingActionButton.hide();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetView", "You dismissed me :(");
                floatingActionButton.hide();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_stops_menu, menu);
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
    public void setAdapter(List<StopActivityObject> stopActivityObjectList) {
        if (!mAdapterIsSet) {
            mProgressBar.setVisibility(View.GONE);
            mStopsActivityAdapter = new StopsActivityAdapter(stopActivityObjectList);
            mRecyclerView.setAdapter(mStopsActivityAdapter);
            mAdapterIsSet = true;

            mStopsActivityAdapter.setOnItemClickListener((parent, v, position, id) -> {
                //Snackbar.make(v, mStopActivityObjectList.get(position).getRoute().getRouteTitle(), Snackbar.LENGTH_SHORT).show();
                Intent intent = ScheduleActivity.newIntent(this,
                        stopActivityObjectList.get(position).getRoute(), mStop);
                startActivity(intent);
            });
        } else {
            mStopsActivityAdapter.dataChange(stopActivityObjectList);
        }

    }

}
