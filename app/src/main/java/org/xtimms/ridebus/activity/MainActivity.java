package org.xtimms.ridebus.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.xtimms.ridebus.App;
import org.xtimms.ridebus.R;
import org.xtimms.ridebus.activity.settings.SettingsHeadersActivity;
import org.xtimms.ridebus.fragment.BookmarkFragment;
import org.xtimms.ridebus.fragment.StopFragment;
import org.xtimms.ridebus.fragment.TabRouteFragment;

import java.util.Objects;

public class MainActivity extends AppBaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean mInstanceState = false;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private TabLayout mTabLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableTransparentStatusBar(android.R.color.transparent);

        Toolbar mToolbar = findViewById(R.id.toolbar_main);
        mToolbar.inflateMenu(R.menu.main);
        mTabLayout = findViewById(R.id.tab_invisible);
        FloatingActionButton mFloatingActionButton = findViewById(R.id.fab);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));

        mDrawerLayout = findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null){
            mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close){
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    invalidateOptionsMenu();
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu();
                }
            };
            mToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.addDrawerListener(mToggle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.menu_item_1);

        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.menu_item_1));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, TabRouteFragment.newInstance(0));
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle != null && mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_about:
                //TODO
                return true;
            case R.id.action_settings:
                //TODO
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mInstanceState = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mInstanceState) {
            App.getInstance().getDatabase().close();
            System.exit(0);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = new Intent();
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.menu_item_1:
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.menu_item_1));
                selectedFragment = TabRouteFragment.newInstance(0);
                mTabLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.menu_item_2:
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.menu_item_2));
                selectedFragment = StopFragment.newInstance();
                mTabLayout.setVisibility(View.GONE);
                break;
            case R.id.menu_item_3:
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.menu_item_3));
                selectedFragment = BookmarkFragment.newInstance();
                mTabLayout.setVisibility(View.GONE);
                break;
            case R.id.nav_action_about:
                //TODO
                break;
            case R.id.nav_action_settings:
                intent.setClass(MainActivity.this, SettingsHeadersActivity.class);
                startActivity(intent);
                break;
            default:
                return false;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

        if (selectedFragment != null) {
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        }
        closeDrawer();
        return true;
    }

    private void closeDrawer() {
        if (mDrawerLayout != null){
            mDrawerLayout.closeDrawer(mNavigationView);
        }
    }

    private void openDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.openDrawer(mNavigationView);
        }
    }
}
