package com.blotout.sampleapp;

import android.app.Application;

import com.blotout.BlotoutAnalytics;
import com.blotout.BlotoutAnalyticsConfiguration;

public class MySampleApplication extends Application {

    public static final String TAG = MySampleApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        BlotoutAnalyticsConfiguration blotoutAnalyticsConfiguration = new BlotoutAnalyticsConfiguration();
        blotoutAnalyticsConfiguration.setBlotoutSDKKey("KHPREXFRED7HMGB");
        blotoutAnalyticsConfiguration.setEndPointUrl("https://stage.blotout.io/sdk/");
        BlotoutAnalytics.INSTANCE.init(this,blotoutAnalyticsConfiguration);
    }

}