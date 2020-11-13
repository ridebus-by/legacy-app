package org.xtimms.trackbus.adapter;

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

import org.xtimms.trackbus.util.ConstantUtils;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.object.StopActivityObject;
import org.xtimms.trackbus.util.ColorUtils;
import org.xtimms.trackbus.util.DateTime;
import org.xtimms.trackbus.util.ThemeUtils;

import java.util.List;

public class StopsActivityAdapter extends RecyclerView.Adapter<StopsActivityAdapter.ViewHolder> {

    private List<StopActivityObject> mStopActivityObjectList;
    private AdapterView.OnItemClickListener onItemClickListener;

    public StopsActivityAdapter(List<StopActivityObject> stopActivityObjectList) {
        this.mStopActivityObjectList = stopActivityObjectList;
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
        holder.mRouteNumberText.setText(mStopActivityObjectList.get(holder.getAdapterPosition()).getRoute().getRouteNumber());
        holder.mRouteTitleText.setText(mStopActivityObjectList.get(holder.getAdapterPosition()).getRoute().getRouteTitle());
        holder.mClosestTime.setText(mStopActivityObjectList.get(holder.getAdapterPosition()).getClosestTime());
        holder.setRemainingTime(mStopActivityObjectList.get(holder.getAdapterPosition()).getRemainingTime());

        ColorUtils.setBackgroundColor(mStopActivityObjectList
                .get(holder.getAdapterPosition()).getRoute().getTransportId(), holder.mColor);

        holder.setIsRecyclable(false);

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
    public int getItemCount() {
        return mStopActivityObjectList.size();
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

    public void dataChange(List<StopActivityObject> stopActivityObjectList) {
        mStopActivityObjectList = stopActivityObjectList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        private void setRemainingTime(String remainingTime) {

            if (remainingTime.equals(ConstantUtils.TIME_EMPTY)) {
                mRemainingTimeText.setText(ConstantUtils.TIME_EMPTY);
                return;
            }

            String remainingStringTime = DateTime.formatRemainingTime(remainingTime);

            if (ThemeUtils.isAppThemeDark(itemView.getContext())) {
                if (remainingStringTime.contains(ConstantUtils.TIME_EMPTY)) {
                    mRemainingTimeText.setTextColor(Color.RED);
                } else mRemainingTimeText.setTextColor(Color.WHITE);
            }

            if (ThemeUtils.isAppThemeNotDark(itemView.getContext())) {
                if (remainingStringTime.contains(ConstantUtils.TIME_EMPTY)) {
                    mRemainingTimeText.setTextColor(Color.RED);
                } else mRemainingTimeText.setTextColor(Color.BLACK);
            }

            mRemainingTimeText.setText(remainingStringTime);
        }
    }

}
