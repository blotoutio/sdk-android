package com.blotout.analytics;

import android.content.Context;
import android.view.OrientationEventListener;

import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.events.BOAEvents;
import com.blotout.events.BOAppSessionEvents;
import com.blotout.eventsExecutor.BOWorkerHelper;
import com.blotout.model.session.*;
import com.blotout.utilities.*;

import java.util.*;

/**
 * Created by Blotout on 21,November,2019
 */

public class BOActivityOrientationListener extends OrientationEventListener {

    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BOActivityOrientationListener";
    private static final int ORIENTATION_PORTRAIT = 0;
    private static final int ORIENTATION_LANDSCAPE = 1;
    private static final int ORIENTATION_PORTRAIT_REVERSE = 2;
    private static final int ORIENTATION_LANDSCAPE_REVERSE = 3;
    private int lastOrientation = 0;

    public BOActivityOrientationListener(Context context) {
        super(context);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        try {
            if (orientation < 0) {
                return; // Flip screen, Not take account
            }

            int curOrientation;

            if (orientation <= 45) {
                curOrientation = ORIENTATION_PORTRAIT;
            } else if (orientation <= 135) {
                curOrientation = ORIENTATION_LANDSCAPE_REVERSE;
            } else if (orientation <= 225) {
                curOrientation = ORIENTATION_PORTRAIT_REVERSE;
            } else if (orientation <= 315) {
                curOrientation = ORIENTATION_LANDSCAPE;
            } else {
                curOrientation = ORIENTATION_PORTRAIT;
            }

            if (curOrientation != lastOrientation) {
                // TODO: need to discuss use case for this event
                // BOAppSessionEvents.getInstance().applicationWillChangeStatusBarFrameNotification();
                lastOrientation = curOrientation;
                if (BOAEvents.isSessionModelInitialised) {
                    BOWorkerHelper.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            if (curOrientation == ORIENTATION_PORTRAIT || curOrientation == ORIENTATION_PORTRAIT_REVERSE) {
                                HashMap<String, Object> appState = new HashMap<>();
                                appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                                appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                                appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                                appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("Portrait"));
                                BOApp appStates = BOApp.fromJsonDictionary(appState);
                                BOAppSessionDataModel sessionInstance = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null);
                                List<BOApp> existingData = sessionInstance.getSingleDaySessions().getAppStates().getAppOrientationPortrait();
                                existingData.add(appStates);
                                sessionInstance.getSingleDaySessions().getAppStates().setAppOrientationPortrait(existingData);
                            } else {
                                HashMap<String, Object> appState = new HashMap<>();
                                appState.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                                appState.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                                appState.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                                appState.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("Landscape"));
                                BOApp appStates = BOApp.fromJsonDictionary(appState);
                                BOAppSessionDataModel sessionInstance = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null);
                                List<BOApp> existingData = sessionInstance.getSingleDaySessions().getAppStates().getAppOrientationLandscape();
                                existingData.add(appStates);
                                sessionInstance.getSingleDaySessions().getAppStates().setAppOrientationLandscape(existingData);
                            }
                            logDeviceOrientation(curOrientation);
                        }
                    });
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void logDeviceOrientation(int curOrientation) {
        try {
            BODeviceOrientation boDeviceOrientation = new BODeviceOrientation();
            boDeviceOrientation.setOrientation("" + curOrientation);
            boDeviceOrientation.setSentToServer(false);
            boDeviceOrientation.setTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
            BOSingleDaySessions singleDaySessions = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions();
            if (singleDaySessions != null) {
                BOSessionDeviceInfo boSessionDeviceInfo = singleDaySessions.getDeviceInfo();
                List<BODeviceOrientation> existingOrientations = boSessionDeviceInfo.getDeviceOrientation();
                existingOrientations.add(boDeviceOrientation);
                boSessionDeviceInfo.setDeviceOrientation(existingOrientations);

                singleDaySessions.setDeviceInfo(boSessionDeviceInfo);
                BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).setSingleDaySessions(singleDaySessions);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

}
