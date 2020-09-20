package org.xtimms.trackbus.adapter;

import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.util.ConstantUtils;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.util.ThemeUtils;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private final Map<String, List<String>> mMap;
    private final Object[] mKeys;
    private String mClosestHours;
    private String mClosestMinutes;
    private AdapterView.OnItemClickListener onItemClickListener;
    @ColorInt
    private int mColor;

    public ScheduleAdapter(Map<String, List<String>> map, String closestTime) {
        this.mMap = map;
        mKeys = map.keySet().toArray();
        setClosestTime(closestTime);
    }

    private void setClosestTime(String closestTime) {

        if (closestTime == null || closestTime.equals(ConstantUtils.TIME_EMPTY)) return;

        StringTokenizer tokenizer = new StringTokenizer(closestTime, ConstantUtils.TIME_DELIM);
        mClosestHours = tokenizer.nextToken();
        mClosestMinutes = tokenizer.nextToken();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        mColor = ThemeUtils.getThemeAttrColor(parent.getContext(), R.attr.colorAccent);
        return new ViewHolder(constraintLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean flag = false;



        if (mKeys != null) {
            String key = (String) mKeys[position];
            List<String> minutes = mMap.get(key);

            if (key.equals(mClosestHours) && (minutes != null) && (!minutes.isEmpty())) {
                holder.mTextHours.setBackgroundColor(mColor);
                holder.mTextHours.setTextColor(Color.WHITE);
                flag = true;
            }

            holder.mTextHours.setText(key);

            if (minutes != null) {
                for (String m : minutes) {
                    if (flag && m.equals(mClosestMinutes)) {
                        String htmlString = "<u><b>" + m + "</b></u>";
                        Spanned textSpan = Html.fromHtml(htmlString);
                        //textMinutes.setText(textSpan);

                        Spannable spannable = getMinutesColor(textSpan);
                        holder.mTextMinutes.append(spannable);
                    } else {
                        holder.mTextMinutes.append(m);
                    }
                    holder.mTextMinutes.append(ConstantUtils.TWO_SPACES);
                }
            }
        }

    }

    private Spannable getMinutesColor(Spanned closestMinutes) {
        Spannable spannable = new SpannableString(closestMinutes);
        ForegroundColorSpan fgSpan = new ForegroundColorSpan(mColor);
        spannable.setSpan(CharacterStyle.wrap(fgSpan), 0, 2, 0);

        return spannable;
    }

    @Override
    public int getItemCount() {
        return mMap.size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mTextHours;
        private final TextView mTextMinutes;
        private final ScheduleAdapter adapter;


        ViewHolder(View itemView, ScheduleAdapter adapter) {
            super(itemView);
            mTextHours = itemView.findViewById(R.id.text_hours_schedulecardview);
            mTextMinutes = itemView.findViewById(R.id.text_minutes_schedulecardview);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemHolderClick(this);
        }
    }


}