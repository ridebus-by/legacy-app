package org.xtimms.ridebus.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lucasurbas.listitemview.ListItemView;

import org.xtimms.ridebus.Constant;
import org.xtimms.ridebus.R;
import org.xtimms.ridebus.adapter.TimeLineAdapter;
import org.xtimms.ridebus.model.Route;
import org.xtimms.ridebus.object.StopsActivityTimeLineObject;
import org.xtimms.ridebus.presenter.TimeLineActivityPresenter;
import org.xtimms.ridebus.util.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class StopsTimeLineActivity extends AppBaseActivity implements TimeLineActivityPresenter.View {

    public static final String EXTRA_ROUTE = StopsTimeLineActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ListItemView mWeekDay;
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

        mWeekDay = findViewById(R.id.weekday);
        mWeekDay.setTitle(DateTime.getCurrentDate());

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
        ImageView holidayStar = findViewById(R.id.star);

        textTitle.setText(getString(R.string.number_route) + mRoute.getNumber());
        textSubtitle.setText(mRoute.getRouteTitle());

        holidayStar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(StopsTimeLineActivity.this);
            builder.setTitle(R.string.star_desc)
                    .setMessage(R.string.star_desc_full)
                    .setIcon(R.drawable.ic_star_face_black_24dp)
                    .setCancelable(false)
                    .setNegativeButton(R.string.ok,
                            (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });

        mWeekDay.setOnClickListener(v -> {
            Calendar mcurrentDate = Calendar.getInstance();
            int year = mcurrentDate.get(Calendar.YEAR);
            int month = mcurrentDate.get(Calendar.MONTH);
            int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(StopsTimeLineActivity.this, (view, year1, monthOfYear, dayOfMonth) -> {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year1, monthOfYear, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
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
        switch (item.getItemId()) {
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
                    Toast.makeText(StopsTimeLineActivity.this, getString(R.string.about_not_found_email), Toast.LENGTH_SHORT).show();
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
        TimeLineActivityPresenter presenter = new TimeLineActivityPresenter(this,
                DateTime.getCurrentTime(), mWeekDay.getTitle(), mRoute.getId());
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
                Intent intent = ScheduleActivity.newIntent(this, mRoute,
                        stopsActivityTimeLineObjects.get(position).getStop());
                startActivity(intent);
            });
        } else {
            mTimeLineAdapter.dataChange(stopsActivityTimeLineObjects, currentTime);
        }

    }

}
