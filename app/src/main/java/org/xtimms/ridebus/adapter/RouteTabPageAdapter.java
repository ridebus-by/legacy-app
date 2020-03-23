package org.xtimms.ridebus.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.xtimms.ridebus.Constant;
import org.xtimms.ridebus.fragment.RouteFragment;
import org.xtimms.ridebus.fragment.TabRouteFragment;

public class RouteTabPageAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;

    public RouteTabPageAdapter(FragmentManager fm) {
        super(fm);
        tabTitles = Constant.ROUTE_TABS;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return RouteFragment.newInstance();
        }
        return TabRouteFragment.newInstance(i + 1);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
