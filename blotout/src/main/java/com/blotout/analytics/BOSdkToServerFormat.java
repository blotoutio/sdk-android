package com.blotout.analytics;

import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.Controllers.BOSDKManifestController;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.model.lifetime.BOAppLifetimeData;
import com.blotout.model.lifetime.BOCustomEvents;
import com.blotout.model.session.BOAccessoriesAttached;
import com.blotout.model.session.BOAdInfo;
import com.blotout.model.session.BOAddToCart;
import com.blotout.model.session.BOApp;
import com.blotout.model.session.BOAppNavigation;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.model.session.BOAppStates;
import com.blotout.model.session.BOBatteryLevel;
import com.blotout.model.session.BOBroadcastAddress;
import com.blotout.model.session.BOChargeTransaction;
import com.blotout.model.session.BOCommonEvent;
import com.blotout.model.session.BOConnectedTo;
import com.blotout.model.session.BOCrashDetail;
import com.blotout.model.session.BOCustomEvent;
import com.blotout.model.session.BODeveloperCodified;
import com.blotout.model.session.BODoubleTap;
import com.blotout.model.session.BOIPAddress;
import com.blotout.model.session.BOMemoryInfo;
import com.blotout.model.session.BONameOfAttachedAccessory;
import com.blotout.model.session.BONetMask;
import com.blotout.model.session.BONetworkInfo;
import com.blotout.model.session.BONumberOfA;
import com.blotout.model.session.BOProcessorsUsage;
import com.blotout.model.session.BORetentionEvent;
import com.blotout.model.session.BOScreenEdgePan;
import com.blotout.model.session.BOSessionDeviceInfo;
import com.blotout.model.session.BOSessionInfo;
import com.blotout.model.session.BOSingleDaySessions;
import com.blotout.model.session.BOStorageInfo;
import com.blotout.model.session.BOTimedEvent;
import com.blotout.model.session.BOUbiAutoDetected;
import com.blotout.model.session.BOView;
import com.blotout.model.session.BOWifiRouterAddress;
import com.blotout.model.session.BOWifiSSID;
import com.blotout.referrerapi.BOInstallReferrerHelper;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.BODeviceDetection;
import com.blotout.utilities.BOEncryptionManager;
import com.blotout.utilities.BOServerDataConverter;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Blotout on 03,November,2019
 * This class is preparing data to send on server for further processing
 */
public class BOSdkToServerFormat {

    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BOSdkToServerFormat";

    private static volatile BOSdkToServerFormat instance;
    private HashMap<String, Object> previousMetaData;

