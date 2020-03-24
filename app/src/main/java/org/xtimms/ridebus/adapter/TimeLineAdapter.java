package org.xtimms.ridebus.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.ridebus.Constant;
import org.xtimms.ridebus.R;
import org.xtimms.ridebus.object.StopsActivityTimeLineObject;
import org.xtimms.ridebus.util.DateTime;

import java.text.ParseException;
import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {
    private AdapterView.OnItemClickListener onItemClickListener;
    private String mCurrentTime;
    private List<StopsActivityTimeLineObject> mStopsActivityTimeLineObjects;

    public TimeLineAdapter(List<StopsActivityTimeLineObject> stopsActivityTimeLineObjects, String currentTime) {
        this.mStopsActivityTimeLineObjects = stopsActivityTimeLineObjects;
        this.mCurrentTime = currentTime;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline, parent, false);

        return new ViewHolder(layout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StopsActivityTimeLineObject object = mStopsActivityTimeLineObjects.get(position);
        holder.mMainText.setText(object.getStop().getStopTitle());

        if ((object.getTimeList() == null) || (object.getTimeList().isEmpty())) {
            holder.mRemainingTimeText.setText(Constant.TIME_EMPTY);
            holder.mClosestTime.setText(Constant.TIME_EMPTY);
        } else {
            DateTime dateTime = new DateTime();
            DateTime.ResultTime resultTime = null;

            try {
                resultTime = dateTime.getRemainingClosestTime(object.getTimeList(), mCurrentTime);
            } catch (ParseException e) {
                holder.mRemainingTimeText.setText(Constant.TIME_EMPTY);
                holder.mClosestTime.setText(Constant.TIME_EMPTY);
            }

            if (resultTime != null) {
                holder.setRemainingTime(resultTime.getRemainingTime());
                holder.mClosestTime.setText(resultTime.getClosestTime());
            }

        }

    }

    @Override
    public int getItemCount() {
        return mStopsActivityTimeLineObjects.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(ViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public void dataChange(List<StopsActivityTimeLineObject> stopsActivityTimeLineObjects, String currentTime) {
        mStopsActivityTimeLineObjects = stopsActivityTimeLineObjects;
        mCurrentTime = currentTime;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TimeLineAdapter mAdapter;
        private TextView mMainText;
        private TextView mRemainingTimeText;
        private TextView mClosestTime;

        ViewHolder(View itemView, TimeLineAdapter adapter) {
            super(itemView);
            mMainText = itemView.findViewById(R.id.text_stoptitle_timeline);
            mRemainingTimeText = itemView.findViewById(R.id.text_remainingtime_timeline);
            mClosestTime = itemView.findViewById(R.id.text_closesttime_timeline);
            this.mAdapter = adapter;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        private void setRemainingTime(String remainingTime) {

            if (remainingTime.equals(Constant.TIME_EMPTY)) {
                mRemainingTimeText.setText(Constant.TIME_EMPTY);
                return;
            }

            String remainingStringTime = DateTime.formatRemainingTime(remainingTime);

            mRemainingTimeText.setText(remainingStringTime);
        }

    }

}