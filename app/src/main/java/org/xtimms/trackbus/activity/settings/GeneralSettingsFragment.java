package org.xtimms.trackbus.activity.settings;

import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import org.xtimms.trackbus.App;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.util.PreferencesUtils;

public class GeneralSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PreferencesUtils.bindPreferenceSummary(findPreference("lang"), (preference, newValue) -> {
            App.setLanguage(preference.getContext().getApplicationContext().getResources(), (String) newValue);
            int index = ((ListPreference) preference).findIndexOfValue((String) newValue);
            String summ = ((ListPreference) preference).getEntries()[index].toString();
            preference.setSummary(summ);
            Toast.makeText(preference.getContext(), R.string.need_restart, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (activity != null) {
            activity.setTitle(R.string.general);
        }
    }
}
