package org.xtimms.trackbus.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import org.xtimms.trackbus.R;

public class TestDialog implements View.OnClickListener {

    private final AlertDialog mDialog;

    @SuppressLint("InflateParams")
    public TestDialog(Context context) {
        View mContentView = LayoutInflater.from(context)
                .inflate(R.layout.test_dialog, null, false);
        Toolbar mToolbar = mContentView.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        mToolbar.setNavigationOnClickListener(this);

        mDialog = new AlertDialog.Builder(context)
                .setView(mContentView)
                .create();
    }

    public void showSave() {
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        mDialog.dismiss();
    }
}
