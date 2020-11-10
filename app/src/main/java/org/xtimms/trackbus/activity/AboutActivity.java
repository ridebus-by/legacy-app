package org.xtimms.trackbus.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import org.xtimms.trackbus.util.ConstantUtils;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.util.AppUtils;

public class AboutActivity extends AppBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initView();
    }

    public void initView() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show);
        ScrollView scroll_about = findViewById(R.id.scroll_about);
        scroll_about.startAnimation(animation);

        LinearLayout ll_card_about_2_google_play = findViewById(R.id.ll_card_about_2_google_play);
        LinearLayout ll_card_about_2_email = findViewById(R.id.ll_card_about_2_email);
        LinearLayout ll_card_about_2_report = findViewById(R.id.ll_card_about_2_report);
        ll_card_about_2_google_play.setOnClickListener(this);
        ll_card_about_2_email.setOnClickListener(this);
        ll_card_about_2_report.setOnClickListener(this);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setStartOffset(600);

        TextView tv_about_version = findViewById(R.id.tv_about_version);
        tv_about_version.setText(AppUtils.getVersionName(this));
        tv_about_version.startAnimation(alphaAnimation);
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

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        int x = view.getId();
        switch (x) {
            case R.id.ll_card_about_2_google_play:
                intent.setData(Uri.parse(ConstantUtils.APP_URL));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;

            case R.id.ll_card_about_2_email:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(ConstantUtils.EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_email_intent));
                //intent.putExtra(Intent.EXTRA_TEXT, "Hi,");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(AboutActivity.this, getString(R.string.about_not_found_email), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_card_about_2_report:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(ConstantUtils.EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_report_intent));
                //intent.putExtra(Intent.EXTRA_TEXT, "Hi,");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(AboutActivity.this, getString(R.string.about_not_found_email), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
