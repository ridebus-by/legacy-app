package org.xtimms.trackbus.activity.settings;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.util.PreferencesUtils;

public final class AppearanceSettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_appearance);
		PreferencesUtils.bindSummaryMultiple(
				this,
				"theme"
		);
	}

	@Override
	public void onResume() {
		super.onResume();
		Activity activity = getActivity();
		if (activity != null) {
			activity.setTitle(R.string.appearance);
		}
	}

}
