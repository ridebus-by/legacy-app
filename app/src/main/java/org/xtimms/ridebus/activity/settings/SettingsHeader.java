package org.xtimms.ridebus.activity.settings;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

public final class SettingsHeader {

	public final int id;
	public String title;
	String summary;
	public Drawable icon;

	@StringRes
	private final int actionText;
	@IdRes
	private final int actionId;

	SettingsHeader(@NonNull Context context, int id, @StringRes int title, @DrawableRes int icon) {
		this(context, id, title, 0, icon, 0, 0);
	}

	private SettingsHeader(@NonNull Context context, int id, @StringRes int title, @StringRes int summary, @DrawableRes int icon, @StringRes int actionText, @IdRes int actionId) {
		this.id = id;
		this.title = context.getString(title);
		this.icon = ContextCompat.getDrawable(context, icon);
		this.summary = summary == 0 ? null : context.getString(summary);
		this.actionText = actionText;
		this.actionId = actionId;
	}

	boolean hasAction() {
		return actionText != 0 && actionId != 0;
	}
}
