package com.bo.salesdemo.activity;

import android.app.Application;
import android.content.res.Configuration;

import com.blotout.analytics.BlotoutAnalytics;


public class BlotoutApplication extends Application {

    private static final String TAG = "BlotoutApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        //registerActivityLifecycleCallbacks(BOAnalyticsActivityLifecycleCallbacks.getInstance());

        ///Initialize SDK here
        //ZM96SHRF7F2V7QB , PGH92NV367NA5ZX
        //5DNGP7DR2KD9JSY, KV4HBCWXY65DTVT
        //2M7U3RMRM54VJ36
        BlotoutAnalytics.getInstance().setEnabled(true);
        //BlotoutAnalytics.getInstance().initializeAnalyticsEngine(this,"YVQMSUX5VJUM8XN","http://dev.blotout.io");
        //BlotoutAnalytics.getInstance().initializeAnalyticsEngine(this,"3Y465G7CJ48P9ZY","https://sales.blotout.io");
        BlotoutAnalytics.getInstance().initializeAnalyticsEngine(this,"7KAXV3Z2EGAPHGU","https://stage.blotout.io");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        BlotoutAnalytics.getInstance().onAppTerminate();
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
