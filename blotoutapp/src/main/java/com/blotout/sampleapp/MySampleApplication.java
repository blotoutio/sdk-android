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
        blotoutAnalyticsConfiguration.setBlotoutSDKKey("X5THPQXV3K5D9GA");
        blotoutAnalyticsConfiguration.setEndPointUrl("https://sandbox.blotout.io/sdk/");
        BlotoutAnalytics.INSTANCE.init(this, blotoutAnalyticsConfiguration, new CompletionHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
    }

}