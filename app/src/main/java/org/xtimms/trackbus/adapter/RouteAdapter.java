package org.xtimms.trackbus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.model.Route;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private final List<Route> routes;
    private AdapterView.OnItemClickListener onItemClickListener;

    public RouteAdapter(List<Route> routes) {
        this.routes = routes;
    }

    @NonNull
    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_bus, parent, false);
        return new RouteAdapter.ViewHolder(constraintLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteAdapter.ViewHolder holder, int position) {
        holder.mRouteNumber.setLetter(routes.get(position).getRouteNumber());
        holder.mRouteTitle.setText(routes.get(position).getRouteTitle());
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(RouteAdapter.ViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MaterialLetterIcon mRouteNumber;
        private final TextView mRouteTitle;
        private final RouteAdapter adapter;


        ViewHolder(ConstraintLayout itemView, RouteAdapter adapter) {
            super(itemView);
            mRouteNumber = itemView.findViewById(R.id.route_bus_number);
            mRouteTitle = itemView.findViewById(R.id.route_bus_title);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemHolderClick(this);
        }
    }
}
