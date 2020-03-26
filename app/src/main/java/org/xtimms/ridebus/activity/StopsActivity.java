package org.xtimms.ridebus.activity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lucasurbas.listitemview.ListItemView;

import org.xtimms.ridebus.Constant;
import org.xtimms.ridebus.R;
import org.xtimms.ridebus.adapter.StopsActivityAdapter;
import org.xtimms.ridebus.model.Stop;
import org.xtimms.ridebus.object.StopActivityObject;
import org.xtimms.ridebus.presenter.StopActivityPresenter;
import org.xtimms.ridebus.util.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StopsActivity extends AppBaseActivity implements StopActivityPresenter.View {
    private static final String EXTRA_STOP = StopsActivity.class.getSimpleName();
    private Stop mStop;
    private ListItemView mWeekDay;
    private RecyclerView mRecyclerView;
    private boolean mAdapterIsSet = false;
    private StopsActivityAdapter mStopsActivityAdapter;
    private BroadcastReceiver mBroadcastReceiver;

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

        int resId = R.anim.layout_animation_fall_down;

        mWeekDay = findViewById(R.id.weekday);
        mWeekDay.setTitle(DateTime.getCurrentDate());

        LayoutAnimationController animation = android.view.animation.AnimationUtils.loadLayoutAnimation(getBaseContext(), resId);
        mRecyclerView = findViewById(R.id.recyclerview_stops_activity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutAnimation(animation);
        mRecyclerView.scheduleLayoutAnimation();

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);

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

        mWeekDay.setOnClickListener(v -> {
            Calendar mcurrentDate = Calendar.getInstance();
            int year = mcurrentDate.get(Calendar.YEAR);
            int month = mcurrentDate.get(Calendar.MONTH);
            int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(StopsActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year1, monthOfYear, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM yyyy", Locale.getDefault());
                mWeekDay.setTitle(sdf.format(newDate.getTime()));
                dateTimeChange();
            }, year, month, day);
            datePickerDialog.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_error, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.report_problem:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(Constant.EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_report_intent));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(StopsActivity.this, getString(R.string.about_not_found_email), Toast.LENGTH_SHORT).show();
                }
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
        StopActivityPresenter presenter = new StopActivityPresenter(
                this, DateTime.getCurrentTime(), mWeekDay.getTitle(), mStop.getId());
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
