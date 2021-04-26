package org.xtimms.trackbus.adapter;

import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.App;
import org.xtimms.trackbus.model.Stop;
import org.xtimms.trackbus.ui.DataViewHolder;
import org.xtimms.trackbus.util.ConstantUtils;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.object.StopActivityObject;
import org.xtimms.trackbus.util.ColorUtils;
import org.xtimms.trackbus.util.DateTime;
import org.xtimms.trackbus.util.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class StopsActivityAdapter extends RecyclerView.Adapter<StopsActivityAdapter.ViewHolder> {

    private ArrayList<StopActivityObject> mDataset;
    private AdapterView.OnItemClickListener onItemClickListener;

    public StopsActivityAdapter(ArrayList<StopActivityObject> dataset) {
        setHasStableIds(true);
        this.mDataset = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stop_activity, parent, false);
        return new ViewHolder(constraintLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StopActivityObject item = mDataset.get(holder.getAdapterPosition());
        holder.mRouteNumberText.setText(item.getRoute().getRouteNumber());
        holder.mRouteTitleText.setText(item.getRoute().getRouteTitle());
        holder.mClosestTime.setText(item.getClosestTime());
        holder.setRemainingTime(item.getRemainingTime());

        ColorUtils.setBackgroundColor(item.getRoute().getTransportId(), holder.mColor);

        holder.itemView.setTag(item);
        holder.bind(item);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.recycle();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
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

    public void dataChange(ArrayList<StopActivityObject> stopActivityObjectList) {
        mDataset = stopActivityObjectList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends DataViewHolder<StopActivityObject> implements View.OnClickListener {
        private final StopsActivityAdapter adapter;
        private final ImageView mColor;
        private final TextView mRouteNumberText;
        private final TextView mRouteTitleText;
        private final TextView mRemainingTimeText;
        private final TextView mClosestTime;

        ViewHolder(View itemView, StopsActivityAdapter adapter) {
            super(itemView);
            mColor = itemView.findViewById(R.id.color);
            mRouteNumberText = itemView.findViewById(R.id.text_routenumber_stopactivity_card);
            mRouteTitleText = itemView.findViewById(R.id.text_routetitle_stopactivity_card);
            mRemainingTimeText = itemView.findViewById(R.id.text_remainingtime_stopactivity_card);
            mClosestTime = itemView.findViewById(R.id.text_closesttime_stopactivity_card);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemHolderClick(this);
        }

        @Override
        public void recycle() {
            super.recycle();
        }

        @Override
        public void bind(StopActivityObject stopActivityObject) {
            super.bind(stopActivityObject);
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

}
