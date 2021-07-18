

package com.blotout.sampleapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.analytics.blotout.BlotoutAnalytics;
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
        BlotoutAnalytics.INSTANCE.capture("custom event",eventInfo);
        BlotoutAnalytics.INSTANCE.capturePersonal("custom phi event",eventInfo,true);
        BlotoutAnalytics.INSTANCE.capturePersonal("custom pii event",eventInfo,false);
        Intent intent = new Intent(getApplicationContext(), ECartHomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void getDemo(View view) {
        HashMap<String,Object> eventInfo = new HashMap<>();
        eventInfo.put("Get Blotout Demo",0);
        BlotoutAnalytics.INSTANCE.capture("custom",eventInfo);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, "sales@blotout.io");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Get Demo");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Select email"));
    }

    public void joinSlack(View view) {
        HashMap<String,Object> eventInfo = new HashMap<>();
        eventInfo.put("Join Blotout Slack",0);
        BlotoutAnalytics.INSTANCE.capture("custom",eventInfo);
    }

}
