package org.xtimms.trackbus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.ScheduleActivity;
import org.xtimms.trackbus.adapter.TimeLineAdapter;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.object.StopsActivityTimeLineObject;
import org.xtimms.trackbus.presenter.TimeLineActivityPresenter;
import org.xtimms.trackbus.timeline.TimelineActivity;

import java.util.List;
import java.util.Objects;

public class TimelineFragment extends Fragment implements TimeLineActivityPresenter.View {

    private boolean mAdapterIsSet = false;
    private TimeLineAdapter mTimeLineAdapter;
    private RecyclerView mRecyclerView;
    private Route mRoute;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline_stops, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRoute = (Route) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra(TimelineActivity.EXTRA_ROUTE);
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void setAdapter(List<StopsActivityTimeLineObject> stopsActivityTimeLineObjects, String currentTime) {
        if (!mAdapterIsSet) {
            mTimeLineAdapter = new TimeLineAdapter(stopsActivityTimeLineObjects, currentTime);
            mRecyclerView.setAdapter(mTimeLineAdapter);
            mAdapterIsSet = true;

            mTimeLineAdapter.setOnItemClickListener((parent, view, position, id) -> {
                //Snackbar.make(view, mStopsActivityTimeLineObjects.get(position).getStop().getStopTitle(), Snackbar.LENGTH_SHORT).show();
                Intent intent = ScheduleActivity.newIntent(getContext(), mRoute,
                        stopsActivityTimeLineObjects.get(position).getStop());
                startActivity(intent);
            });
        } else {
            mTimeLineAdapter.dataChange(stopsActivityTimeLineObjects, currentTime);
        }
    }
}
