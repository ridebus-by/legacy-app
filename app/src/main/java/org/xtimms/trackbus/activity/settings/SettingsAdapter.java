package org.xtimms.trackbus.activity.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.PreferenceHolder> {

	private final ArrayList<SettingsHeader> mDataset;
	private final AdapterView.OnItemClickListener mClickListener;

	SettingsAdapter(ArrayList<SettingsHeader> headers, AdapterView.OnItemClickListener clickListener) {
		mDataset = headers;
		mClickListener = clickListener;
		setHasStableIds(true);
	}

	@NonNull
    @Override
	public PreferenceHolder onCreateViewHolder(@NonNull ViewGroup parent, @ItemType int viewType) {
		if (viewType == ItemType.TYPE_ITEM_DEFAULT) {
			return new PreferenceHolder(LayoutInflater.from(parent.getContext())
					.inflate(R.layout.item_two_lines_icon, parent, false));
		}
		throw new AssertionError("Unknown viewType");
	}

	@Override
	public void onBindViewHolder(PreferenceHolder holder, int position) {
		SettingsHeader item = mDataset.get(position);
		holder.text1.setText(item.title);
		holder.icon.setImageDrawable(item.icon);
		if (item.summary == null) {
			holder.text2.setVisibility(View.GONE);
		} else {
			holder.text2.setText(item.summary);
			holder.text2.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public int getItemViewType(int position) {
		return mDataset.get(position).hasAction() ? ItemType.TYPE_TIP : ItemType.TYPE_ITEM_DEFAULT;

	}

	@Override
	public long getItemId(int position) {
		return mDataset.get(position).title.hashCode();
	}

	@Override
	public int getItemCount() {
		return mDataset.size();
	}

	class PreferenceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private final ImageView icon;
		private final TextView text1;
		private final TextView text2;

		PreferenceHolder(View itemView) {
			super(itemView);
			icon = itemView.findViewById(android.R.id.icon);
			text1 = itemView.findViewById(android.R.id.text1);
			text2 = itemView.findViewById(android.R.id.text2);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			mClickListener.onItemClick(null, itemView, getAdapterPosition(), getItemId());
		}
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({ItemType.TYPE_ITEM_DEFAULT, ItemType.TYPE_TIP})
	public @interface ItemType {
		int TYPE_ITEM_DEFAULT = 0;
		int TYPE_TIP = 1;
	}
}