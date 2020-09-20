package org.xtimms.trackbus.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.joda.time.LocalDate;
import org.xtimms.trackbus.App;
import org.xtimms.trackbus.util.ConstantUtils;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.settings.SettingsHeadersActivity;
import org.xtimms.trackbus.fragment.BookmarkFragment;
import org.xtimms.trackbus.fragment.StopFragment;
import org.xtimms.trackbus.fragment.TabRouteFragment;
import org.xtimms.trackbus.ui.DrawerHeaderImageTool;

import java.util.Objects;
import java.util.Set;

import de.galgtonold.jollydayandroid.Holiday;
import de.galgtonold.jollydayandroid.HolidayCalendar;
import de.galgtonold.jollydayandroid.HolidayManager;

public class MainActivity extends AppBaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean mDoubleBackToExitPressedOnce = false;
    private boolean mInstanceState = false;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    private DrawerHeaderImageTool mDrawerHeaderTool;
    private FloatingActionButton floatingActionButton;

    private TabRouteFragment tabRouteFragment;
    private StopFragment stopFragment;
    private BookmarkFragment bookmarkFragment;

    private static final String KEY_NAV_ITEM = "CURRENT_NAV_ITEM";

    private int selectedNavItem = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableTransparentStatusBar(android.R.color.transparent);

        toolbar = findViewById(R.id.toolbar_main);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setTitle(R.string.menu_item_1);
        toolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        floatingActionButton = findViewById(R.id.fab);
        setSupportActionBar(toolbar);

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
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Init the fragments.
        if (savedInstanceState != null) {
            tabRouteFragment = (TabRouteFragment) getSupportFragmentManager().getFragment(savedInstanceState, "TabRouteFragment");
            stopFragment = (StopFragment) getSupportFragmentManager().getFragment(savedInstanceState, "StopFragment");
            bookmarkFragment = (BookmarkFragment) getSupportFragmentManager().getFragment(savedInstanceState, "BookmarkFragment");
            selectedNavItem = savedInstanceState.getInt(KEY_NAV_ITEM);
        } else {
            tabRouteFragment = (TabRouteFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if (tabRouteFragment == null) {
                tabRouteFragment = TabRouteFragment.newInstance(0);
            }

            stopFragment = (StopFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if (stopFragment == null) {
                stopFragment = StopFragment.newInstance();
            }

            bookmarkFragment = (BookmarkFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if (bookmarkFragment == null) {
                bookmarkFragment = BookmarkFragment.newInstance();
            }
        }

        // Add the fragments.
        if (!tabRouteFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, tabRouteFragment, "TabRouteFragment")
                    .commit();
        }

        if (!stopFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, stopFragment, "StopFragment")
                    .commit();
        }

        if (!bookmarkFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, bookmarkFragment, "BookmarkFragment")
                    .commit();
        }

        // Show the default fragment.
        if (selectedNavItem == 0) {
            showRoutesFragment();
        } else if (selectedNavItem == 1) {
            showStopsFragment();
        } else if (selectedNavItem == 2) {
            showBookmarksFragment();
        }

        final View headerView = mNavigationView.getHeaderView(0);
        TextView mDbVersion = headerView.findViewById(R.id.db_version);
        mDbVersion.setText(App.getInstance().getAppContext().getString(R.string.db_version) + " " + ConstantUtils.DB_VERSION);

        if (isDarkTheme()) {
            ColorStateList csl = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white_overlay_85));
            mNavigationView.setItemTextColor(csl);
            mNavigationView.setItemIconTintList(csl);
        }

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            initTapTargetView();
        } else {
            floatingActionButton.hide();
        }

        initDrawerHeaderTool();
        initOnHolidayDialog();

    }

    private void initDrawerHeaderTool() {
        mDrawerHeaderTool = new DrawerHeaderImageTool(this, mNavigationView);
        mDrawerHeaderTool.initDrawerImage();
    }

    private void initOnHolidayDialog() {
        HolidayManager m = HolidayManager.getInstance(HolidayCalendar.BELARUS);
        Set<Holiday> holidays = m.getHolidays(2019);

        for (Holiday holiday : holidays) {
            if (holiday.getDate().equals(LocalDate.now())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Кажется сегодня праздничный день!")
                        .setMessage("Возможно, транспорт ходит с изменениями в маршруте или по расписанию выходного дня. За подробной информацией обратитесь в автопарк.")
                        .setIcon(R.drawable.ic_new_releases_black_24dp)
                        .setCancelable(false)
                        .setNegativeButton("Понятно",
                                (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();

            }
        }
    }

    private void initTapTargetView() {
        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forToolbarMenuItem(toolbar, R.id.action_settings, "Настройте приложение под себя", "Также, эта кнопка доступна в новом меню навигации.").
                                cancelable(false)
                                .id(1),
                        TapTarget.forToolbarNavigationIcon(toolbar, "В этом обновлении теперь изменён способ навигации", "Нажмите на эту иконку, чтобы показать меню.")
                                .cancelable(true)
                                .id(2)
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        Log.d("TapTargetView", "Finished!");
                        openDrawer();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                });

        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.fab), "Привет!", "Давайте посмотрим, что изменилось в этом обновлении.")
                .cancelable(true)
                .drawShadow(true)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                sequence.start();
                floatingActionButton.hide();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetView", "You dismissed me :(");
                sequence.start();
                floatingActionButton.hide();
            }

        });

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDrawerHeaderTool.onActivityResult(requestCode, resultCode, data);
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
                intent.setClass(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                intent.setClass(MainActivity.this, SettingsHeadersActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (mDoubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.mDoubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.double_back_to_exit_message), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> mDoubleBackToExitPressedOnce = false, 2000);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.d(TAG, "onSaveInstanceState()");
        mInstanceState = true;
        //outState.putSerializable(DATABASE_INSTANCE_STATE, App.getInstance().getDatabase());
        // Store the fragments' states.
        if (tabRouteFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "TabRouteFragment", tabRouteFragment);
        }
        if (stopFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "StopFragment", stopFragment);
        }
        if (bookmarkFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "BookmarkFragment", bookmarkFragment);
        }
    }

    @Override
    protected void onDestroy() {
        //Log.d(TAG, "onDestroy()");
        super.onDestroy();
        if (!mInstanceState) {
            //Log.d(TAG, "Database - close");
            App.getInstance().getDatabase().close();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = new Intent();
        int id = menuItem.getItemId();
        if (id == R.id.menu_item_1) {
            showRoutesFragment();
        } else if (id == R.id.menu_item_2) {
            showStopsFragment();
        } else if (id == R.id.menu_item_3) {
            showBookmarksFragment();
        } else if (id == R.id.nav_action_about) {
            intent.setClass(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_action_settings) {
            intent.setClass(MainActivity.this, SettingsHeadersActivity.class);
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.openDrawer(mNavigationView);
        }
    }

    /**
     * Show the routes list fragment.
     */
    private void showRoutesFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(tabRouteFragment);
        fragmentTransaction.hide(stopFragment);
        fragmentTransaction.hide(bookmarkFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.menu_item_1));
        mNavigationView.setCheckedItem(R.id.menu_item_1);

    }

    /**
     * Show the companies list fragment.
     */
    private void showStopsFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(stopFragment);
        fragmentTransaction.hide(tabRouteFragment);
        fragmentTransaction.hide(bookmarkFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.menu_item_2));
        mNavigationView.setCheckedItem(R.id.menu_item_2);

    }

    /**
     * Show the bookmarks list fragment.
     */
    private void showBookmarksFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(bookmarkFragment);
        fragmentTransaction.hide(tabRouteFragment);
        fragmentTransaction.hide(stopFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.menu_item_3));
        mNavigationView.setCheckedItem(R.id.menu_item_3);

    }

}
