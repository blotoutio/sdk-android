package com.blotout.newsticker;

import android.app.Application;
import android.content.res.Configuration;

import com.blotout.analytics.BlotoutAnalytics;

import java.util.HashMap;


public class NewsTickerApplication extends Application {

    private static final String TAG = "NewsTickerApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        BlotoutAnalytics.getInstance().setEnabled(true);
        BlotoutAnalytics.getInstance().initializeAnalyticsEngine(this,"2M7U3RMRM54VJ36","http://dev.blotout.io");

        init();
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
        HashMap<String,Object> item2 = new HashMap<>();
        item2.put("product","iPhone");
        item2.put("color","green");
        BlotoutAnalytics.getInstance().logEvent("InCart",item2);

        BlotoutAnalytics.getInstance().logEvent("LoginView",null);
        BlotoutAnalytics.getInstance().logEvent("Item Selected",null);
        BlotoutAnalytics.getInstance().logEvent("Add To Cart",null);
        BlotoutAnalytics.getInstance().logEvent("List Item Cart View",null);

        BlotoutAnalytics.getInstance().logEvent("LoginView",null);
    }
}
