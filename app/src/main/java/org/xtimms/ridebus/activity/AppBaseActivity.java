package org.xtimms.ridebus.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import org.xtimms.ridebus.util.ThemeUtils;

public abstract class AppBaseActivity extends AppCompatActivity {

    private boolean mActionBarVisible = false;
    private int mTheme = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTheme = ThemeUtils.getAppTheme(this);
        setTheme(ThemeUtils.getAppThemeRes(mTheme));
    }

    public int getActivityTheme() {
        return mTheme;
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        mActionBarVisible = toolbar != null;
    }

    public void setSupportActionBar(@IdRes int toolbarId) {
        setSupportActionBar(findViewById(toolbarId));
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
}
