package com.blotout.events;

import androidx.annotation.Nullable;

import com.blotout.analytics.BOAnalyticsActivityLifecycleCallbacks;
import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.model.session.BOAppInfo;
import com.blotout.model.session.BOAppInstalled;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.model.session.BOCustomEvent;
import com.blotout.model.session.BODast;
import com.blotout.model.session.BODau;
import com.blotout.model.session.BODpu;
import com.blotout.model.session.BONewUser;
import com.blotout.storage.BOFileSystemManager;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Blotout on 03,November,2019
 */
public class BORetentionEvents extends BOAEvents {

    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BORetentionEvents";
    private static BORetentionEvents instance;

    public static BORetentionEvents getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            instance = new BORetentionEvents();
        }
        return instance;
    }

    public void recordDAUWithPayload(@Nullable HashMap<String, Object> eventInfo) {
        try {
            if (BOAEvents.isSessionModelInitialised) {

                BODau dauData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getRetentionEvent().getDau();
                if (dauData == null) {
                    HashMap<String, Object> dateInfo = new HashMap<String, Object>();
                    dateInfo.put(BOCommonConstants.BO_DATE, BODateTimeUtils.getStringFromDate(BODateTimeUtils.getCurrentDate(), BOCommonConstants.BO_DATE_FORMAT));
                    Object infoActiveUser = (eventInfo != null && (eventInfo.keySet().size() > 0)) ? eventInfo : dateInfo;

                    HashMap<String, Object> appDau = new HashMap<>();
                    appDau.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    appDau.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_DAU));
                    appDau.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    appDau.put(BOCommonConstants.BO_DAU_INFO, infoActiveUser);
                    appDau.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                    BODau dau = BODau.fromJsonDictionary(appDau);

                    BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getRetentionEvent().setDau(dau);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordDPUWithPayload(@Nullable HashMap<String, Object> eventInfo) {
        try {
            if (BOAEvents.isSessionModelInitialised) {
                BODpu dpuData = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getRetentionEvent().getDpu();
                if (dpuData == null && BlotoutAnalytics_Internal.getInstance().isPayingUser) {
                    HashMap<String, Object> dateInfo = new HashMap<String, Object>();
                    dateInfo.put(BOCommonConstants.BO_DATE, BODateTimeUtils.getStringFromDate(BODateTimeUtils.getCurrentDate(), BOCommonConstants.BO_DATE_FORMAT));
                    Object infoPayingUser = (eventInfo != null && (eventInfo.keySet().size() > 0)) ? eventInfo : dateInfo;

                    HashMap<String, Object> appDpu = new HashMap<>();
                    appDpu.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    appDpu.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_DPU));
                    appDpu.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    appDpu.put(BOCommonConstants.BO_DPU_INFO, infoPayingUser);
                    appDpu.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                    BODpu dpu = BODpu.fromJsonDictionary(appDpu);
                    BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getRetentionEvent().setDpu(dpu);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordAppInstalledWithPayload(boolean isFirstlaunch, @Nullable HashMap<String, Object> eventInfo) {
        try {
            boolean isSDKFirstLaunch = BOSharedPreferenceImpl.getInstance().isSDKFirstLaunched();
            if (BOAEvents.isSessionModelInitialised && !isSDKFirstLaunch) {
                BOSharedPreferenceImpl.getInstance().saveSDKFirstLaunched();
                Object appInstalledInfo = (eventInfo != null && (eventInfo.keySet().size()) > 0) ? eventInfo : null;
                //Check for reinstall case
                String documentsDir = BOFileSystemManager.getInstance().getBOSDKRootDirectory();
                File directory = new File(documentsDir);
                Date documentsDirCrDate = new Date(directory.lastModified());

                HashMap<String, Object> appInstalled = new HashMap<>();
                appInstalled.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                appInstalled.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_INSTALLED));
                appInstalled.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStampFor(documentsDirCrDate));
                appInstalled.put(BOCommonConstants.BO_IS_FIRST_LAUNCH, true);
                appInstalled.put(BOCommonConstants.BO_APP_INSTALLED_INFO, appInstalledInfo);
                appInstalled.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAppInstalled appInstall = BOAppInstalled.fromJsonDictionary(appInstalled);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getRetentionEvent().setAppInstalled(appInstall);

            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordNewUserWithPayload(boolean isNewUser, @Nullable HashMap<String, Object> eventInfo) {
        try {
            if (BOAEvents.isSessionModelInitialised) {
                HashMap<String, Object> dateInfo = new HashMap<String, Object>();
                dateInfo.put(BOCommonConstants.BO_DATE, BODateTimeUtils.getStringFromDate(BODateTimeUtils.getCurrentDate(), BOCommonConstants.BO_DATE_FORMAT));
                Object newUserInfo = (eventInfo != null && (eventInfo.keySet().size() > 0)) ? eventInfo : dateInfo;
                boolean isNewUserInstallCheck = BOSharedPreferenceImpl.getInstance().isNewUserRecorded();
                if (!isNewUserInstallCheck) {
                    BOSharedPreferenceImpl.getInstance().saveNewUserRecorded();
                    HashMap<String, Object> newUser = new HashMap<>();
                    newUser.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    newUser.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_NEW_USER));
                    newUser.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    newUser.put(BOCommonConstants.BO_IS_NEW_USER, true);
                    newUser.put(BOCommonConstants.BO_THE_NEW_USER_INFO, newUserInfo);
                    newUser.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                    BONewUser user = BONewUser.fromJsonDictionary(newUser);
                    BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getRetentionEvent().setNewUser(user);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    //WIP for previous session DAST calculation
    public void storeDASTupdatedSessionFile(BOAppSessionDataModel appSessionData) {
        if (appSessionData != null) {
            String apSessionDataJsonString = BOAppSessionDataModel.toJsonString(appSessionData);
            BOSharedPreferenceImpl.getInstance().saveString(BOCommonConstants.BO_ANALYTICS_SESSION_MODEL_DEFAULTS_KEY, apSessionDataJsonString);
        }
    }

    public void recordDASTWithPayload(long averageTime, HashMap<String, Object> sessionDict, @Nullable HashMap<String, Object> eventInfo) {
        try {
            if (BOAEvents.isSessionModelInitialised && sessionDict != null && sessionDict.keySet().size() > 0) {
                BOAppSessionDataModel apSessionData = BOAppSessionDataModel.fromJsonDictionary(sessionDict);
                BODast dast = apSessionData.getSingleDaySessions().getRetentionEvent().getDast();

                if (apSessionData != null && dast == null) {

                    HashMap<String, Object> dateInfo = new HashMap<String, Object>();
                    dateInfo.put(BOCommonConstants.BO_DATE, BODateTimeUtils.getStringFromDate(BODateTimeUtils.getCurrentDate(), BOCommonConstants.BO_DATE_FORMAT));

                    Object averageSessionInfo = (eventInfo != null && (eventInfo.keySet().size() > 0)) ? eventInfo : dateInfo;

                    List<BOAppInfo> appInfo = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppInfo();
                    BOAppInfo info = null;
                    if (appInfo != null && !appInfo.isEmpty()) {
                        info = appInfo.get(appInfo.size() - 1);
                    }
                    long averageSessionTime = averageTime <= 0 ? averageTime : info.getAverageSessionsDuration();

                    HashMap<String, Object> dastUser = new HashMap<>();
                    dastUser.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    dastUser.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_DAST));
                    dastUser.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    dastUser.put(BOCommonConstants.BO_AVERAGE_SESSION_TIME, averageSessionTime);
                    dastUser.put(BOCommonConstants.BO_PAYLOAD, averageSessionInfo);
                    dastUser.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                    BODast dastModel = BODast.fromJsonDictionary(dastUser);
                    BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getRetentionEvent().setDast(dastModel);
                    storeDASTupdatedSessionFile(apSessionData);

                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordCustomEventsWithNameWithPayload(String eventName, @Nullable HashMap<String, Object> eventInfo) {
        try {
            if (BOAEvents.isSessionModelInitialised) {
                HashMap<String, Object> costumEventInfo = (eventInfo != null && (eventInfo.keySet().size() > 0)) ? eventInfo : null;

                HashMap<String, Object> customEvents = new HashMap<>();
                customEvents.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                customEvents.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(eventName));
                customEvents.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                customEvents.put(BOCommonConstants.BO_EVENT_INFO, costumEventInfo);
                customEvents.put(BOCommonConstants.BO_EVENT_SUB_CODE, BOCommonUtils.codeForCustomCodifiedEvent(eventName));
                customEvents.put(BOCommonConstants.BO_EVENT_NAME, eventName);
                customEvents.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                customEvents.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOCustomEvent customEvent = BOCustomEvent.fromJsonDictionary(customEvents);

                List<BOCustomEvent> existingCustomEvents = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getRetentionEvent().getCustomEvents();
                existingCustomEvents.add(customEvent);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getRetentionEvent().setCustomEvents(existingCustomEvents);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }
}

