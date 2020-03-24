package org.xtimms.ridebus.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.ridebus.App;
import org.xtimms.ridebus.R;
import org.xtimms.ridebus.model.DatabaseObject;
import org.xtimms.ridebus.model.Route;
import org.xtimms.ridebus.model.Stop;
import org.xtimms.ridebus.util.Color;
import org.xtimms.ridebus.util.TransportId;

import java.util.ArrayList;
import java.util.List;

import static org.xtimms.ridebus.Constant.EMPTY_STRING;

public class BookmarkSearchAdapter extends RecyclerView.Adapter<BookmarkSearchAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<DatabaseObject> mDatabaseObjects;
    private List<DatabaseObject> mDatabaseObjectFiltered;
    private AdapterView.OnItemClickListener onItemClickListener;

    public BookmarkSearchAdapter(List<DatabaseObject> databaseObjects) {
        this.mDatabaseObjects = databaseObjects;
        this.mDatabaseObjectFiltered = databaseObjects;
    }

    public List<DatabaseObject> getStopsListFiltered() {
        return mDatabaseObjectFiltered;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bookmarks_search, parent, false);
        return new ViewHolder(frameLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DatabaseObject object = mDatabaseObjectFiltered.get(position);

        holder.mTramImage.setVisibility(View.GONE);

        if (object instanceof Stop) {
            Stop stop = (Stop) object;
            holder.mTitleText.setText(stop.getStopTitle());
            holder.mMarkText.setText(stop.getMark());

            if (stop.getTransportId() == TransportId.TRAM.getIdInDatabase()) {
                holder.mTramImage.setVisibility(View.VISIBLE);
            } else holder.mTramImage.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mPictureText.setBackground(App.getInstance().getAppContext().getResources()
                        .getDrawable(R.drawable.circle_stop));
            } else {
                holder.mPictureText.setBackground(App.getInstance().getAppContext().getResources()
                        .getDrawable(R.drawable.ic_store));
            }

            holder.mPictureText.setText(EMPTY_STRING);
        }

        if (object instanceof Route) {
            holder.mMarkText.setText("маршрут");
            Route route = (Route) object;
            holder.mTitleText.setText(route.getRouteTitle());

            Color.setBackgroundCircle(route.getTransportId(), holder.mPictureText, context);

            holder.mPictureText.setText(route.getRouteNumber());
        }

    }

    @Override
    public int getItemCount() {
        return mDatabaseObjectFiltered.size();
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
                    mDatabaseObjectFiltered = mDatabaseObjects;
                } else {
                    List<DatabaseObject> filteredList = new ArrayList<>();
                    for (DatabaseObject row : mDatabaseObjects) {

                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getNumber().equals(charString.toLowerCase())) {
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

                List<DatabaseObject> databaseObjects = new ArrayList<>();
                List<?> result = (List<?>) filterResults.values;

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

        private BookmarkSearchAdapter adapter;
        private TextView mTitleText;
        private TextView mMarkText;
        private TextView mPictureText;
        private ImageView mTramImage;
        //private LinearLayout mLayout;

        ViewHolder(View itemView, BookmarkSearchAdapter adapter) {
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