package org.xtimms.ridebus.activity.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import org.xtimms.ridebus.R;
import org.xtimms.ridebus.util.PreferencesUtils;

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
}
