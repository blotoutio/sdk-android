package com.blotout.model;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.analytics.BOSharedManager;
import com.blotout.utilities.BOCommonUtils;

public class BOALocalDefaultJSONs {

    @NonNull
    public static String appSessionJSONString() {
        return "{\"appBundle\":null,\"date\":null,\"singleDaySessions\":{\"sentToServer\":false,\"systemUptime\":[],\"lastServerSyncTimeStamp\":null,\"allEventsSyncTimeStamp\":null,\"appInfo\":[],\"ubiAutoDetected\":{\"screenShotsTaken\":[],\"appNavigation\":[],\"appGesture\":{\"touchOrClick\":[],\"drag\":[],\"flick\":[],\"swipe\":[],\"doubleTap\":[],\"moreThanDoubleTap\":[],\"twoFingerTap\":[],\"moreThanTwoFingerTap\":[],\"pinch\":[],\"touchAndHold\":[],\"shake\":[],\"rotate\":[],\"screenEdgePan\":[]}},\"developerCodified\":{\"touchClick\":[],\"drag\":[],\"flick\":[],\"swipe\":[],\"doubleTap\":[],\"moreThanDoubleTap\":[],\"twoFingerTap\":[],\"moreThanTwoFingerTap\":[],\"pinch\":[],\"touchAndHold\":[],\"shake\":[],\"rotate\":[],\"screenEdgePan\":[],\"view\":[],\"addToCart\":[],\"chargeTransaction\":[],\"listUpdated\":[],\"timedEvent\":[],\"customEvent\":[]},\"appStates\":{\"sentToServer\":false,\"appLaunched\":[],\"appActive\":[],\"appResignActive\":[],\"appInBackground\":[],\"appInForeground\":[],\"appBackgroundRefreshAvailable\":[],\"appReceiveMemoryWarning\":[],\"appSignificantTimeChange\":[],\"appOrientationPortrait\":[],\"appOrientationLandscape\":[],\"appStatusbarFrameChange\":[],\"appBackgroundRefreshStatusChange\":[],\"appNotificationReceived\":[],\"appNotificationViewed\":[],\"appNotificationClicked\":[]},\"deviceInfo\":{\"multitaskingEnabled\":[],\"proximitySensorEnabled\":[],\"debuggerAttached\":[],\"pluggedIn\":[],\"jailBroken\":[],\"numberOfActiveProcessors\":[],\"processorsUsage\":[],\"accessoriesAttached\":[],\"headphoneAttached\":[],\"numberOfAttachedAccessories\":[],\"nameOfAttachedAccessories\":[],\"batteryLevel\":[],\"isCharging\":[],\"fullyCharged\":[],\"deviceOrientation\":[],\"cfUUID\":[],\"vendorID\":[]},\"networkInfo\":{\"currentIPAddress\":[],\"externalIPAddress\":[],\"cellIPAddress\":[],\"cellNetMask\":[],\"cellBroadcastAddress\":[],\"wifiIPAddress\":[],\"wifiNetMask\":[],\"wifiBroadcastAddress\":[],\"wifiRouterAddress\":[],\"wifiSSID\":[],\"connectedToWifi\":[],\"connectedToCellNetwork\":[]},\"storageInfo\":[],\"memoryInfo\":[],\"location\":[],\"crashDetails\":[],\"retentionEvent\":{\"sentToServer\":false,\"dau\":null,\"dpu\":null,\"appInstalled\":null,\"newUser\":null,\"DAST\":null,\"customEvents\":[]}}}";
    }

    @NonNull
    public static String appLifeTimeDataJSONString(){
        return "{\"appBundle\":null,\"appID\":null,\"date\":null,\"lastServerSyncTimeStamp\":null,\"allEventsSyncTimeStamp\":null,\"appLifeTimeInfo\":[]}";
    }


    @Nullable
    public static String getAppSessionJsonString(@NonNull Context context, @NonNull String fileName) {
        String appSessionString;
        appSessionString = BOCommonUtils.loadJSONFromAsset(context, fileName);

        return appSessionString;
    }

    @Nullable
    public static String getFunnelJsonString() {
        String appFunnelString;
        appFunnelString = BOCommonUtils.loadJSONFromAsset(BOSharedManager.getInstance().getContext(), "test_funnel_data.json");

        return appFunnelString;
    }
}
