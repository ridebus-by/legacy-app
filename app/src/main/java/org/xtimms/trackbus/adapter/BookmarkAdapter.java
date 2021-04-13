package org.xtimms.trackbus.adapter;

import android.os.Build;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private final List<DatabaseObject> mBookmarks;
    private AdapterView.OnItemClickListener onItemClickListener;
    private int mPosition;

    public BookmarkAdapter(List<DatabaseObject> databaseObjects) {
        this.mBookmarks = databaseObjects;
    }

    public List<DatabaseObject> getBookmarks() {
        return mBookmarks;
    }

    public void changeBookmarks(int position, DatabaseObject object) {
        mBookmarks.set(position, object);
        this.notifyItemChanged(position);
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(constraintLayout, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setOnLongClickListener(v -> {
            setPosition(holder.getAdapterPosition());
            return false;
        });

        DatabaseObject object = mBookmarks.get(position);

        if (object.isEmpty()) {
            holder.mTextTitleBtn.setVisibility(View.GONE);
            holder.mTextDescriptionBtn.setVisibility(View.GONE);
            holder.mTextPlus.setVisibility(View.VISIBLE);
            holder.mCircle.setVisibility(View.VISIBLE);
            holder.mLinearLayout.setVisibility(View.GONE);
            return;
        }

        holder.mTextPlus.setVisibility(View.INVISIBLE);
        holder.mCircle.setVisibility(View.INVISIBLE);
        holder.mLinearLayout.setVisibility(View.VISIBLE);

        if (object instanceof Route) {
            Route route = (Route) object;
            holder.mTextTitleBtn.setVisibility(View.VISIBLE);
            holder.mTextDescriptionBtn.setVisibility(View.VISIBLE);
            holder.mTextImageBtn.setText(route.getRouteNumber());
            holder.mTextDescriptionBtn.setVisibility(View.GONE);

            ColorUtils.setBackgroundCircle(route.getTransportId(), holder.mTextImageBtn);

            holder.mTextTitleBtn.setText(route.getRouteTitle());
            holder.mTextDescriptionBtn.setVisibility(View.GONE);
            holder.mTextTitleBtn.setGravity(Gravity.CENTER_VERTICAL);
        }

        if (object instanceof Stop) {
            Stop stop = (Stop) object;
            holder.mTextImageBtn.setText("");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mTextImageBtn.setBackground(App.getInstance().getAppContext().getResources()
                        .getDrawable(R.drawable.ic_store_mall_directory_black_24dp));
            } else {
                holder.mTextImageBtn.setBackground(App.getInstance().getAppContext().getResources()
                        .getDrawable(R.drawable.ic_store));
            }
            holder.mTextTitleBtn.setVisibility(View.VISIBLE);
            holder.mTextDescriptionBtn.setVisibility(View.VISIBLE);
            holder.mTextTitleBtn.setText(stop.getStopTitle());
            holder.mTextDescriptionBtn.setText(stop.getMark());
        }

    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mBookmarks.size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private final BookmarkAdapter adapter;
        private final ConstraintLayout mLinearLayout;
        private final TextView mTextImageBtn;
        private final TextView mTextTitleBtn;
        private final TextView mTextDescriptionBtn;
        private final TextView mTextPlus;
        private final TextView mCircle;

        ViewHolder(View itemView, BookmarkAdapter adapter) {
            super(itemView);
            mTextImageBtn = itemView.findViewById(R.id.text_image_bookmark);
            mTextTitleBtn = itemView.findViewById(R.id.text_title_bookmark);
            mTextDescriptionBtn = itemView.findViewById(R.id.text_description_bookmark);
            mLinearLayout = itemView.findViewById(R.id.layout_bookmark);
            mTextPlus = itemView.findViewById(R.id.text_plus_bookmark);
            mCircle = itemView.findViewById(R.id.circle);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            adapter.onItemHolderClick(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            DatabaseObject databaseObject = adapter.getBookmarks().get(getAdapterPosition());

            if (databaseObject.isEmpty()) return;

            MenuInflater inflater = new MenuInflater(App.getInstance().getAppContext());
            inflater.inflate(R.menu.menu_context_bookmarks, menu);
        }
    }
}
