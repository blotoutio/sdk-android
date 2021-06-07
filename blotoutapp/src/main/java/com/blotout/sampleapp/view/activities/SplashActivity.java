

package com.blotout.sampleapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.blotout.BlotoutAnalytics;
import com.blotout.sampleapp.R;

import java.util.HashMap;


public class SplashActivity extends FragmentActivity {

    private Animation animation;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.logo_img);
        if (savedInstanceState == null) {
            flyIn();
        }
    }

    private void flyIn() {
        animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(animation);
    }



    public void launchBlotoutApp(View view) {
        HashMap<String,Object> eventInfo = new HashMap<>();
        eventInfo.put("Launch Blotout app",0);
        BlotoutAnalytics.INSTANCE.capture("custom",eventInfo);
        Intent intent = new Intent(getApplicationContext(),
                ECartHomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void getDemo(View view) {
        HashMap<String,Object> eventInfo = new HashMap<>();
        eventInfo.put("Get Blotout Demo",0);
        BlotoutAnalytics.INSTANCE.capture("custom",eventInfo);
    }

    public void joinSlack(View view) {
        HashMap<String,Object> eventInfo = new HashMap<>();
        eventInfo.put("Join Blotout Slack",0);
        BlotoutAnalytics.INSTANCE.capture("custom",eventInfo);
    }

}
