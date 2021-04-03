package com.blotout.events;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.Controllers.BOSDKManifestController;
import com.blotout.analytics.BOExceptionHandler;
import com.blotout.eventsExecutor.BODeviceOperationExecutorHelper;
import com.blotout.eventsExecutor.BOGeoRetentionOperationExecutorHelper;
import com.blotout.analytics.BOHandlerMessage;
import com.blotout.eventsExecutor.BOLifetimeOperationExecutorHelper;
import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.model.session.BOAppInfo;
import com.blotout.model.lifetime.BOAppLifetimeData;
import com.blotout.model.BOALocalDefaultJSONs;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.network.jobs.BOAPostEventsDataJob;
import com.blotout.storage.BOFileSystemManager;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Blotout on 22,October,2019
 */

public class BOAEvents {
    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BOAEvents";
    private static final String BOSyncWithServerForLifeTimeSessionAfterDelay = "BOSyncWithServerForLifeTimeSessionAfterDelay";
    private static final String BOSyncWithServerForSessionAfterDelay = "BOSyncWithServerForSessionAfterDelay";


    @Nullable
    private static volatile BOAppSessionDataModel mAppSessionDataModel;
    @Nullable
    private static volatile BOAppLifetimeData mAppLifetimeData;

    public static volatile boolean isSessionModelInitialised;
    public static volatile boolean isAppLifeModelInitialised;

    private static long delayInterval() {
        return BOSDKManifestController.delayInterval();
    }

