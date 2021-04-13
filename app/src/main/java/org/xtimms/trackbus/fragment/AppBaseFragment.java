package org.xtimms.trackbus.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class AppBaseFragment extends Fragment {

    protected View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @LayoutRes int resource) {
        return inflater.inflate(resource, container, false);
    }

    public void scrollToTop() {
    }
}
