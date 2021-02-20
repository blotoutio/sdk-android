package com.blotout.network.jobs;

import androidx.annotation.NonNull;

import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.model.lifetime.BOAppLifeTimeInfo;
import com.blotout.model.lifetime.BOAppLifetimeData;
import com.blotout.model.session.BOAccessoriesAttached;
import com.blotout.model.session.BOAdInfo;
import com.blotout.model.session.BOAddToCart;
import com.blotout.model.session.BOApp;
import com.blotout.model.session.BOAppNavigation;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.model.session.BOBatteryLevel;
import com.blotout.model.session.BOBroadcastAddress;
import com.blotout.model.session.BOChargeTransaction;
import com.blotout.model.session.BOCommonEvent;
import com.blotout.model.session.BOConnectedTo;
import com.blotout.model.session.BOCustomEvent;
import com.blotout.model.session.BODoubleTap;
import com.blotout.model.session.BOIPAddress;
import com.blotout.model.session.BOListUpdated;
import com.blotout.model.session.BOMemoryInfo;
import com.blotout.model.session.BONameOfAttachedAccessory;
import com.blotout.model.session.BONetMask;
import com.blotout.model.session.BONetworkInfo;
import com.blotout.model.session.BONumberOfA;
import com.blotout.model.session.BOProcessorsUsage;
import com.blotout.model.session.BOScreenEdgePan;
import com.blotout.model.session.BOSessionDeviceInfo;
import com.blotout.model.session.BOSessionInfo;
import com.blotout.model.session.BOSingleDaySessions;
import com.blotout.model.session.BOStorageInfo;
import com.blotout.model.session.BOTimedEvent;
import com.blotout.model.session.BOView;
import com.blotout.model.session.BOWifiRouterAddress;
import com.blotout.model.session.BOWifiSSID;
import com.blotout.utilities.Logger;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Blotout on 21,November,2019
 */
public class BOSDKServerPostSyncEventConfiguration {
    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BOSDKServerPostSyncEventConfiguration";

    public BOAppSessionDataModel sessionObject;
    public BOAppLifetimeData lifetimeDataObject;
    private static volatile BOSDKServerPostSyncEventConfiguration instance;

