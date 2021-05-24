package org.xtimms.trackbus.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.settings.DebugSettingsFragment;
import org.xtimms.trackbus.util.AppUtils;
import org.xtimms.trackbus.util.ConstantUtils;

import static org.xtimms.trackbus.R.anim;
import static org.xtimms.trackbus.R.id;
import static org.xtimms.trackbus.R.layout;
import static org.xtimms.trackbus.R.string;

public class AboutActivity extends AppBaseActivity implements View.OnClickListener {

    private PreferenceFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_about);

        Toolbar toolbar = findViewById(id.toolbar_about);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initView();
    }

    @SuppressLint("SetTextI18n")
    public void initView() {
        Animation animation = AnimationUtils.loadAnimation(this, anim.anim_about_card_show);
        ScrollView scroll_about = findViewById(id.scroll_about);
        scroll_about.startAnimation(animation);

        LinearLayout ll_card_about_2_google_play = findViewById(id.ll_card_about_2_google_play);
        LinearLayout ll_card_about_2_email = findViewById(id.ll_card_about_2_email);
        LinearLayout ll_card_about_2_report = findViewById(id.ll_card_about_2_report);
        LinearLayout ll_card_about_2_rumblur = findViewById(id.ll_card_about_2_rumblur);
        LinearLayout ll_card_about_2_telegram = findViewById(id.ll_card_about_2_telegram);
        ll_card_about_2_google_play.setOnClickListener(this);
        ll_card_about_2_email.setOnClickListener(this);
        ll_card_about_2_report.setOnClickListener(this);
        ll_card_about_2_rumblur.setOnClickListener(this);
        ll_card_about_2_telegram.setOnClickListener(this);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setStartOffset(600);

        TextView tv_about_version = findViewById(id.tv_about_version);
        TextView tv_db_version = findViewById(id.tv_db_version);
        tv_about_version.setText(AppUtils.getVersionName(this));
        tv_db_version.setText(getResources().getString(string.db_version) + " " + ConstantUtils.getDbVersion());
        tv_about_version.startAnimation(alphaAnimation);
        tv_db_version.startAnimation(alphaAnimation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        //When home is clicked
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        int x = view.getId();
        switch (x) {
            case id.ll_card_about_2_google_play:
                intent.setData(Uri.parse(ConstantUtils.APP_URL));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;

            case id.ll_card_about_2_email:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(ConstantUtils.EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(string.about_email_intent));
                //intent.putExtra(Intent.EXTRA_TEXT, "Hi,");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(AboutActivity.this, getString(string.about_not_found_email), Toast.LENGTH_SHORT).show();
                }
                break;
            case id.ll_card_about_2_report:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(ConstantUtils.EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(string.about_report_intent));
                //intent.putExtra(Intent.EXTRA_TEXT, "Hi,");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(AboutActivity.this, getString(string.about_not_found_email), Toast.LENGTH_SHORT).show();
                }
                break;
            case id.ll_card_about_2_rumblur:
                intent.setData(Uri.parse(ConstantUtils.RUMBLUR));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;
            case id.ll_card_about_2_telegram:
                intent.setData(Uri.parse(ConstantUtils.TELEGRAM));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;
        }
    }

}
