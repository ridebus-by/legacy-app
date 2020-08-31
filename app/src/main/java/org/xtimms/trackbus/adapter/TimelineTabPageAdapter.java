package org.xtimms.trackbus.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.xtimms.trackbus.Constant;
import org.xtimms.trackbus.fragment.InfoFragment;
import org.xtimms.trackbus.fragment.TimelineFragment;

public class TimelineTabPageAdapter extends FragmentPagerAdapter {

    private final String[] tabTitles;

    public TimelineTabPageAdapter(FragmentManager fm) {
        super(fm);
        tabTitles = Constant.TIMELINE_TABS;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return InfoFragment.newInstance();
            case 1:
                return TimelineFragment.newInstance();
        }
        return null;
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
