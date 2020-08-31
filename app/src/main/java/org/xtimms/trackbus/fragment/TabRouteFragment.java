package org.xtimms.trackbus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.adapter.RouteTabPageAdapter;

public class TabRouteFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    public static TabRouteFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TabRouteFragment fragment = new TabRouteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = view.findViewById(R.id.tabs_viewpager);
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        RouteTabPageAdapter pagerAdapter = new RouteTabPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