    private static void syncWithServerForFile(String filePath) {
        try {
            Date previousDate = BODateTimeUtils.getPreviousDayDateFrom(BODateTimeUtils.getCurrentDate());
            String fileName = BOCommonUtils.lastPathComponent(filePath);
            String fileNameWithoutExtention = BOCommonUtils.stringByDeletingPathExtension(fileName);
            Date fileDate = BODateTimeUtils.getDateFromString(fileNameWithoutExtention, BOCommonConstants.BO_DATE_FORMAT);
            boolean isPreviousDate = BODateTimeUtils.isDateLessThan(fileDate, previousDate);
            if (isPreviousDate) {
                BOAPostEventsDataJob eventsJob = new BOAPostEventsDataJob();
                eventsJob.filePath = filePath;
                BOSharedManager.getInstance().getJobManager().addJobInBackground(eventsJob);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private static void syncWithServerAllFilesWithExtention(@Nullable String extention, String directoryPath) {
        try {
            String syncedFiles = getSyncedDirectoryPath();
            if (syncedFiles.equals(directoryPath)) {
                return;
            }
            String fileExtention = extention != null ? extention : "txt";
            List<String> allFile = BOFileSystemManager.getInstance().getAllFilesWithExtension(directoryPath, fileExtention);
            for (String filePath : allFile) {
                syncWithServerForFile(filePath);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private static void syncRecursiveWithServerForSession(BOAppSessionDataModel sessionObject) {
        try {
            BOAPostEventsDataJob eventsJob = new BOAPostEventsDataJob();
            eventsJob.sessionObject= sessionObject;
            BOSharedManager.getInstance().getJobManager().addJobInBackground(eventsJob);
            BOAEvents.syncWithServerAfterDelay(delayInterval(), sessionObject);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private static void syncWithServerAfterDelay(long milliSeconds, BOAppSessionDataModel sessionObject) {
        try {
            BODeviceOperationExecutorHelper.getInstance().postDelayedWithKey(BOSyncWithServerForSessionAfterDelay, new Runnable() {
                @Override
                public void run() {
                    syncRecursiveWithServerForSession(sessionObject);
                }
            }, milliSeconds);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

    }

    @Nullable
    private static String getSessionDirectoryPath() {
        try {
            String rootDirectory = BOFileSystemManager.getInstance().getBOSDKRootDirectory();
            return BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(rootDirectory + "/" + "SessionData");
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    @Nullable
    public static String getSyncedDirectoryPath() {
        try {
            String rootDirectory = getSessionDirectoryPath();
            return BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(rootDirectory + "/" + "syncedFiles");
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    @Nullable
    public static String getNotSyncedDirectoryPath() {
        try {
            String rootDirectory = getSessionDirectoryPath();
            return BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(rootDirectory + "/" + "notSyncedFiles");
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }


    private static void syncRecursiveWithServerForLifeTimeSession(BOAppLifetimeData lifeTimeSessionObject) {
        try {
            BOAPostEventsDataJob eventsJob = new BOAPostEventsDataJob();
            eventsJob.appLifetimeData = lifeTimeSessionObject;
            BOSharedManager.getInstance().getJobManager().addJobInBackground(eventsJob);
            BOAEvents.syncWithServerForLifeTimeSessionAfterDelay(delayInterval(), lifeTimeSessionObject);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private static void syncWithServerForLifeTimeSessionAfterDelay(long milliSeconds, BOAppLifetimeData lifeTimeSessionObject) {
        try {
            BODeviceOperationExecutorHelper.getInstance().postDelayedWithKey(BOSyncWithServerForLifeTimeSessionAfterDelay, new Runnable() {
                @Override
                public void run() {
                    syncRecursiveWithServerForLifeTimeSession(lifeTimeSessionObject);
                }
            }, milliSeconds);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

    }

    @Nullable
    private static String getLifeTimeDirectoryPath() {
        try {
            String rootDirectory = BOFileSystemManager.getInstance().getBOSDKRootDirectory();
            return BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(rootDirectory + "/" + "LifetimeData");
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return null;
    }

    @Nullable
    public static String getLifeTimeDataSyncedDirectoryPath() {
        try {
            String rootDirectory = getLifeTimeDirectoryPath();
            return BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(rootDirectory + "/" + "syncedFiles");
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return null;
    }

    @Nullable
    private static String getLifeTimeDataNotSyncedDirectoryPath() {
        try {
            String rootDirectory = getLifeTimeDirectoryPath();
            return BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(rootDirectory + "/" + "notSyncedFiles");
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return null;
    }

    /**
     * unRegisterDayChangeEvent
     */
    public static void unRegisterDayChangeEvent() {
        try {
            if (BOAEvents.receiver.isReceiverRegistered()) {
                BOSharedManager.getInstance().getContext().unregisterReceiver(BOAEvents.receiver);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    /**
     * registerDayChangeEvent
     */
    private static void registerDayChangeEvent() {
        try {
            BOAEvents.unRegisterDayChangeEvent();
            BOSharedManager.getInstance().getContext().registerReceiver(BOAEvents.receiver, BODayChangedBroadcastReceiver.getIntentFilter());
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    /**
     *
     * @param context Application context
     * @param callback  BOHandlerMessage callback
     */
    public static void initDefaultConfigurationWithHandler(@NonNull final Context context,
                                                           @NonNull final BOHandlerMessage callback) {
        try {

            BOAEvents.registerDayChangeEvent();

            initSuccessForAppDailySession(context, new BOHandlerMessage() {
                @Override
                public void handleMessage(@NonNull BOMessage msg) {
                    if (msg.what == 1) {
                        //Init AppLifeTime Model
                        initExcepionalhandler();
                        initSuccessForAppLifeTime(context, new BOHandlerMessage() {
                            @Override
                            public void handleMessage(@NonNull BOMessage msg) {
                                callback.handleMessage(new BOHandlerMessage.BOMessage(msg.what, null));
                            }
                        });
                    } else {
                        callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
                    }
                }
            });
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
    }

    /**
     * This Method is responsible for appSession Json Schema initialization.
     * @param context Application context
     * @param callback BOHandlerMessage callback
     */
    public static void initSuccessForAppDailySession(@NonNull Context context, @NonNull BOHandlerMessage callback) {
        try {
            String appSessionModelStr = BOSharedPreferenceImpl.getInstance().getString(BOCommonConstants.BO_ANALYTICS_SESSION_MODEL_DEFAULTS_KEY);
            if (appSessionModelStr != null && !appSessionModelStr.equals("null")) {

                HashMap<String, Object> appSessionModelUD = BOCommonUtils.getHashmapFromJsonString(appSessionModelStr);
                String dateString = (String) appSessionModelUD.get(BOCommonConstants.BO_DATE);
                if (dateString == null || dateString.equals("") || dateString.contains("0x00") || dateString.length() != 10) {
                    dateString = BODateTimeUtils.getFormattedCurrentDateString(BOCommonConstants.BO_DATE_FORMAT);
                    appSessionModelUD.put(BOCommonConstants.BO_DATE, dateString);
                }

                boolean isSameDay = BODateTimeUtils.isDayMonthAndYearSameOfDate(BODateTimeUtils.getCurrentDate(), dateString, BOCommonConstants.BO_DATE_FORMAT);
                if (isSameDay) { //Compare day, month and year using AND condition. else if user launch App after one month then day is same and condition will be true. Same is applicable for year old user. I know year old user is not good business case but technically should be
                    mAppSessionDataModel = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(appSessionModelUD);
                    //If date is same then take directory path and check all previous date files which are not yet synced and move to synced folder after sync
                    BOAEvents.syncWithServerAllFilesWithExtention("txt", BOAEvents.getNotSyncedDirectoryPath());

                } else {

                    storePreviousDayAppInfoViaNotification(appSessionModelUD);

                    HashMap<String, Object> singleDaySessions = (HashMap<String, Object>) appSessionModelUD.get(BOCommonConstants.BO_SINGLE_DAY_SESSIONS);
                    List<HashMap<String, Object>> appInfoArr = (List<HashMap<String, Object>>) singleDaySessions.get(BOCommonConstants.BO_APP_INFO);
                    HashMap<String, Object> appInfoDict = BOCommonUtils.lastObject(appInfoArr);
                    BOAppInfo appInfoPrevious = null;
                    if (appInfoDict != null && appInfoDict.values().size() > 0) {
                        appInfoPrevious = BOAppInfo.fromJsonDictionary(appInfoDict);
                    }

                    List sessionDurations = new ArrayList();
                    for (HashMap<String, Object> infoDict : appInfoArr) {
                        sessionDurations.add(infoDict.get("sessionsDuration"));
                    }

                    long averageSessionDuration = Long.valueOf((Integer) appInfoDict.get(BOCommonConstants.BO_AVERAGE_SESSION_DURATION));
                    HashMap<String, Object> payload = new HashMap<>();
                    payload.put("sessionsCount", appInfoArr.size());
                    payload.put("sessionsDuration", sessionDurations);
                    BORetentionEvents.getInstance().recordDASTWithPayload(averageSessionDuration, appSessionModelUD, payload);

                    appSessionModelStr = getStoreDASTUpdatedSessionStr();
                    appSessionModelUD = BOCommonUtils.getHashmapFromJsonString(appSessionModelStr);
                    dateString = (String) appSessionModelUD.get(BOCommonConstants.BO_DATE);

                    if (dateString != null && !dateString.equals("")) {

                        String filePath = BOAEvents.getNotSyncedDirectoryPath() + "/" + dateString + ".txt";
                        //else file write operation and prapare new object
                        boolean success = BOFileSystemManager.getInstance().writeToFile(filePath, appSessionModelStr);
                        //prepare operation manager and send data to server & convert data into server format
                        BOAPostEventsDataJob eventsJob = new BOAPostEventsDataJob();
                        eventsJob.filePath = filePath;
                        BOSharedManager.getInstance().getJobManager().addJobInBackground(eventsJob);
                    }

                    mAppSessionDataModel = BOAppSessionDataModel.sharedInstanceFromJSONString(
                            BOALocalDefaultJSONs.getAppSessionJsonString(context,
                                    "app_session_default.json"));
                }

            } else {
                mAppSessionDataModel = BOAppSessionDataModel.sharedInstanceFromJSONString(
                        BOALocalDefaultJSONs.getAppSessionJsonString(context,
                                "app_session_default.json"));

            }
            BOAEvents.isSessionModelInitialised = mAppSessionDataModel != null;

            if (BOAEvents.isSessionModelInitialised) {
                if (BOSharedPreferenceImpl.getInstance().isSDKFirstLaunched()) {
                    long delayIntervalMillies = delayInterval();
                    long lastSyncTime = BOAEvents.mAppSessionDataModel.getSingleDaySessions().getLastServerSyncTimeStamp();
                    if (lastSyncTime == 0) {
                        BOAEvents.syncWithServerAfterDelay(BOCommonConstants.BO_ANALYTICS_POST_INIT_NETWORK_DELAY, BOAEvents.mAppSessionDataModel);
                    } else if ((lastSyncTime > 0) && ((BODateTimeUtils.get13DigitNumberObjTimeStamp() - lastSyncTime) >= delayIntervalMillies)) {
                        BOAEvents.syncWithServerAfterDelay(BOCommonConstants.BO_ANALYTICS_POST_INIT_NETWORK_DELAY, BOAEvents.mAppSessionDataModel);
                    } else {
                        long remainingDelay = delayIntervalMillies - (BODateTimeUtils.get13DigitNumberObjTimeStamp() - lastSyncTime);
                        BOAEvents.syncWithServerAfterDelay(remainingDelay, BOAEvents.mAppSessionDataModel);
                    }
                } else {
                    BOAEvents.syncWithServerAfterDelay(BOCommonConstants.BO_ANALYTICS_POST_INIT_NETWORK_DELAY, mAppSessionDataModel);
                }
                callback.handleMessage(new BOHandlerMessage.BOMessage(1, null));
            } else {
                callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
    }


    /**
     * This Method is responsible for lifeTime Session Json Schema initialization.
     * @param context Application context
     * @param callback BOHandlerMessage callback
     */
    public static void initSuccessForAppLifeTime(@NonNull Context context, @NonNull BOHandlerMessage callback) {
        try {

            String appLifeTimeModelStr = BOSharedPreferenceImpl.getInstance().getString(BOCommonConstants.BO_ANALYTICS_LIFETIME_MODEL_DEFAULTS_KEY);
            if (appLifeTimeModelStr != null && !appLifeTimeModelStr.equals("null")) {
                HashMap<String, Object> appLifeTimeModelUD = BOCommonUtils.getHashmapFromJsonString(appLifeTimeModelStr);
                String dateString = (String) appLifeTimeModelUD.get(BOCommonConstants.BO_DATE);

                if (dateString == null || dateString.equals("") || dateString.contains("0x00") || dateString.length() != 10) {
                    dateString = BODateTimeUtils.getFormattedCurrentDateString(BOCommonConstants.BO_DATE_FORMAT);
                    appLifeTimeModelUD.put(BOCommonConstants.BO_DATE, dateString);
                }

                boolean isSameMonth = BODateTimeUtils.isMonthAndYearSameOfDate(BODateTimeUtils.getCurrentDate(), dateString, BOCommonConstants.BO_DATE_FORMAT);
                if (isSameMonth) { //Compare day, month and year using AND condition. else if user launch App after one month then day is same and condition will be true. Same is applicable for year old user. I know year old user is not good business case but technically should be
                    mAppLifetimeData = BOAppLifetimeData.sharedInstanceFromJSONDictionary(appLifeTimeModelUD);
                    //If date is same then take directory path and check all previous date files which are not yet synced and move to synced folder after sync
                    //If date is same then take directory path and check all previous date files which are not yet synced and move to synced folder after sync
                    BOAEvents.syncWithServerAllFilesWithExtention("txt", BOAEvents.getLifeTimeDataNotSyncedDirectoryPath());

                } else {
                    String dateL = (String) appLifeTimeModelUD.get(BOCommonConstants.BO_DATE);

                    if (dateL != null && !dateString.equals("")) {

                        String filePath = BOAEvents.getLifeTimeDataNotSyncedDirectoryPath() + "/" + dateString + ".txt";
                        //else file write operation and prapare new object
                        boolean success = BOFileSystemManager.getInstance().writeToFile(filePath, appLifeTimeModelStr);
                        //prepare operation manager and send data to server & convert data into server format
                        BOAPostEventsDataJob eventsJob = new BOAPostEventsDataJob();
                        eventsJob.filePathLifetimeData = filePath;
                        BOSharedManager.getInstance().getJobManager().addJobInBackground(eventsJob);
                    }

                    mAppLifetimeData = BOAppLifetimeData.sharedInstanceFromJSONString(
                            BOALocalDefaultJSONs.getAppSessionJsonString(context,
                                    "app_lifetime_default.json"));
                }

            } else {

                mAppLifetimeData = BOAppLifetimeData.sharedInstanceFromJSONString(
                        BOALocalDefaultJSONs.getAppSessionJsonString(context,
                                "app_lifetime_default.json"));

            }
            BOAEvents.isAppLifeModelInitialised = mAppLifetimeData != null;

            if (BOAEvents.isAppLifeModelInitialised) {
                if (BOSharedPreferenceImpl.getInstance().isSDKFirstLaunched()) {
                    long delayIntervalMillies = delayInterval();
                    long lastSyncTime = BOAEvents.mAppSessionDataModel.getSingleDaySessions().getLastServerSyncTimeStamp();
                    if (lastSyncTime == 0) {
                        BOAEvents.syncWithServerForLifeTimeSessionAfterDelay(BOCommonConstants.BO_ANALYTICS_POST_INIT_NETWORK_DELAY, BOAEvents.mAppLifetimeData);
                    } else if ((lastSyncTime > 0) && ((BODateTimeUtils.get13DigitNumberObjTimeStamp() - lastSyncTime) >= delayIntervalMillies)) {
                        BOAEvents.syncWithServerForLifeTimeSessionAfterDelay(BOCommonConstants.BO_ANALYTICS_POST_INIT_NETWORK_DELAY, BOAEvents.mAppLifetimeData);
                    } else {
                        long remainingDelay = delayIntervalMillies - (BODateTimeUtils.get13DigitNumberObjTimeStamp() - lastSyncTime);
                        BOAEvents.syncWithServerForLifeTimeSessionAfterDelay(remainingDelay, BOAEvents.mAppLifetimeData);
                    }
                } else {
                    BOAEvents.syncWithServerForLifeTimeSessionAfterDelay(BOCommonConstants.BO_ANALYTICS_POST_INIT_NETWORK_DELAY, mAppLifetimeData);
                }
                callback.handleMessage(new BOHandlerMessage.BOMessage(1, null));
            } else {
                callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
            }

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
    }

    /**
     *  BODayChangedBroadcastReceiver is receiver for day change event
     */
    @NonNull
    private static BODayChangedBroadcastReceiver receiver = new BODayChangedBroadcastReceiver() {
        @Override
        public void onDayChanged() {
            try {
                BOAppSessionEvents.getInstance().appTerminationFunctionalityOnDayChange();
                BOAEvents.mAppLifetimeData = null;
                BOAEvents.isAppLifeModelInitialised = false;
                BOAEvents.mAppSessionDataModel = null;
                BOAEvents.isSessionModelInitialised = false;

                BOAppSessionDataModel.resetDailySessionSharedInstanceToken();
                BOAppLifetimeData.resetLifeTimeSharedInstanceToken();

                BOAEvents.initSuccessForAppDailySession(BOSharedManager.getInstance().getContext(), new BOHandlerMessage() {
                    @Override
                    public void handleMessage(@NonNull BOMessage msg) {
                        if (msg.what == 1) {
                            //Init AppLifeTime Model
                            BOAEvents.initSuccessForAppLifeTime(BOSharedManager.getInstance().getContext(), new BOHandlerMessage() {
                                @Override
                                public void handleMessage(BOMessage msg) {
                                    BOAEvents.performSDKInitFunctionalityAsOnRelaunch();
                                }
                            });
                        } else {

                        }
                    }
                });

            } catch (Exception e) {
                Logger.INSTANCE.e(TAG, e.getMessage());
            }
        }
    };

    /**
     * Initiate recording device events
     */
    private static void recordingDeviceEvents() {

        BODeviceEvents.getInstance().isEnabled = true;
        BODeviceOperationExecutorHelper.getInstance().post(new Runnable() {
            @Override
            public void run() {

                try {
                    BODeviceEvents.getInstance().recordDeviceEvents();
                } catch (Exception e) {
                    Logger.INSTANCE.e(TAG, e.toString());
                }

                try {
                    BODeviceEvents.getInstance().recordMemoryEvents();
                    BODeviceEvents.getInstance().recordNetworkEvents();
                    BODeviceEvents.getInstance().recordStorageEvents();
                    BODeviceEvents.getInstance().recordAdInformation();
                } catch (Exception e) {
                    Logger.INSTANCE.e(TAG, e.toString());
                }
            }
        });
    }

    /**
     * This method is responsible for reinit sdk
     */
    @SuppressLint("MissingPermission")
    private static void performSDKInitFunctionalityAsOnRelaunch() {

        try {
            BOAppSessionEvents.getInstance().isEnabled = true;
            BOAppSessionEvents.getInstance().startRecordingEvents();


            BlotoutAnalytics_Internal.getInstance().setRetentionEventsEnabled(true);
            BOGeoRetentionOperationExecutorHelper.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        BORetentionEvents.getInstance().recordDAUWithPayload(null);
                        BORetentionEvents.getInstance().recordDPUWithPayload(null);
                        BORetentionEvents.getInstance().recordDPUWithPayload(null);
                        if (!BOSharedPreferenceImpl.getInstance().isNewUserRecorded()) {
                            BORetentionEvents.getInstance().recordAppInstalledWithPayload(true, null);
                            BORetentionEvents.getInstance().recordNewUserWithPayload(true, null);
                        }
                    } catch (Exception e) {
                        Logger.INSTANCE.e(TAG, e.toString());
                    }
                }
            });

            recordingDeviceEvents();

            try {
                BOPiiEvents.getInstance().isEnabled = true;
                BOPiiEvents.getInstance().startCollectingUserLocationEvent();
            } catch (SecurityException e) {
                //Location Permission is not there
                Logger.INSTANCE.e(TAG, e.toString());
            }

            BOLifetimeOperationExecutorHelper.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        BOLifeTimeAllEvent.getInstance().setAppLifeTimeSystemInfoOnAppLaunch();
                    } catch (Exception e) {
                        Logger.INSTANCE.e(TAG, e.toString());
                    }
                }
            });

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    /**
     * This method is responsible for initialize exception handler
     */
    private static void initExcepionalhandler() {
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new BOExceptionHandler());
    }

    public static void storePreviousDayAppInfoViaNotification(HashMap<String, Object> appSessionObject) {

        try {
            HashMap<String, Object> singleDaySessions = (HashMap<String, Object>) appSessionObject.get(BOCommonConstants.BO_SINGLE_DAY_SESSIONS);
            List<HashMap<String, Object>> appInfoArr = (List<HashMap<String, Object>>) singleDaySessions.get(BOCommonConstants.BO_APP_INFO);
            HashMap<String, Object> appInfoDict = BOCommonUtils.lastObject(appInfoArr);

            if (appInfoDict != null && appInfoDict.values().size() > 0) {
                String appInfoDictStr = BOCommonUtils.getJsonStringFromHashMap(appInfoDict);
                BOSharedPreferenceImpl.getInstance().saveString(BOCommonConstants.BO_ANALYTICS_ROOT_USER_DEFAULTS_PREVIOUS_DAY_APP_INFO, appInfoDictStr);
            }

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public static String getStoreDASTUpdatedSessionStr() {
        try {
            return BOSharedPreferenceImpl.getInstance().getString(BOCommonConstants.BO_ANALYTICS_SESSION_MODEL_DEFAULTS_KEY);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
            return null;
        }
    }

}