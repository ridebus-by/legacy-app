package org.xtimms.trackbus.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import org.xtimms.trackbus.R;

public abstract class ThemeUtils {

	private static final int[] APP_THEMES = new int[]{
			R.style.Theme_RideBus,
			R.style.Theme_RideBus_LightBlue,
			R.style.Theme_RideBus_DarkBlue
	};

	@ColorInt
	public static int getThemeAttrColor(Context context, @AttrRes int resId) {
		TypedArray a = context.getTheme().obtainStyledAttributes(getAppThemeRes(context), new int[] { resId });
		int color = a.getColor(0, 0);
		a.recycle();
		return color;
	}

	public static Drawable getAttrDrawable(Context context, @AttrRes int resId) {
		TypedValue typedValue = new TypedValue();
		TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { resId });
		Drawable drawable = a.getDrawable(0);
		a.recycle();
		return drawable;
	}

	public static int getAppThemeRes(Context context) {
		return APP_THEMES[getAppTheme(context)];
	}

	@StyleRes
	public static int getAppThemeRes(int index) {
		return APP_THEMES[index];
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
