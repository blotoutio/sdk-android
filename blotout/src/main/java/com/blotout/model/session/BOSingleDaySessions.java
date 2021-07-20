package com.blotout.model.session;

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

public class BOSingleDaySessions {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOSingleDaySessions";

    private boolean sentToServer;
    private List<Long> systemUptime;
    private long lastServerSyncTimeStamp;
    private long allEventsSyncTimeStamp;
    private List<BOAppInfo> appInfo;
    private BOUbiAutoDetected ubiAutoDetected;
    private BODeveloperCodified developerCodified;
    private BOAppStates appStates;
    private BOSessionDeviceInfo deviceInfo;
    private BONetworkInfo networkInfo;
    private List<BOAdInfo> adInfo;
    private List<BOStorageInfo> storageInfo;
    private List<BOMemoryInfo> memoryInfo;
    private List<BOLocation> location;
    private List<BOCrashDetail> crashDetails;
    private List<BOCommonEvent> commonEvents;
    private BORetentionEvent retentionEvent;

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("systemUptime")
    public List<Long> getSystemUptime() { return systemUptime; }
    @JsonProperty("systemUptime")
    public void setSystemUptime(List<Long> value) { this.systemUptime = value; }

    @JsonProperty("lastServerSyncTimeStamp")
    public long getLastServerSyncTimeStamp() { return lastServerSyncTimeStamp; }
    @JsonProperty("lastServerSyncTimeStamp")
    public void setLastServerSyncTimeStamp(long value) { this.lastServerSyncTimeStamp = value; }

    @JsonProperty("allEventsSyncTimeStamp")
    public long getAllEventsSyncTimeStamp() { return allEventsSyncTimeStamp; }
    @JsonProperty("allEventsSyncTimeStamp")
    public void setAllEventsSyncTimeStamp(long value) { this.allEventsSyncTimeStamp = value; }

    @JsonProperty("appInfo")
    public List<BOAppInfo> getAppInfo() { return appInfo; }
    @JsonProperty("appInfo")
    public void setAppInfo(List<BOAppInfo> value) {
        this.appInfo = value;
    }

    @JsonProperty("adInfo")
    public List<BOAdInfo> getAdInfo() { return adInfo; }
    @JsonProperty("adInfo")
    public void setAdInfo(List<BOAdInfo> value) {
        this.adInfo = value;
    }

    @JsonProperty("ubiAutoDetected")
    public BOUbiAutoDetected getUbiAutoDetected() { return ubiAutoDetected; }
    @JsonProperty("ubiAutoDetected")
    public void setUbiAutoDetected(BOUbiAutoDetected value) { this.ubiAutoDetected = value; }

    @JsonProperty("developerCodified")
    public BODeveloperCodified getDeveloperCodified() { return developerCodified; }
    @JsonProperty("developerCodified")
    public void setDeveloperCodified(BODeveloperCodified value) { this.developerCodified = value; }

    @JsonProperty("appStates")
    public BOAppStates getAppStates() { return appStates; }
    @JsonProperty("appStates")
    public void setAppStates(BOAppStates value) { this.appStates = value; }

    @JsonProperty("deviceInfo")
    public BOSessionDeviceInfo getDeviceInfo() { return deviceInfo; }
    @JsonProperty("deviceInfo")
    public void setDeviceInfo(BOSessionDeviceInfo value) { this.deviceInfo = value; }

    @JsonProperty("networkInfo")
    public BONetworkInfo getNetworkInfo() { return networkInfo; }
    @JsonProperty("networkInfo")
    public void setNetworkInfo(BONetworkInfo value) { this.networkInfo = value; }

    @JsonProperty("storageInfo")
    public List<BOStorageInfo> getStorageInfo() { return storageInfo; }
    @JsonProperty("storageInfo")
    public void setStorageInfo(List<BOStorageInfo> value) { this.storageInfo = value; }

    @JsonProperty("memoryInfo")
    public List<BOMemoryInfo> getMemoryInfo() { return memoryInfo; }
    @JsonProperty("memoryInfo")
    public void setMemoryInfo(List<BOMemoryInfo> value) { this.memoryInfo = value; }

    @JsonProperty("location")
    public List<BOLocation> getLocation() { return location; }
    @JsonProperty("location")
    public void setLocation(List<BOLocation> value) { this.location = value; }

    @JsonProperty("crashDetails")
    public List<BOCrashDetail> getCrashDetails() { return crashDetails; }
    @JsonProperty("crashDetails")
    public void setCrashDetails(List<BOCrashDetail> value) { this.crashDetails = value; }

    @JsonProperty("commonEvents")
    public List<BOCommonEvent> getCommonEvents() { return commonEvents; }
    @JsonProperty("commonEvents")
    public void setCommonEvents(List<BOCommonEvent> value) { this.commonEvents = value; }

    @JsonProperty("retentionEvent")
    public BORetentionEvent getRetentionEvent() { return retentionEvent; }
    @JsonProperty("retentionEvent")
    public void setRetentionEvent(BORetentionEvent value) { this.retentionEvent = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOSingleDaySessions fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOSingleDaySessions fromJsonDictionary(HashMap<String,Object> jsonDict) {
       try {
           String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOSingleDaySessions obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOSingleDaySessions.class);
        writer = mapper.writerFor(BOSingleDaySessions.class);
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
