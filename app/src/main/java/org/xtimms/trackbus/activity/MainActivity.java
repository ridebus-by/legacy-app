package org.xtimms.trackbus.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.joda.time.LocalDate;
import org.xtimms.trackbus.App;
import org.xtimms.trackbus.Constant;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.settings.SettingsHeadersActivity;
import org.xtimms.trackbus.fragment.BookmarkFragment;
import org.xtimms.trackbus.fragment.StopFragment;
import org.xtimms.trackbus.fragment.TabRouteFragment;
import org.xtimms.trackbus.onboarding.Intro;
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
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private DrawerHeaderImageTool mDrawerHeaderTool;
    private FloatingActionButton floatingActionButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableTransparentStatusBar(android.R.color.transparent);

        toolbar = findViewById(R.id.toolbar_main);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        tabLayout = findViewById(R.id.tab_invisible);
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
        mNavigationView.setCheckedItem(R.id.menu_item_1);

        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.menu_item_1));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, TabRouteFragment.newInstance(0));
        transaction.commit();

        final View headerView = mNavigationView.getHeaderView(0);
        TextView mDbVersion = headerView.findViewById(R.id.db_version);
        mDbVersion.setText(App.getInstance().getAppContext().getString(R.string.db_version) + " " + Constant.DB_VERSION);

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
        initOnboarding();

    }

    private void initOnboarding() {
        Thread t = new Thread(() -> {
            SharedPreferences getPrefs = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());

            boolean isFirstStart = getPrefs.getBoolean("startOnboarding", true);

            if (isFirstStart) {
                Intent i = new Intent(MainActivity.this, Intro.class);
                startActivity(i);

                SharedPreferences.Editor e = getPrefs.edit();
                e.putBoolean("startOnboarding", false);
                e.apply();
            }
        });

        // Start the thread
        t.start();
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
            case R.id.action_test:
                TestDialog activity = new TestDialog(this);
                activity.showSave();
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
    }

    @Override
    protected void onDestroy() {
        //Log.d(TAG, "onDestroy()");
        super.onDestroy();
        if (!mInstanceState) {
            //Log.d(TAG, "Database - close");
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
                selectedFragment = TabRouteFragment.newInstance(0);
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.menu_item_1));
                tabLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.menu_item_2:
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.menu_item_2));
                selectedFragment = StopFragment.newInstance();
                tabLayout.setVisibility(View.GONE);
                break;
            case R.id.menu_item_3:
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.menu_item_3));
                selectedFragment = BookmarkFragment.newInstance();
                tabLayout.setVisibility(View.GONE);
                break;
            case R.id.nav_action_about:
                intent.setClass(MainActivity.this, AboutActivity.class);
                startActivity(intent);
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
