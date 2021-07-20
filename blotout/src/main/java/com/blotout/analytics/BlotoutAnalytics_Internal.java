package com.blotout.analytics;

import com.blotout.model.session.BOPendingEvents;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODeviceDetection;
import com.blotout.utilities.BOEncryptionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Blotout on 19,May,2020
 */
public class BlotoutAnalytics_Internal {

    private static final String TAG = "BlotoutAnalytics_Internal";
    private static volatile BlotoutAnalytics_Internal instance;
    public boolean isSDKEnabled  = true;
    public boolean isProductionMode;
    public boolean isDevModeEnabled = false;
    public boolean isPayingUser = false;
    public boolean sdkInitConfirmationSend =false;
    public boolean isFunnelEventsEnabled = true;
    public boolean isSystemEventsEnabled = true;
    public boolean isRetentionEventsEnabled = true;
    public boolean isSegmentEventsEnabled = true;
    public boolean isDeveloperEventsEnabled = true;
    public boolean isDataCollectionEnabled = true;
    public boolean isNetworkSyncEnabled = true;
    public boolean isDataEncryptionEnabled = true;
    public boolean isSDKLogEnabled = false;

    public String testBlotoutKey ;
    public String prodBlotoutKey;
    public String externalServerEndPointUrl;

    public List<BOPendingEvents> sdkInitWaitPendingEvents;

    public static BlotoutAnalytics_Internal getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BlotoutAnalytics_Internal.class) {
                if (instance == null) {
                    instance = new BlotoutAnalytics_Internal();
                }
            }
        }
        return instance;
    }


    private BlotoutAnalytics_Internal() {
        sdkInitWaitPendingEvents = new ArrayList<>();
    }

    public void setSystemEventsEnabled(boolean systemEventsEnabled) { isSystemEventsEnabled = systemEventsEnabled; }

    public void setRetentionEventsEnabled(boolean retentionEventsEnabled) { isRetentionEventsEnabled = retentionEventsEnabled; }

    public void setSegmentEventsEnabled(boolean segmentEventsEnabled) { isSegmentEventsEnabled = segmentEventsEnabled; }

    public void setNetworkSyncEnabled(boolean networkSyncEnabled) { isNetworkSyncEnabled = networkSyncEnabled; }

    public void setFunnelEventsEnabled(boolean funnelEventsEnabled) { isFunnelEventsEnabled = funnelEventsEnabled; }

    public void setDeveloperEventsEnabled(boolean developerEventsEnabled) { isDeveloperEventsEnabled = developerEventsEnabled; }

    public void setDataCollectionEnabled(boolean dataCollectionEnabled) { isDataCollectionEnabled = dataCollectionEnabled;  }

    public void setProductionMode(boolean productionMode) {
        isProductionMode = productionMode;
    }

    public void setDevModeEnabled(boolean devModeEnabled) {
        isDevModeEnabled = devModeEnabled;
    }

    public void setSDKEnabled(boolean SDKEnabled) {
        isSDKEnabled = SDKEnabled;
    }

    public void setPayingUser(boolean payingUser) {
        isPayingUser = payingUser;
    }

    public void setTestBlotoutKey(String testBlotoutKey) {
        this.testBlotoutKey = testBlotoutKey;
    }

    public void setProdBlotoutKey(String prodBlotoutKey) {  this.prodBlotoutKey = prodBlotoutKey; }

    public void setDataEncryptionEnabled(boolean dataEncryptionEnabled) { isDataEncryptionEnabled = dataEncryptionEnabled; }

    public void setExternalServerEndPointUrl(String externalServerEndPointUrl) { this.externalServerEndPointUrl = externalServerEndPointUrl; }

    public void setSDKLogEnabled(boolean SDKLogEnabled) {
        isSDKLogEnabled = SDKLogEnabled;
    }
}
