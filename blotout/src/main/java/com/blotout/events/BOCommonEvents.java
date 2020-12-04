package com.blotout.events;

import com.blotout.analytics.BOAnalyticsActivityLifecycleCallbacks;
import com.blotout.analytics.BOSharedManager;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.model.session.BOCommonEvent;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Blotout on 23,May,2020
 */
public class BOCommonEvents {

    private static final String TAG = "BOCommonEvents";
    private static BOCommonEvents instance;
    public boolean isEnabled;

    public static BOCommonEvents getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            instance = new BOCommonEvents();
        }
        return instance;
    }

    private BOCommonEvents() {
        isEnabled = true;
    }

    public void recordFunnelReceived() {

        try {
            if (BOAEvents.isSessionModelInitialised && isEnabled) {

                String eventName = BONetworkConstants.BO_FUNNEL_RECEIVED;
                Long eventSubCode = BONetworkConstants.BO_Funnel_Received;
                long timeStamp =  BODateTimeUtils.get13DigitNumberObjTimeStamp();

                HashMap<String, Object> customEventModelDict = new HashMap<>();
                customEventModelDict.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                customEventModelDict.put(BOCommonConstants.BO_TIME_STAMP, timeStamp);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_SUB_CODE, eventSubCode);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_CODE, BONetworkConstants.BO_EVENT_FUNNEL_KEY);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_NAME, eventName);
                customEventModelDict.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                customEventModelDict.put(BONetworkConstants.BO_MESSAGE_ID,BOCommonUtils.getMessageIDForEvent(eventName));
                customEventModelDict.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOCommonEvent customEventModel = BOCommonEvent.fromJsonDictionary(customEventModelDict);
                List<BOCommonEvent> existingCommonEvents = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getCommonEvents();
                existingCommonEvents.add(customEventModel);
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordFunnelTriggered() {

        try {
            if (BOAEvents.isSessionModelInitialised && isEnabled) {

                String eventName = BONetworkConstants.BO_FUNNEL_TRIGGERED;
                Long eventSubCode = BONetworkConstants.BO_Funnel_Triggered;
                long timeStamp =  BODateTimeUtils.get13DigitNumberObjTimeStamp();

                HashMap<String, Object> customEventModelDict = new HashMap<>();
                customEventModelDict.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                customEventModelDict.put(BOCommonConstants.BO_TIME_STAMP, timeStamp);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_SUB_CODE, eventSubCode);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_CODE, BONetworkConstants.BO_EVENT_FUNNEL_KEY);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_NAME, eventName);
                customEventModelDict.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                customEventModelDict.put(BONetworkConstants.BO_MESSAGE_ID,BOCommonUtils.getMessageIDForEvent(eventName));
                customEventModelDict.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOCommonEvent customEventModel = BOCommonEvent.fromJsonDictionary(customEventModelDict);
                List<BOCommonEvent> existingCommonEvents = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getCommonEvents();
                existingCommonEvents.add(customEventModel);
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordSegmentReceived() {

        try {
            if (BOAEvents.isSessionModelInitialised && isEnabled) {

                String eventName = BONetworkConstants.BO_SEGMENT_RECEIVED;
                Long eventSubCode = BONetworkConstants.BO_Segment_Received;
                long timeStamp =  BODateTimeUtils.get13DigitNumberObjTimeStamp();

                HashMap<String, Object> customEventModelDict = new HashMap<>();
                customEventModelDict.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                customEventModelDict.put(BOCommonConstants.BO_TIME_STAMP, timeStamp);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_SUB_CODE, eventSubCode);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_CODE, BONetworkConstants.BO_EVENT_SEGMENT_KEY);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_NAME, eventName);
                customEventModelDict.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                customEventModelDict.put(BONetworkConstants.BO_MESSAGE_ID,BOCommonUtils.getMessageIDForEvent(eventName));
                customEventModelDict.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOCommonEvent customEventModel = BOCommonEvent.fromJsonDictionary(customEventModelDict);
                List<BOCommonEvent> existingCommonEvents = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getCommonEvents();
                existingCommonEvents.add(customEventModel);
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordSegmentTriggered() {

        try {
            if (BOAEvents.isSessionModelInitialised && isEnabled) {

                String eventName = BONetworkConstants.BO_SEGMENT_TRIGGERED;
                Long eventSubCode = BONetworkConstants.BO_Segment_Triggred;
                long timeStamp =  BODateTimeUtils.get13DigitNumberObjTimeStamp();

                HashMap<String, Object> customEventModelDict = new HashMap<>();
                customEventModelDict.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                customEventModelDict.put(BOCommonConstants.BO_TIME_STAMP, timeStamp);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_SUB_CODE, eventSubCode);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_CODE, BONetworkConstants.BO_EVENT_SEGMENT_KEY);
                customEventModelDict.put(BOCommonConstants.BO_EVENT_NAME, eventName);
                customEventModelDict.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                customEventModelDict.put(BONetworkConstants.BO_MESSAGE_ID,BOCommonUtils.getMessageIDForEvent(eventName));
                customEventModelDict.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOCommonEvent customEventModel = BOCommonEvent.fromJsonDictionary(customEventModelDict);
                List<BOCommonEvent> existingCommonEvents = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getCommonEvents();
                existingCommonEvents.add(customEventModel);
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }
}