    public static BOSDKServerPostSyncEventConfiguration getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOSDKServerPostSyncEventConfiguration.class) {
                if (instance == null) {
                    instance = new BOSDKServerPostSyncEventConfiguration();
                }
            }
        }
        return instance;
    }

    public BOSDKServerPostSyncEventConfiguration() {

    }

    public void updateSentToServerForSessionEvents(HashMap<String, Object> events) {
        try {
            if (this.sessionObject != null && events != null) {
                List<HashMap> eventList = (List<HashMap>) events.get("events");
                for (HashMap<String, Object> singleEvent : eventList) {
                    String serverEventMid = (String) singleEvent.get("mid");
                    long evcs = (long) singleEvent.get("evcs");
                    updateConfigForEvent(serverEventMid, evcs, serverEventMid, this.sessionObject);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updateSentToServerForLifeTimeEvents(HashMap<String, Object> events) {
        try {
            if (this.lifetimeDataObject != null && events != null) {
                List<HashMap> eventList = (List<HashMap>) events.get("events");
                for (HashMap<String, Object> singleEvent : eventList) {
                    String serverEventMid = (String) singleEvent.get("mid");
                    long evcs = (long) singleEvent.get("evcs");
                    updateConfigForLifeTimeEvent(serverEventMid, evcs, serverEventMid, this.lifetimeDataObject);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updateSentToServerForEvents(HashMap<String, Object> events, BOAppSessionDataModel sessionObjectModel) {
        try {
            if (sessionObjectModel != null && events != null) {
                List<HashMap> eventList = (List<HashMap>) events.get("events");
                for (HashMap<String, Object> singleEvent : eventList) {
                    String serverEventMid = (String) singleEvent.get("mid");
                    long evcs = (long) singleEvent.get("evcs");
                    updateConfigForEvent(serverEventMid, evcs, serverEventMid, sessionObjectModel);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updateSentToServerForLifeTimeEvents(HashMap<String, Object> events, BOAppLifetimeData lifeTimeModel) {
        try {
            if (lifeTimeModel != null && events != null) {
                List<HashMap> eventList = (List<HashMap>) events.get("events");
                for (HashMap<String, Object> singleEvent : eventList) {
                    String serverEventMid = (String) singleEvent.get("mid");
                    long evcs = (long) singleEvent.get("evcs");
                    updateConfigForLifeTimeEvent(serverEventMid, evcs, serverEventMid, lifeTimeModel);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updateConfigForEvent(String serverEvent, long eventSubCode, String messageID, BOAppSessionDataModel sessionData) {
        try {
            if ((eventSubCode >= BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY) && (eventSubCode < BONetworkConstants.BO_EVENT_FUNNEL_KEY)) {
                updateDeveloperCodifiedEvents(serverEvent, sessionObject);
            } else if ((eventSubCode >= BONetworkConstants.BO_EVENT_RETENTION_KEY) && (eventSubCode < BONetworkConstants.BO_EVENT_EXCEPTION_KEY)) {
                updateSessionRetentionEvents(serverEvent, sessionObject);
            } else if (eventSubCode >= BONetworkConstants.BO_EVENT_FUNNEL_KEY && eventSubCode < BONetworkConstants.BO_EVENT_RETENTION_KEY) {
                updateFunnelCommonEvents(serverEvent, sessionObject);
            } else if (eventSubCode >= BONetworkConstants.BO_EVENT_SEGMENT_KEY && eventSubCode < 80001) {
                //TODO: 80001 need to make constant for next base events
                updateSegmentsCommonEvents(serverEvent, sessionObject);
            } else if (eventSubCode >= BONetworkConstants.BO_EVENT_SYSTEM_KEY && eventSubCode < BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY) {
                updateSystemEvents(serverEvent, sessionObject);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updatePIIPHIEvents(HashMap<String, Object> events, BOAppSessionDataModel sessionObject) {
        try {

            if (this.sessionObject != null && events != null) {
                //Update PII event
                List<HashMap> piiEventList = (List<HashMap>) events.get(BONetworkConstants.BO_PII_DATA);
                for (HashMap<String, Object> singleEvent : piiEventList) {
                    String serverEventMid = (String) singleEvent.get("mid");
                    for (BOCustomEvent customEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getPiiEvent()) {
                        if (serverEventMid.equals(customEvent.getMid())) {
                            customEvent.setSentToServer(true);
                        }
                    }
                }

                List<HashMap> phiEventList = (List<HashMap>) events.get(BONetworkConstants.BO_PHI_DATA);
                for (HashMap<String, Object> singleEvent : phiEventList) {
                    String serverEventMid = (String) singleEvent.get("mid");
                    for (BOCustomEvent customEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getPhiEvent()) {
                        if (serverEventMid.equals(customEvent.getMid())) {
                            customEvent.setSentToServer(true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updateSystemEvents(String serverEventMid, BOAppSessionDataModel sessionObject) {
        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppLaunched()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppInForeground()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppInBackground()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppActive()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppResignActive()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppBackgroundRefreshAvailable()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppReceiveMemoryWarning()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppSignificantTimeChange()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppOrientationPortrait()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppOrientationLandscape()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppStatusbarFrameChange()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppBackgroundRefreshStatusChange()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppNotificationReceived()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppNotificationViewed()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOApp event : sessionObject.getSingleDaySessions().getAppStates().getAppNotificationClicked()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        for (BOAppNavigation event : sessionObject.getSingleDaySessions().getUbiAutoDetected().getAppNavigation()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        if(!sessionObject.getSingleDaySessions().getUbiAutoDetected().getAppNavigation().isEmpty()) {
            sessionObject.getSingleDaySessions().getUbiAutoDetected().getAppNavigation().clear();
        }

        for (BOSessionInfo event : sessionObject.getSingleDaySessions().getAppStates().getAppSessionInfo()) {
            if (serverEventMid.equals(event.getMid())) {
                event.setSentToServer(true);
            }
        }

        if(!sessionObject.getSingleDaySessions().getAppStates().getAppSessionInfo().isEmpty()) {
            sessionObject.getSingleDaySessions().getAppStates().getAppSessionInfo().clear();
        }

        this.updatePIIEvents(serverEventMid, sessionObject);
        this.updateAdInfo(serverEventMid, sessionObject);
        this.updateMemoryEvents(serverEventMid, sessionObject);
        this.updateDeviceEvents(serverEventMid, sessionObject);
    }

    private void updatePIIEvents(String serverEventMid, @NonNull BOAppSessionDataModel sessionData) {

        try {
            BONetworkInfo networkInfoArray = sessionData.getSingleDaySessions().getNetworkInfo();

            if (networkInfoArray.getCurrentIPAddress() != null && networkInfoArray.getCurrentIPAddress().size() > 0) {
                for (BOIPAddress networkInfo : networkInfoArray.getCurrentIPAddress()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getCellBroadcastAddress() != null && networkInfoArray.getCellBroadcastAddress().size() > 0) {
                for (BOBroadcastAddress networkInfo : networkInfoArray.getCellBroadcastAddress()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getCellIPAddress() != null && networkInfoArray.getCellIPAddress().size() > 0) {
                for (BOIPAddress networkInfo : networkInfoArray.getCellIPAddress()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getCellNetMask() != null && networkInfoArray.getCellNetMask().size() > 0) {
                for (BONetMask networkInfo : networkInfoArray.getCellNetMask()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getConnectedToCellNetwork() != null && networkInfoArray.getConnectedToCellNetwork().size() > 0) {
                for (BOConnectedTo networkInfo : networkInfoArray.getConnectedToCellNetwork()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getConnectedToWifi() != null && networkInfoArray.getConnectedToWifi().size() > 0) {
                for (BOConnectedTo networkInfo : networkInfoArray.getConnectedToWifi()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getExternalIPAddress() != null && networkInfoArray.getExternalIPAddress().size() > 0) {
                for (BOIPAddress networkInfo : networkInfoArray.getExternalIPAddress()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getWifiBroadcastAddress() != null && networkInfoArray.getWifiBroadcastAddress().size() > 0) {
                for (BOBroadcastAddress networkInfo : networkInfoArray.getWifiBroadcastAddress()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getWifiIPAddress() != null && networkInfoArray.getWifiIPAddress().size() > 0) {
                for (BOIPAddress networkInfo : networkInfoArray.getWifiIPAddress()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getWifiRouterAddress() != null && networkInfoArray.getWifiRouterAddress().size() > 0) {
                for (BOWifiRouterAddress networkInfo : networkInfoArray.getWifiRouterAddress()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getWifiSSID() != null && networkInfoArray.getWifiSSID().size() > 0) {
                for (BOWifiSSID networkInfo : networkInfoArray.getWifiSSID()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }

            if (networkInfoArray.getWifiNetMask() != null && networkInfoArray.getWifiNetMask().size() > 0) {
                for (BONetMask networkInfo : networkInfoArray.getWifiNetMask()) {
                    if (serverEventMid.equals(networkInfo.getMid())) {
                        networkInfo.setSentToServer(true);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void updateAdInfo(String serverEventMid, @NonNull BOAppSessionDataModel sessionData) {

        try {
            List<BOAdInfo> adInfo = sessionData.getSingleDaySessions().getAdInfo();
            if (adInfo != null && adInfo.size() > 0) {
                for (BOAdInfo adInformation : adInfo) {
                    if (serverEventMid.equals(adInformation.getMid())) {
                        adInformation.setSentToServer(true);
                    }
                }
            }

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void updateDeviceEvents(String serverEventMid, @NonNull BOAppSessionDataModel sessionData) {

        try {
            BOSessionDeviceInfo deviceInfo = sessionData.getSingleDaySessions().getDeviceInfo();

            if (deviceInfo.getBatteryLevel() != null && deviceInfo.getBatteryLevel().size() > 0) {
                for (BOBatteryLevel appInfo : deviceInfo.getBatteryLevel()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getMultitaskingEnabled() != null && deviceInfo.getMultitaskingEnabled().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getMultitaskingEnabled()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getProximitySensorEnabled() != null && deviceInfo.getProximitySensorEnabled().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getProximitySensorEnabled()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getDebuggerAttached() != null && deviceInfo.getDebuggerAttached().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getDebuggerAttached()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getPluggedIn() != null && deviceInfo.getPluggedIn().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getPluggedIn()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getJailBroken() != null && deviceInfo.getJailBroken().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getJailBroken()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getNumberOfActiveProcessors() != null && deviceInfo.getNumberOfActiveProcessors().size() > 0) {
                for (BONumberOfA appInfo : deviceInfo.getNumberOfActiveProcessors()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getProcessorsUsage() != null && deviceInfo.getProcessorsUsage().size() > 0) {
                for (BOProcessorsUsage appInfo : deviceInfo.getProcessorsUsage()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getAccessoriesAttached() != null && deviceInfo.getAccessoriesAttached().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getAccessoriesAttached()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getHeadphoneAttached() != null && deviceInfo.getHeadphoneAttached().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getHeadphoneAttached()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getNumberOfAttachedAccessories() != null && deviceInfo.getNumberOfAttachedAccessories().size() > 0) {
                for (BONumberOfA appInfo : deviceInfo.getNumberOfAttachedAccessories()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getNameOfAttachedAccessories() != null && deviceInfo.getNameOfAttachedAccessories().size() > 0) {
                for (BONameOfAttachedAccessory appInfo : deviceInfo.getNameOfAttachedAccessories()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getIsCharging() != null && deviceInfo.getIsCharging().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getIsCharging()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }

            if (deviceInfo.getFullyCharged() != null && deviceInfo.getFullyCharged().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getFullyCharged()) {
                    if (serverEventMid.equals(appInfo.getMid())) {
                        appInfo.setSentToServer(true);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void updateMemoryEvents(String serverEventMid, @NonNull BOAppSessionDataModel sessionData) {

        try {
            BOSingleDaySessions singleDaySessions = sessionData.getSingleDaySessions();

            if (singleDaySessions.getMemoryInfo() != null && singleDaySessions.getMemoryInfo().size() > 0) {
                for (BOMemoryInfo memoryInfo : singleDaySessions.getMemoryInfo()) {
                    if (serverEventMid.equals(memoryInfo.getMid())) {
                        memoryInfo.setSentToServer(true);
                    }
                }
            }

            if (singleDaySessions.getStorageInfo() != null && singleDaySessions.getStorageInfo().size() > 0) {
                for (BOStorageInfo storageInfo : singleDaySessions.getStorageInfo()) {
                    if (serverEventMid.equals(storageInfo.getMid())) {
                        storageInfo.setSentToServer(true);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updateFunnelCommonEvents(String serverEventMid, BOAppSessionDataModel sessionObject) {
        try {
            for (BOCommonEvent event : sessionObject.getSingleDaySessions().getCommonEvents()) {
                if (serverEventMid.equals(event.getMid())) {
                    event.setSentToServer(true);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updateSegmentsCommonEvents(String serverEventMid, BOAppSessionDataModel sessionObject) {
        try {
            for (BOCommonEvent event : sessionObject.getSingleDaySessions().getCommonEvents()) {
                if (serverEventMid.equals(event.getMid())) {
                    event.setSentToServer(true);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updateConfigForLifeTimeEvent(String serverEvent, long eventSubCode, String messageID, BOAppLifetimeData lifetimeData) {
        // BOARetentionEvent
        try {
            updateRetentionEvents(serverEvent, lifetimeDataObject);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updateDeveloperCodifiedEvents(String serverEventMid, BOAppSessionDataModel sessionObject) {
        // BODeveloperCodified & BORetentionEvent
        //Update Developer Codified and Retention Event Send To Server
        try {
            for (BODoubleTap touchEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getTouchClick()) {
                if (serverEventMid.equals(touchEvent.getMid())) {
                    touchEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap dragEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getDrag()) {
                if (serverEventMid.equals(dragEvent.getMid())) {
                    dragEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap flickEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getFlick()) {
                if (serverEventMid.equals(flickEvent.getMid())) {
                    flickEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap swipeEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getSwipe()) {
                if (serverEventMid.equals(swipeEvent.getMid())) {
                    swipeEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap doubleTapEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getDoubleTap()) {
                if (serverEventMid.equals(doubleTapEvent.getMid())) {
                    doubleTapEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap moreThanDoubleTapEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getMoreThanDoubleTap()) {
                if (serverEventMid.equals(moreThanDoubleTapEvent.getMid())) {
                    moreThanDoubleTapEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap twoFingerTapEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getTwoFingerTap()) {
                if (serverEventMid.equals(twoFingerTapEvent.getMid())) {
                    twoFingerTapEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap moreThanTwoFingerTapEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getMoreThanTwoFingerTap()) {
                if (serverEventMid.equals(moreThanTwoFingerTapEvent.getMid())) {
                    moreThanTwoFingerTapEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap pinchEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getPinch()) {
                if (serverEventMid.equals(pinchEvent.getMid())) {
                    pinchEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap touchAndHoldEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getTouchAndHold()) {
                if (serverEventMid.equals(touchAndHoldEvent.getMid())) {
                    touchAndHoldEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap shakeEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getShake()) {
                if (serverEventMid.equals(shakeEvent.getMid())) {
                    shakeEvent.setSentToServer(true);
                }
            }

            for (BODoubleTap rotateEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getRotate()) {
                if (serverEventMid.equals(rotateEvent.getMid())) {
                    rotateEvent.setSentToServer(true);
                }
            }

            for (BOScreenEdgePan screenEdgePanEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getScreenEdgePan()) {
                if (serverEventMid.equals(screenEdgePanEvent.getMid())) {
                    screenEdgePanEvent.setSentToServer(true);
                }
            }

            for (BOView viewEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getView()) {
                if (serverEventMid.equals(viewEvent.getMid())) {
                    viewEvent.setSentToServer(true);
                }
            }

            for (BOAddToCart addToCartEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getAddToCart()) {
                if (serverEventMid.equals(addToCartEvent.getMid())) {
                    addToCartEvent.setSentToServer(true);
                }
            }

            for (BOChargeTransaction chargeTransactionEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getChargeTransaction()) {
                if (serverEventMid.equals(chargeTransactionEvent.getMid())) {
                    chargeTransactionEvent.setSentToServer(true);
                }
            }

            for (BOListUpdated listUpdatedEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getListUpdated()) {
                if (serverEventMid.equals(listUpdatedEvent.getMid())) {
                    listUpdatedEvent.setSentToServer(true);
                }
            }

            for (BOTimedEvent timedEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getTimedEvent()) {
                if (serverEventMid.equals(timedEvent.getMid())) {
                    timedEvent.setSentToServer(true);
                }
            }

            for (BOCustomEvent singleCusEvent : sessionObject.getSingleDaySessions().getDeveloperCodified().getCustomEvent()) {
                if (serverEventMid.equals(singleCusEvent.getMid())) {
                    singleCusEvent.setSentToServer(true);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

    }

    public void updateSessionRetentionEvents(String serverEventMid, BOAppSessionDataModel sessionObject) {
        //Update Retention Event Send To Server
        try {
            if (sessionObject.getSingleDaySessions().getRetentionEvent() != null && serverEventMid.equals(sessionObject.getSingleDaySessions().getRetentionEvent().getMid())) {
                sessionObject.getSingleDaySessions().getRetentionEvent().setSentToServer(true);
            }

            if (sessionObject.getSingleDaySessions().getRetentionEvent().getDau() != null && sessionObject.getSingleDaySessions().getRetentionEvent().getDau() != null && serverEventMid.equals(sessionObject.getSingleDaySessions().getRetentionEvent().getDau().getMid())) {
                sessionObject.getSingleDaySessions().getRetentionEvent().getDau().setSentToServer(true);
            }

            if (sessionObject.getSingleDaySessions().getRetentionEvent().getDpu() != null && serverEventMid.equals(sessionObject.getSingleDaySessions().getRetentionEvent().getDpu().getMid())) {
                sessionObject.getSingleDaySessions().getRetentionEvent().getDpu().setSentToServer(true);
            }

            if (sessionObject.getSingleDaySessions().getRetentionEvent().getAppInstalled() != null && serverEventMid.equals(sessionObject.getSingleDaySessions().getRetentionEvent().getAppInstalled().getMid())) {
                sessionObject.getSingleDaySessions().getRetentionEvent().getAppInstalled().setSentToServer(true);
            }

            if (sessionObject.getSingleDaySessions().getRetentionEvent().getNewUser() != null && serverEventMid.equals(sessionObject.getSingleDaySessions().getRetentionEvent().getNewUser().getMid())) {
                sessionObject.getSingleDaySessions().getRetentionEvent().getNewUser().setSentToServer(true);
            }

            if (sessionObject.getSingleDaySessions().getRetentionEvent().getDast() != null && serverEventMid.equals(sessionObject.getSingleDaySessions().getRetentionEvent().getDast().getMid())) {
                sessionObject.getSingleDaySessions().getRetentionEvent().getDast().setSentToServer(true);
            }

            for (BOCustomEvent singleRenEvent : sessionObject.getSingleDaySessions().getRetentionEvent().getCustomEvents()) {
                if (serverEventMid.equals(singleRenEvent.getMid())) {
                    singleRenEvent.setSentToServer(true);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    public void updateRetentionEvents(String serverEventMid, BOAppLifetimeData lifetimeDataObject) {
        try {
            for (BOAppLifeTimeInfo appLifeInfo : lifetimeDataObject.getAppLifeTimeInfo()) {
                if (appLifeInfo.getRetentionEvent().getCustomEvents() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getCustomEvents().getMid())) {
                    appLifeInfo.getRetentionEvent().getCustomEvents().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getDau() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getDau().getMid())) {
                    appLifeInfo.getRetentionEvent().getDau().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getWau() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getWau().getMid())) {
                    appLifeInfo.getRetentionEvent().getWau().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getMau() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getMau().getMid())) {
                    appLifeInfo.getRetentionEvent().getMau().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getDpu() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getDpu().getMid())) {
                    appLifeInfo.getRetentionEvent().getDpu().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getWpu() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getWpu().getMid())) {
                    appLifeInfo.getRetentionEvent().getWpu().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getMpu() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getMpu().getMid())) {
                    appLifeInfo.getRetentionEvent().getMpu().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getAppInstalled() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getAppInstalled().getMid())) {
                    appLifeInfo.getRetentionEvent().getAppInstalled().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getNewUser() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getNewUser().getMid())) {
                    appLifeInfo.getRetentionEvent().getNewUser().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getDast() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getDast().getMid())) {
                    appLifeInfo.getRetentionEvent().getDast().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getWast() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getWast().getMid())) {
                    appLifeInfo.getRetentionEvent().getWast().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getMast() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getMast().getMid())) {
                    appLifeInfo.getRetentionEvent().getMast().setSentToServer(true);
                }
                if (appLifeInfo.getRetentionEvent().getDast() != null && serverEventMid.equals(appLifeInfo.getRetentionEvent().getDast().getMid())) {
                    appLifeInfo.getRetentionEvent().getDast().setSentToServer(true);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }
}
