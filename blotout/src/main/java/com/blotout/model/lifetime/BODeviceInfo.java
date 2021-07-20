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

public class BODeviceInfo {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BODeviceInfo";

    private boolean sentToServer;
    private long timeStamp;
    private boolean multitaskingEnabled;
    private boolean proximitySensorEnabled;
    private boolean debuggerAttached;
    private boolean pluggedIn;
    private boolean jailBroken;
    private long numberOfActiveProcessors;
    private List<BOProcessorsUsage> processorsUsage;
    private boolean accessoriesAttached;
    private boolean headphoneAttached;
    private long numberOfAttachedAccessories;
    private List<String> nameOfAttachedAccessories;
    private long batteryLevelPercentage;
    private boolean isCharging;
    private boolean fullyCharged;
    private String deviceOrientation;
    private String cfUUID;
    private String vendorID;
    private String deviceModel;
    private String deviceName;
    private String systemName;
    private String systemVersion;
    private String systemDeviceTypeUnformatted;
    private String systemDeviceTypeFormatted;
    private String deviceScreenWidth;
    private String deviceScreenHeight;
    private String appUIWidth;
    private String appUIHeight;
    private String screenBrightness;
    private boolean stepCountingAvailable;
    private boolean distanceAvailbale;
    private boolean floorCountingAvailable;
    private long numberOfProcessors;
    private String country;
    private String language;
    private String timeZone;
    private String currency;
    private String clipboardContent;
    private boolean doNotTrackEnabled;
    private String advertisingID;
    private List<BOOtherID> otherIDs;

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("multitaskingEnabled")
    public boolean getMultitaskingEnabled() { return multitaskingEnabled; }
    @JsonProperty("multitaskingEnabled")
    public void setMultitaskingEnabled(boolean value) { this.multitaskingEnabled = value; }

    @JsonProperty("proximitySensorEnabled")
    public boolean getProximitySensorEnabled() { return proximitySensorEnabled; }
    @JsonProperty("proximitySensorEnabled")
    public void setProximitySensorEnabled(boolean value) { this.proximitySensorEnabled = value; }

    @JsonProperty("debuggerAttached")
    public boolean getDebuggerAttached() { return debuggerAttached; }
    @JsonProperty("debuggerAttached")
    public void setDebuggerAttached(boolean value) { this.debuggerAttached = value; }

    @JsonProperty("pluggedIn")
    public boolean getPluggedIn() { return pluggedIn; }
    @JsonProperty("pluggedIn")
    public void setPluggedIn(boolean value) { this.pluggedIn = value; }

    @JsonProperty("jailBroken")
    public boolean getJailBroken() { return jailBroken; }
    @JsonProperty("jailBroken")
    public void setJailBroken(boolean value) { this.jailBroken = value; }

    @JsonProperty("numberOfActiveProcessors")
    public long getNumberOfActiveProcessors() { return numberOfActiveProcessors; }
    @JsonProperty("numberOfActiveProcessors")
    public void setNumberOfActiveProcessors(long value) { this.numberOfActiveProcessors = value; }

    @JsonProperty("processorsUsage")
    public List<BOProcessorsUsage> getProcessorsUsage() { return processorsUsage; }
    @JsonProperty("processorsUsage")
    public void setProcessorsUsage(List<BOProcessorsUsage> value) { this.processorsUsage = value; }

    @JsonProperty("accessoriesAttached")
    public boolean getAccessoriesAttached() { return accessoriesAttached; }
    @JsonProperty("accessoriesAttached")
    public void setAccessoriesAttached(boolean value) { this.accessoriesAttached = value; }

    @JsonProperty("headphoneAttached")
    public boolean getHeadphoneAttached() { return headphoneAttached; }
    @JsonProperty("headphoneAttached")
    public void setHeadphoneAttached(boolean value) { this.headphoneAttached = value; }

    @JsonProperty("numberOfAttachedAccessories")
    public long getNumberOfAttachedAccessories() { return numberOfAttachedAccessories; }
    @JsonProperty("numberOfAttachedAccessories")
    public void setNumberOfAttachedAccessories(long value) { this.numberOfAttachedAccessories = value; }

    @JsonProperty("nameOfAttachedAccessories")
    public List<String> getNameOfAttachedAccessories() { return nameOfAttachedAccessories; }
    @JsonProperty("nameOfAttachedAccessories")
    public void setNameOfAttachedAccessories(List<String> value) { this.nameOfAttachedAccessories = value; }

    @JsonProperty("batteryLevelPercentage")
    public long getBatteryLevelPercentage() { return batteryLevelPercentage; }
    @JsonProperty("batteryLevelPercentage")
    public void setBatteryLevelPercentage(long value) { this.batteryLevelPercentage = value; }

    @JsonProperty("isCharging")
    public boolean getIsCharging() { return isCharging; }
    @JsonProperty("isCharging")
    public void setIsCharging(boolean value) { this.isCharging = value; }

    @JsonProperty("fullyCharged")
    public boolean getFullyCharged() { return fullyCharged; }
    @JsonProperty("fullyCharged")
    public void setFullyCharged(boolean value) { this.fullyCharged = value; }

    @JsonProperty("deviceOrientation")
    public String getDeviceOrientation() { return deviceOrientation; }
    @JsonProperty("deviceOrientation")
    public void setDeviceOrientation(String value) { this.deviceOrientation = value; }

    @JsonProperty("cfUUID")
    public String getcfUUID() { return cfUUID; }
    @JsonProperty("cfUUID")
    public void setcfUUID(String value) { this.cfUUID = value; }

