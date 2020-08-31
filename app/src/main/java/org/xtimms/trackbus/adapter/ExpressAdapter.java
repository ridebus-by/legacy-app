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

public class ExpressAdapter extends RecyclerView.Adapter<ExpressAdapter.ViewHolder> {

    private final List<Route> routes;
    private AdapterView.OnItemClickListener onItemClickListener;

    public ExpressAdapter(List<Route> routes) {
        this.routes = routes;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_express, parent, false);
        return new ViewHolder(constraintLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mRouteNumber.setLetter(routes.get(position).getRouteNumber());
        holder.mRouteTitle.setText(routes.get(position).getRouteTitle());
    }

    private void onItemHolderClick(ViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MaterialLetterIcon mRouteNumber;
        private final TextView mRouteTitle;
        private final ExpressAdapter adapter;


        ViewHolder(ConstraintLayout itemView, ExpressAdapter adapter) {
            super(itemView);
            mRouteNumber = itemView.findViewById(R.id.route_express_number);
            mRouteTitle = itemView.findViewById(R.id.route_express_title);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemHolderClick(this);
        }
    }

}
