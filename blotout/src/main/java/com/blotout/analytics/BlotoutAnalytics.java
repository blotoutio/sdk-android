package com.blotout.analytics;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.Controllers.BODeviceAndAppFraudController;
import com.blotout.Controllers.BOFunnelSyncController;
import com.blotout.Controllers.BOSDKManifestController;
import com.blotout.Controllers.BOSegmentsSyncController;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.events.*;
import com.blotout.events.BOADeveloperEvents;
import com.blotout.events.BOAEvents;
import com.blotout.events.BOLifeTimeAllEvent;
import com.blotout.events.BORetentionEvents;
import com.blotout.eventsExecutor.BODeviceOperationExecutorHelper;
import com.blotout.eventsExecutor.BOGeoRetentionOperationExecutorHelper;
import com.blotout.eventsExecutor.BOInitializationExecutorHelper;
import com.blotout.eventsExecutor.BOLifetimeOperationExecutorHelper;
import com.blotout.eventsExecutor.BONetworkFunnelExecutorHelper;
import com.blotout.eventsExecutor.BONetworkSegmentExecutorHelper;
import com.blotout.eventsExecutor.BOWorkerHelper;
import com.blotout.model.session.BOPendingEvents;
import com.blotout.storage.BOFileSystemManager;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
/**
 * Created by Blotout on 22,October,2019
 */

/**
 * The BlotoutAnalytics main class, the developer/customer interacts with the SDK through this class.
 */

public class BlotoutAnalytics {

    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BlotoutAnalytics";

    private static volatile BlotoutAnalytics instance;

    //name of the setting properties
    private boolean isEnabled = true;
    private boolean isProductionMode = false;
    //Individual Module enable or disable control
    //System Events, which SDK detect automatically
    private boolean isSystemEventsEnabled = true;
    //Rentention Events, which SDK detect for retention tracking like DAU, MAU
    private boolean isRetentionEventsEnabled = true;
    //Funnel Events, which SDK process for funnel analysis
    private boolean isFunnelEventsEnabled = true;
    //Segments Events, which SDK process for segment analysis
    private boolean isSegmentEventsEnabled = true;
    //Developer Codified Events, which SDK collects when developer send some events
    private boolean isDeveloperEventsEnabled = true;
    //for disable data write on disk
    private boolean isDataCollectionEnabled = true;
    //for disable network
    private boolean isNetworkSyncEnabled = true;
   //if user is a payingUser
    private boolean isPayingUser = false;
    //Internal_classification for development mode testing
    private boolean  isDevModeEnabled = false;

    //for enable all development logs
    private boolean isSDKLogEnabled = false;

