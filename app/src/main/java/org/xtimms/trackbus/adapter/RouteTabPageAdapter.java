package org.xtimms.trackbus.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.xtimms.trackbus.Constant;
import org.xtimms.trackbus.fragment.ExpressFragment;
import org.xtimms.trackbus.fragment.MinibusFragment;
import org.xtimms.trackbus.fragment.RouteFragment;
import org.xtimms.trackbus.fragment.TabRouteFragment;

public class RouteTabPageAdapter extends FragmentPagerAdapter {

    private final String[] tabTitles;

    public RouteTabPageAdapter(FragmentManager fm) {
        super(fm);
        tabTitles = Constant.ROUTE_TABS;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return RouteFragment.newInstance();
            case 1:
                return MinibusFragment.newInstance();
            //case 2:
            //   return TramsFragment.newInstance();
            case 2:
                return ExpressFragment.newInstance();
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
