package org.xtimms.trackbus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class AppSettings {

	@Nullable
	private static WeakReference<AppSettings> sInstanceReference = null;

	private final SharedPreferences mPreferences;

	private AppSettings(Context context) {
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
	}

	public static AppSettings get(Context context) {
		AppSettings instance = sInstanceReference == null ? null : sInstanceReference.get();
		if (instance == null) {
			instance = new AppSettings(context);
			sInstanceReference = new WeakReference<>(instance);
		}
		return instance;
	}

	public int getAppTheme() {
		try {
			return Integer.parseInt(mPreferences.getString("theme", "0"));
		} catch (Exception e) {
			return 0;
		}
	}
}