    @JsonProperty("vendorID")
    public String getVendorID() { return vendorID; }
    @JsonProperty("vendorID")
    public void setVendorID(String value) { this.vendorID = value; }

    @JsonProperty("deviceModel")
    public String getDeviceModel() { return deviceModel; }
    @JsonProperty("deviceModel")
    public void setDeviceModel(String value) { this.deviceModel = value; }

    @JsonProperty("deviceName")
    public String getDeviceName() { return deviceName; }
    @JsonProperty("deviceName")
    public void setDeviceName(String value) { this.deviceName = value; }

    @JsonProperty("systemName")
    public String getSystemName() { return systemName; }
    @JsonProperty("systemName")
    public void setSystemName(String value) { this.systemName = value; }

    @JsonProperty("systemVersion")
    public String getSystemVersion() { return systemVersion; }
    @JsonProperty("systemVersion")
    public void setSystemVersion(String value) { this.systemVersion = value; }

    @JsonProperty("systemDeviceTypeUnformatted")
    public String getSystemDeviceTypeUnformatted() { return systemDeviceTypeUnformatted; }
    @JsonProperty("systemDeviceTypeUnformatted")
    public void setSystemDeviceTypeUnformatted(String value) { this.systemDeviceTypeUnformatted = value; }

    @JsonProperty("systemDeviceTypeFormatted")
    public String getSystemDeviceTypeFormatted() { return systemDeviceTypeFormatted; }
    @JsonProperty("systemDeviceTypeFormatted")
    public void setSystemDeviceTypeFormatted(String value) { this.systemDeviceTypeFormatted = value; }

    @JsonProperty("deviceScreenWidth")
    public String getDeviceScreenWidth() { return deviceScreenWidth; }
    @JsonProperty("deviceScreenWidth")
    public void setDeviceScreenWidth(String value) { this.deviceScreenWidth = value; }

    @JsonProperty("deviceScreenHeight")
    public String getDeviceScreenHeight() { return deviceScreenHeight; }
    @JsonProperty("deviceScreenHeight")
    public void setDeviceScreenHeight(String value) { this.deviceScreenHeight = value; }

    @JsonProperty("appUIWidth")
    public String getAppUIWidth() { return appUIWidth; }
    @JsonProperty("appUIWidth")
    public void setAppUIWidth(String value) { this.appUIWidth = value; }

    @JsonProperty("appUIHeight")
    public String getAppUIHeight() { return appUIHeight; }
    @JsonProperty("appUIHeight")
    public void setAppUIHeight(String value) { this.appUIHeight = value; }

    @JsonProperty("screenBrightness")
    public String getScreenBrightness() { return screenBrightness; }
    @JsonProperty("screenBrightness")
    public void setScreenBrightness(String value) { this.screenBrightness = value; }

    @JsonProperty("stepCountingAvailable")
    public boolean getStepCountingAvailable() { return stepCountingAvailable; }
    @JsonProperty("stepCountingAvailable")
    public void setStepCountingAvailable(boolean value) { this.stepCountingAvailable = value; }

    @JsonProperty("distanceAvailbale")
    public boolean getDistanceAvailbale() { return distanceAvailbale; }
    @JsonProperty("distanceAvailbale")
    public void setDistanceAvailbale(boolean value) { this.distanceAvailbale = value; }

    @JsonProperty("floorCountingAvailable")
    public boolean getFloorCountingAvailable() { return floorCountingAvailable; }
    @JsonProperty("floorCountingAvailable")
    public void setFloorCountingAvailable(boolean value) { this.floorCountingAvailable = value; }

    @JsonProperty("numberOfProcessors")
    public long getNumberOfProcessors() { return numberOfProcessors; }
    @JsonProperty("numberOfProcessors")
    public void setNumberOfProcessors(long value) { this.numberOfProcessors = value; }

    @JsonProperty("country")
    public String getCountry() { return country; }
    @JsonProperty("country")
    public void setCountry(String value) { this.country = value; }

    @JsonProperty("Language")
    public String getLanguage() { return language; }
    @JsonProperty("Language")
    public void setLanguage(String value) { this.language = value; }

    @JsonProperty("timeZone")
    public String getTimeZone() { return timeZone; }
    @JsonProperty("timeZone")
    public void setTimeZone(String value) { this.timeZone = value; }

    @JsonProperty("currency")
    public String getCurrency() { return currency; }
    @JsonProperty("currency")
    public void setCurrency(String value) { this.currency = value; }

    @JsonProperty("clipboardContent")
    public String getClipboardContent() { return clipboardContent; }
    @JsonProperty("clipboardContent")
    public void setClipboardContent(String value) { this.clipboardContent = value; }

    @JsonProperty("doNotTrackEnabled")
    public boolean getDoNotTrackEnabled() { return doNotTrackEnabled; }
    @JsonProperty("doNotTrackEnabled")
    public void setDoNotTrackEnabled(boolean value) { this.doNotTrackEnabled = value; }

    @JsonProperty("advertisingID")
    public String getAdvertisingID() { return advertisingID; }
    @JsonProperty("advertisingID")
    public void setAdvertisingID(String value) { this.advertisingID = value; }

    @JsonProperty("otherIDs")
    public List<BOOtherID> getOtherIDs() { return otherIDs; }
    @JsonProperty("otherIDs")
    public void setOtherIDs(List<BOOtherID> value) { this.otherIDs = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BODeviceInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BODeviceInfo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
           jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
          return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BODeviceInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BODeviceInfo.class);
        writer = mapper.writerFor(BODeviceInfo.class);
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
