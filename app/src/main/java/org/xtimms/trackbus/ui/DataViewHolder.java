package org.xtimms.trackbus.ui;

import android.content.Context;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DataViewHolder<D> extends RecyclerView.ViewHolder {

    @Nullable
    private D mData;

    public DataViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @CallSuper
    public void bind(D d) {
        mData = d;
    }

    @CallSuper
    public void recycle() {
        mData = null;
    }

    @Nullable
    protected final D getData() {
        return mData;
    }

    public final Context getContext() {
        return itemView.getContext();
    }

}
