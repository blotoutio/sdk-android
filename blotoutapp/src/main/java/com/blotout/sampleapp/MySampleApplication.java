package com.blotout.sampleapp;

import android.app.Application;

import com.analytics.blotout.BlotoutAnalytics;
import com.analytics.blotout.BlotoutAnalyticsConfiguration;
import com.analytics.blotout.model.CompletionHandler;

public class MySampleApplication extends Application {

    public static final String TAG = MySampleApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        BlotoutAnalyticsConfiguration blotoutAnalyticsConfiguration = new BlotoutAnalyticsConfiguration();
        blotoutAnalyticsConfiguration.setBlotoutSDKKey("4JN7CGWHSRPSUSD");
        blotoutAnalyticsConfiguration.setEndPointUrl("https://sandbox.blotout.io/sdk/");
        BlotoutAnalytics.INSTANCE.init(this, blotoutAnalyticsConfiguration, new CompletionHandler() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

}