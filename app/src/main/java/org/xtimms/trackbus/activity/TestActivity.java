package org.xtimms.trackbus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.adapter.TimelineTabPageAdapter;
import org.xtimms.trackbus.model.Route;

public class TestActivity extends AppBaseActivity {

    public static final String EXTRA_ROUTE = TestActivity.class.getSimpleName();

    public static Intent newIntent(Context context, Route route) {
        Intent intent = new Intent(context, StopsTimeLineActivity.class);
        intent.putExtra(EXTRA_ROUTE, route);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_newest);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TimelineTabPageAdapter pagerAdapter = new TimelineTabPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
