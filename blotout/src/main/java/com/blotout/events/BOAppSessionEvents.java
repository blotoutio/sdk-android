package com.blotout.events;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.blotout.Controllers.BODeviceAndAppFraudController;
import com.blotout.Controllers.BOFunnelSyncController;
import com.blotout.analytics.BOAnalyticsActivityLifecycleCallbacks;
import com.blotout.constants.BONetworkConstants;
import com.blotout.eventsExecutor.BOGeoRetentionOperationExecutorHelper;
import com.blotout.eventsExecutor.BOLifetimeOperationExecutorHelper;
import com.blotout.eventsExecutor.BONetworkFunnelExecutorHelper;
import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.deviceinfo.device.DeviceInfo;
import com.blotout.model.session.BOAppInfo;
import com.blotout.model.lifetime.BOAppLifetimeData;
import com.blotout.model.session.BOApp;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.model.session.BOScreenShotsTaken;
import com.blotout.model.session.BOSessionInfo;
import com.blotout.network.BOAPIFactory;
import com.blotout.network.api.BOBaseAPI;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.BODeviceDetection;
import com.blotout.utilities.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Blotout on 03,November,2019
 */
public class BOAppSessionEvents extends BOAEvents {

    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BOAppSessionEvents";
    public boolean isEnabled;
    public volatile HashMap<String, Object> sessionAppInfo;
    private long averageSessionDuration;
    private static volatile BOAppSessionEvents instance;
    private static final String boSessionWastMastHandlerKey = "boSessionWastMastHandlerKey";

