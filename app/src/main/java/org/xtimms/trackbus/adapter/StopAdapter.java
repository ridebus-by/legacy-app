package org.xtimms.trackbus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.model.Stop;
import org.xtimms.trackbus.util.TransportId;

import java.util.ArrayList;
import java.util.List;

public class StopAdapter extends RecyclerView.Adapter<StopAdapter.ViewHolder> implements Filterable, FastScrollRecyclerView.SectionedAdapter {

    private final ArrayList<Stop> stops;
    private AdapterView.OnItemClickListener onItemClickListener;
    private ArrayList<Stop> stopsListFiltered;

    public StopAdapter(ArrayList<Stop> stops) {
        setHasStableIds(true);
        this.stops = stops;
        this.stopsListFiltered = stops;
    }

    public ArrayList<Stop> getStopsListFiltered() {
        return stopsListFiltered;
    }

    @NonNull
    @Override
    public StopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stop, parent, false);
        return new ViewHolder(relativeLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull StopAdapter.ViewHolder holder, int position) {
        Stop item = stopsListFiltered.get(position);
        holder.mMainText.setText(item.getStopTitle());
        holder.mMarkText.setText(item.getMark());

        if (stopsListFiltered.get(position).getTransportId() == TransportId.TRAM.getIdInDatabase()) {
            holder.mTramImage.setVisibility(View.VISIBLE);
        } else holder.mTramImage.setVisibility(View.GONE);
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return stopsListFiltered.size();
    }

    @Override
    public long getItemId(int position) {
        return stopsListFiltered.get(position).getId();
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    stopsListFiltered = stops;
                } else {
                    ArrayList<Stop> filteredList = new ArrayList<>();
                    for (Stop row : stops) {

                        if (row.getStopTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    stopsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = stopsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                ArrayList<Stop> stopList = new ArrayList<>();
                ArrayList<?> result = (ArrayList<?>) filterResults.values;

                for (Object object : result) {
                    if (object instanceof Stop) {
                        stopList.add((Stop) object);
                    }
                }

                stopsListFiltered = stopList;
                //stopsListFiltered = (ArrayList<Stop>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return Character.toString(stops.get(position).getTitle().charAt(0));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final StopAdapter adapter;
        private final TextView mMainText;
        private final TextView mMarkText;
        private final ImageView mTramImage;

        ViewHolder(View itemView, StopAdapter adapter) {
            super(itemView);
            mMainText = itemView.findViewById(R.id.stop_title);
            mMarkText = itemView.findViewById(R.id.stop_description);
            mTramImage = itemView.findViewById(R.id.item_tram);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemHolderClick(this);
        }
    }


}
