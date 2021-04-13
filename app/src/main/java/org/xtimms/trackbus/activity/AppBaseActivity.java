package org.xtimms.trackbus.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import org.xtimms.trackbus.util.ThemeUtils;

public abstract class AppBaseActivity extends AppCompatActivity {

	private boolean mActionBarVisible = false;
	private boolean mHomeAsUpEnabled = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getDelegate().setLocalNightMode(
				AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
	}

	public void enableHomeAsUp() {
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null && !mHomeAsUpEnabled) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			mHomeAsUpEnabled = true;
		}
	}

	public void disableTitle() {
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayShowTitleEnabled(false);
		}
	}

	@Override
	public void setSupportActionBar(@Nullable Toolbar toolbar) {
		super.setSupportActionBar(toolbar);
		mActionBarVisible = toolbar != null;
	}

	public void setSupportActionBar(@IdRes int toolbarId) {
		setSupportActionBar(findViewById(toolbarId));
	}

	public void hideActionBar() {
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null && mActionBarVisible) {
			mActionBarVisible = false;
			actionBar.hide();
		}
	}

	public void showActionBar() {
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null && !mActionBarVisible) {
			mActionBarVisible = true;
			actionBar.show();
		}
	}

	public void toggleActionBar() {
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			if (!mActionBarVisible) {
				mActionBarVisible = true;
				actionBar.show();
			} else {
				mActionBarVisible = false;
				actionBar.hide();
			}
		}
	}

	public boolean isActionBarVisible() {
		return mActionBarVisible;
	}

	public void setSubtitle(@Nullable CharSequence subtitle) {
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setSubtitle(subtitle);
		}
	}

	public void enableTransparentStatusBar(@ColorRes int color) {
		if (Build.VERSION.SDK_INT >= 21) {
			Window window = getWindow();
			window.getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			if (color != 0) {
				window.setStatusBarColor(ContextCompat.getColor(this, color));
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home && mHomeAsUpEnabled) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	protected void stub() {
		Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
	}
}

