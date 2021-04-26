package com.blotout.app;

import android.app.Application;
import android.content.res.Configuration;

import com.blotout.BlotoutAnalytics;
import com.blotout.BlotoutAnalyticsConfiguration;


public class BlotoutApplication extends Application {

    private static final String TAG = "BlotoutApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        //registerActivityLifecycleCallbacks(BOAnalyticsActivityLifecycleCallbacks.getInstance());

        ///Initialize SDK here
       // BlotoutAnalytics.getInstance().initializeAnalyticsEngine(this,"PGH92NV367NA5ZX","http://dev.blotout.io");


        BlotoutAnalyticsConfiguration blotoutAnalyticsConfiguration = new BlotoutAnalyticsConfiguration();
        blotoutAnalyticsConfiguration.setBlotoutSDKKey("KHPREXFRED7HMGB");
        blotoutAnalyticsConfiguration.setEndPointUrl("https://stage.blotout.io/sdk/");
        BlotoutAnalytics.INSTANCE.init(this,blotoutAnalyticsConfiguration);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void init() {

    }
}
