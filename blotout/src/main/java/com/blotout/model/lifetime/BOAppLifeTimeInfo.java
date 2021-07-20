package com.blotout.model.lifetime;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.*;

import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class BOAppLifeTimeInfo {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAppLifeTimeInfo";

    private boolean sentToServer;
    private String dateAndTime;
    private long timeStamp;
    private BOAppSessionInfo appSessionsInfo;
    private BOAAppInfo appInstallInfo;
    private BOAAppInfo appUpdatesInfo;
    private BOAppLaunchInfo appLaunchInfo;
    private BOBlotoutSDKsInfo blotoutSDKsInfo;
    private List<BOAppLanguagesSupported> appLanguagesSupported;
    private boolean appSupportShakeToEdit;
    private boolean appSupportRemoteNotifications;
    private List<String> appCategory;
    private BODeviceInfo deviceInfo;
    private BONetworkInfo networkInfo;
    private BOStorageInfo storageInfo;
    private BOMemoryInfo memoryInfo;
    private BOLocation location;
    private BORetentionEvent retentionEvent;

    @JsonProperty("dateAndTime")
    public String getDateAndTime() { return dateAndTime; }
    @JsonProperty("dateAndTime")
    public void setDateAndTime(String value) { this.dateAndTime = value; }

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("appInstallInfo")
    public BOAAppInfo getAppInstallInfo() { return appInstallInfo; }
    @JsonProperty("appInstallInfo")
    public void setAppInstallInfo(BOAAppInfo value) { this.appInstallInfo = value; }

    @JsonProperty("appUpdatesInfo")
    public BOAAppInfo getAppUpdatesInfo() { return appUpdatesInfo; }
    @JsonProperty("appUpdatesInfo")
    public void setAppUpdatesInfo(BOAAppInfo value) { this.appUpdatesInfo = value; }

    @JsonProperty("appLaunchInfo")
    public BOAppLaunchInfo getAppLaunchInfo() { return appLaunchInfo; }
    @JsonProperty("appLaunchInfo")
    public void setAppLaunchInfo(BOAppLaunchInfo value) { this.appLaunchInfo = value; }

    @JsonProperty("blotoutSDKsInfo")
    public BOBlotoutSDKsInfo getBlotoutSDKsInfo() { return blotoutSDKsInfo; }
    @JsonProperty("blotoutSDKsInfo")
    public void setBlotoutSDKsInfo(BOBlotoutSDKsInfo value) { this.blotoutSDKsInfo = value; }

    @JsonProperty("appLanguagesSupported")
    public List<BOAppLanguagesSupported> getAppLanguagesSupported() { return appLanguagesSupported; }
    @JsonProperty("appLanguagesSupported")
    public void setAppLanguagesSupported(List<BOAppLanguagesSupported> value) { this.appLanguagesSupported = value; }

    @JsonProperty("appSupportShakeToEdit")
    public boolean getAppSupportShakeToEdit() { return appSupportShakeToEdit; }
    @JsonProperty("appSupportShakeToEdit")
    public void setAppSupportShakeToEdit(boolean value) { this.appSupportShakeToEdit = value; }

    @JsonProperty("appSupportRemoteNotifications")
    public boolean getAppSupportRemoteNotifications() { return appSupportRemoteNotifications; }
    @JsonProperty("appSupportRemoteNotifications")
    public void setAppSupportRemoteNotifications(boolean value) { this.appSupportRemoteNotifications = value; }

    @JsonProperty("appCategory")
    public List<String> getAppCategory() { return appCategory; }
    @JsonProperty("appCategory")
    public void setAppCategory(List<String> value) { this.appCategory = value; }

    @JsonProperty("deviceInfo")
    public BODeviceInfo getDeviceInfo() { return deviceInfo; }
    @JsonProperty("deviceInfo")
    public void setDeviceInfo(BODeviceInfo value) { this.deviceInfo = value; }

    @JsonProperty("networkInfo")
    public BONetworkInfo getNetworkInfo() { return networkInfo; }
    @JsonProperty("networkInfo")
    public void setNetworkInfo(BONetworkInfo value) { this.networkInfo = value; }

    @JsonProperty("storageInfo")
    public BOStorageInfo getStorageInfo() { return storageInfo; }
    @JsonProperty("storageInfo")
    public void setStorageInfo(BOStorageInfo value) { this.storageInfo = value; }

    @JsonProperty("memoryInfo")
    public BOMemoryInfo getMemoryInfo() { return memoryInfo; }
    @JsonProperty("memoryInfo")
    public void setMemoryInfo(BOMemoryInfo value) { this.memoryInfo = value; }

    @JsonProperty("location")
    public BOLocation getLocation() { return location; }
    @JsonProperty("location")
    public void setLocation(BOLocation value) { this.location = value; }

    @JsonProperty("retentionEvent")
    public BORetentionEvent getRetentionEvent() { return retentionEvent; }
    @JsonProperty("retentionEvent")
    public void setRetentionEvent(BORetentionEvent value) { this.retentionEvent = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAppLifeTimeInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAppLifeTimeInfo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOAppLifeTimeInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAppLifeTimeInfo.class);
        writer = mapper.writerFor(BOAppLifeTimeInfo.class);
    }

    private static ObjectReader getObjectReader() {
        if (reader == null) instantiateMapper();
        return reader;
    }

    private static ObjectWriter getObjectWriter() {
        if (writer == null) instantiateMapper();
        return writer;
    }
}
