package org.xtimms.trackbus.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.joda.time.LocalDate;
import org.xtimms.trackbus.App;
import org.xtimms.trackbus.activity.settings.SettingsActivity;
import org.xtimms.trackbus.fragment.AppBaseFragment;
import org.xtimms.trackbus.fragment.RateItDialogFragment;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.settings.SettingsHeadersActivity;
import org.xtimms.trackbus.fragment.BookmarkFragment;
import org.xtimms.trackbus.fragment.StopFragment;
import org.xtimms.trackbus.fragment.TabRouteFragment;

import java.util.Calendar;
import java.util.Set;

import de.galgtonold.jollydayandroid.Holiday;
import de.galgtonold.jollydayandroid.HolidayCalendar;
import de.galgtonold.jollydayandroid.HolidayManager;

public class MainActivity extends AppBaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener {

    private AppBaseFragment mFragment;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView mBottomNavigationView = findViewById(R.id.navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mBottomNavigationView.setOnNavigationItemReselectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setTitle(R.string.menu_item_1);
        setSupportActionBar(toolbar);

        mFragment = new TabRouteFragment();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.fade_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.fade_out  // popExit
                )
                .replace(R.id.frame_layout, mFragment)
                .commitAllowingStateLoss();

        initOnHolidayDialog();

        RateItDialogFragment.show(this, getSupportFragmentManager());

    }

    private void initOnHolidayDialog() {
        HolidayManager m = HolidayManager.getInstance(HolidayCalendar.BELARUS);
        Set<Holiday> holidays = m.getHolidays(Calendar.getInstance().get(Calendar.YEAR));

        for (Holiday holiday : holidays) {
            if (holiday.getDate().equals(LocalDate.now())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AlertDialog);
                builder.setTitle("Кажется сегодня праздничный день!")
                        .setMessage("Возможно, транспорт ходит с изменениями в маршруте или по расписанию выходного дня. За подробной информацией обратитесь в автопарк.")
                        .setCancelable(false)
                        .setNegativeButton("Понятно",
                                (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        int x = item.getItemId();
        switch (x) {
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
    protected void onDestroy() {
        //Log.d(TAG, "onDestroy()");
        super.onDestroy();
        boolean mInstanceState = false;
        if (!mInstanceState) {
            //Log.d(TAG, "Database - close");
            App.getInstance().getDatabase().close();
        }

    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment != null && fragment instanceof AppBaseFragment) {
            ((AppBaseFragment) fragment).scrollToTop();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mFragment.onDestroyOptionsMenu();
        switch (item.getItemId()) {
            case R.id.menu_item_1:
                mFragment = new TabRouteFragment();
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_item_1));
                break;
            case R.id.menu_item_2:
                mFragment = new StopFragment();
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_item_2));
                break;
            case R.id.menu_item_3:
                mFragment = new BookmarkFragment();
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_item_3));
                break;
            default:
                return false;
        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.fade_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.fade_out  // popExit
                )
                .replace(R.id.frame_layout, mFragment)
                .commit();
        return true;
    }
}
