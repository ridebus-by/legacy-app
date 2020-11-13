package org.xtimms.trackbus.activity.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.AppBaseActivity;
import org.xtimms.trackbus.util.LogUtils;
import org.xtimms.trackbus.util.TextUtils;

public class SettingsActivity extends AppBaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    public static final String ACTION_SETTINGS_APPEARANCE = "org.xtimms.trackbus.ACTION_SETTINGS_APPEARANCE";
    public static final String ACTION_SETTINGS_GENERAL = "org.xtimms.trackbus.ACTION_SETTINGS_GENERAL";
    public static final String ACTION_SETTINGS_DEBUG = "org.xtimms.trackbus.DEBUG_SETTINGS_GENERAL";

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
            case ACTION_SETTINGS_DEBUG:
                mFragment = new DebugSettingsFragment();
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
        switch (preference.getKey()) {
            case "bugreport":
                LogUtils.sendLog(this);
                return true;
            default:
                return false;
        }
    }
}
