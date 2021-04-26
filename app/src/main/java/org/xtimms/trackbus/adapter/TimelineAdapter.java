package org.xtimms.trackbus.adapter;

import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;

import org.xtimms.trackbus.util.ConstantUtils;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.object.StopsActivityTimeLineObject;
import org.xtimms.trackbus.util.DateTime;
import org.xtimms.trackbus.util.ThemeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private AdapterView.OnItemClickListener onItemClickListener;
    private String mCurrentTime;
    private ArrayList<StopsActivityTimeLineObject> mDataset;

    public TimelineAdapter(ArrayList<StopsActivityTimeLineObject> dataset, String currentTime) {
        setHasStableIds(true);
        this.mDataset = dataset;
        this.mCurrentTime = currentTime;
    }

    //@Override
    //public int getItemViewType(int position) {
    //    return TimelineView.getTimeLineViewType(position, getItemCount());
    //}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline, parent, false);

        return new ViewHolder(layout, viewType, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StopsActivityTimeLineObject item = mDataset.get(position);
        holder.mMainText.setText(item.getStop().getStopTitle());

        if ((item.getTimeList() == null) || (item.getTimeList().isEmpty())) {
            holder.mRemainingTimeText.setText(ConstantUtils.TIME_EMPTY);
            holder.mClosestTime.setText(ConstantUtils.TIME_EMPTY);
        } else {
            DateTime dateTime = new DateTime();
            DateTime.ResultTime resultTime = null;

            try {
                resultTime = dateTime.getRemainingClosestTime(item.getTimeList(), mCurrentTime);
            } catch (ParseException e) {
                //e.printStackTrace();
                holder.mRemainingTimeText.setText(ConstantUtils.TIME_EMPTY);
                holder.mClosestTime.setText(ConstantUtils.TIME_EMPTY);
            }

            if (resultTime != null) {
                holder.setRemainingTime(resultTime.getRemainingTime());
                holder.mClosestTime.setText(resultTime.getClosestTime());
            }

        }
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    public void dataChange(ArrayList<StopsActivityTimeLineObject> dataset, String currentTime) {
        mDataset = dataset;
        mCurrentTime = currentTime;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TimelineAdapter mAdapter;
        private TimelineView mTimelineView;
        private final TextView mMainText;
        private final TextView mRemainingTimeText;
        private final TextView mClosestTime;

        ViewHolder(View itemView, int viewType, TimelineAdapter adapter) {
            super(itemView);
            mMainText = itemView.findViewById(R.id.text_stoptitle_timeline);
            mRemainingTimeText = itemView.findViewById(R.id.text_remainingtime_timeline);
            mClosestTime = itemView.findViewById(R.id.text_closesttime_timeline);
            mTimelineView = itemView.findViewById(R.id.timemarker_timelineview);
            mTimelineView.initLine(viewType);
            this.mAdapter = adapter;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        private void setRemainingTime(String remainingTime) {

            int nightModeFlags = itemView.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

            if (remainingTime.equals(ConstantUtils.TIME_EMPTY)) {
                mRemainingTimeText.setText(ConstantUtils.TIME_EMPTY);
                return;
            }

            String remainingStringTime = DateTime.formatRemainingTime(remainingTime);

            if (ThemeUtils.isAppThemeDark(itemView.getContext())) {
                if (remainingStringTime.contains(ConstantUtils.TIME_EMPTY)) {
                    mRemainingTimeText.setTextColor(ThemeUtils.getAttrColor(itemView.getContext(), R.attr.colorError));
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    mRemainingTimeText.setTextColor(Color.WHITE);
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    mRemainingTimeText.setTextColor(Color.WHITE);
                } else {
                    mRemainingTimeText.setTextColor(Color.BLACK);
                }
            }

            if (ThemeUtils.isAppThemeNotDark(itemView.getContext())) {
                if (remainingStringTime.contains(ConstantUtils.TIME_EMPTY)) {
                    mRemainingTimeText.setTextColor(ThemeUtils.getAttrColor(itemView.getContext(), R.attr.colorError));
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    mRemainingTimeText.setTextColor(Color.WHITE);
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    mRemainingTimeText.setTextColor(Color.BLACK);
                } else {
                    mRemainingTimeText.setTextColor(Color.BLACK);
                }
            }

            mRemainingTimeText.setText(remainingStringTime);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }
}