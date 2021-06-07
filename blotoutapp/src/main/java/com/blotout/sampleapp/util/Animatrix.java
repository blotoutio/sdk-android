

package com.blotout.sampleapp.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

public class Animatrix {



    public static void circularRevealView(View revealLayout) {

        int cx = revealLayout.getWidth() / 2;
        int cy = revealLayout.getHeight() / 2;

        float finalRadius = Math.max(revealLayout.getWidth(), revealLayout.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularReveal = ViewAnimationUtils.createCircularReveal(revealLayout, cx, cy, 0, finalRadius);

            circularReveal.setDuration(1000);

            // make the view visible and start the animation
            revealLayout.setVisibility(View.VISIBLE);

            circularReveal.start();
        } else {
            revealLayout.setVisibility(View.VISIBLE);
        }
    }

    public static Animator exitReveal(final View myView) {

        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;


        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });
        }
        return anim;
    }


}
