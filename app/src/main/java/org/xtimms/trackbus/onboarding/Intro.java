package org.xtimms.trackbus.onboarding;

import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.MainActivity;

public class Intro extends AppIntro2 {
    @Override
    public void init(Bundle savedInstanceState) {

// Здесь указываем количество слайдов, например нам нужно 4
        addSlide(Slide.newInstance(R.layout.intro_1)); //
        addSlide(Slide.newInstance(R.layout.intro_2));
        addSlide(Slide.newInstance(R.layout.intro_3));
        addSlide(Slide.newInstance(R.layout.intro_4));

    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onNextPressed() {
        // Do something here
    }

    @Override
    public void onDonePressed() {
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something here
    }

}
