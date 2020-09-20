package org.xtimms.trackbus.timeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class PageHolder extends Fragment {

    @NonNull
    private final View mView;

    public PageHolder(@NonNull ViewGroup parent, @LayoutRes int layoutResId) {
        mView = onCreateView(parent, layoutResId);
        onViewCreated(mView);
    }

    @NonNull
    protected View onCreateView(@NonNull ViewGroup parent, @LayoutRes int layoutResId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
    }

    protected abstract void onViewCreated(@NonNull View view);

    public String getTitle() {
        return String.valueOf(mView.getTag());
    }

    @NonNull
    public final View getView() {
        return mView;
    }

    public Context getContext() {
        return mView.getContext();
    }
}