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

public class MinibusAdapter extends RecyclerView.Adapter<MinibusAdapter.ViewHolder> {
    private final List<Route> routes;
    private AdapterView.OnItemClickListener onItemClickListener;

    public MinibusAdapter(List<Route> routes) {
        this.routes = routes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_minibus, parent, false);
        return new ViewHolder(constraintLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

    private void onItemHolderClick(ViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MaterialLetterIcon mRouteNumber;
        private final TextView mRouteTitle;
        private final MinibusAdapter adapter;


        ViewHolder(View itemView, MinibusAdapter adapter) {
            super(itemView);
            mRouteNumber = itemView.findViewById(R.id.route_minibus_number);
            mRouteTitle = itemView.findViewById(R.id.route_minibus_title);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemHolderClick(this);
        }
    }
}