    public static BOSdkToServerFormat getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOSdkToServerFormat.class) {
                if (instance == null) {
                    instance = new BOSdkToServerFormat();
                }
            }
        }
        return instance;
    }

    private BOSdkToServerFormat() {

    }

    /**
     * This method encapsulate json data for developer codified event
     *
     * @param sessionData BOAppSessionDataModel
     * @return HashMap
     */
    public HashMap<String, Object> serverFormatEventsJSONFrom(@NonNull BOAppSessionDataModel sessionData) {

        try {
            List<HashMap<String, Object>> events = new ArrayList<>();

            //Prepare
            if (BlotoutAnalytics_Internal.getInstance().isDeveloperEventsEnabled && BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
                events.addAll(this.prepareDeveloperCodifiedEvents(sessionData));
            }

            if (BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
                events.addAll(this.prepareCrashEvents(sessionData));
                if (!BOSharedPreferenceImpl.getInstance().isReferralEventSent())
                    events.addAll(this.prepareInstallReferrarInfo());

                //system events
                if (BOSDKManifestController.getInstance().sdkPushSystemEvents) {
                    events.addAll(this.prepareAppStateEvents(sessionData));
                    events.addAll(this.prepareCommonEvents(sessionData));
                    events.addAll(this.prepareDeviceEvents(sessionData));
                    events.addAll(this.prepareMemoryEvents(sessionData));
                }

                if (BOSDKManifestController.getInstance().sdkPushPIIEvents) {
                    events.addAll(this.preparePIIEvents(sessionData));
                    events.addAll(this.prepareAdInfo(sessionData));
                }

                if (BOSDKManifestController.getInstance().sdkBehaviourEvents) {
                    events.addAll(this.prepareNavigationEvents(sessionData));
                }
            }

            //add userID mapping
            if (BOSDKManifestController.getInstance().sdkMapUserId && events != null && events.size() > 0) {
                events = this.addUserIdMapping(events);
            }

            HashMap<String, Object> serverData = new HashMap<>();

            if (events != null && events.size() > 0) {
                serverData.put(BONetworkConstants.BO_META, this.prepareMetaData(sessionData));
                serverData.put(BONetworkConstants.BO_GEO, this.prepareGeoData(sessionData));
                serverData.put(BONetworkConstants.BO_PMETA, null);
                serverData.put(BONetworkConstants.BO_EVENTS, events);
                return serverData;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method encapsulate json data for developer codified event
     *
     * @param sessionData BOAppSessionDataModel
     * @return HashMap
     */
    public HashMap<String, Object> serverFormatPIIPHIEventsJSONFrom(@NonNull BOAppSessionDataModel sessionData) {

        try {

            List<HashMap<String, Object>> piiServerData = null;
            List<HashMap<String, Object>> phiServerData = null;

            //Prepare
            if (BlotoutAnalytics_Internal.getInstance().isDeveloperEventsEnabled && BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {

                if (BOSDKManifestController.getInstance().sdkPushPIIEvents) {
                    piiServerData = this.prepareDeveloperCodifiedPIIEvents(sessionData);
                }

                if (BOSDKManifestController.getInstance().sdkPushPHIEvents) {
                    phiServerData = this.prepareDeveloperCodifiedPHIEvents(sessionData);
                }
            }

            //add userID mapping
            if (BOSDKManifestController.getInstance().sdkMapUserId) {
                if (piiServerData != null) {
                    piiServerData = this.addUserIdMapping(piiServerData);
                }
                if (phiServerData != null) {
                    phiServerData = this.addUserIdMapping(phiServerData);
                }
            }

            HashMap<String, Object> serverData = new HashMap<>();

            if ((piiServerData != null && piiServerData.size() > 0)
                    || (phiServerData != null && phiServerData.size() > 0)) {

                String secretKey = BOCommonUtils.getUUID();
                secretKey = secretKey.replace("-", "");
                BOEncryptionManager encryptionManager = new BOEncryptionManager(BOEncryptionManager.ALGORITHM_AES_CBC_PKCS5Padding, secretKey, BOEncryptionManager.MODE_256BIT);

                serverData.put(BONetworkConstants.BO_META, this.prepareMetaData(sessionData));
                serverData.put(BONetworkConstants.BO_GEO, this.prepareGeoData(sessionData));

                String piiEncryptedData = null;
                String piiEncryptedSecretKey = null;
                if (piiServerData != null && piiServerData.size() > 0) {

                    byte[] dataToEncryptPII = this.convertObjectToByte(piiServerData);
                    piiEncryptedData = Base64.encodeToString(encryptionManager.encrypt(dataToEncryptPII), Base64.NO_WRAP);
                    piiEncryptedSecretKey = BOEncryptionManager.encryptText(secretKey.getBytes(), BOSDKManifestController.getInstance().sdkPIIPublicKey);

                    HashMap<String, Object> piiPayload = new HashMap<>();
                    piiPayload.put(BONetworkConstants.BO_KEY, piiEncryptedSecretKey);
                    piiPayload.put(BONetworkConstants.BO_IV, BOEncryptionManager.CRYPTO_IVX_STRING);
                    piiPayload.put(BONetworkConstants.BO_DATA, piiEncryptedData);

                    if (piiEncryptedSecretKey != null && piiEncryptedData != null) {
                        serverData.put(BONetworkConstants.BO_PII, piiPayload);
                    } else {
                        serverData.put(BONetworkConstants.BO_PII, null);
                    }

                } else {
                    serverData.put(BONetworkConstants.BO_PII, null);
                }

                String phiEncryptedData = null;
                String phiEncryptedSecretKey = null;
                if (phiServerData != null && phiServerData.size() > 0) {
                    byte[] dataToEncryptPHI = this.convertObjectToByte(phiServerData);
                    phiEncryptedData = Base64.encodeToString(encryptionManager.encrypt(dataToEncryptPHI), Base64.NO_WRAP);
                    phiEncryptedSecretKey = BOEncryptionManager.encryptText(secretKey.getBytes(), BOSDKManifestController.getInstance().sdkPHIPublicKey);

                    HashMap<String, Object> phiPayload = new HashMap<>();
                    phiPayload.put(BONetworkConstants.BO_KEY, phiEncryptedSecretKey);
                    phiPayload.put(BONetworkConstants.BO_IV, BOEncryptionManager.CRYPTO_IVX_STRING);
                    phiPayload.put(BONetworkConstants.BO_DATA, phiEncryptedData);

                    if (phiEncryptedSecretKey != null && phiEncryptedData != null) {
                        serverData.put(BONetworkConstants.BO_PHI, phiPayload);
                    } else {
                        serverData.put(BONetworkConstants.BO_PHI, null);
                    }

                } else {
                    serverData.put(BONetworkConstants.BO_PHI, null);
                }
                serverData.put(BONetworkConstants.BO_PII_DATA, piiServerData);
                serverData.put(BONetworkConstants.BO_PHI_DATA, phiServerData);


                if ((piiEncryptedData != null && piiEncryptedSecretKey != null) || (phiEncryptedData != null && phiEncryptedSecretKey != null)) {
                    return serverData;
                } else {
                    //clear all data if encryption failed
                    serverData.clear();
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method encapsulate json data for retention events
     *
     * @param sessionData BOAppSessionDataModel
     * @return HashMap
     */
    public HashMap<String, Object> serverFormatRetentionEventsFrom(@NonNull BOAppSessionDataModel sessionData) {

        try {

            //Return No retention Event in case of firstParty container
            if (BOSDKManifestController.getInstance().sdkModeDeployment == BOSDKManifestController.BO_DEPLOYMENT_MODE_FIRST_PARTY) {
                return null;
            }

            List<HashMap<String, Object>> events = new ArrayList<>();
            if (BlotoutAnalytics_Internal.getInstance().isRetentionEventsEnabled && BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
                events.addAll(this.prepareRetentionEvents(sessionData));
            }

            //add userID mapping
            if (BOSDKManifestController.getInstance().sdkMapUserId && events != null && events.size() > 0) {
                events = this.addUserIdMapping(events);
            }

            HashMap<String, Object> serverData = new HashMap<>();
            if (events.size() > 0) {

                HashMap<String, Object> pMeta = this.preparePreviousMetaData(sessionData);

                serverData.put(BONetworkConstants.BO_META, this.prepareMetaData(sessionData));
                serverData.put(BONetworkConstants.BO_GEO, this.prepareGeoData(sessionData));
                serverData.put(BONetworkConstants.BO_PMETA, pMeta);
                serverData.put(BONetworkConstants.BO_EVENTS, events);

                return serverData;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    /**
     * This method encapsulate json data for lifetime events
     *
     * @param lifetimeSessionData BOAppLifetimeData
     * @return HashMap
     */
    public HashMap<String, Object> serverFormatLifeTimeRetentionEventsJSONFrom(@NonNull BOAppLifetimeData lifetimeSessionData) {

        try {

            //Return No retention Event in case of firstParty container
            if (BOSDKManifestController.getInstance().sdkModeDeployment == BOSDKManifestController.BO_DEPLOYMENT_MODE_FIRST_PARTY) {
                return null;
            }

            List<HashMap<String, Object>> events = new ArrayList<>();

            if (BlotoutAnalytics_Internal.getInstance().isRetentionEventsEnabled && BlotoutAnalytics_Internal.getInstance().isSDKEnabled) {
                List<HashMap<String, Object>> event = this.prepareRetentionEventsFromLifeTime(lifetimeSessionData);
                if (event != null && event.size() > 0) {
                    events.addAll(event);
                }
            }

            //add userID mapping
            if (BOSDKManifestController.getInstance().sdkMapUserId && events != null && events.size() > 0) {
                events = this.addUserIdMapping(events);
            }

            HashMap<String, Object> serverData = new HashMap<>();
            if (events.size() > 0) {
                serverData.put(BONetworkConstants.BO_META, this.prepareMetaData(null));
                serverData.put(BONetworkConstants.BO_GEO, this.prepareGeoData(null));
                serverData.put(BONetworkConstants.BO_PMETA, null);
                serverData.put(BONetworkConstants.BO_EVENTS, events);
                return serverData;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    public HashMap<String, Object> serverFormatLifeTimeEventsJSONFrom(@NonNull BOAppLifetimeData lifetimeSessionData) {
        return new HashMap<>();
    }

    /**
     * This method preparing json for pMeta changes
     *
     * @param sessionData BOAppSessionDataModel
     * @return HashMap
     */
    private HashMap<String, Object> preparePreviousMetaData(BOAppSessionDataModel sessionData) {
        try {
            if (this.previousMetaData == null || this.previousMetaData.size() == 0) {
                HashMap<String, Object> previousMetaDatas = BOServerDataConverter.preparePreviousMetaData(sessionData);
                if (previousMetaDatas != null) {
                    this.previousMetaData = new HashMap<>(previousMetaDatas);
                    return this.previousMetaData;
                } else {
                    return new HashMap<>();
                }
            } else {
                return new HashMap<>(this.previousMetaData);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return new HashMap<>();
        }
    }


    /* prepare Meta Data*/
    private HashMap<String, Object> prepareMetaData(BOAppSessionDataModel sessionData) {
        try {
                HashMap<String, Object> metaDataMap = BOServerDataConverter.prepareMetaData();
                if (metaDataMap != null) {
                    return new HashMap<>(metaDataMap);
                } else {
                    return new HashMap<>();
                }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return new HashMap<>();
        }
    }

    /* prepare Geo Data*/
    @Nullable
    private HashMap<String, Object> prepareGeoData(BOAppSessionDataModel sessionData) {

        try {
                HashMap<String, Object> geoDataMap = BOServerDataConverter.prepareGeoData();
                if (geoDataMap != null) {
                    return new HashMap<>(geoDataMap);
                } else {
                    return new HashMap<>();
                }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return new HashMap<>();
        }
    }

    @NonNull
    private List<HashMap<String, Object>> prepareDeveloperCodifiedEvents(
            @NonNull BOAppSessionDataModel sessionData) {
        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();
        try {

            BODeveloperCodified developerCodified = sessionData.getSingleDaySessions().getDeveloperCodified();
            if (developerCodified.getAddToCart() != null && developerCodified.getAddToCart().size() > 0) {
                for (BOAddToCart addToCart : developerCodified.getAddToCart()) {
                    if (!addToCart.getSentToServer()) {
                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getAddToCart().indexOf(addToCart));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_ADD_TO_CART);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(addToCart.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_ADD_TO_CART_KEY);
                        customEventJson.put(BONetworkConstants.BO_PROPERTIES, addToCart.getAdditionalInfo());
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, addToCart.getMid());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, addToCart.getSessionId());
                        eventsArray.add(customEventJson);
                    }
                }
            }
            if (developerCodified.getChargeTransaction() != null && developerCodified.getChargeTransaction().size() > 0) {
                for (BOChargeTransaction transaction : developerCodified.getChargeTransaction()) {
                    if (!transaction.getSentToServer()) {
                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getChargeTransaction().indexOf(transaction));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_CHARGE_TRANSACTION);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(transaction.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_CHARGE_TRANSACTION_BUTTON_KEY);
                        customEventJson.put(BONetworkConstants.BO_PROPERTIES, transaction.getTransactionInfo());
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, transaction.getMid());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, transaction.getSessionId());
                        eventsArray.add(customEventJson);
                    }
                }
            }
            if (developerCodified.getCustomEvent() != null && developerCodified.getCustomEvent().size() > 0) {
                for (BOCustomEvent customEvent : developerCodified.getCustomEvent()) {
                    if (customEvent != null && !customEvent.getSentToServer()) {

                        long eventSubCode = customEvent.getEventSubCode();

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getCustomEvent().indexOf(customEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, customEvent.getEventName());
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(customEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, eventSubCode); //BONetworkConstants.BO_DEV_EVENT_CUSTOM_KEY
                        customEventJson.put(BONetworkConstants.BO_PROPERTIES, customEvent.getEventInfo());
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, customEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, customEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, customEvent.getSessionId());
                        eventsArray.add(customEventJson);
                    }
                }
            }
            if (developerCodified.getTimedEvent() != null && developerCodified.getTimedEvent().size() > 0) {
                for (BOTimedEvent timedEvent : developerCodified.getTimedEvent()) {
                    if (!timedEvent.getSentToServer()) {

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getTimedEvent().indexOf(timedEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, timedEvent.getEventName());
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(timedEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_TIMED_KEY);
                        customEventJson.put(BONetworkConstants.BO_PROPERTIES, timedEvent.getTimedEvenInfo());
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, timedEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, timedEvent.getSessionId());
                        eventsArray.add(customEventJson);
                    }
                }
            }

            if (developerCodified.getScreenEdgePan() != null && developerCodified.getScreenEdgePan().size() > 0) {
                for (BOScreenEdgePan screenEdgepan : developerCodified.getScreenEdgePan()) {
                    if (!screenEdgepan.getSentToServer()) {

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getScreenEdgePan().indexOf(screenEdgepan));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_SCREEN_EDGE_PAN);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(screenEdgepan.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_EDGE_PAN_GESTURE_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, screenEdgepan.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, screenEdgepan.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_FROM, screenEdgepan.getScreenRectFrom().getScreenX());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_TO, screenEdgepan.getScreenRectFrom().getScreenY());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, screenEdgepan.getSessionId());
                        eventsArray.add(customEventJson);
                    }
                }
            }

            if (developerCodified.getView() != null && developerCodified.getView().size() > 0) {
                for (BOView viewEvent : developerCodified.getView()) {
                    if (!viewEvent.getSentToServer()) {
                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getView().indexOf(viewEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_View);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(viewEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_VIEW_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, viewEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, viewEvent.getViewClassName());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, viewEvent.getSessionId());
                        eventsArray.add(customEventJson);
                    }
                }
            }

            if (developerCodified.getTouchClick() != null && developerCodified.getTouchClick().size() > 0) {
                for (BODoubleTap gestureEvent : developerCodified.getTouchClick()) {
                    if (!gestureEvent.getSentToServer()) {

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getTouchClick().indexOf(gestureEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_TOUCH_CLICK);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(gestureEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_CLICK_TAP_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, gestureEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, gestureEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_TYPE, gestureEvent.getObjectType());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_RECT, gestureEvent.getObjectRect());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, gestureEvent.getSessionId());
                        eventsArray.add(customEventJson);

                    }
                }
            }

            if (developerCodified.getDrag() != null && developerCodified.getDrag().size() > 0) {
                for (BODoubleTap gestureEvent : developerCodified.getDrag()) {
                    if (!gestureEvent.getSentToServer()) {
                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getDrag().indexOf(gestureEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_DRAG);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(gestureEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_DRAG_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, gestureEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, gestureEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_TYPE, gestureEvent.getObjectType());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_RECT, gestureEvent.getObjectRect());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, gestureEvent.getSessionId());
                        List<Object> screenRect = new ArrayList<>();
                        screenRect.add(gestureEvent.getScreenRect().getScreenX());
                        screenRect.add(gestureEvent.getScreenRect().getScreenY());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_SCREEN_RECT, screenRect);

                        eventsArray.add(customEventJson);
                    }
                }
            }

            if (developerCodified.getFlick() != null && developerCodified.getFlick().size() > 0) {
                for (BODoubleTap gestureEvent : developerCodified.getFlick()) {
                    if (!gestureEvent.getSentToServer()) {
                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getFlick().indexOf(gestureEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_FLICK);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(gestureEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_FLICK_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, gestureEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, gestureEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_TYPE, gestureEvent.getObjectType());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_RECT, gestureEvent.getObjectRect());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, gestureEvent.getSessionId());
                        List<Object> screenRect = new ArrayList<>();
                        screenRect.add(gestureEvent.getScreenRect().getScreenX());
                        screenRect.add(gestureEvent.getScreenRect().getScreenY());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_SCREEN_RECT, screenRect);

                        eventsArray.add(customEventJson);
                    }
                }
            }

            if (developerCodified.getSwipe() != null && developerCodified.getSwipe().size() > 0) {
                for (BODoubleTap gestureEvent : developerCodified.getSwipe()) {
                    if (!gestureEvent.getSentToServer()) {
                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getSwipe().indexOf(gestureEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_SWIPE);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(gestureEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_SWIPE_UP_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, gestureEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, gestureEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_TYPE, gestureEvent.getObjectType());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_RECT, gestureEvent.getObjectRect());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, gestureEvent.getSessionId());
                        List<Object> screenRect = new ArrayList<>();
                        screenRect.add(gestureEvent.getScreenRect().getScreenX());
                        screenRect.add(gestureEvent.getScreenRect().getScreenY());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_SCREEN_RECT, screenRect);
                        eventsArray.add(customEventJson);
                    }
                }
            }

            if (developerCodified.getDoubleTap() != null && developerCodified.getDoubleTap().size() > 0) {
                for (BODoubleTap gestureEvent : developerCodified.getDoubleTap()) {
                    if (!gestureEvent.getSentToServer()) {

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getDoubleTap().indexOf(gestureEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_DOUBLE_TAP);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(gestureEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_DOUBLE_CLICK_TAP_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, gestureEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, gestureEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_TYPE, gestureEvent.getObjectType());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_RECT, gestureEvent.getObjectRect());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, gestureEvent.getSessionId());
                        List<Object> screenRect = new ArrayList<>();
                        screenRect.add(gestureEvent.getScreenRect().getScreenX());
                        screenRect.add(gestureEvent.getScreenRect().getScreenY());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_SCREEN_RECT, screenRect);
                        eventsArray.add(customEventJson);
                    }
                }
            }

            if (developerCodified.getTwoFingerTap() != null && developerCodified.getTwoFingerTap().size() > 0) {
                for (BODoubleTap gestureEvent : developerCodified.getTwoFingerTap()) {
                    if (!gestureEvent.getSentToServer()) {

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getTwoFingerTap().indexOf(gestureEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_TWO_FINGER_TAP);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(gestureEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_GESTURE_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, gestureEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, gestureEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_TYPE, gestureEvent.getObjectType());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_RECT, gestureEvent.getObjectRect());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, gestureEvent.getSessionId());

                        List<Object> screenRect = new ArrayList<>();
                        screenRect.add(gestureEvent.getScreenRect().getScreenX());
                        screenRect.add(gestureEvent.getScreenRect().getScreenY());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_SCREEN_RECT, screenRect);
                        eventsArray.add(customEventJson);
                    }
                }
            }

            if (developerCodified.getPinch() != null && developerCodified.getPinch().size() > 0) {
                for (BODoubleTap gestureEvent : developerCodified.getPinch()) {
                    if (!gestureEvent.getSentToServer()) {

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getPinch().indexOf(gestureEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_PINCH);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(gestureEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_PINCH_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, gestureEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, gestureEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_TYPE, gestureEvent.getObjectType());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_RECT, gestureEvent.getObjectRect());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, gestureEvent.getSessionId());

                        List<Object> screenRect = new ArrayList<>();
                        screenRect.add(gestureEvent.getScreenRect().getScreenX());
                        screenRect.add(gestureEvent.getScreenRect().getScreenY());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_SCREEN_RECT, screenRect);
                        eventsArray.add(customEventJson);
                    }
                }
            }
            if (developerCodified.getTouchAndHold() != null && developerCodified.getTouchAndHold().size() > 0) {
                for (BODoubleTap gestureEvent : developerCodified.getTouchAndHold()) {
                    if (!gestureEvent.getSentToServer()) {

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getTouchAndHold().indexOf(gestureEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_TOUCH_AND_HOLD);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(gestureEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_LONG_PRESS_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, gestureEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, gestureEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_TYPE, gestureEvent.getObjectType());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_RECT, gestureEvent.getObjectRect());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, gestureEvent.getSessionId());

                        List<Object> screenRect = new ArrayList<>();
                        screenRect.add(gestureEvent.getScreenRect().getScreenX());
                        screenRect.add(gestureEvent.getScreenRect().getScreenY());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_SCREEN_RECT, screenRect);
                        eventsArray.add(customEventJson);
                    }
                }
            }

            if (developerCodified.getShake() != null && developerCodified.getShake().size() > 0) {
                for (BODoubleTap gestureEvent : developerCodified.getShake()) {
                    if (!gestureEvent.getSentToServer()) {

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getShake().indexOf(gestureEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_SHAKE);
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(gestureEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_DEV_EVENT_SHAKE_KEY);
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, gestureEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, gestureEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_TYPE, gestureEvent.getObjectType());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_RECT, gestureEvent.getObjectRect());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, gestureEvent.getSessionId());

                        List<Object> screenRect = new ArrayList<>();
                        screenRect.add(gestureEvent.getScreenRect().getScreenX());
                        screenRect.add(gestureEvent.getScreenRect().getScreenY());
                        customEventJson.put(BONetworkConstants.BO_OBJECT_SCREEN_RECT, screenRect);
                        eventsArray.add(customEventJson);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }

        return eventsArray;
    }

    /* prepare retention events from session data*/
    @NonNull
    private List<HashMap<String, Object>> prepareRetentionEvents(@NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();

        try {

            BORetentionEvent retentionEvent = sessionData.getSingleDaySessions().getRetentionEvent();
            if (retentionEvent.getDau() != null && !retentionEvent.getDau().getSentToServer()) {
                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "DAU");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getDau().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_DAU_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getDau().getDauInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getDau().getMid());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getDpu() != null && !retentionEvent.getDpu().getSentToServer()) {

                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "DPU");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getDpu().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_DPU_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getDpu().getDpuInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getDpu().getMid());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getAppInstalled() != null && !retentionEvent.getAppInstalled().getSentToServer()) {
                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "AppInstalled");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getAppInstalled().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_APP_INSTALL_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getAppInstalled().getAppInstalledInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getAppInstalled().getMid());
                event.put("isFirstLaunch", retentionEvent.getAppInstalled().getIsFirstLaunch());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getNewUser() != null && !retentionEvent.getNewUser().getSentToServer()) {
                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "NUO");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getNewUser().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_NUO_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getNewUser().getTheNewUserInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getNewUser().getMid());
                event.put("isFirstLaunch", retentionEvent.getNewUser().getIsNewUser());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getDast() != null && !retentionEvent.getDast().getSentToServer()) {

                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "DAST");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getDast().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_DAST_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getDast().getPayload());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getDast().getMid());
                event.put("tst", retentionEvent.getDast().getAverageSessionTime());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getCustomEvents() != null && retentionEvent.getCustomEvents().size() > 0) {

                for (BOCustomEvent customEvent : retentionEvent.getCustomEvents()) {
                    if (!customEvent.getSentToServer()) {
                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, retentionEvent.getCustomEvents().indexOf(customEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, customEvent.getEventName());
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(customEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_CUS_KEY1);
                        customEventJson.put(BONetworkConstants.BO_PROPERTIES, customEvent.getEventInfo());
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, customEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, customEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, customEvent.getSessionId());
                        eventsArray.add(customEventJson);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }

        return eventsArray;
    }

    /* prepare retention events from lifetime data*/
    @NonNull
    private List<HashMap<String, Object>> prepareRetentionEventsFromLifeTime(@NonNull BOAppLifetimeData lifetimeData) {
        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();

        try {

            com.blotout.model.lifetime.BORetentionEvent retentionEvent = null;
            if (lifetimeData.getAppLifeTimeInfo() != null && lifetimeData.getAppLifeTimeInfo().size() > 0) {
                retentionEvent = lifetimeData.getAppLifeTimeInfo().get(lifetimeData.getAppLifeTimeInfo().size() - 1).getRetentionEvent();
            }

            if (retentionEvent == null) {
                return null;
            }

            if (retentionEvent.getDau() != null && !retentionEvent.getDau().getSentToServer()) {
                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "DAU");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getDau().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_DAU_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getDau().getDauInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getDau().getMid());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getDau().getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getWau() != null && !retentionEvent.getWau().getSentToServer()) {
                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "WAU");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getWau().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_WAU_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getWau().getWauInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getWau().getMid());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getWau().getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getMau() != null && !retentionEvent.getMau().getSentToServer()) {
                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "MAU");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getMau().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_MAU_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getMau().getMauInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getMau().getMid());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getMau().getSessionId());
                eventsArray.add(event);
            }
            if (BlotoutAnalytics_Internal.getInstance().isPayingUser) {
                if (retentionEvent.getDpu() != null && !retentionEvent.getDpu().getSentToServer()) {

                    HashMap<String, Object> event = new HashMap<>();
                    event.put(BONetworkConstants.BO_EVENT_NAME, "DPU");
                    event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getDpu().getTimeStamp()));
                    event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                    event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_DPU_KEY);
                    event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getDpu().getDpuInfo());
                    event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getDpu().getMid());
                    event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getDpu().getSessionId());
                    eventsArray.add(event);
                }

                if (retentionEvent.getWpu() != null && !retentionEvent.getWpu().getSentToServer()) {
                    HashMap<String, Object> event = new HashMap<>();
                    event.put(BONetworkConstants.BO_EVENT_NAME, "WPU");
                    event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getWpu().getTimeStamp()));
                    event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                    event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_WPU_KEY);
                    event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getWpu().getWpuInfo());
                    event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getWpu().getMid());
                    event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getWpu().getSessionId());
                    eventsArray.add(event);
                }

                if (retentionEvent.getMpu() != null && !retentionEvent.getMpu().getSentToServer()) {
                    HashMap<String, Object> event = new HashMap<>();
                    event.put(BONetworkConstants.BO_EVENT_NAME, "MPU");
                    event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getMau().getTimeStamp()));
                    event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                    event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_MPU_KEY);
                    event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getMpu().getMpuInfo());
                    event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getMpu().getMid());
                    event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getMpu().getSessionId());
                    eventsArray.add(event);
                }
            }
            if (retentionEvent.getAppInstalled() != null && !retentionEvent.getAppInstalled().getSentToServer()) {
                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "AppInstalled");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getAppInstalled().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_APP_INSTALL_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getAppInstalled().getAppInstalledInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getAppInstalled().getMid());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getAppInstalled().getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getNewUser() != null && !retentionEvent.getNewUser().getSentToServer()) {
                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "NUO");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getNewUser().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_NUO_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getNewUser().getTheNewUserInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getNewUser().getMid());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getNewUser().getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getDast() != null && !retentionEvent.getDast().getSentToServer()) {

                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "DAST");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getDast().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_DAST_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getDast().getDastInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getDast().getMid());
                event.put("tst", retentionEvent.getDast().getAverageSessionTime());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getDast().getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getWast() != null && !retentionEvent.getWast().getSentToServer()) {

                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "WAST");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getWast().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_WAST_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getWast().getWastInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getWast().getMid());
                event.put("tst", retentionEvent.getWast().getAverageSessionTime());
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getWast().getSessionId());
                eventsArray.add(event);
            }

            if (retentionEvent.getMast() != null && !retentionEvent.getMast().getSentToServer()) {

                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, "MAST");
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(retentionEvent.getMast().getTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_MAST_KEY);
                event.put(BONetworkConstants.BO_PROPERTIES, retentionEvent.getMast().getMastInfo());
                event.put(BONetworkConstants.BO_MESSAGE_ID, retentionEvent.getMast().getMid());
                event.put("tst", retentionEvent.getMast().getAverageSessionTime());
                eventsArray.add(event);
                event.put(BONetworkConstants.BO_SESSION_ID, retentionEvent.getMast().getSessionId());
            }

            if (retentionEvent.getCustomEvents() != null && retentionEvent.getCustomEvents().getTimeStamp() > 0) {

                BOCustomEvents customEvent = retentionEvent.getCustomEvents();
                if (!customEvent.getSentToServer()) {
                    HashMap<String, Object> customEventJson = new HashMap<>();
                    customEventJson.put(BONetworkConstants.BO_EVENT_NAME, customEvent.getEventName());
                    customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(customEvent.getTimeStamp()));
                    customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_RETENTION_KEY);
                    customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_RETEN_CUS_KEY1);
                    customEventJson.put(BONetworkConstants.BO_PROPERTIES, customEvent.getEventInfo());
                    customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, customEvent.getMid());
                    customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, customEvent.getVisibleClassName());
                    customEventJson.put(BONetworkConstants.BO_SESSION_ID, customEvent.getSessionId());
                    eventsArray.add(customEventJson);
                }

            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    /* prepare crash events*/
    @NonNull
    private List<HashMap<String, Object>> prepareCrashEvents(@NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();

        try {

            List<BOCrashDetail> crashEvents = sessionData.getSingleDaySessions().getCrashDetails();
            if (crashEvents != null && crashEvents.size() > 0) {
                for (BOCrashDetail crashEvent : crashEvents) {
                    if (crashEvent != null && !crashEvent.getSentToServer()) {

                        HashMap<String, Object> crashEventProperties = new HashMap<>();
                        crashEventProperties.put("info", crashEvent.getInfo());
                        if (crashEvent.getCallStackSymbols() != null) {
                            crashEventProperties.put("callStackSymbols", crashEvent.getCallStackSymbols().toString());
                        }
                        if (crashEvent.getCallStackReturnAddress() != null) {
                            crashEventProperties.put("callStackAddress", crashEvent.getCallStackReturnAddress().toString());
                        }

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, crashEvents.indexOf(crashEvent));
                        event.put(BONetworkConstants.BO_EVENT_NAME, crashEvent.getName());
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(crashEvent.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_RUN_TIME_EXCEPTION);
                        event.put(BONetworkConstants.BO_PROPERTIES, crashEventProperties);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, crashEvent.getMid());
                        event.put(BONetworkConstants.BO_SESSION_ID, crashEvent.getSessionId());

                        List<String> callbackSymbol = crashEvent.getCallStackSymbols();
                        String lastObject = null;
                        if (callbackSymbol != null && !callbackSymbol.isEmpty()) {
                            lastObject = callbackSymbol.get(callbackSymbol.size() - 1);
                        }
                        event.put(BONetworkConstants.BO_SCREEN_NAME, lastObject);

                        List<Integer> uustate = new ArrayList<>();
                        uustate.add(101);
                        event.put("uustate", uustate);

                        event.put("value", crashEvent.getReason());
                        eventsArray.add(event);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    /* prepare app states events like background and forground*/
    @NonNull
    private List<HashMap<String, Object>> prepareAppStateEvents(@NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();

        try {

            BOAppStates appStates = sessionData.getSingleDaySessions().getAppStates();

            if (appStates.getAppLaunched() != null && appStates.getAppLaunched().size() > 0) {
                for (BOApp appInfo : appStates.getAppLaunched()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, appStates.getAppLaunched().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_APP_LAUNCHED);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_SESSION_START_KEY);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (appStates.getAppResignActive() != null && appStates.getAppResignActive().size() > 0) {
                for (BOApp appInfo : appStates.getAppResignActive()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, appStates.getAppResignActive().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_RESIGN_ACTIVE);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_SESSION_END_KEY);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (appStates.getAppInBackground() != null && appStates.getAppInBackground().size() > 0) {
                for (BOApp appInfo : appStates.getAppInBackground()) {
                    if (!appInfo.getSentToServer()) {
                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, appStates.getAppInBackground().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_APP_IN_BACKGROUND);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_BACKGROUND_KEY);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (appStates.getAppInForeground() != null && appStates.getAppInForeground().size() > 0) {
                for (BOApp appInfo : appStates.getAppInForeground()) {
                    if (!appInfo.getSentToServer()) {
                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, appStates.getAppInForeground().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_APP_IN_FOREGROUND);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_FOREGROUND_KEY);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (appStates.getAppOrientationLandscape() != null && appStates.getAppOrientationLandscape().size() > 0) {
                for (BOApp appInfo : appStates.getAppOrientationLandscape()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, appStates.getAppOrientationLandscape().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_APP_ORIENTATION_LANDSCAPE);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_LANDSCAPE_ORIENTATION_KEY);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (appStates.getAppOrientationPortrait() != null && appStates.getAppOrientationPortrait().size() > 0) {
                for (BOApp appInfo : appStates.getAppOrientationPortrait()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, appStates.getAppOrientationPortrait().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_APP_ORIENTATION_PORTRAIT);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_PORTRAIT_ORIENTATION_KEY);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (appStates.getAppNotificationReceived() != null && appStates.getAppNotificationReceived().size() > 0) {
                for (BOApp appInfo : appStates.getAppNotificationReceived()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, appStates.getAppNotificationReceived().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_APP_NOTIFICATION_RECEIVED);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_NOTIFICATION_RECEIVED_KEY);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }
            if (appStates.getAppNotificationViewed() != null && appStates.getAppNotificationViewed().size() > 0) {
                for (BOApp appInfo : appStates.getAppNotificationViewed()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, appStates.getAppNotificationViewed().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_APP_NOTIFICATION_VIEWED);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_NOTIFICATION_VIEWED_KEY);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (appStates.getAppNotificationClicked() != null && appStates.getAppNotificationClicked().size() > 0) {
                for (BOApp appInfo : appStates.getAppNotificationClicked()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, appStates.getAppNotificationClicked().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_APP_NOTIFICATION_CLICKED);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_NOTIFICATION_CLICKED_KEY);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BONetworkConstants.BO_APP_NOTIFICATION_CLICKED));
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (appStates.getAppSessionInfo() != null && appStates.getAppSessionInfo().size() > 0) {
                for (BOSessionInfo sessionInfo : appStates.getAppSessionInfo()) {
                    if (!sessionInfo.getSentToServer()) {

                        HashMap<String, Object> sessionEvent = new HashMap<>();
                        sessionEvent.put(BOCommonConstants.BO_START, sessionInfo.getStart());
                        sessionEvent.put(BOCommonConstants.BO_END, sessionInfo.getEnd());
                        sessionEvent.put(BOCommonConstants.BO_DURATION, sessionInfo.getDuration());

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, appStates.getAppSessionInfo().indexOf(sessionInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_APP_SESSION_INFO);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(sessionInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_SESSION_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BONetworkConstants.BO_APP_SESSION_INFO));
                        event.put(BONetworkConstants.BO_PROPERTIES, sessionEvent);
                        event.put(BONetworkConstants.BO_SESSION_ID, sessionInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            return eventsArray;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
    }

    /* prepare app common events send status of other work*/
    @NonNull
    private List<HashMap<String, Object>> prepareCommonEvents(@NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();

        try {

            List<BOCommonEvent> commonEvents = sessionData.getSingleDaySessions().getCommonEvents();
            if (commonEvents != null && commonEvents.size() > 0) {
                for (BOCommonEvent commonEvent : commonEvents) {

                    if (!BlotoutAnalytics_Internal.getInstance().isFunnelEventsEnabled && commonEvent.getEventCode() == BONetworkConstants.BO_EVENT_FUNNEL_KEY) {
                        continue;
                    }

                    if (!BlotoutAnalytics_Internal.getInstance().isSegmentEventsEnabled && commonEvent.getEventCode() == BONetworkConstants.BO_EVENT_SEGMENT_KEY) {
                        continue;
                    }

                    if (!commonEvent.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, commonEvents.indexOf(commonEvent));
                        event.put(BONetworkConstants.BO_EVENT_NAME, commonEvent.getEventName());
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(commonEvent.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, commonEvent.getEventCode());
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, commonEvent.getEventSubCode());
                        event.put(BONetworkConstants.BO_PROPERTIES, commonEvent.getEventInfo());
                        event.put(BONetworkConstants.BO_MESSAGE_ID, commonEvent.getMid());
                        event.put(BONetworkConstants.BO_SESSION_ID, commonEvent.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    @NonNull
    private List<HashMap<String, Object>> prepareDeviceEvents(@NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();

        try {
            BOSessionDeviceInfo deviceInfo = sessionData.getSingleDaySessions().getDeviceInfo();

            if (deviceInfo.getBatteryLevel() != null && deviceInfo.getBatteryLevel().size() > 0) {
                for (BOBatteryLevel appInfo : deviceInfo.getBatteryLevel()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getBatteryLevel().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_BATTERY_LEVEL);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getPercentage());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getMultitaskingEnabled() != null && deviceInfo.getMultitaskingEnabled().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getMultitaskingEnabled()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getMultitaskingEnabled().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_MULTITASKING_ENABLED);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getStatus());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getProximitySensorEnabled() != null && deviceInfo.getProximitySensorEnabled().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getProximitySensorEnabled()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getProximitySensorEnabled().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_PROXIMITY_SENSOR_ENABLED);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getStatus());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getDebuggerAttached() != null && deviceInfo.getDebuggerAttached().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getDebuggerAttached()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getDebuggerAttached().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_DEBUGGER_ATTACHED);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getStatus());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getPluggedIn() != null && deviceInfo.getPluggedIn().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getPluggedIn()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getPluggedIn().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_PLUGGEDIN);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getStatus());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getJailBroken() != null && deviceInfo.getJailBroken().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getJailBroken()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getJailBroken().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_JAIL_BROKEN);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getStatus());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getNumberOfActiveProcessors() != null && deviceInfo.getNumberOfActiveProcessors().size() > 0) {
                for (BONumberOfA appInfo : deviceInfo.getNumberOfActiveProcessors()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getNumberOfActiveProcessors().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_NUMBER_OF_ACTIVE_PROCESSORS);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getNumber());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getProcessorsUsage() != null && deviceInfo.getProcessorsUsage().size() > 0) {
                for (BOProcessorsUsage appInfo : deviceInfo.getProcessorsUsage()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getProcessorsUsage().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_PROCESSORS_USAGE);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getUsagePercentage());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getAccessoriesAttached() != null && deviceInfo.getAccessoriesAttached().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getAccessoriesAttached()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getAccessoriesAttached().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_ACCESSORIES_ATTACHED);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getStatus());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getHeadphoneAttached() != null && deviceInfo.getHeadphoneAttached().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getHeadphoneAttached()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getHeadphoneAttached().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_HEADPHONE_ATTACHED);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getStatus());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getNumberOfAttachedAccessories() != null && deviceInfo.getNumberOfAttachedAccessories().size() > 0) {
                for (BONumberOfA appInfo : deviceInfo.getNumberOfAttachedAccessories()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getNumberOfAttachedAccessories().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_NUMBER_OF_ATTACHED_ACCESSORIES);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getNumber());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getNameOfAttachedAccessories() != null && deviceInfo.getNameOfAttachedAccessories().size() > 0) {
                for (BONameOfAttachedAccessory appInfo : deviceInfo.getNameOfAttachedAccessories()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getNameOfAttachedAccessories().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_NAME_OF_ATTACHED_ACCESSORIES);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getNames().toString());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getIsCharging() != null && deviceInfo.getIsCharging().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getIsCharging()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getIsCharging().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_IS_CHARGING);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getStatus());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (deviceInfo.getFullyCharged() != null && deviceInfo.getFullyCharged().size() > 0) {
                for (BOAccessoriesAttached appInfo : deviceInfo.getFullyCharged()) {
                    if (!appInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, deviceInfo.getFullyCharged().indexOf(appInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_FULLY_CHARGED);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(appInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, appInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, appInfo.getStatus());
                        event.put(BONetworkConstants.BO_SESSION_ID, appInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    @NonNull
    private List<HashMap<String, Object>> prepareMemoryEvents(@NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();
        try {
            BOSingleDaySessions singleDaySessions = sessionData.getSingleDaySessions();

            if (singleDaySessions.getMemoryInfo() != null && singleDaySessions.getMemoryInfo().size() > 0) {
                for (BOMemoryInfo memoryInfo : singleDaySessions.getMemoryInfo()) {
                    if (!memoryInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, singleDaySessions.getMemoryInfo().indexOf(memoryInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_MEMORY_INFO);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(memoryInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, memoryInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, memoryInfo.getSessionId());

                        HashMap<String, Object> properties = new HashMap<>();
                        properties.put(BONetworkConstants.BO_EVENT_ACTIVE_MEMORY, memoryInfo.getActiveMemory());
                        properties.put(BONetworkConstants.BO_EVENT_AT_MEMORY_WARNING, memoryInfo.getAtMeoryWarning());
                        properties.put(BONetworkConstants.BO_EVENT_FREE_MEMORY, memoryInfo.getFreeMemory());
                        properties.put(BONetworkConstants.BO_EVENT_INACTIVE_MEMORY, memoryInfo.getInActiveMemory());
                        properties.put(BONetworkConstants.BO_EVENT_PURGEABLE_MEMORY, memoryInfo.getPurgeableMemory());
                        properties.put(BONetworkConstants.BO_EVENT_TOTAL_RAM, memoryInfo.getTotalRAM());
                        properties.put(BONetworkConstants.BO_EVENT_USED_MEMORY, memoryInfo.getUsedMemory());
                        properties.put(BONetworkConstants.BO_EVENT_WIRED_MEMORY, memoryInfo.getWiredMemory());
                        event.put(BONetworkConstants.BO_PROPERTIES, properties);

                        eventsArray.add(event);
                    }
                }
            }

            if (singleDaySessions.getStorageInfo() != null && singleDaySessions.getStorageInfo().size() > 0) {
                for (BOStorageInfo storageInfo : singleDaySessions.getStorageInfo()) {
                    if (!storageInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, singleDaySessions.getStorageInfo().indexOf(storageInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_STORAGE_INFO);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(storageInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, storageInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_SESSION_ID, storageInfo.getSessionId());

                        HashMap<String, Object> properties = new HashMap<>();
                        properties.put(BONetworkConstants.BO_EVENT_UNIT, storageInfo.getUnit());
                        properties.put(BONetworkConstants.BO_EVENT_TOTAL_DISK_SPACE, storageInfo.getTotalDiskSpace());
                        properties.put(BONetworkConstants.BO_EVENT_FREE_DISK_SPACE, storageInfo.getFreeDiskSpace());
                        properties.put(BONetworkConstants.BO_EVENT_USED_DISK_SPACE, storageInfo.getUsedDiskSpace());

                        event.put(BONetworkConstants.BO_PROPERTIES, properties);

                        eventsArray.add(event);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    @NonNull
    private List<HashMap<String, Object>> prepareDeveloperCodifiedPIIEvents(
            @NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();
        try {

            BODeveloperCodified developerCodified = sessionData.getSingleDaySessions().getDeveloperCodified();
            if (developerCodified.getPiiEvent() != null && developerCodified.getPiiEvent().size() > 0) {
                for (BOCustomEvent customEvent : developerCodified.getPiiEvent()) {
                    if (!customEvent.getSentToServer()) {

                        long eventSubCode = customEvent.getEventSubCode();

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getPiiEvent().indexOf(customEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, customEvent.getEventName());
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(customEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, eventSubCode); //BONetworkConstants.BO_DEV_EVENT_CUSTOM_KEY
                        customEventJson.put(BONetworkConstants.BO_PROPERTIES, customEvent.getEventInfo());
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, customEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, customEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, customEvent.getSessionId());

                        eventsArray.add(customEventJson);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    @NonNull
    private List<HashMap<String, Object>> prepareDeveloperCodifiedPHIEvents(
            @NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();
        try {

            BODeveloperCodified developerCodified = sessionData.getSingleDaySessions().getDeveloperCodified();
            if (developerCodified.getPhiEvent() != null && developerCodified.getPhiEvent().size() > 0) {
                for (BOCustomEvent customEvent : developerCodified.getPhiEvent()) {
                    if (!customEvent.getSentToServer()) {

                        long eventSubCode = customEvent.getEventSubCode();

                        HashMap<String, Object> customEventJson = new HashMap<>();
                        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, developerCodified.getPhiEvent().indexOf(customEvent));
                        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, customEvent.getEventName());
                        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(customEvent.getTimeStamp()));
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_DEVELOPER_CODED_KEY);
                        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, eventSubCode);
                        customEventJson.put(BONetworkConstants.BO_PROPERTIES, customEvent.getEventInfo());
                        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, customEvent.getMid());
                        customEventJson.put(BONetworkConstants.BO_SCREEN_NAME, customEvent.getVisibleClassName());
                        customEventJson.put(BONetworkConstants.BO_SESSION_ID, customEvent.getSessionId());
                        eventsArray.add(customEventJson);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    @NonNull
    private List<HashMap<String, Object>> preparePIIEvents(@NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();
        try {
            BONetworkInfo networkInfoArray = sessionData.getSingleDaySessions().getNetworkInfo();

            if (networkInfoArray.getCurrentIPAddress() != null && networkInfoArray.getCurrentIPAddress().size() > 0) {
                for (BOIPAddress networkInfo : networkInfoArray.getCurrentIPAddress()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getCurrentIPAddress().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_CURRENT_IP_ADDRESS);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getipAddress());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());

                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getCellBroadcastAddress() != null && networkInfoArray.getCellBroadcastAddress().size() > 0) {
                for (BOBroadcastAddress networkInfo : networkInfoArray.getCellBroadcastAddress()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getCellBroadcastAddress().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_CELL_BROADCAST_ADDRESS);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getBroadcastAddress());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getCellIPAddress() != null && networkInfoArray.getCellIPAddress().size() > 0) {
                for (BOIPAddress networkInfo : networkInfoArray.getCellIPAddress()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getCellIPAddress().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_CELL_IP_ADDRESS);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getipAddress());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getCellNetMask() != null && networkInfoArray.getCellNetMask().size() > 0) {
                for (BONetMask networkInfo : networkInfoArray.getCellNetMask()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getCellNetMask().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_CELL_NETMASK);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getNetmask());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getConnectedToCellNetwork() != null && networkInfoArray.getConnectedToCellNetwork().size() > 0) {
                for (BOConnectedTo networkInfo : networkInfoArray.getConnectedToCellNetwork()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getConnectedToCellNetwork().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_CONNECTED_TO_CELL_NETWORK);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getIsConnected());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getConnectedToWifi() != null && networkInfoArray.getConnectedToWifi().size() > 0) {
                for (BOConnectedTo networkInfo : networkInfoArray.getConnectedToWifi()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getConnectedToWifi().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_CONNECTED_WIFI);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getIsConnected());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getExternalIPAddress() != null && networkInfoArray.getExternalIPAddress().size() > 0) {
                for (BOIPAddress networkInfo : networkInfoArray.getExternalIPAddress()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getExternalIPAddress().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_EXTERNAL_IP_ADDRESS);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getipAddress());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getWifiBroadcastAddress() != null && networkInfoArray.getWifiBroadcastAddress().size() > 0) {
                for (BOBroadcastAddress networkInfo : networkInfoArray.getWifiBroadcastAddress()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getWifiBroadcastAddress().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_WIFI_BROADCAST_ADDRESS);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getBroadcastAddress());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getWifiIPAddress() != null && networkInfoArray.getWifiIPAddress().size() > 0) {
                for (BOIPAddress networkInfo : networkInfoArray.getWifiIPAddress()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getWifiIPAddress().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_WIFI_IP_ADDRESS);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getipAddress());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getWifiRouterAddress() != null && networkInfoArray.getWifiRouterAddress().size() > 0) {
                for (BOWifiRouterAddress networkInfo : networkInfoArray.getWifiRouterAddress()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getWifiRouterAddress().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_WIFI_ROUTER_ADDRESS);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getRouterAddress());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getWifiSSID() != null && networkInfoArray.getWifiSSID().size() > 0) {
                for (BOWifiSSID networkInfo : networkInfoArray.getWifiSSID()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getWifiSSID().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_WIFI_SSID);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getssid());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }

            if (networkInfoArray.getWifiNetMask() != null && networkInfoArray.getWifiNetMask().size() > 0) {
                for (BONetMask networkInfo : networkInfoArray.getWifiNetMask()) {
                    if (!networkInfo.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, networkInfoArray.getWifiNetMask().indexOf(networkInfo));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_WIFI_NET_MASK);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(networkInfo.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DEVICE_INFO);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, networkInfo.getMid());
                        event.put(BONetworkConstants.BO_SCREEN_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                        event.put(BONetworkConstants.BO_VALUE, networkInfo.getNetmask());
                        event.put(BONetworkConstants.BO_SESSION_ID, networkInfo.getSessionId());
                        eventsArray.add(event);
                    }
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    @NonNull
    private List<HashMap<String, Object>> prepareNavigationEvents(@NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();
        try {
            BOUbiAutoDetected autoDetected = sessionData.getSingleDaySessions().getUbiAutoDetected();
            List<String> screenName = new ArrayList<>();
            List<Long> screenTime = new ArrayList<>();
            if (autoDetected.getAppNavigation() != null && autoDetected.getAppNavigation().size() > 0) {
                for (BOAppNavigation appNavigation : autoDetected.getAppNavigation()) {
                    screenName.add(appNavigation.getTo());
                    screenTime.add(appNavigation.getTimeSpent());
                }
            }
            if (!screenName.isEmpty() && !screenTime.isEmpty()) {
                HashMap<String, Object> event = new HashMap<>();
                event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_APP_NAVIGATION);
                event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp()));
                event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_NAVIGATION);
                event.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BONetworkConstants.BO_APP_NAVIGATION));
                event.put(BONetworkConstants.BO_NAVIGATION_SCREEN, screenName);
                event.put(BONetworkConstants.BO_NAVIGATION_TIME, screenTime);
                event.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);
                eventsArray.add(event);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    @NonNull
    private List<HashMap<String, Object>> prepareAdInfo(@NonNull BOAppSessionDataModel sessionData) {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();
        try {
            List<BOAdInfo> adInfo = sessionData.getSingleDaySessions().getAdInfo();
            if (adInfo != null && adInfo.size() > 0) {
                for (BOAdInfo adInformation : adInfo) {
                    if (!adInformation.getSentToServer()) {

                        HashMap<String, Object> event = new HashMap<>();
                        event.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, adInfo.indexOf(adInformation));
                        event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_EVENT_AD_INFO);
                        event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(adInformation.getTimeStamp()));
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
                        event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_DO_NOT_TRACK);
                        event.put(BONetworkConstants.BO_MESSAGE_ID, adInformation.getMid());
                        event.put(BONetworkConstants.BO_SESSION_ID, adInformation.getSessionId());

                        HashMap<String, Object> property = new HashMap<>();
                        property.put(BONetworkConstants.BO_AD_IDENTIFIER, adInformation.getAdvertisingId());
                        property.put(BONetworkConstants.BO_AD_DO_NOT_TRACK, adInformation.getAdDoNotTrack());

                        event.put(BONetworkConstants.BO_PROPERTIES, property);

                        eventsArray.add(event);


                    }
                }
            }

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    @NonNull
    private List<HashMap<String, Object>> prepareInstallReferrarInfo() {

        List<HashMap<String, Object>> eventsArray = new ArrayList<HashMap<String, Object>>();
        try {

            HashMap<String, Object> event = new HashMap<>();
            event.put(BONetworkConstants.BO_EVENT_NAME, BONetworkConstants.BO_INSTALL_REFERRAR);
            event.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp()));
            event.put(BONetworkConstants.BO_EVENT_CATEGORY, BONetworkConstants.BO_EVENT_SYSTEM_KEY);
            event.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, BONetworkConstants.BO_EVENT_APP_INSTALL_REFERRER);
            event.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BONetworkConstants.BO_INSTALL_REFERRAR));
            event.put(BONetworkConstants.BO_PROPERTIES, BOInstallReferrerHelper.getInstance().getInstallReferrarData());
            event.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

            eventsArray.add(event);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return eventsArray;
        }
        return eventsArray;
    }

    /*Add userId in every item in the list*/
    @NonNull
    private List<HashMap<String, Object>> addUserIdMapping(List<HashMap<String, Object>> events) {
        List<HashMap<String, Object>> userIdMappedEvents = new ArrayList<>();
        String userId = BODeviceDetection.getDeviceId();
        for (HashMap<String, Object> item : events) {
            item.put(BONetworkConstants.BO_USER_ID, userId);
            userIdMappedEvents.add(item);
        }
        return userIdMappedEvents;
    }

    private byte[] convertObjectToByte(Object object) {

        try {
            String jsonStr = null;
            ObjectMapper mapper = new ObjectMapper();
            jsonStr = mapper.writeValueAsString(object);
            return jsonStr.getBytes("utf-8");
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(bos);
//            oos.writeObject(object);
//            oos.flush();
//            byte[] bytes = bos.toByteArray();
//            return bytes;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    public HashMap<String, Object> createEventObject(String eventName, long eventCategory, long eventSubCode) {
        HashMap<String, Object> serverData = new HashMap<>();
        List<HashMap<String, Object>> events = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> customEventJson = new HashMap<>();
        customEventJson.put(BONetworkConstants.BO_EVENT_DAY_OCCURENCE_COUNT, 0);
        customEventJson.put(BONetworkConstants.BO_EVENT_NAME, eventName);
        customEventJson.put(BONetworkConstants.BO_EVENTS_TIME, BODateTimeUtils.roundOffTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp()));
        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY, eventCategory);
        customEventJson.put(BONetworkConstants.BO_EVENT_CATEGORY_SUBTYPE, eventSubCode);
        customEventJson.put(BONetworkConstants.BO_PROPERTIES, null);
        customEventJson.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(eventName));
        customEventJson.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);
        customEventJson.put(BONetworkConstants.BO_USER_ID, BODeviceDetection.getDeviceId());
        events.add(customEventJson);

        serverData.put(BONetworkConstants.BO_META, this.prepareMetaData(null));
        serverData.put(BONetworkConstants.BO_GEO, this.prepareGeoData(null));
        serverData.put(BONetworkConstants.BO_PMETA, null);
        serverData.put(BONetworkConstants.BO_EVENTS, events);
        return serverData;
    }

}
