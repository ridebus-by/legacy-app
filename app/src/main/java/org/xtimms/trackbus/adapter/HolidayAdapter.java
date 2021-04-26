package org.xtimms.trackbus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;

import java.util.List;

import de.galgtonold.jollydayandroid.Holiday;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.ViewHolder> {

    private final List<Holiday> holidays;

    public HolidayAdapter(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    @NonNull
    @Override
    public HolidayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_holiday, parent, false);
        return new ViewHolder(relativeLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayAdapter.ViewHolder holder, int position) {
        holder.mHolidayDate.setText(holidays.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return holidays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mHolidayDate;
        private final TextView mWeekday;

        ViewHolder(View itemView, HolidayAdapter adapter) {
            super(itemView);
            mHolidayDate = itemView.findViewById(R.id.holiday_date);
            mWeekday = itemView.findViewById(R.id.weekday);
        }
    }

}
