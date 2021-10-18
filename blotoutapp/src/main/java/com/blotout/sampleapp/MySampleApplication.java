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
        blotoutAnalyticsConfiguration.setBlotoutSDKKey("EADAH5FV8B5MMVZ");
        blotoutAnalyticsConfiguration.setEndPointUrl("https://stage.blotout.io/sdk/");
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