    /**
     * public method to get the singleton instance of the BlotoutAnalytics object,
     * @return BlotoutAnalytics instance
     */
    public static BlotoutAnalytics getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BlotoutAnalytics.class) {
                if (instance == null) {
                    instance = new BlotoutAnalytics();
                }
            }
        }
        return instance;
    }

    /**
     * non public constructor to create a BlotoutAnalytics Instance as
     * makes use of the Singleton Pattern here.
     */
    private BlotoutAnalytics() {
        //TODO: uncomment when go for dev testing
        //setDevModeEnabled(true);
    }

    /**
     *
     * @param context Application Context
     * @param blotoutTestKey stage key as String
     * @param blotoutProductionKey production key as String
     */
    private void updateAnalyticsEngine(@NonNull Context context, @NonNull String blotoutTestKey, @NonNull String blotoutProductionKey) {

        String blotoutTestKeyTrimmed = blotoutTestKey.replaceAll(" ","");
        String blotoutProductionKeyTrimmed = blotoutProductionKey.replaceAll(" ","");
        //Confirm and perform 15 character length check if needed
        if (blotoutTestKeyTrimmed.equals("") || blotoutProductionKeyTrimmed.equals("")) {
            Logger.INSTANCE.i(TAG,"Key must not be empty or null");
            return;
        }

        BlotoutAnalytics_Internal.getInstance().setTestBlotoutKey(blotoutTestKeyTrimmed);
        BlotoutAnalytics_Internal.getInstance().setProdBlotoutKey(blotoutProductionKeyTrimmed);
        BOSharedManager.getInstance(context);
    }

    /**
     *
     * this initializes the BlotoutAnalytics tracking configuration, it has to be called only once when the
     * application starts, for example in the Application Class or the Main Activities onCreate.
     * @param context Application context of the application
     * @param blotoutTestKey stage key as String
     * @param blotoutProductionKey production key as String
     * @param isProdMode this param decides whether app work on production or stage mode
     */
    private void initializeAnalyticsEngine(@NonNull Context context, @NonNull String blotoutTestKey, @NonNull String blotoutProductionKey, boolean isProdMode) {

        try {
            if(context == null) {
                Logger.INSTANCE.i(TAG,"Context must not be null");
                return;
            }

            if(blotoutTestKey == null || blotoutProductionKey == null) {
                Logger.INSTANCE.i(TAG,"Key must not be null");
                return;
            }

            String blotoutTestKeyTrimmed = blotoutTestKey.replaceAll(" ","");
            String blotoutProductionKeyTrimmed = blotoutProductionKey.replaceAll(" ","");

            //Confirm and perform 15 character length check if needed
            if (blotoutTestKeyTrimmed.equals("") || blotoutProductionKeyTrimmed.equals("")) {
                Logger.INSTANCE.i(TAG,"Key must not be empty or null");
                return;
            }
            setProductionMode(isProdMode);
            BlotoutAnalytics_Internal.getInstance().setProdBlotoutKey(blotoutProductionKeyTrimmed);
            BlotoutAnalytics_Internal.getInstance().setTestBlotoutKey(blotoutTestKeyTrimmed);

            this.checkManifestAndInitAnalyticsWithCompletionHandler(context);

        }catch(Exception e) {
            Logger.INSTANCE.i(TAG,e.toString());
        }
    }

    /**
     *
     * this initializes the BlotoutAnalytics tracking configuration, it has to be called only once when the
     * application starts, for example in the Application Class or the Main Activities onCreate.
     * @param context Application context of the application
     * @param blotoutSDKKey Blotout SDK key as String
     * @param endPointUrl production key as String, Server Endpoint Url e.g. https://blotout.io, http://blotout.io
     */

    public void initializeAnalyticsEngine(@NonNull Context context, @NonNull String blotoutSDKKey, @NonNull String endPointUrl) {
        try {
            this.setProductionMode(true);
            if(context == null) {
                Logger.INSTANCE.i(TAG,"Context must not be null");
                return;
            }

            if(blotoutSDKKey.isEmpty() || endPointUrl.isEmpty()) {
                Logger.INSTANCE.i(TAG,"Key and EndPoint must not be empty or null");
                return;
            }

            String blotoutKeyTrimmed = blotoutSDKKey.trim();
            String endPointUrlTrimmed = endPointUrl.trim();

            //Confirm and perform 15 character length check if needed
            if (blotoutKeyTrimmed.isEmpty() || endPointUrlTrimmed.isEmpty()) {
                Logger.INSTANCE.i(TAG,"Key and EndPoint must not be empty or null");
                return;
            }
            setProductionMode(true);
            BlotoutAnalytics_Internal.getInstance().setProdBlotoutKey(blotoutKeyTrimmed);
            BlotoutAnalytics_Internal.getInstance().setExternalServerEndPointUrl(endPointUrlTrimmed);
            this.setSDKEndPointUrl(endPointUrlTrimmed);

            this.checkManifestAndInitAnalyticsWithCompletionHandler(context);

        }catch(Exception e) {
            Logger.INSTANCE.i(TAG,e.toString());
        }
    }
    private void checkManifestAndInitAnalyticsWithCompletionHandler(Context context) {
        try {
        BOSharedManager.getInstance(context);
        BOSharedManager.getInstance().initEncryptionManager();

        if(!BOSharedPreferenceImpl.getInstance().isSDKFirstLaunched() || !BOSDKManifestController.isManifestAvailable()) {
            fetchManifest(new BOHandlerMessage() {
                @Override
                public void handleMessage(BOMessage msg) {

                    if(msg.what == 1) {
                        initiateGeoAPI();
                        initializeEngine(context);
                    } else {
                        Log.i(TAG,"SDK Initialization Failed !, Please check sdk keys & network connection!");
                    }
                }
            });

        } else {
            BOSDKManifestController.getInstance().reloadManifestData(); //Initialize manifest data

            initiateGeoAPI();

            initializeEngine(context);

            BOSDKManifestController.getInstance().syncManifestWithServer();

        }
    }catch(Exception e) {
        Logger.INSTANCE.i(TAG,e.toString());
    }
    }

    /**
     *
     * @param enabled default is true, set false to disable the sdk
     */
    public void setEnabled(boolean enabled) {
        try {
            isEnabled = enabled;
            BlotoutAnalytics_Internal.getInstance().setSDKEnabled(enabled);
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     * Server Endpoint Url e.g. https://blotout.io, http://blotout.io
     * @param endPointUrl
     */
    private void setSDKEndPointUrl(String endPointUrl) {
        try {
            BlotoutAnalytics_Internal.getInstance().setExternalServerEndPointUrl(endPointUrl);
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param devModeEnabled default is true, set false to disable the dev mode
     */
    private void setDevModeEnabled(boolean devModeEnabled) {
        try {
            isDevModeEnabled = devModeEnabled;
            BlotoutAnalytics_Internal.getInstance().setDevModeEnabled(devModeEnabled);
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param productionMode default is true, set false to disable the production mode
     */
    private void setProductionMode(boolean productionMode) {
        try {
             if(!BlotoutAnalytics_Internal.getInstance().isDevModeEnabled) {
                 isProductionMode = productionMode;
                 BlotoutAnalytics_Internal.getInstance().setProductionMode(productionMode);
             }
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param payingUser default is false, set true to enable the payingUser analytics
     */
    public void setPayingUser(boolean payingUser) {
        try {
            isPayingUser = payingUser;
            BlotoutAnalytics_Internal.getInstance().setPayingUser(payingUser);
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param networkSyncEnabled default is true, set false to disable the Network call
     */
    public void setNetworkSyncEnabled(boolean networkSyncEnabled) {
        try {
            isNetworkSyncEnabled = networkSyncEnabled;
            BlotoutAnalytics_Internal.getInstance().setNetworkSyncEnabled(networkSyncEnabled);
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param dataCollectionEnabled default is true, set false to disable data collection
     */
    public void setDataCollectionEnabled(boolean dataCollectionEnabled) {
        try {
          isDataCollectionEnabled = dataCollectionEnabled;
          BlotoutAnalytics_Internal.getInstance().setDataCollectionEnabled(dataCollectionEnabled);
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param funnelEventsEnabled default is true, set false to disable funnel execution
     */
    public void setFunnelEventsEnabled(boolean funnelEventsEnabled) {
        try {
            isFunnelEventsEnabled = funnelEventsEnabled;
            BlotoutAnalytics_Internal.getInstance().setFunnelEventsEnabled(funnelEventsEnabled);
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param segmentEventsEnabled default is true, set false to disable segment execution
     */
    public void setSegmentEventsEnabled(boolean segmentEventsEnabled) {
        try {
            BlotoutAnalytics_Internal.getInstance().setSegmentEventsEnabled(segmentEventsEnabled);
            isSegmentEventsEnabled = segmentEventsEnabled;

            BONetworkSegmentExecutorHelper.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    if (segmentEventsEnabled) {
                        BOSegmentsSyncController.getInstance().prepareSegmentsSyncAndAnalyser();
                    } else {
                        BOSegmentsSyncController.getInstance().pauseSegmentsSyncAndAnalyser();
                    }
                }
            });
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param retentionEventsEnabled default is true, set false to disable retention events execution
     */

    public void setRetentionEventsEnabled(boolean retentionEventsEnabled) {
        try {
            isRetentionEventsEnabled = retentionEventsEnabled;
            BlotoutAnalytics_Internal.getInstance().setRetentionEventsEnabled(retentionEventsEnabled);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param developerEventsEnabled default is true, set false to disable developer events execution
     */
    public void setDeveloperEventsEnabled(boolean developerEventsEnabled) {
        try {
            isDeveloperEventsEnabled = developerEventsEnabled;
            BlotoutAnalytics_Internal.getInstance().setDeveloperEventsEnabled(developerEventsEnabled);
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param systemEventsEnabled default is true, set false to disable device/system events execution
     */
    @SuppressLint("MissingPermission")
    public void setSystemEventsEnabled(boolean systemEventsEnabled) {
        try {
            BOAppSessionEvents.getInstance().isEnabled = systemEventsEnabled;
            BODeviceEvents.getInstance().isEnabled = systemEventsEnabled;
            BODeviceEvents.getInstance().isEnabled = systemEventsEnabled;
            BOPiiEvents.getInstance().isEnabled = systemEventsEnabled;
            BlotoutAnalytics_Internal.getInstance().setSystemEventsEnabled(systemEventsEnabled);
        }catch (Exception e) {
             Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     * This Method is used to enable sdk logs
     * @param SDKLogEnabled value as true or false, default value is false.
     */
    public void setSDKLogEnabled(boolean SDKLogEnabled) {
        isSDKLogEnabled = SDKLogEnabled;
        BlotoutAnalytics_Internal.getInstance().setSDKLogEnabled(SDKLogEnabled);
    }

    //Log Events

    /**
     *
     * @param eventName name of the event
     * @param startEventInfo properties in key/value pair
     */
    public void startTimedEvent(@NonNull String eventName, @Nullable HashMap<String, Object> startEventInfo) {
     try {
         if (BlotoutAnalytics.this.isEnabled) {
             if(BlotoutAnalytics_Internal.getInstance().sdkInitConfirmationSend) {
             BOWorkerHelper.getInstance().post(new Runnable() {
                 @Override
                 public void run() {
                     // BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                     //devEvents.startTimedEvent(eventName,startEventInfo);
                     if (startEventInfo != null) {
                         HashMap<String, Object> userDataDict = BlotoutAnalytics.this.replaceAllOccuranceOfDateInDict(null, startEventInfo);
                         String afterDateStr = BOCommonUtils.getJsonStringFromHashMap(userDataDict);
                         Logger.INSTANCE.d(TAG, "events in analytics event afterDateStr");
                         BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                         devEvents.startTimedEvent(eventName, userDataDict);
                     } else {
                         BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                         devEvents.startTimedEvent(eventName, startEventInfo);
                     }
                 }
             });
             } else {
                 this.addPendingEvents(eventName,BOCommonConstants.BO_EVENT_TYPE_START_TIMED_EVENT,startEventInfo,null);
             }
         }
     }catch (Exception e) {
         Logger.INSTANCE.e(TAG,e.toString());
     }

    }

    /**
     *
     * @param eventName name of the event
     * @param endEventInfo properties in key/value pair
     */
    public void endTimedEvent(@NonNull String eventName, @Nullable HashMap<String, Object> endEventInfo) {
       try {
           if (BlotoutAnalytics.this.isEnabled) {
            if(BlotoutAnalytics_Internal.getInstance().sdkInitConfirmationSend) {
               BOWorkerHelper.getInstance().post(new Runnable() {
                   @Override
                   public void run() {
                           if (endEventInfo != null) {
                               HashMap<String, Object> userDataDict = BlotoutAnalytics.this.replaceAllOccuranceOfDateInDict(null, endEventInfo);
                               String afterDateStr = BOCommonUtils.getJsonStringFromHashMap(userDataDict);
                               Logger.INSTANCE.d(TAG, "events in analytics event afterDateStr");
                               BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                               devEvents.endTimedEvent(eventName, userDataDict);
                           } else {
                               BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                               devEvents.endTimedEvent(eventName, endEventInfo);
                           }
                       }
               });
            } else {
                this.addPendingEvents(eventName,BOCommonConstants.BO_EVENT_TYPE_END_TIMED_EVENT,endEventInfo,null);
            }
           }
       }catch (Exception e) {
           Logger.INSTANCE.e(TAG,e.toString());
       }
    }

    /**
     * This Method is used to add pending events based on their type
     * @param eventName
     * @param eventType
     * @param eventInfo
     */

    public void addPendingEvents(String eventName, int eventType, HashMap<String, Object> eventInfo, Date eventTime) {
        BOPendingEvents pendingEvent = new BOPendingEvents();
        pendingEvent.setEventInfo(eventInfo);
        pendingEvent.setEventName(eventName);
        pendingEvent.setEventType(eventType);
        pendingEvent.setEventTime(eventTime);
        BlotoutAnalytics_Internal.getInstance().sdkInitWaitPendingEvents.add(pendingEvent);
    }

    /**
     *
     * @param id any userid
     * @param provider e.g google, Mixpanel
     * @param eventInfo dictionary of events
     */
    public void mapId(@NonNull String id, @NonNull String provider, @Nullable HashMap<String, Object> eventInfo) {
        try {
            HashMap<String, Object> mapIdInfo = new HashMap<>();
            mapIdInfo.put(BOCommonConstants.BO_EVENT_MAP_ID, id);
            mapIdInfo.put(BOCommonConstants.BO_EVENT_MAP_Provider, provider);

            if(eventInfo != null) {
               mapIdInfo.putAll(eventInfo);
            }

            if (BlotoutAnalytics.this.isEnabled) {
                if(BlotoutAnalytics_Internal.getInstance().sdkInitConfirmationSend) {
                    BOWorkerHelper.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                                HashMap<String, Object> userDataDict = BlotoutAnalytics.this.replaceAllOccuranceOfDateInDict(null, mapIdInfo);
                                BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                                devEvents.logEvent(BOCommonConstants.BO_EVENT_MAP_ID, userDataDict, BONetworkConstants.BO_DEV_EVENT_MAP_ID);
                        }
                    });
                } else {
                    this.addPendingEvents(BOCommonConstants.BO_EVENT_MAP_ID,BOCommonConstants.BO_EVENT_TYPE_SESSION,mapIdInfo, null);
                }
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param eventName name of the event
     * @param eventInfo properties in key/value pair
     */
    public void logEvent(@NonNull String eventName, @Nullable HashMap<String, Object> eventInfo) {
        try {
            if (BlotoutAnalytics.this.isEnabled) {
                if(BlotoutAnalytics_Internal.getInstance().sdkInitConfirmationSend) {
                    BOWorkerHelper.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            if (eventInfo != null) {
                                HashMap<String, Object> userDataDict = BlotoutAnalytics.this.replaceAllOccuranceOfDateInDict(null, eventInfo);
                                BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                                devEvents.logEvent(eventName, userDataDict,0);
                            } else {
                                BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                                devEvents.logEvent(eventName, eventInfo,0);
                            }
                        }
                    });
                } else {
                    this.addPendingEvents(eventName,BOCommonConstants.BO_EVENT_TYPE_SESSION,eventInfo,null);
                }
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }


    /**
     *
     * @param eventName name of the event as String
     * @param eventInfo properties in key/value pair
     * @param eventTime eventTime as Date
     *
     */
    public void logPIIEvent(@NonNull String eventName, @Nullable HashMap<String, Object> eventInfo, Date eventTime) {
        try {
            try {
                if (BlotoutAnalytics.this.isEnabled) {
                    if(BlotoutAnalytics_Internal.getInstance().sdkInitConfirmationSend) {
                        BOWorkerHelper.getInstance().post(new Runnable() {
                            @Override
                            public void run() {
                                if (eventInfo != null) {
                                    HashMap<String, Object> userDataDict = BlotoutAnalytics.this.replaceAllOccuranceOfDateInDict(null, eventInfo);
                                    BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                                    devEvents.logPIIEvent(eventName, userDataDict, eventTime);
                                } else {
                                    BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                                    devEvents.logPIIEvent(eventName, eventInfo, eventTime);
                                }
                            }
                        });
                    } else {
                        this.addPendingEvents(eventName,BOCommonConstants.BO_EVENT_TYPE_PII,eventInfo, eventTime);
                    }
                }
            }catch (Exception e) {
                Logger.INSTANCE.e(TAG,e.toString());
            }

        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param eventName name of the event as String
     * @param eventInfo properties in key/value pair
     * @param eventTime eventTime as Date
     *
     */
    public void logPHIEvent(@NonNull String eventName, @Nullable HashMap<String, Object> eventInfo, Date eventTime) {
        try {
            try {
                if (BlotoutAnalytics.this.isEnabled) {
                    if(BlotoutAnalytics_Internal.getInstance().sdkInitConfirmationSend) {
                        BOWorkerHelper.getInstance().post(new Runnable() {
                            @Override
                            public void run() {
                                if (eventInfo != null) {
                                    HashMap<String, Object> userDataDict = BlotoutAnalytics.this.replaceAllOccuranceOfDateInDict(null, eventInfo);
                                    String afterDateStr = BOCommonUtils.getJsonStringFromHashMap(userDataDict);
                                    Logger.INSTANCE.d(TAG, "events in analytics event afterDateStr");
                                    BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                                    devEvents.logPHIEvent(eventName, userDataDict, eventTime);
                                } else {
                                    BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                                    devEvents.logPHIEvent(eventName, eventInfo, eventTime);
                                }
                            }
                        });
                    } else {
                        this.addPendingEvents(eventName,BOCommonConstants.BO_EVENT_TYPE_PHI,eventInfo, eventTime);
                    }
                }
            }catch (Exception e) {
                Logger.INSTANCE.e(TAG,e.toString());
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     *
     * @param eventName name of the event as String
     * @param eventInfo properties in key/value pair
     * @param eventTime eventTime as Date
     */
    public void logEvent(@NonNull String eventName, @Nullable HashMap<String, Object> eventInfo, Date eventTime){
        try {
            if (BlotoutAnalytics.this.isEnabled) {
                if(BlotoutAnalytics_Internal.getInstance().sdkInitConfirmationSend) {
                    BOWorkerHelper.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            if (eventInfo != null) {
                                HashMap<String, Object> userDataDict = BlotoutAnalytics.this.replaceAllOccuranceOfDateInDict(null, eventInfo);
                                String afterDateStr = BOCommonUtils.getJsonStringFromHashMap(userDataDict);
                                Logger.INSTANCE.d(TAG, "events in analytics event afterDateStr");
                                BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                                devEvents.logEvent(eventName, userDataDict, eventTime,0);
                            } else {
                                BOADeveloperEvents devEvents = BOADeveloperEvents.getInstance();
                                devEvents.logEvent(eventName, eventInfo, eventTime,0);
                            }
                        }
                    });
                } else {
                    this.addPendingEvents(eventName,BOCommonConstants.BO_EVENT_TYPE_SESSION_WITH_TIME,eventInfo, eventTime);
                }
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }

    }

    /**
     *
     * @param eventName eventName as String
     * @param eventInfo HashMap of eventInfo
     */
    public void logUserRetentionEvent(String eventName, @Nullable HashMap<String, Object> eventInfo) {
        try {
            if (BlotoutAnalytics.this.isEnabled) {
                if(BlotoutAnalytics_Internal.getInstance().sdkInitConfirmationSend) {
                    BOWorkerHelper.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            if (eventInfo != null) {
                                HashMap<String, Object> userDataDict = BlotoutAnalytics.this.replaceAllOccuranceOfDateInDict(null, eventInfo);
                                String afterDateStr = BOCommonUtils.getJsonStringFromHashMap(userDataDict);
                                Logger.INSTANCE.d(TAG, "events in analytics event afterDateStr");
                                BORetentionEvents.getInstance().recordCustomEventsWithNameWithPayload(eventName, userDataDict);
                                BOLifeTimeAllEvent.getInstance().recordCustomEventsWithNamewithPayload(eventName, userDataDict);
                            } else {
                                BORetentionEvents.getInstance().recordCustomEventsWithNameWithPayload(eventName, eventInfo);
                                BOLifeTimeAllEvent.getInstance().recordCustomEventsWithNamewithPayload(eventName, eventInfo);
                            }

                        }
                    });
                } else {
                    this.addPendingEvents(eventName,BOCommonConstants.BO_EVENT_TYPE_RETENTION_EVENT,eventInfo, null);
                }
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }

    }

    //Fraud Services
    public boolean isDeviceCompromised () {
        try {
            return BODeviceAndAppFraudController.getInstance().isDeviceCompromised();
        } catch (Exception e) {
          Logger.INSTANCE.e(TAG,e.toString());
        }
        return false;
    }

    public boolean isAppCompromised () {
        try {
            return BODeviceAndAppFraudController.getInstance().isAppCompromised();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
        return false;
    }

    public boolean isNetworkProxied () {
        return false;
    }

    public boolean isSimulator () {
        try {
            return BODeviceAndAppFraudController.getInstance().isTestBuild();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
        return false;
    }

    public boolean isRunningOnVM () {
        return BODeviceAndAppFraudController.getInstance().isRunningOnVM();
    }

    public boolean isEnvironmentSecure () {
        try {
            boolean isDcom = this.isDeviceCompromised();
            boolean isAcom = this.isAppCompromised();
            boolean isProxied = this.isNetworkProxied();
            boolean isSim = this.isSimulator();
            boolean isVM = this.isRunningOnVM();

            return !(isDcom || isAcom || isProxied || isSim || isVM);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
      return false;
    }

    public void onAppTerminate() {
        try {
            BOAppSessionEvents.getInstance().applicationWillTerminateNotification();
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    /**
     * Other Utility Methods, Internal Classification
     */

    /**
     * This Method is used to fetch manifest values from the server
     * @param boHandlerMessage callback
     */
    private void fetchManifest(BOHandlerMessage boHandlerMessage) {
        try {
            BOSDKManifestController sdkManifest = BOSDKManifestController.getInstance();
            sdkManifest.serverSyncManifestAndAppVerification(new BOHandlerMessage() {
                @SuppressLint("MissingPermission")
                @Override
                public void handleMessage(BOMessage msg) {
                    if (msg.what == 1) {
                        boHandlerMessage.handleMessage(new BOMessage(1, null));
                        setupManifestValues();
                    }else {
                        boHandlerMessage.handleMessage(new BOMessage(0, null));
                    }
                }
            });
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            boHandlerMessage.handleMessage(new BOHandlerMessage.BOMessage(0, null));
        }

    }

    @SuppressLint("MissingPermission")
    private void setupManifestValues() {
        try {
            BOSDKManifestController sdkManifest = BOSDKManifestController.getInstance();
            if (sdkManifest.storageCutoffReached) {
                BlotoutAnalytics.this.isEnabled = false;
                setEnabled(false);
                BOAppSessionEvents.getInstance().isEnabled = false;
                BlotoutAnalytics_Internal.getInstance().setRetentionEventsEnabled(false);
                BODeviceEvents.getInstance().isEnabled = false;
                try {
                    BOPiiEvents.getInstance().isEnabled = true;
                } catch (SecurityException e) {
                    //Location Permission is not there
                }
            }
            if (!sdkManifest.storageCutoffReached) {
                //use developer interface to set developer preference on storageCutoffReached false state
                BlotoutAnalytics.this.isEnabled = true;
                setEnabled(true);
                BOAppSessionEvents.getInstance().isEnabled = true;
                BlotoutAnalytics_Internal.getInstance().setRetentionEventsEnabled(true);
                BODeviceEvents.getInstance().isEnabled = true;
                try {
                    BOPiiEvents.getInstance().isEnabled = false;
                } catch (SecurityException e) {
                    //Location Permission is not there
                }
            }

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void initializeEngine(@NonNull Context context) {
        try {

            BOInitializationExecutorHelper.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    BOAEvents.initDefaultConfigurationWithHandler(context, new BOHandlerMessage() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void handleMessage(@NonNull BOMessage msg) {

                            setupManifestValues();

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

                            BOAppSessionEvents.getInstance().postInitLaunchEventsRecording();

                            BOGeoRetentionOperationExecutorHelper.getInstance().post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        BORetentionEvents.getInstance().recordDAUWithPayload(null);
                                        BORetentionEvents.getInstance().recordDPUWithPayload(null);
                                        BORetentionEvents.getInstance().recordAppInstalledWithPayload(true, null);
                                        BORetentionEvents.getInstance().recordNewUserWithPayload(true, null);
                                    } catch (Exception e) {
                                        Logger.INSTANCE.e(TAG, e.toString());
                                    }
                                }
                            });


                            BONetworkSegmentExecutorHelper.getInstance().post(new Runnable() {
                                @Override
                                public void run() {
                                    BOSegmentsSyncController.getInstance().prepareSegmentsSyncAndAnalyser();
                                }
                            });

                            BONetworkFunnelExecutorHelper.getInstance().post(new Runnable() {
                                @Override
                                public void run() {
                                    BOFunnelSyncController.getInstance().prepareFunnnelSyncAndAnalyser();
                                }
                            });

                            try {
                                BOPiiEvents.getInstance().isEnabled = true;
                                BOPiiEvents.getInstance().startCollectingUserLocationEvent();
                            } catch (SecurityException e) {
                                //Location Permission is not there
                                Logger.INSTANCE.e(TAG, e.toString());
                            }

                            recordingDeviceEvents();

                            postPendingEvents();

                        }
                    });
                }
            });


        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void postPendingEvents() {
        BlotoutAnalytics_Internal.getInstance().sdkInitConfirmationSend = true;
        try {
            if(BlotoutAnalytics_Internal.getInstance().sdkInitWaitPendingEvents != null && BlotoutAnalytics_Internal.getInstance().sdkInitWaitPendingEvents.size()>0) {
                for (BOPendingEvents event : BlotoutAnalytics_Internal.getInstance().sdkInitWaitPendingEvents) {
                    switch (event.getEventType()) {
                        case BOCommonConstants.BO_EVENT_TYPE_SESSION:
                            logEvent(event.getEventName(), event.getEventInfo());
                            break;
                        case BOCommonConstants.BO_EVENT_TYPE_SESSION_WITH_TIME:
                            logEvent(event.getEventName(), event.getEventInfo(),event.getEventTime());
                            break;
                        case BOCommonConstants.BO_EVENT_TYPE_PII:
                            logPIIEvent(event.getEventName(), event.getEventInfo(), event.getEventTime());
                            break;
                        case BOCommonConstants.BO_EVENT_TYPE_PHI:
                            logPHIEvent(event.getEventName(), event.getEventInfo(), event.getEventTime());
                            break;
                        case BOCommonConstants.BO_EVENT_TYPE_END_TIMED_EVENT:
                            endTimedEvent(event.getEventName(), event.getEventInfo());
                            break;
                        case BOCommonConstants.BO_EVENT_TYPE_START_TIMED_EVENT:
                            startTimedEvent(event.getEventName(), event.getEventInfo());
                            break;
                        case BOCommonConstants.BO_EVENT_TYPE_RETENTION_EVENT:
                            logUserRetentionEvent(event.getEventName(), event.getEventInfo());
                            break;
                    }
                }
                BlotoutAnalytics_Internal.getInstance().sdkInitWaitPendingEvents.clear();
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void recordingDeviceEvents() {
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

        BODeviceOperationExecutorHelper.getInstance().post(new Runnable() {
            @Override
            public void run() {
                //Need to Check
                BOFileSystemManager.getInstance().deleteFilesAndDirectoryRecursively(true, getStoreInterval(), BOFileSystemManager.getInstance().getBOSDKRootDirectory());
            }
        });
    }

    private  HashMap<String,Object> replaceAllOccuranceOfDateInDict(Date date, HashMap<String,Object> jsonDict){
        try {
            HashMap<String,Object> jsonDictMutable = new HashMap<>(jsonDict);

            Set<String> allKeys = jsonDictMutable.keySet();

            for (String key : allKeys) {
                Object value = jsonDictMutable.get(key);

                if (value instanceof Date) {
                    String dateStr = BODateTimeUtils.getStringFromDate((Date) value, BOCommonConstants.BO_DATE_FORMAT);
                    jsonDictMutable.remove(key);
                    jsonDictMutable.put(key,dateStr);

                }else if (value instanceof HashMap){

                    HashMap<String,Object> newDict = replaceAllOccuranceOfDateInDict(null,jsonDictMutable);
                    jsonDictMutable.remove(key);
                    jsonDictMutable.put(key,newDict);

                }else if(value instanceof List){

                    List<Object> newArr = new ArrayList<>();

                    for (Object arraySingleObj : (List)value) {
                        if (arraySingleObj instanceof HashMap) {
                            HashMap newDict1 = replaceAllOccuranceOfDateInDict(null,jsonDictMutable);
                            newArr.add(newDict1);
                        }else if (arraySingleObj instanceof List) {
                            //this case of recurrsive arrays in not handled as it will require check for infite arrays in side arrays
                            //Just log this for informaton
                            Logger.INSTANCE.e(TAG,"Unhandled case in replaceAllOccuranceOf - Invalid case until recurrsive handling is implemented, \n if NSDate is passed inside this then run time exception might occure");
                        }else if (arraySingleObj instanceof Date) {
                            String dateStrInner = BODateTimeUtils.getStringFromDate((Date)arraySingleObj,BOCommonConstants.BO_DATE_FORMAT);
                            //NSUInteger valIndex = [value indexOfObject:arraySingleObj];
                            newArr.add(dateStrInner);
                        }
                    }
                    jsonDictMutable.remove(key);
                    jsonDictMutable.put(key,newArr);
                }
            }
            return jsonDictMutable;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
        return null;
    }

    private static int getStoreInterval() {
        int storeEvents = BOSDKManifestController.getInstance().intervalStoreEvents;
        if(storeEvents >0) {
            return storeEvents;
        }
        return 180;
    }

    private void initiateGeoAPI() {
        try {
            BOAppSessionEvents.getInstance().getGeoIPAndPublish(true);
        }catch(Exception e) {
            Logger.INSTANCE.e(TAG,e.toString());
        }
    }

    public boolean isSDKInitialized() {
        if(BOAEvents.isSessionModelInitialised && BOAEvents.isAppLifeModelInitialised) {
            return true;
        } else {
            return false;
        }
    }

}
