package org.xtimms.trackbus.activity.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.xtimms.trackbus.R;

public class TabsSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_tabs);
    }

}
