package org.xtimms.trackbus.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.App;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.model.DatabaseObject;
import org.xtimms.trackbus.model.Route;
import org.xtimms.trackbus.model.Stop;
import org.xtimms.trackbus.util.ColorUtils;
import org.xtimms.trackbus.util.TransportId;

import java.util.ArrayList;
import java.util.List;

import static org.xtimms.trackbus.util.ConstantUtils.EMPTY_STRING;

public class BookmarksSearchAdapter extends RecyclerView.Adapter<BookmarksSearchAdapter.ViewHolder> implements Filterable {

    private final ArrayList<DatabaseObject> mDataset;
    private ArrayList<DatabaseObject> mDatabaseObjectFiltered;
    private AdapterView.OnItemClickListener onItemClickListener;

    public BookmarksSearchAdapter(ArrayList<DatabaseObject> dataset) {
        setHasStableIds(true);
        this.mDataset = dataset;
        this.mDatabaseObjectFiltered = dataset;
    }

    public ArrayList<DatabaseObject> getStopsListFiltered() {
        return mDatabaseObjectFiltered;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bookmarks_search, parent, false);
        return new ViewHolder(constraintLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DatabaseObject object = mDatabaseObjectFiltered.get(position);

        holder.mTramImage.setVisibility(View.GONE);
        holder.mTitleText.setVisibility(View.GONE);
        holder.mMarkText.setVisibility(View.GONE);

        if (object instanceof Stop) {
            Stop stop = (Stop) object;
            holder.mTitleText.setVisibility(View.VISIBLE);
            holder.mMarkText.setVisibility(View.VISIBLE);
            holder.mTitleText.setText(stop.getStopTitle());
            holder.mMarkText.setText(stop.getMark());

            if (stop.getTransportId() == TransportId.TRAM.getIdInDatabase()) {
                holder.mTramImage.setVisibility(View.VISIBLE);
            } else holder.mTramImage.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mPictureText.setBackground(App.getInstance().getAppContext().getResources()
                        .getDrawable(R.drawable.ic_store_mall_directory_black_24dp));
            } else {
                holder.mPictureText.setBackground(App.getInstance().getAppContext().getResources()
                        .getDrawable(R.drawable.ic_store));
            }

            holder.mPictureText.setText(EMPTY_STRING);
        }

        if (object instanceof Route) {
            Route route = (Route) object;
            holder.mTitleText.setVisibility(View.VISIBLE);
            holder.mTitleText.setText(route.getRouteTitle());

            ColorUtils.setBackgroundCircle(route.getTransportId(), holder.mPictureText);

            holder.mPictureText.setText(route.getRouteNumber());
        }
        holder.itemView.setTag(object);
    }

    @Override
    public int getItemCount() {
        return mDatabaseObjectFiltered.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
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
                    mDatabaseObjectFiltered = mDataset;
                } else {
                    ArrayList<DatabaseObject> filteredList = new ArrayList<>();
                    for (DatabaseObject row : mDataset) {

                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getNumber().equalsIgnoreCase(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mDatabaseObjectFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDatabaseObjectFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                ArrayList<DatabaseObject> databaseObjects = new ArrayList<>();
                ArrayList<?> result = (ArrayList<?>) filterResults.values;

                for (Object object : result) {
                    if (object instanceof DatabaseObject) {
                        databaseObjects.add((DatabaseObject) object);
                    }
                }

                mDatabaseObjectFiltered = databaseObjects;
                //stopsListFiltered = (ArrayList<Stop>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final BookmarksSearchAdapter adapter;
        private final TextView mTitleText;
        private final TextView mMarkText;
        private final TextView mPictureText;
        private final ImageView mTramImage;
        //private LinearLayout mLayout;

        ViewHolder(View itemView, BookmarksSearchAdapter adapter) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.text_title_bookmarks_cardview);
            mMarkText = itemView.findViewById(R.id.text_mark_bookmarks_cardview);
            mPictureText = itemView.findViewById(R.id.text_picture_bookmarks_cardview);
            mTramImage = itemView.findViewById(R.id.ImageView_tram_itemBookmarksSearch);
            //mLayout = cardView.findViewById(R.id.layout_bookmarks_cardview);

            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemHolderClick(this);
        }
    }

}
