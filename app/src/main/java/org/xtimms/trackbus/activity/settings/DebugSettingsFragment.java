package org.xtimms.trackbus.activity.settings;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import org.xtimms.trackbus.R;

public class DebugSettingsFragment extends PreferenceFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        Preference.OnPreferenceClickListener onPreferenceClickListener = (Preference.OnPreferenceClickListener) activity;
        Preference b = findPreference("bugreport");
        b.setOnPreferenceClickListener(onPreferenceClickListener);
        Preference t = findPreference("checkdb");
        t.setOnPreferenceClickListener(onPreferenceClickListener);
        Preference s = findPreference("checkserver");
        s.setOnPreferenceClickListener(onPreferenceClickListener);
        Preference l = findPreference("viewlog");
        l.setOnPreferenceClickListener(onPreferenceClickListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_debug);
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity != null) {
            activity.setTitle(R.string.debug);
        }
    }

}