    public static BOAppSessionEvents getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOAppSessionEvents.class) {
                if (instance == null) {
                    instance = new BOAppSessionEvents();
                }
            }
        }
        return instance;
    }

    private BOAppSessionEvents() {
        averageSessionDuration = 0;
        sessionAppInfo = new HashMap<>();
    }

    public void startRecordingEvents() {
        this.isEnabled = true;
    }

    public void stopRecordingEvnets() {
        this.isEnabled = false;
    }

    private void averageAppSessionDurationForTheDay() {
        try {
            recordSystemUptime(null);

            long terminationTimeStamp = BODateTimeUtils.get13DigitNumberObjTimeStamp();
            this.sessionAppInfo.put(BOCommonConstants.BO_TERMINATION_TIME_STAMP, terminationTimeStamp);

            Long launchTimeStamp = (Long) this.sessionAppInfo.get(BOCommonConstants.BO_LAUNCH_TIME_STAMP);
            long sessionDuration = terminationTimeStamp - launchTimeStamp.longValue();
            this.sessionAppInfo.put(BOCommonConstants.BO_SESSION_DURATION, sessionDuration);

            List<BOAppInfo> boAppInfos = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppInfo();
            long numberOfSesisons = boAppInfos.size() + 1;
            long allSessionDuration = sessionDuration;
            for (BOAppInfo appInfo : boAppInfos) {
                long duration = appInfo.getSessionsDuration() <= 0 ? appInfo.getSessionsDuration() : 0;
                allSessionDuration = allSessionDuration + duration;
            }
            averageSessionDuration = allSessionDuration / numberOfSesisons;
            this.sessionAppInfo.put(BOCommonConstants.BO_AVERAGE_SESSION_DURATION, averageSessionDuration);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    private void recordAppLaunched(BOAppSessionDataModel sInstance) {
        HashMap<String, Object> appState = new HashMap<>();
        appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
        appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
        appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
        appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_LAUNCHED));
        BOApp appStates = BOApp.fromJsonDictionary(appState);

        List<BOApp> existingData = sInstance.getSingleDaySessions().getAppStates().getAppLaunched();
        existingData.add(appStates);
        sInstance.getSingleDaySessions().getAppStates().setAppLaunched(existingData);

    }

    private void recordSessionInfo(BOAppSessionDataModel sInstance) {
        HashMap<String, Object> sessionInfo = new HashMap<>();
        sessionInfo.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
        sessionInfo.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
        sessionInfo.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_LAUNCHED));
        sessionInfo.put(BOCommonConstants.BO_START, BODateTimeUtils.get13DigitNumberObjTimeStamp());

        BOSessionInfo sessionInfoObj = BOSessionInfo.fromJsonDictionary(sessionInfo);

        List<BOSessionInfo> existingData = sInstance.getSingleDaySessions().getAppStates().getAppSessionInfo();
        existingData.add(sessionInfoObj);
        sInstance.getSingleDaySessions().getAppStates().setAppSessionInfo(existingData);

    }

    private void recordNotificationsInBackgroundWith(HashMap<String, Object> notificationData) {
        try {
            if (BOAEvents.isSessionModelInitialised) {


                BOAppSessionDataModel sInstance = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null);
                this.recordAppLaunched(sInstance);
                this.recordSessionInfo(sInstance);

                if (sInstance.getAppBundle() == null) {
                    String bundleIdentifier = BOSharedManager.getInstance().getContext().getPackageName();
                    sInstance.setAppBundle(bundleIdentifier);
                }

                if (sInstance.getDate() == null) {
                    String sessionDate = BODateTimeUtils.getStringFromDate(new Date(), BOCommonConstants.BO_DATE_FORMAT);
                    sInstance.setDate(sessionDate);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    private void recordSystemUptime(@Nullable Long time) {
        try {
            // Get the info about a process
            long systemUptime = SystemClock.uptimeMillis();
            // Get the uptime of the system
            Long systemUptimeN = time != null ? time : Long.valueOf(systemUptime);
            List<Long> existingSystemUptime = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getSystemUptime();
            existingSystemUptime.add(systemUptimeN);
            BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().setSystemUptime(existingSystemUptime);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordSessionOnDayChangeOrAppTermination() {
        try {

            if (averageSessionDuration <= 0) {
                averageAppSessionDurationForTheDay();
            }
            if (BOAEvents.isSessionModelInitialised && (this.sessionAppInfo.keySet().size() > 0)) {
                BOAppInfo appInfo = BOAppInfo.fromJsonDictionary(this.sessionAppInfo);
                List<BOAppInfo> existingAppInfo = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppInfo();
                existingAppInfo.add(appInfo);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().setAppInfo(existingAppInfo);
            }

            String jsonString = BOAppSessionDataModel.toJsonString(BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null));
            BOSharedPreferenceImpl.getInstance().saveString(BOCommonConstants.BO_ANALYTICS_SESSION_MODEL_DEFAULTS_KEY, jsonString);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordLifeTimeOnDayChangeOrAppTermination() {
        try {
            String lifeTimeModelJsonString = BOAppLifetimeData.toJsonString(BOAppLifetimeData.sharedInstanceFromJSONDictionary(null));
            BOSharedPreferenceImpl.getInstance().saveString(BOCommonConstants.BO_ANALYTICS_LIFETIME_MODEL_DEFAULTS_KEY, lifeTimeModelJsonString);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        //completionHandler(YES, nil);
    }

    public void resetAverageSessionDuration() {
        averageSessionDuration = 0;
    }

    public void appTerminationFunctionalityOnDayChange() {
        try {
            recordSessionOnDayChangeOrAppTermination();
            recordLifeTimeOnDayChangeOrAppTermination();
            resetAverageSessionDuration();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordAppInformation() {

        try {

            long launchTimeStamp = BODateTimeUtils.get13DigitNumberObjTimeStamp();
            this.sessionAppInfo.put(BOCommonConstants.BO_LAUNCH_TIME_STAMP, launchTimeStamp);

            PackageInfo packageInfo = BOAnalyticsActivityLifecycleCallbacks.getPackageInfo(BOSharedManager.getInstance().getContext());
            String currentVersion = packageInfo.versionName;
            int currentBuild = packageInfo.versionCode;
            this.sessionAppInfo.put(BONetworkConstants.BO_VERSION, currentVersion + currentBuild);
            String sdkVersion = BOCommonConstants.SDK_VERSION;
            this.sessionAppInfo.put(BOCommonConstants.BO_SDK_VERSION, sdkVersion);

            String bundleIdentifier = BOSharedManager.getInstance().getContext().getPackageName();
            this.sessionAppInfo.put(BOCommonConstants.BO_BUNDLE, bundleIdentifier);

            final String packageName = bundleIdentifier;
            PackageManager packageManager = BOSharedManager.getInstance().getContext().getPackageManager();
            String appName = "";
            try {
                appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            this.sessionAppInfo.put(BOCommonConstants.BO_NAME, appName);

            DeviceInfo mDeviceInfo = new DeviceInfo(BOSharedManager.getInstance().getContext());
            this.sessionAppInfo.put(BOCommonConstants.BO_LANGUAGE, mDeviceInfo.getLanguage());

            this.sessionAppInfo.put(BOCommonConstants.BO_PLATFORM, BODeviceDetection.getDevicePlatformCode());

            //get OS name
            String fieldName = null;
            int fieldValue = -1;
            Field[] fields = Build.VERSION_CODES.class.getFields();
            for (Field field : fields) {
                fieldName = field.getName();
                try {
                    fieldValue = field.getInt(new Object());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fieldValue == Build.VERSION.SDK_INT) {
                    break;
                }
            }
            this.sessionAppInfo.put(BOCommonConstants.BO_OS_NAME, fieldName);
            this.sessionAppInfo.put(BOCommonConstants.BO_OS_VERSION, Build.VERSION.RELEASE);
            this.sessionAppInfo.put(BOCommonConstants.BO_DEVICE_MFT, Build.MANUFACTURER);
            this.sessionAppInfo.put(BOCommonConstants.BO_DEVICE_MODEL, Build.MODEL);
            this.sessionAppInfo.put(BOCommonConstants.BO_VPN_STATUS, BOCommonUtils.checkVPN());
            this.sessionAppInfo.put(BOCommonConstants.BO_JBN_STATUS, BODeviceAndAppFraudController.getInstance().isDeviceJailbroken());
            this.sessionAppInfo.put(BOCommonConstants.BO_DCOMP_STATUS, BODeviceAndAppFraudController.getInstance().isDeviceCompromised());
            this.sessionAppInfo.put(BOCommonConstants.BO_ACOMP_STATUS, BODeviceAndAppFraudController.getInstance().isAppCompromised());

            HashMap<String, Object> cKnownLocation = getGeoIPAndPublish(false);
            this.sessionAppInfo.put(BOCommonConstants.BO_CURRENT_LOCATION, cKnownLocation);

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }

    }

    public HashMap<String, Object> getGeoIPAndPublish(boolean fetchNewLocation) {

        if (!BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
            return null;
        }

        try {
            BOSharedPreferenceImpl analyticsRootUD = BOSharedPreferenceImpl.getInstance();
            String currLocStr = analyticsRootUD.getString(BOCommonConstants.BO_ANALYTICS_CURRENT_LOCATION_DICT);
            HashMap<String, Object> cKnownLocation = currLocStr != null ? BOCommonUtils.getHashmapFromJsonString(currLocStr) : null;
            if (!fetchNewLocation && cKnownLocation != null) {
                return cKnownLocation;
            } else {
                if (cKnownLocation != null && cKnownLocation.keySet().size() > 0) {
                    long lastLocTimeStamp = (long) cKnownLocation.get(BOCommonConstants.BO_TIME_STAMP);
                    long currTime = BODateTimeUtils.get13DigitNumberObjTimeStamp();

                    long diffMilli = currTime - lastLocTimeStamp;
                    long dayMilli = 24 * 60 * 60 * 1000;
                    if (diffMilli < dayMilli) {
                        fetchNewLocation = false;
                    }
                } else {
                    fetchNewLocation = true;
                }

                if (fetchNewLocation) {
                    BOGeoRetentionOperationExecutorHelper.getInstance().post(new Runnable() {
                        @Override
                        public void run() {

                            BOAPIFactory api = BOSharedManager.getInstance().getAPIFactory();
                            Call<HashMap<String, Object>> call = api.geoAPI.getGeoData(BOBaseAPI.getInstance().getGeoPath());
                            try {
                                Response response = call.execute();
                                if (response.isSuccessful()) {
                                    HashMap<String, Object> geoInfo = new HashMap<>();
                                    HashMap<String, Object> geoData = (HashMap<String, Object>) ((HashMap<String, Object>) response.body()).get("geo");

                                    if (geoData.containsKey("couc")) {
                                        String country = (String) geoData.get("couc");
                                        geoInfo.put("country", country);
                                    }
                                    if (geoData.containsKey("reg")) {
                                        String state = (String) geoData.get("reg");
                                        geoInfo.put("state", state);
                                    }
                                    if (geoData.containsKey("city")) {
                                        String city = (String) geoData.get("city");
                                        geoInfo.put("city", city);
                                    }
                                    if (geoData.containsKey("zip")) {
                                        String zip = (String) geoData.get("zip");
                                        geoInfo.put("zip", zip);
                                    }
                                    if (geoData.containsKey("conc")) {
                                        String continentCode = (String) geoData.get("conc");
                                        geoInfo.put("continentCode", continentCode);
                                    }
                                    if (geoData.containsKey("lat")) {
                                        double latitude = (double) geoData.get("lat");
                                        ;
                                        geoInfo.put("latitude", latitude);
                                    }
                                    if (geoData.containsKey("long")) {
                                        double longitude = (double) geoData.get("long");
                                        geoInfo.put("longitude", longitude);
                                    }
                                    if (geoData.containsKey(BOCommonConstants.BO_TIME_STAMP)) {
                                        long timeStamp = (long) geoData.get(BOCommonConstants.BO_TIME_STAMP);

                                        if (timeStamp <= 0) {
                                            timeStamp = BODateTimeUtils.get13DigitNumberObjTimeStamp();
                                        }
                                        geoInfo.put(BOCommonConstants.BO_TIME_STAMP, timeStamp);
                                    } else {
                                        long timeStamp = BODateTimeUtils.get13DigitNumberObjTimeStamp();
                                        geoInfo.put(BOCommonConstants.BO_TIME_STAMP, timeStamp);
                                    }
                                    //Store all other information for future use
                                    if (geoInfo.keySet().size() > 0) {
                                        String currentLocStr = BOCommonUtils.getJsonStringFromHashMap(geoInfo);
                                        analyticsRootUD.saveString(BOCommonConstants.BO_ANALYTICS_CURRENT_LOCATION_DICT, currentLocStr);
                                    }
                                }
                            } catch (IOException e) {
                                Logger.INSTANCE.d(TAG, e.toString());
                            }

                        }
                    });
                }
                return cKnownLocation;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return null;
        }
    }

    public void postInitLaunchEventsRecording() {
        try {

            BOLifetimeOperationExecutorHelper.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    recordAppInformation();
                    recordSystemUptime(null);
                    recordNotificationsInBackgroundWith(null);
                }
            });

            BOLifetimeOperationExecutorHelper.getInstance().postDelayedWithKey(boSessionWastMastHandlerKey, new Runnable() {
                @Override
                public void run() {
                    BOLifeTimeAllEvent sharedLifeTimeEvents = BOLifeTimeAllEvent.getInstance();
                    sharedLifeTimeEvents.recordWASTWithPayload(0, null);
                    sharedLifeTimeEvents.recordMASTWithPayload(0, null);
                }
            }, 30 * 1000);

            BONetworkFunnelExecutorHelper.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    if (BOSharedPreferenceImpl.getInstance().isSDKFirstLaunched()) {
                        BOFunnelSyncController.getInstance().appLaunchedWithInfo(null);
                    }
                }
            });

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void applicationDidFinishLaunchingNotification() {
        try {
            //TODO: code move to postInitLaunchEventsRecording
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void applicationDidEnterBackgroundNotification() {
        try {
            if (BOAEvents.isSessionModelInitialised) {

                HashMap<String, Object> appState = new HashMap<>();
                appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_IN_BACKGROUND));
                BOApp appStates = BOApp.fromJsonDictionary(appState);
                List<BOApp> existingData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().getAppInBackground();
                existingData.add(appStates);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().setAppInBackground(existingData);

                BOFunnelSyncController.getInstance().appInBackgroundWithInfo(null);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void applicationWillEnterForegroundNotification() {
        try {
            if (BOAEvents.isSessionModelInitialised) {

                HashMap<String, Object> appState = new HashMap<>();
                appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_IN_FOREGROUND));

                appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                BOApp appStates = BOApp.fromJsonDictionary(appState);
                List<BOApp> existingData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().getAppInForeground();
                existingData.add(appStates);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().setAppInForeground(existingData);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void applicationDidBecomeActiveNotification() {
        try {
            if (BOAEvents.isSessionModelInitialised) {

                HashMap<String, Object> appState = new HashMap<>();
                appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_BECOME_ACTIVE));

                BOApp appStates = BOApp.fromJsonDictionary(appState);
                List<BOApp> existingData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().getAppActive();
                existingData.add(appStates);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().setAppActive(existingData);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void applicationWillResignActiveNotification() {
        try {
            if (BOAEvents.isSessionModelInitialised) {

                HashMap<String, Object> appState = new HashMap<>();
                appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_RESIGN_ACTIVE));


                BOApp appStates = BOApp.fromJsonDictionary(appState);
                List<BOApp> existingData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().getAppResignActive();
                existingData.add(appStates);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().setAppResignActive(existingData);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void applicationDidReceiveMemoryWarningNotification() {
        try {
            if (BOAEvents.isSessionModelInitialised) {

                HashMap<String, Object> appState = new HashMap<>();
                appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_MEMORY_WARNING));

                BOApp appStates = BOApp.fromJsonDictionary(appState);
                List<BOApp> existingData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().getAppReceiveMemoryWarning();
                existingData.add(appStates);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().setAppReceiveMemoryWarning(existingData);

            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    @SuppressLint("MissingPermission")
    public void applicationWillTerminateNotification() {
        try {

            if (BOAEvents.isSessionModelInitialised) {
                List<BOSessionInfo> existingData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().getAppSessionInfo();
                if (existingData != null && !existingData.isEmpty()) {
                    BOSessionInfo sessionInfo = existingData.get(existingData.size() - 1);
                    if (sessionInfo != null) {
                        sessionInfo.setEnd(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                        long duration = sessionInfo.getEnd() - sessionInfo.getStart();
                        sessionInfo.setDuration(duration);
                    }

                    BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().setAppSessionInfo(existingData);
                }
            }

            this.applicationWillResignActiveNotification();
            this.recordSessionOnDayChangeOrAppTermination();
            this.recordLifeTimeOnDayChangeOrAppTermination();
            this.resetAverageSessionDuration();
            BOPiiEvents.getInstance().stopCollectingUserLocationEvent();
            BOFunnelSyncController.getInstance().appWillTerminatWithInfo(null);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void applicationSignificantTimeChangeNotification() {
        try {
            if (BOAEvents.isSessionModelInitialised) {

                HashMap<String, Object> appState = new HashMap<>();
                appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_SIGNIFICANT_TIME_CHANGE));

                BOApp appStates = BOApp.fromJsonDictionary(appState);
                List<BOApp> existingData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().getAppSignificantTimeChange();
                existingData.add(appStates);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().setAppSignificantTimeChange(existingData);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void applicationWillChangeStatusBarFrameNotification() {
        try {
            if (BOAEvents.isSessionModelInitialised) {

                HashMap<String, Object> appState = new HashMap<>();
                appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_STATUS_BAR_FRAME_CHANGED));


                BOApp appStates = BOApp.fromJsonDictionary(appState);
                List<BOApp> existingData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().getAppStatusbarFrameChange();
                existingData.add(appStates);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppStates().setAppStatusbarFrameChange(existingData);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void applicationUserDidTakeScreenshotNotification() {
        try {
            if (BOAEvents.isSessionModelInitialised) {

                HashMap<String, Object> appState = new HashMap<>();
                appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_TAKEN_SCREEN_SHOT));


                BOScreenShotsTaken appStates = BOScreenShotsTaken.fromJsonDictionary(appState);
                List<BOScreenShotsTaken> existingData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getUbiAutoDetected().getScreenShotsTaken();
                existingData.add(appStates);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getUbiAutoDetected().setScreenShotsTaken(existingData);

            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }
}
