package org.xtimms.trackbus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.model.Route;

import java.util.List;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {
    private final List<Route> routes;
    private AdapterView.OnItemClickListener onItemClickListener;

    public BottomSheetAdapter(List<Route> routes) {
        this.routes = routes;
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bottom_sheet, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.simpleListRow.setText(routes.get(position).getRouteTitle());
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView simpleListRow;
        private final BottomSheetAdapter adapter;

        ViewHolder(View itemView, BottomSheetAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
            simpleListRow = itemView.findViewById(R.id.text_routetitle_itembottomsheet);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemHolderClick(this);
        }
    }

}