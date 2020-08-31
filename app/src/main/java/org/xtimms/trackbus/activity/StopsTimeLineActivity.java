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
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import org.joda.time.LocalDate;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.adapter.TimeLineAdapter;
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

public class StopsTimeLineActivity extends AppBaseActivity implements TimeLineActivityPresenter.View {
    //public static final String TAG = StopsTimeLineActivity.class.getSimpleName() + "TAG";
    public static final String EXTRA_ROUTE = StopsTimeLineActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    //private TextView mWeekDay;
    //private TextView mTimeText;
    private Route mRoute;
    private Toolbar toolbar;
    private boolean mAdapterIsSet = false;
    private TimeLineAdapter mTimeLineAdapter;
    private BroadcastReceiver mBroadcastReceiver;

    public static Intent newIntent(Context context, Route route) {
        Intent intent = new Intent(context, StopsTimeLineActivity.class);
        intent.putExtra(EXTRA_ROUTE, route);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mRoute = (Route) getIntent().getSerializableExtra(StopsTimeLineActivity.EXTRA_ROUTE);

        //mTimeText = findViewById(R.id.text_timeline_time_appbar);
        //mWeekDay = findViewById(R.id.text_timeline_weekday);
        //mWeekDay.setText(DateTime.getCurrentDate());

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
        //TextView textLargeNumber = findViewById(R.id.large_number);
        //TextView textFare = findViewById(R.id.price);
        //TextView textTraffic = findViewById(R.id.traffic);
        //TextView textWorkingTime = findViewById(R.id.work_time);
        //TextView textSmallClass = findViewById(R.id.little_class_label);
        //TextView textMiddleClass = findViewById(R.id.middle_class_label);
        //TextView textBigClass = findViewById(R.id.big_class_label);
        //TextView textVeryBigClass = findViewById(R.id.very_big_class_label);
        //ImageView holidayStar = findViewById(R.id.holidayStar);

        textTitle.setText(R.string.route_info);
        textSubtitle.setText(mRoute.getRouteTitle());
        //textLargeNumber.setText(mRoute.getNumber());
        //textFare.setText(mRoute.getFare());
        //textTraffic.setText(mRoute.getTraffic());
        //textWorkingTime.setText(mRoute.getWorkingTime());

        if (mRoute.getTransportClassId() == 1) {
            //textBigClass.setVisibility(View.VISIBLE);
            //textVeryBigClass.setVisibility(View.VISIBLE);
        } else if (mRoute.getTransportClassId() == 2) {
            //textSmallClass.setVisibility(View.VISIBLE);
            //textBigClass.setVisibility(View.VISIBLE);
            //textVeryBigClass.setVisibility(View.VISIBLE);
        } else if (mRoute.getTransportClassId() == 3) {
            //textBigClass.setVisibility(View.VISIBLE);
        } else if (mRoute.getTransportClassId() == 4) {
            //textMiddleClass.setVisibility(View.VISIBLE);
        } else if (mRoute.getTransportClassId() == 5) {
            //textSmallClass.setVisibility(View.VISIBLE);
        }  //textSmallClass.setVisibility(View.VISIBLE);
        //textBigClass.setVisibility(View.VISIBLE);


        HolidayManager m = HolidayManager.getInstance(HolidayCalendar.BELARUS);
        Set<Holiday> holidays = m.getHolidays(2019);

        for (Holiday holiday : holidays) {
            LocalDate.now();//holidayStar.setVisibility(View.VISIBLE);
        }

        //holidayStar.setOnClickListener(v -> {
        //    AlertDialog.Builder builder = new AlertDialog.Builder(StopsTimeLineActivity.this);
        //    builder.setTitle("Что означает эта иконка?")
        //            .setMessage("Она означает, что сегодня праздничный день. Вероятно, транспорт ходит по расписанию выходного дня. Попробуйте в приложении изменить день недели на субботу или воскресенье, чтобы получить расписание выходного дня.")
        //            .setIcon(R.drawable.ic_star_face_black_24dp)
        //            .setCancelable(false)
        //            .setNegativeButton("Понятно",
        //                    (dialog, id) -> dialog.cancel());
        //    AlertDialog alert = builder.create();
        //    alert.show();
        //});

//        mTimeText.setOnClickListener(v -> {
//            Calendar mcurrentTime = Calendar.getInstance();
//            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//            int minute = mcurrentTime.get(Calendar.MINUTE);
//            TimePickerDialog mTimePicker;
//            mTimePicker = new TimePickerDialog(StopsTimeLineActivity.this, (timePicker, selectedHour, selectedMinute) -> {
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

        //    DatePickerDialog datePickerDialog = new DatePickerDialog(StopsTimeLineActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
        //        Calendar newDate = Calendar.getInstance();
        //        newDate.set(year1, monthOfYear, dayOfMonth);
        //        //SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        //        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
        //        mWeekDay.setText(sdf.format(newDate.getTime()));
        //        dateTimeChange();
        //    }, year, month, day);
        //    datePickerDialog.show();
        //});

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
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.calendar:
                Calendar mcurrentDate = Calendar.getInstance();
                int year = mcurrentDate.get(Calendar.YEAR);
                int month = mcurrentDate.get(Calendar.MONTH);
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(StopsTimeLineActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year1, monthOfYear, dayOfMonth);
                    //SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
                    //mWeekDay.setText(sdf.format(newDate.getTime()));
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
        TimeLineActivityPresenter presenter = new TimeLineActivityPresenter(this,
                DateTime.getCurrentTime(), DateTime.getCurrentDate(), mRoute.getId());
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
            mTimeLineAdapter = new TimeLineAdapter(stopsActivityTimeLineObjects, currentTime);
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
