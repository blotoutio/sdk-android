package com.blotout.Controllers;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.analytics.BODataRuleEngine;
import com.blotout.analytics.BOHandlerMessage;
import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BOManifestConstants;
import com.blotout.eventsExecutor.BOGeoRetentionOperationExecutorHelper;
import com.blotout.model.SDKManifest.BOManifestVariable;
import com.blotout.model.SDKManifest.BOSDKManifest;
import com.blotout.network.BOAPIFactory;
import com.blotout.network.api.BOBaseAPI;
import com.blotout.storage.BOFileSystemManager;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Blotout on 07,December,2019
 */
public class BOSDKManifestController {
    private static long SYNC_MAX_TIME_GAP_IN_SECONDS = 604800; //7*24*60*60 7 days
    private static long STORAGE_MAX_TIME_GAP_IN_SECONDS = 14 * 24 * 60 * 60; //14*24*60*60 //1209600 14 days

    private static final String TAG = "BOSDKManifestController";
    private static final String BOSDKManifestFileName = "sdkManifest";
    public static final int BO_DEPLOYMENT_MODE_PRIVACY_ANALYTICS = 0;
    public static final int BO_DEPLOYMENT_MODE_FIRST_PARTY = 1;

    private static volatile BOSDKManifestController instance;
    @Nullable
    public BOSDKManifest sdkManifestModel;
    public boolean isSyncedNow;

    public boolean networkCutoffReached;
    public boolean storageCutoffReached;

    public int eventPushThresholdInterval;
    public int eventPushThresholdEventCounter;
    public int eventGEOLocationGrain;
    public int eventDeviceInfoGrain;
    public int eventSystemMergeCounter;
    public int eventCodifiedMergeCounter;
    public int eventOfflineInterval;
    public int licenseExpireDayAlive;
    public int intervalManifestRefresh;
    public int intervalStoreEvents;
    public int intervalRetryInterval;
    public boolean sdkPushSystemEvents;
    public boolean sdkPushPIIEvents;
    public boolean sdkPushPHIEvents;
    public String sdkPIIPublicKey;
    public String sdkPHIPublicKey;
    public boolean sdkMapUserId;
    public boolean sdkBehaviourEvents;
    public int sdkModeDeployment;
    public String serverBaseURL;

    public String geoIPPath;
    public String eventFunnelPath;
    public String eventFunnelPathsFeedback;
    public String eventRetentionPath;
    public String eventPath;
    public String segmentPath;
    public String segmentPathFeedback;
    public String manifestPath;

