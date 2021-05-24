package org.xtimms.trackbus.util;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

public abstract class ThemeUtils {

	public static int getAttrColor(Context context, @ColorInt int colorAttrId) {
		TypedValue typedValue = new  TypedValue();
		context.getTheme().resolveAttribute(colorAttrId, typedValue, true);
		return typedValue.data;
	}

	public static int getAppTheme(Context context) {
		return AppSettings.get(context).getAppTheme();
	}

	public static boolean isAppThemeDark(Context context) {
		return getAppTheme(context) > 13;
	}

	public static boolean isAppThemeNotDark(Context context) {
		return getAppTheme(context) < 13;
	}
}
