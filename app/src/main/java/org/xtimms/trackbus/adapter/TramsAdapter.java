package org.xtimms.trackbus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.model.Route;

import java.util.ArrayList;
import java.util.List;

public class TramsAdapter extends RecyclerView.Adapter<TramsAdapter.ViewHolder> {

    private final ArrayList<Route> mDataset;
    private AdapterView.OnItemClickListener onItemClickListener;

    public TramsAdapter(ArrayList<Route> dataset) {
        setHasStableIds(true);
        this.mDataset = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route, parent, false);
        return new ViewHolder(constraintLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Route item = mDataset.get(position);
        holder.mRouteNumber.setText(item.getRouteNumber());
        holder.mRouteTitle.setText(item.getRouteTitle());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataset.get(position).getId();
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
        private final TextView mRouteNumber;
        private final TextView mRouteTitle;
        private final TramsAdapter adapter;


        ViewHolder(ConstraintLayout itemView, TramsAdapter adapter) {
            super(itemView);
            mRouteNumber = itemView.findViewById(R.id.route_number);
            mRouteTitle = itemView.findViewById(R.id.route_title);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemHolderClick(this);
        }
    }
}
