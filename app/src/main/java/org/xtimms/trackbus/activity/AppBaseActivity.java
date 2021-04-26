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

	@Override
	public void setSupportActionBar(@Nullable Toolbar toolbar) {
		super.setSupportActionBar(toolbar);
		mActionBarVisible = toolbar != null;
	}

	public void setSupportActionBar(@IdRes int toolbarId) {
		setSupportActionBar(findViewById(toolbarId));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home && mHomeAsUpEnabled) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}

