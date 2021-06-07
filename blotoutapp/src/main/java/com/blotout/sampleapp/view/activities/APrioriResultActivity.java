

package com.blotout.sampleapp.view.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.blotout.sampleapp.R;
import com.blotout.sampleapp.util.Animatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class APrioriResultActivity extends AppCompatActivity  {


    View appRoot;
    Toolbar toolbar;


    List<String> setEntries = new ArrayList<>();
    Toast mCurrentToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apriori_result);
        appRoot = (View) findViewById(R.id.app_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        ViewTreeObserver viewTreeObserver = appRoot.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    Animatrix.circularRevealView(appRoot);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        appRoot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        appRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apriori, menu);
        return true;
    }



    @Override
    public void onBackPressed() {
        animateExitScreen();
    }

    private void animateExitScreen() {

        //Slide out toolbar to Distract user
        toolbar.animate().y(-500f).setDuration(500);

        //Circular exit Animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = Animatrix.exitReveal(appRoot);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    supportFinishAfterTransition();
                }
            });

            anim.start();
        } else {
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
