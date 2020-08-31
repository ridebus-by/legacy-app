package org.xtimms.trackbus.timeline.stops;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.ScheduleActivity;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.object.StopsActivityTimeLineObject;
import org.xtimms.trackbus.presenter.TimeLineActivityPresenter;
import org.xtimms.trackbus.timeline.PageHolder;
import org.xtimms.trackbus.util.DateTime;

import java.util.List;

public class Stops extends PageHolder implements TimeLineActivityPresenter.View {

    private RecyclerView mRecyclerView;
    private Route mRoute;
    private boolean mAdapterIsSet = false;
    private StopsAdapter mTimeLineAdapter;

    public Stops(@NonNull ViewGroup parent) {
        super(parent, R.layout.fragment_timeline_stops);
    }

    @Override
    protected void onViewCreated(@NonNull View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
    }

    public void setData(Route route) {
        TimeLineActivityPresenter presenter = new TimeLineActivityPresenter(this,
                DateTime.getCurrentTime(), DateTime.getCurrentDate(), route.getId());
        presenter.setAdapter();
    }

    @Override
    public void setAdapter(List<StopsActivityTimeLineObject> stopsActivityTimeLineObjects, String currentTime) {

        if (!mAdapterIsSet) {
            mTimeLineAdapter = new StopsAdapter(stopsActivityTimeLineObjects, currentTime);
            mRecyclerView.setAdapter(mTimeLineAdapter);
            mAdapterIsSet = true;

            mTimeLineAdapter.setOnItemClickListener((parent, view, position, id) -> {
                //Snackbar.make(view, stopsActivityTimeLineObjects.get(position).getStop().getStopTitle(), Snackbar.LENGTH_SHORT).show();
                //Intent intent = ScheduleActivity.newIntent(getContext(), mRoute,
                //       stopsActivityTimeLineObjects.get(position).getStop());
                Intent intent = ScheduleActivity.newIntent(getContext(), mRoute,
                        stopsActivityTimeLineObjects.get(position).getStop());
                startActivity(intent);
            });
        } else {
            mTimeLineAdapter.dataChange(stopsActivityTimeLineObjects, currentTime);
        }
    }

}