    public static BOSDKManifestController getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOSDKManifestController.class) {
                if (instance == null) {
                    instance = new BOSDKManifestController();
                }
            }
        }
        return instance;
    }

    private BOSDKManifestController() {

        long lastManifestSyncTimeStampInit = BOSharedPreferenceImpl.getInstance().getLong(BOCommonConstants.BO_ANALYTICS_SDK_MANIFEST_LAST_TIMESTAMP_SYNC_DEFAULTS_KEY);

        if (lastManifestSyncTimeStampInit <= 0) {
            BOSharedPreferenceImpl.getInstance().saveLong(BOCommonConstants.BO_ANALYTICS_SDK_MANIFEST_LAST_TIMESTAMP_SYNC_DEFAULTS_KEY, 0);
        }
    }

    private static long manifestRefreshInterval() {

        int delayHour = BOSDKManifestController.getInstance().intervalManifestRefresh;
        if (delayHour > 0) {
            return delayHour * 60 * 60 * 1000;
        }

        return 24 * 60 * 60 * 1000; //default value
    }

    //set Default value when manifest success to load
    private void setupManifestExtraParamOnSuccess () {
        try {
            long currentTime = BODateTimeUtils.get13DigitNumberObjTimeStamp();
            BOSharedPreferenceImpl.getInstance().saveLong(BOCommonConstants.BO_ANALYTICS_SDK_MANIFEST_LAST_TIMESTAMP_SYNC_DEFAULTS_KEY, currentTime);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }
    //set Default value when manifest failed to load
    private void setupManifestExtraParamOnFailure () {
        try {
            //error case
            long lastManifestSyncTimeStamp = BOSharedPreferenceImpl.getInstance().getLong(BOCommonConstants.BO_ANALYTICS_SDK_MANIFEST_LAST_TIMESTAMP_SYNC_DEFAULTS_KEY);

            long timeGap = lastManifestSyncTimeStamp;
            if (timeGap < SYNC_MAX_TIME_GAP_IN_SECONDS) {
                BOSDKManifestController.this.networkCutoffReached = false;
                BOSDKManifestController.this.storageCutoffReached = false;
            }
            if (timeGap > SYNC_MAX_TIME_GAP_IN_SECONDS) {
                BOSDKManifestController.this.networkCutoffReached = true;
                BOSDKManifestController.this.storageCutoffReached = false;
            }
            if (timeGap > STORAGE_MAX_TIME_GAP_IN_SECONDS) {
                BOSDKManifestController.this.networkCutoffReached = true;
                BOSDKManifestController.this.storageCutoffReached = true;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    /**
     * This method will use to fetch manifest in Background
     */
    public void syncManifestWithServer() {

        if (!BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
            return;
        }

        BOGeoRetentionOperationExecutorHelper.getInstance().post(new Runnable() {
            @Override
            public void run() {

                long lastManifestSyncTimeStamp = BOSharedPreferenceImpl.getInstance().getLong(BOCommonConstants.BO_ANALYTICS_SDK_MANIFEST_LAST_TIMESTAMP_SYNC_DEFAULTS_KEY);
                if(lastManifestSyncTimeStamp > 0) {
                    long currentTime = BODateTimeUtils.get13DigitNumberObjTimeStamp();
                    if((currentTime - lastManifestSyncTimeStamp >=  manifestRefreshInterval())) {

                        HashMap<String, Object> manifestInfo = new HashMap<>();
                        manifestInfo.put(BOCommonConstants.BO_LAST_UPDATED_TIME, lastManifestSyncTimeStamp > 0 ? lastManifestSyncTimeStamp : 0);
                        manifestInfo.put(BOCommonConstants.BO_BUNDLE_ID, BOSharedManager.getInstance().getContext().getPackageName());

                        BOAPIFactory api = BOSharedManager.getInstance().getAPIFactory();
                        Call<BOSDKManifest> call = api.manifestAPI.getSDKManifest(BOBaseAPI.getInstance().getManifestPath(), manifestInfo);

                        try {
                            Response resp = call.execute();
                            if(resp.isSuccessful()) {
                                BOSDKManifest sdkManifestData = (BOSDKManifest) resp.body();
                                if(sdkManifestData != null && sdkManifestData.getVariables() != null && sdkManifestData.getVariables().size() > 0) {
                                    sdkManifestPathAfterWriting(BOSDKManifest.toJsonString(sdkManifestData));
                                    reloadManifestData();
                                    setupManifestExtraParamOnSuccess();
                                } else {
                                    setupManifestExtraParamOnFailure();
                                }
                            } else {
                                setupManifestExtraParamOnFailure();
                            }

                        } catch (Exception e) {
                            Logger.INSTANCE.e(TAG, e.toString());
                        }
                    }
                }
            }
        });
    }

    /**
     * This method will use to fetch manifest Asynchronously and return callback
     */
    public void serverSyncManifestAndAppVerification(@NonNull BOHandlerMessage callback) {

        try {
            fetchAndPrepareSDKModel(new BOHandlerMessage() {
                @Override
                public void handleMessage(@NonNull BOMessage msg) {
                    if (msg.what == 1) {
                        //success
                        reloadManifestData();
                        setupManifestExtraParamOnSuccess();
                    } else {
                        //error case
                        setupManifestExtraParamOnFailure();
                    }

                    if (BOSDKManifestController.this.sdkManifestModel == null) {
                        //TODO:
                        String jsonString = latestSDKManifestJSONString();
                        if (jsonString != null) {
                            BOSDKManifest sdkManifestM = BOSDKManifest.fromJsonString(jsonString);
                            BOSDKManifestController.this.sdkManifestModel = sdkManifestM;
                        }
                    }
                    if (msg.what == 1) {
                        callback.handleMessage(new BOMessage(1, null));
                    } else {
                        callback.handleMessage(new BOMessage(0, null));
                    }

                }
            });
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
    }

    /**
     * This Method will provide file Path of Manifest
     * @return file Path as String
     */
    @NonNull
    private String latestSDKManifestPath() {
        String fileName = BOSDKManifestFileName;
        String sdkManifestDir = BOFileSystemManager.getInstance().getSDKManifestDirectoryPath();
        return sdkManifestDir + "/" + fileName + ".txt";
    }

    @Nullable
    private String latestSDKManifestJSONString() {
        String sdkManifestFilePath = latestSDKManifestPath();
        String sdkManifestStr = null;
        try {
            sdkManifestStr = BOFileSystemManager.getInstance().readContentOfFileAtPath(sdkManifestFilePath);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return sdkManifestStr;
    }

    private boolean sdkManifestPathAfterWriting(String sdkManifest) {
        try {
            if (sdkManifest != null && !sdkManifest.equals("") && !sdkManifest.equals("{}") && !sdkManifest.equals("{ }")) {
                String sdkManifestFilePath = latestSDKManifestPath();
                //In this remove and write operation, there is a very minor pssibility that file removed but before write operation App terminated, rare but technical possible. write swaping concept later
                if (BOFileSystemManager.getInstance().checkFileExist(sdkManifestFilePath)) {
                    BOFileSystemManager.getInstance().deleteFilesAndDir(sdkManifestFilePath);
                }

                return BOFileSystemManager.getInstance().writeToFile(sdkManifestFilePath, sdkManifest);
            }
            //  }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    /**
     * Prapere and send netwrok call for fetching data Asynchrnously
     * @param callback
     */
    private void fetchAndPrepareSDKModel(@NonNull BOHandlerMessage callback) {

        //TODO: as it's not implemented yet
        boolean isImplementedAtServer = true;
        if (!BlotoutAnalytics_Internal.getInstance().isSDKEnabled || !isImplementedAtServer) {
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }

        long lastManifestSyncTimeStamp = BOSharedPreferenceImpl.getInstance().getLong(BOCommonConstants.BO_ANALYTICS_SDK_MANIFEST_LAST_TIMESTAMP_SYNC_DEFAULTS_KEY);


        HashMap<String, Object> manifestInfo = new HashMap<>();
        manifestInfo.put(BOCommonConstants.BO_LAST_UPDATED_TIME, lastManifestSyncTimeStamp > 0 ? lastManifestSyncTimeStamp : 0);
        manifestInfo.put(BOCommonConstants.BO_BUNDLE_ID, BOSharedManager.getInstance().getContext().getPackageName());

        BOAPIFactory api = BOSharedManager.getInstance().getAPIFactory();
        Call<BOSDKManifest> call = api.manifestAPI.getSDKManifest(BOBaseAPI.getInstance().getManifestPath(), manifestInfo);

        try {
            //Response resp = call.execute();
            call.enqueue(new Callback<BOSDKManifest>() {
                @Override
                public void onResponse(Call<BOSDKManifest> call, Response<BOSDKManifest> response) {
                    if (response.isSuccessful()) {
                        BOSDKManifest sdkManifestData = response.body();
                        sdkManifestModel = sdkManifestData;
                        sdkManifestPathAfterWriting(BOSDKManifest.toJsonString(sdkManifestData));
                        callback.handleMessage(new BOHandlerMessage.BOMessage(1, null));
                    } else {
                        BOSDKManifestController.this.isSyncedNow = false;
                        callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
                    }
                }

                @Override
                public void onFailure(Call<BOSDKManifest> call, Throwable t) {
                    BOSDKManifestController.this.isSyncedNow = false;
                    callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
                }
            });
        } catch (Exception e) {
            callback.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }
    }

    public static boolean isManifestAvailable() {

        String fileName = BOSDKManifestFileName;
        String sdkManifestDir = BOFileSystemManager.getInstance().getSDKManifestDirectoryPath();
        String sdkManifestFilePath = sdkManifestDir + "/" + fileName + ".txt";

        String sdkManifestStr = null;
        try {
            sdkManifestStr = BOFileSystemManager.getInstance().readContentOfFileAtPath(sdkManifestFilePath);
            if (sdkManifestStr != null && !sdkManifestStr.equals("")) {
                BOSDKManifest sdkManifestM = BOSDKManifest.fromJsonString(sdkManifestStr);
                if (sdkManifestM != null && sdkManifestM.getVariables() != null && sdkManifestM.getVariables().size() > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    public void reloadManifestData() {
        try {
            String manifestStr = latestSDKManifestJSONString();
            if (this.sdkManifestModel == null && manifestStr != null && !manifestStr.equals("")) {
                BOSDKManifest sdkManifestM = BOSDKManifest.fromJsonString(manifestStr);
                this.sdkManifestModel = sdkManifestM;
            }

            if (this.sdkManifestModel != null && this.sdkManifestModel.getVariables() != null && this.sdkManifestModel.getVariables().size() > 0) {
                BOManifestVariable codifiedMergeCounter = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_CodifiedMergeCounter);
                if (codifiedMergeCounter != null) {
                    this.eventCodifiedMergeCounter = Integer.parseInt(codifiedMergeCounter.getValue());
                }

                BOManifestVariable eventPushThresholdIntervals = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_PushThreshold_Interval);
                if (eventPushThresholdIntervals != null) {
                    this.eventPushThresholdInterval = Integer.parseInt(eventPushThresholdIntervals.getValue());
                }

                BOManifestVariable eventPushThresholdCounter = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_PushThreshold_EventCounter);
                if (eventPushThresholdCounter != null) {
                    this.eventPushThresholdEventCounter = Integer.parseInt(eventPushThresholdCounter.getValue());
                }

                BOManifestVariable geoGrain = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_GEOLocationGrain);
                if (geoGrain != null) {
                    this.eventGEOLocationGrain = Integer.parseInt(geoGrain.getValue());
                }

                BOManifestVariable deviceGrain = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_DeviceInfoGrain);
                if (deviceGrain != null) {
                    this.eventDeviceInfoGrain = Integer.parseInt(deviceGrain.getValue());
                }

                BOManifestVariable eventSystemMergeCounters = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_SystemMergeCounter);
                if (eventSystemMergeCounters != null) {
                    this.eventSystemMergeCounter = Integer.parseInt(eventSystemMergeCounters.getValue());
                }

                BOManifestVariable eventOfflineIntervals = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_Offline_Interval);
                if (eventOfflineIntervals != null) {
                    this.eventOfflineInterval = Integer.parseInt(eventOfflineIntervals.getValue());
                }

                BOManifestVariable licenseExpireDayAlives = getManifestVariable(this.sdkManifestModel, BOManifestConstants.License_Expire_Day_Alive);
                if (licenseExpireDayAlives != null) {
                    this.licenseExpireDayAlive = Integer.parseInt(licenseExpireDayAlives.getValue());
                }

                BOManifestVariable manifestRefresh = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Interval_Manifest_Refresh);
                if (manifestRefresh != null) {
                    this.intervalManifestRefresh = Integer.parseInt(manifestRefresh.getValue());
                }

                BOManifestVariable storeEvents = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Interval_Store_Events);
                if (storeEvents != null) {
                    this.intervalStoreEvents = Integer.parseInt(storeEvents.getValue());
                }

                BOManifestVariable intervalRetryIntervals = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Interval_Retry);
                if (intervalRetryIntervals != null) {
                    this.intervalRetryInterval = Integer.parseInt(intervalRetryIntervals.getValue());
                }

                BOManifestVariable serverBaseURL = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Api_Endpoint);
                if (serverBaseURL != null) {
                    this.serverBaseURL = serverBaseURL.getValue();
                }

                BOManifestVariable eventFunnelPaths = getManifestVariable(this.sdkManifestModel, BOManifestConstants.EVENT_FUNNEL_PATH);
                if (eventFunnelPaths != null) {
                    this.eventFunnelPath = eventFunnelPaths.getValue();
                }

                BOManifestVariable eventFunnelPathsFeedback = getManifestVariable(this.sdkManifestModel, BOManifestConstants.EVENT_FUNNEL_FEEDBACK_PATH);
                if (eventFunnelPathsFeedback != null) {
                    this.eventFunnelPathsFeedback = eventFunnelPathsFeedback.getValue();
                }

                BOManifestVariable geoIPPaths = getManifestVariable(this.sdkManifestModel, BOManifestConstants.GEO_IP_PATH);
                if (geoIPPaths != null) {
                    this.geoIPPath = geoIPPaths.getValue();
                }

                BOManifestVariable eventRetentionPaths = getManifestVariable(this.sdkManifestModel, BOManifestConstants.EVENT_RETENTION_PATH);
                if (eventRetentionPaths != null) {
                    this.eventRetentionPath = eventRetentionPaths.getValue();
                }

                BOManifestVariable eventPaths = getManifestVariable(this.sdkManifestModel, BOManifestConstants.EVENT_PATH);
                if (eventPaths != null) {
                    this.eventPath = eventPaths.getValue();
                }


                BOManifestVariable segmentPaths = getManifestVariable(this.sdkManifestModel, BOManifestConstants.SEGMENT_PULL_PATH);
                if (segmentPaths != null) {
                    this.segmentPath = segmentPaths.getValue();
                }

                BOManifestVariable segmentPathFeedbacks = getManifestVariable(this.sdkManifestModel, BOManifestConstants.SEGMENT_FEEDBACK_PATH);
                if (segmentPathFeedbacks != null) {
                    this.segmentPathFeedback = segmentPathFeedbacks.getValue();
                }

                BOManifestVariable sdkPushSystemEvent = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_Push_System_Events);
                if (sdkPushSystemEvent != null) {
                    this.sdkPushSystemEvents = Integer.parseInt(sdkPushSystemEvent.getValue()) == 1;
                }

                BOManifestVariable sdkPushPIIEvent = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_Push_PII_Events);
                if (sdkPushPIIEvent != null) {
                    this.sdkPushPIIEvents = Integer.parseInt(sdkPushPIIEvent.getValue()) == 1;
                }

                BOManifestVariable sdkMapuserIdEvent = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_SDK_Map_User_Id);
                if (sdkMapuserIdEvent != null) {
                    this.sdkMapUserId = Integer.parseInt(sdkMapuserIdEvent.getValue()) == 1;
                }

                BOManifestVariable sdkBehaviourEvent = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_SDK_Behaviour_Id);
                if (sdkBehaviourEvent != null) {
                    this.sdkBehaviourEvents = Integer.parseInt(sdkBehaviourEvent.getValue()) == 1;
                }

                BOManifestVariable sdkPushPHIEvent = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_Push_PHI_Events);
                if (sdkPushPHIEvent != null) {
                    this.sdkPushPHIEvents = Integer.parseInt(sdkPushPHIEvent.getValue()) == 1;
                }

                BOManifestVariable sdkPIIPublicKey = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_PII_Public_Key);
                if (sdkPIIPublicKey != null) {
                    this.sdkPIIPublicKey = sdkPIIPublicKey.getValue();
                }

                BOManifestVariable sdkPHIPublicKey = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_PHI_Public_Key);
                if (sdkPHIPublicKey != null) {
                    this.sdkPHIPublicKey = sdkPHIPublicKey.getValue();
                }

                BOManifestVariable sdkModeDeployment = getManifestVariable(this.sdkManifestModel, BOManifestConstants.Event_SDK_Mode_Deployment);
                if (sdkModeDeployment != null) {
                    this.sdkModeDeployment = Integer.parseInt(sdkModeDeployment.getValue());
                }

            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private BOManifestVariable getManifestVariable(BOSDKManifest manifest, String value) {
        BOManifestVariable oneVar = null;
        for (BOManifestVariable oneVariableDict : manifest.getVariables()) {
            if (oneVariableDict != null) {
                if (oneVariableDict.getVariableName().equals(value)) {
                    oneVar = oneVariableDict;
                    break;
                }
            }
        }
        return oneVar;
    }

    public static long delayInterval() {
        try {
            int delayHour = BOSDKManifestController.getInstance().eventPushThresholdInterval;
            if (delayHour > 0) {
                return delayHour * 60 * 60 * 1000;
            }

            return BOCommonConstants.BO_DEFAULT_EVENT_PUSH_TIME;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return BOCommonConstants.BO_DEFAULT_EVENT_PUSH_TIME;
    }
}
