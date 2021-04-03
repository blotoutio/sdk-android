package com.blotout.analytics;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.blotout.constants.BOCommonConstants;
import com.blotout.events.BOAEvents;
import com.blotout.events.BOAppSessionEvents;
import com.blotout.model.session.BOAppNavigation;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Blotout on 17,November,2019
 */


public class BOAnalyticsActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {
    private static final String TAG = "BOAnalyticsActivityLife";

    private static final String BO_FIRST_ACTIVITY = "BOFirstActivity";
    private static BOAnalyticsActivityLifecycleCallbacks instance;
    private boolean shouldRecordScreenViews = true;
    private AtomicBoolean firstLaunch;
    private boolean appInForeground;
    public String activityName;

    public static BOAnalyticsActivityLifecycleCallbacks getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            instance = new BOAnalyticsActivityLifecycleCallbacks();
        }
        return instance;
    }

    private BOAnalyticsActivityLifecycleCallbacks() {
        firstLaunch = new AtomicBoolean(false);
        this.activityName = "";
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        try {
            if (!firstLaunch.get()) {
                firstLaunch.set(true);
                trackApplicationLifecycleEvents();
                    if (BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
                        BOAppSessionEvents.getInstance().applicationDidFinishLaunchingNotification();
                    }

            }

            if (BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
                String savedActivityName = BOSharedPreferenceImpl.getInstance().getString(BO_FIRST_ACTIVITY);
                if(savedActivityName == null || savedActivityName.length() == 0) {
                    String currentActivityName = getExactActivityName(activity.getLocalClassName());
                    BOSharedPreferenceImpl.getInstance().saveString(BO_FIRST_ACTIVITY, currentActivityName);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public String getExactActivityName(String localClassName) {
        try {
            String newActivityName = localClassName;
            String[] stringArray = localClassName.split("\\.");
            if (stringArray.length > 0) {
                newActivityName = stringArray[stringArray.length - 1];
            }
            return newActivityName;

        } catch (Exception e) {
            //throw new AssertionError("Activity Not Found: " + e.toString());
            Logger.INSTANCE.e(TAG, e.toString());
            return localClassName;
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (shouldRecordScreenViews) {
            recordScreenViews(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        try {
            if (!appInForeground) {
                appInForeground = true;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    @Override
    public void onActivityPaused(Activity activity) { }

    @Override
    public void onActivityStopped(@NonNull Activity activity) { }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }

    @Override
    public void onActivityDestroyed(Activity activity) {
        try {
            if (BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
                String firstActivityId = BOSharedPreferenceImpl.getInstance().getString(BO_FIRST_ACTIVITY);
                String currentActivityName = getExactActivityName(activity.getLocalClassName());
                if (currentActivityName.equals(firstActivityId) && firstLaunch.get()) {
                    // Event Application Did Finished
                    firstLaunch.set(false);
                    BOSharedPreferenceImpl.getInstance().removeKey(BO_FIRST_ACTIVITY);

                    BOAEvents.unRegisterDayChangeEvent();
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void trackApplicationLifecycleEvents() {
        try {
            if (BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
                // Get the current version.
                PackageInfo packageInfo = getPackageInfo(BOSharedManager.getInstance().getContext());
                String currentVersion = packageInfo.versionName;
                int currentBuild = packageInfo.versionCode;

                // Get the previous recorded version.
                BOSharedPreferenceImpl sharedPreferences = BOSharedPreferenceImpl.getInstance();
                String previousVersion = sharedPreferences.getString(BOCommonConstants.BO_VERSION_KEY, null);
                int previousBuild = sharedPreferences.getInt(BOCommonConstants.BO_BUILD_KEY, -1);

                // Check and track Application Installed or Application Updated.
                if (previousBuild == -1) {
                    //"Application Installed"
                    sharedPreferences.saveString(BOCommonConstants.BO_VERSION_KEY, currentVersion);
                    sharedPreferences.saveInteger(BOCommonConstants.BO_BUILD_KEY, currentBuild);
                } else if (currentBuild != previousBuild) {

                    // "Application Updated",
                    sharedPreferences.saveString(BOCommonConstants.BO_VERSION_KEY, currentVersion);
                    sharedPreferences.saveInteger(BOCommonConstants.BO_BUILD_KEY, currentBuild);
                    sharedPreferences.saveString("previous_" + BOCommonConstants.BO_VERSION_KEY, previousVersion);
                    sharedPreferences.saveInteger("previous_" + BOCommonConstants.BO_BUILD_KEY, previousBuild);

                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public static PackageInfo getPackageInfo(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    private void recordScreenViews(@NonNull Activity activity) {
        try {
            this.activityName = getExactActivityName(activity.getLocalClassName());
            recordAppNavigation();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void recordAppNavigation() {
        try {
            BOAppSessionDataModel appSessionData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null);
            BOSharedManager extentionManager = BOSharedManager.getInstance();

            if (extentionManager.currentNavigation != null) {
                List<BOAppNavigation> appNavigationArray = appSessionData.getSingleDaySessions().getUbiAutoDetected().getAppNavigation();
                if (extentionManager.currentTime > 0.0) {
                    if (extentionManager.currentTimer != null) {
                        extentionManager.currentTimer.cancel();
                    }
                    extentionManager.currentNavigation.setTimeSpent(extentionManager.currentTime);
                }

                appNavigationArray.add(extentionManager.currentNavigation);

                appSessionData.getSingleDaySessions().getUbiAutoDetected().setAppNavigation(appNavigationArray);
            }

            BOAppNavigation navObject = new BOAppNavigation();
            if(extentionManager.currentNavigation != null) {
                navObject.setFrom(extentionManager.currentNavigation.getTo()!= null ? extentionManager.currentNavigation.getTo() : this.activityName);
            } else {
                navObject.setFrom(this.activityName);
            }

            navObject.setTo(this.activityName);
            extentionManager.currentNavigation = navObject;


            if (extentionManager.currentTimer != null) {
                extentionManager.currentTimer.cancel();
            }
            extentionManager.currentTimer = createTimer();
            extentionManager.currentTime = 1;

            //Funnel execution and testing based
            //BOFunnelSyncController.getInstance().recordNavigationEventFrom(navObject.getFrom(), navObject.getTo(), new HashMap<>());
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private Timer createTimer() {
        Timer timer = new Timer();
        TimerTask task = new Helper();
        timer.schedule(task, 0,1000);
        return timer;
    }

    @Override
    public void onTrimMemory(int level) {

        try {
            if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN && appInForeground) {
                // lifecycleDelegate instance was passed in on the constructor
                appInForeground = false;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    class Helper extends TimerTask {
        public void run() {
            BOSharedManager extentionManager = BOSharedManager.getInstance();
            extentionManager.currentTime += 1;
        }
    }
}

