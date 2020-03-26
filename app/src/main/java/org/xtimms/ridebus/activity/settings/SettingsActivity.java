package org.xtimms.ridebus.activity.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import org.xtimms.ridebus.R;
import org.xtimms.ridebus.activity.AppBaseActivity;
import org.xtimms.ridebus.util.TextUtils;

public class SettingsActivity extends AppBaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    public static final String ACTION_SETTINGS_APPEARANCE = "org.xtimms.trackbus.ACTION_SETTINGS_APPEARANCE";
    public static final String ACTION_SETTINGS_GENERAL = "org.xtimms.trackbus.ACTION_SETTINGS_GENERAL";

    public static final int RESULT_RESTART = Activity.RESULT_FIRST_USER + 1;

    private PreferenceFragment mFragment;
    private SharedPreferences mDefaultPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar(R.id.toolbar);
        enableHomeAsUp();
        mDefaultPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String action = TextUtils.notNull(getIntent().getAction());
        switch (action) {
            case ACTION_SETTINGS_APPEARANCE:
                mFragment = new AppearanceSettingsFragment();
                break;
            case ACTION_SETTINGS_GENERAL:
                mFragment = new GeneralSettingsFragment();
                break;
            default:
                finish();
        }
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, mFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDefaultPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        mDefaultPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("theme".equals(key)) {
            setResult(RESULT_RESTART);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}
