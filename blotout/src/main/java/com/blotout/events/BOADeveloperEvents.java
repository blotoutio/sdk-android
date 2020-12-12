package com.blotout.events;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.Controllers.BOFunnelSyncController;
import com.blotout.analytics.BOAnalyticsActivityLifecycleCallbacks;
import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.model.BODeveloperEventModel;
import com.blotout.model.session.BOAddToCart;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.model.session.BOChargeTransaction;
import com.blotout.model.session.BOCustomEvent;
import com.blotout.model.session.BODeveloperCodified;
import com.blotout.model.session.BODoubleTap;
import com.blotout.model.session.BOListUpdated;
import com.blotout.model.session.BOScreenEdgePan;
import com.blotout.model.session.BOScreenRect;
import com.blotout.model.session.BOTimedEvent;
import com.blotout.model.session.BOView;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kotlin.jvm.Synchronized;

/**
 * Created by Blotout on 02,November,2019
 */
public class BOADeveloperEvents extends BOAEvents {
    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BOADeveloperEvents";
    @Nullable
    private HashMap<String, Object> devEventUD;
    private static volatile BOADeveloperEvents instance;

    @Synchronized
    public static BOADeveloperEvents getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOADeveloperEvents.class) {
                if (instance == null) {
                    instance = new BOADeveloperEvents();
                }
            }
        }
        return instance;
    }

    private BOADeveloperEvents() {
        this.devEventUD = BOCommonUtils.getHashmapFromJsonString(BOSharedPreferenceImpl.getInstance().getString(BOCommonConstants.BO_ANALYTICS_DEV_EVENT_USER_DEFAULTS_KEY));

        if (this.devEventUD == null) {
            HashMap<String, Object> devEvent = new HashMap<>();
            BOSharedPreferenceImpl.getInstance().saveString(BOCommonConstants.BO_ANALYTICS_DEV_EVENT_USER_DEFAULTS_KEY, BOCommonUtils.getJsonStringFromHashMap(devEvent));
        }
    }

    public void startTimedEvent(@NonNull String eventName, HashMap<String, Object> startEventInfo) {

        if (!BlotoutAnalytics_Internal.getInstance().isDeveloperEventsEnabled) {
            return;
        }
        try {
            //Write cleanup feature for the events which got started but never ended even after 30 days
            HashMap<String, Object> eventStartInfo = (HashMap<String, Object>) this.devEventUD.get(eventName);

            if (eventStartInfo != null && eventStartInfo.keySet().size() > 0) {
                eventStartInfo.put("Yes", "autoEnd");
                this.endTimedEvent(eventName, eventStartInfo);
                this.devEventUD.remove(eventName);
            }

            BODeveloperEventModel event = new BODeveloperEventModel(eventName, startEventInfo);
            event.eventStartTimeReference = BODateTimeUtils.get13DigitNumberObjTimeStamp();
            event.eventStartDate = new Date();

            HashMap<String, Object> eventStartStorage = event.eventInfoForStorage();
            this.devEventUD.put(BOCommonConstants.BO_START_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
            this.devEventUD.put(event.eventName, eventStartStorage);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }

    }

    public void endTimedEvent(@NonNull String eventName, @NonNull HashMap<String, Object> endEventInfo) {
        if (!BlotoutAnalytics_Internal.getInstance().isDeveloperEventsEnabled) {
            return;
        }
        try {
            HashMap<String, Object> eventStartInfo = (HashMap<String, Object>) this.devEventUD.get(eventName);
            if (eventStartInfo != null) {
                this.logEvent(eventName, endEventInfo);
            }

            BODeveloperEventModel event = new BODeveloperEventModel(eventName, endEventInfo);
            event.eventEndTimeReference = BODateTimeUtils.get13DigitNumberObjTimeStamp();
            event.eventEndDate = new Date();

            long eventDuration = event.eventEndTimeReference - (long) eventStartInfo.get(BOCommonConstants.BO_EVENT_START_TIME_REFERENCE);
            event.eventDuration = eventDuration;

            HashMap<String, Object> eventEndAndStartInfo = event.eventInfoForStorage();
            if (eventStartInfo != null && eventStartInfo.keySet().size() > 0) {
                eventEndAndStartInfo.put(BOCommonConstants.BO_EVENT_START_INFO, eventStartInfo);
                this.devEventUD.remove(eventName);
            }
            eventEndAndStartInfo.put(BOCommonConstants.BO_EVENT_DURATION, event.eventDuration);

            //[self.devEventUD setObject:eventEndAndStartInfo forKey:event.eventID];
            HashMap<String, Object> timeEventInfo = new HashMap<>();
            HashMap<String, Object> timeEventDict = new HashMap<>();
            timeEventDict.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
            timeEventDict.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(eventName));
            timeEventDict.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);
            timeEventDict.put(BOCommonConstants.BO_TIME_STAMP, event.eventEndTimeReference);
            timeEventDict.put(BOCommonConstants.BO_EVENT_NAME, eventName);
            timeEventDict.put(BOCommonConstants.BO_EVENT_START_TIME, eventStartInfo.get(BOCommonConstants.BO_EVENT_START_TIME_REFERENCE));
            timeEventDict.put(BOCommonConstants.BO_START_VISIBLE_CLASS_NAME, eventStartInfo.get(BOCommonConstants.BO_START_VISIBLE_CLASS_NAME));
            timeEventDict.put(BOCommonConstants.BO_END_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
            timeEventDict.put(BOCommonConstants.BO_EVENT_END_TIME, event.eventEndTimeReference);
            timeEventDict.put(BOCommonConstants.BO_EVENT_DURATION, event.eventDuration);
            timeEventDict.put(BOCommonConstants.BO_TIMED_EVENT_INFO, timeEventInfo);

            BOTimedEvent timedEvent = BOTimedEvent.fromJsonDictionary(timeEventDict);

            List<BOTimedEvent> existingTimedEvent = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getDeveloperCodified().getTimedEvent();
            existingTimedEvent.add(timedEvent);
            BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getDeveloperCodified().setTimedEvent(existingTimedEvent);
            //Funnel execution and testing based
            long eventSubCode = BOCommonUtils.codeForCustomCodifiedEvent(eventName);
            BOFunnelSyncController.getInstance().recordDevEvent(eventName, eventSubCode, timeEventDict);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void logEvent(@NonNull String eventName, @Nullable HashMap<String, Object> eventInfo) {
        if (!BlotoutAnalytics_Internal.getInstance().isDeveloperEventsEnabled) {
            return;
        }
        this.logEvent(eventName, eventInfo, null);
    }

    public void logEvent(
            @NonNull String eventName, @Nullable HashMap<String, Object> eventInfo, @Nullable Date eventTime ) {

        try {
            if (!BlotoutAnalytics_Internal.getInstance().isDeveloperEventsEnabled) {
                return;
            }
            if (BOAEvents.isSessionModelInitialised) {

                long timeStamp = eventTime != null ? BODateTimeUtils.get13DigitNumberObjTimeStampFor(eventTime) : BODateTimeUtils.get13DigitNumberObjTimeStamp();

                HashMap<String, Object> customEventInfo = (eventInfo != null && (eventInfo.keySet().size() > 0)) ? eventInfo : null;
                Long eventSubCode = BOCommonUtils.codeForCustomCodifiedEvent(eventName);
                HashMap<String, Object> customEventModelDict = new HashMap<>();
                customEventModelDict.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                customEventModelDict.put(BOCommonConstants.BO_TIME_STAMP, timeStamp);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_SUB_CODE, eventSubCode);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_NAME, eventName);
                customEventModelDict.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_INFO, customEventInfo);
                customEventModelDict.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(eventName));
                customEventModelDict.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOCustomEvent customEventModel = BOCustomEvent.fromJsonDictionary(customEventModelDict);
                List<BOCustomEvent> existingCustomEvent = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getDeveloperCodified().getCustomEvent();
                existingCustomEvent.add(customEventModel);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getDeveloperCodified().setCustomEvent(existingCustomEvent);
                BOFunnelSyncController.getInstance().recordDevEvent(eventName, eventSubCode, customEventModelDict);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void logPIIEvent(
            @NonNull String eventName, @Nullable HashMap<String, Object> eventInfo, @Nullable Date eventTime ) {

        try {
            if (!BlotoutAnalytics_Internal.getInstance().isDeveloperEventsEnabled) {
                return;
            }
            if (BOAEvents.isSessionModelInitialised) {

                long timeStamp = eventTime != null ? BODateTimeUtils.get13DigitNumberObjTimeStampFor(eventTime) : BODateTimeUtils.get13DigitNumberObjTimeStamp();

                HashMap<String, Object> customEventInfo = (eventInfo != null && (eventInfo.keySet().size() > 0)) ? eventInfo : null;
                Long eventSubCode = BOCommonUtils.codeForCustomCodifiedEvent(eventName);
                HashMap<String, Object> customEventModelDict = new HashMap<>();
                customEventModelDict.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                customEventModelDict.put(BOCommonConstants.BO_TIME_STAMP, timeStamp);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_SUB_CODE, eventSubCode);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_NAME, eventName);
                customEventModelDict.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_INFO, customEventInfo);
                customEventModelDict.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(eventName));
                customEventModelDict.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOCustomEvent customEventModel = BOCustomEvent.fromJsonDictionary(customEventModelDict);
                List<BOCustomEvent> existingCustomEvent = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getDeveloperCodified().getPiiEvent();
                existingCustomEvent.add(customEventModel);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getDeveloperCodified().setPiiEvent(existingCustomEvent);
                //TODO: Need to discuss on it
                //BOFunnelSyncController.getInstance().recordDevEvent(eventName, eventSubCode, customEventModelDict);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void logPHIEvent(
            @NonNull String eventName, @Nullable HashMap<String, Object> eventInfo, @Nullable Date eventTime ) {

        try {
            if (!BlotoutAnalytics_Internal.getInstance().isDeveloperEventsEnabled) {
                return;
            }
            if (BOAEvents.isSessionModelInitialised) {

                long timeStamp = eventTime != null ? BODateTimeUtils.get13DigitNumberObjTimeStampFor(eventTime) : BODateTimeUtils.get13DigitNumberObjTimeStamp();

                HashMap<String, Object> customEventInfo = (eventInfo != null && (eventInfo.keySet().size() > 0)) ? eventInfo : null;
                Long eventSubCode = BOCommonUtils.codeForCustomCodifiedEvent(eventName);
                HashMap<String, Object> customEventModelDict = new HashMap<>();
                customEventModelDict.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                customEventModelDict.put(BOCommonConstants.BO_TIME_STAMP, timeStamp);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_SUB_CODE, eventSubCode);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_NAME, eventName);
                customEventModelDict.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_INFO, customEventInfo);
                customEventModelDict.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(eventName));
                customEventModelDict.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOCustomEvent customEventModel = BOCustomEvent.fromJsonDictionary(customEventModelDict);
                List<BOCustomEvent> existingCustomEvent = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getDeveloperCodified().getPhiEvent();
                existingCustomEvent.add(customEventModel);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getDeveloperCodified().setPhiEvent(existingCustomEvent);
                //TODO:
                //BOFunnelSyncController.getInstance().recordDevEvent(eventName, eventSubCode, customEventModelDict);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

}
