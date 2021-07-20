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

public class BOSessionDeviceInfo {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOSessionDeviceInfo";

    private List<BOAccessoriesAttached> multitaskingEnabled;
    private List<BOAccessoriesAttached> proximitySensorEnabled;
    private List<BOAccessoriesAttached> debuggerAttached;
    private List<BOAccessoriesAttached> pluggedIn;
    private List<BOAccessoriesAttached> jailBroken;
    private List<BONumberOfA> numberOfActiveProcessors;
    private List<BOProcessorsUsage> processorsUsage;
    private List<BOAccessoriesAttached> accessoriesAttached;
    private List<BOAccessoriesAttached> headphoneAttached;
    private List<BONumberOfA> numberOfAttachedAccessories;
    private List<BONameOfAttachedAccessory> nameOfAttachedAccessories;
    private List<BOBatteryLevel> batteryLevel;
    private List<BOAccessoriesAttached> isCharging;
    private List<BOAccessoriesAttached> fullyCharged;
    private List<BODeviceOrientation> deviceOrientation;
    private List<BOCFUUID> cfUUID;
    private List<BOVendorID> vendorID;

    @JsonProperty("multitaskingEnabled")
    public List<BOAccessoriesAttached> getMultitaskingEnabled() { return multitaskingEnabled; }
    @JsonProperty("multitaskingEnabled")
    public void setMultitaskingEnabled(List<BOAccessoriesAttached> value) { this.multitaskingEnabled = value; }

    @JsonProperty("proximitySensorEnabled")
    public List<BOAccessoriesAttached> getProximitySensorEnabled() { return proximitySensorEnabled; }
    @JsonProperty("proximitySensorEnabled")
    public void setProximitySensorEnabled(List<BOAccessoriesAttached> value) { this.proximitySensorEnabled = value; }

    @JsonProperty("debuggerAttached")
    public List<BOAccessoriesAttached> getDebuggerAttached() { return debuggerAttached; }
    @JsonProperty("debuggerAttached")
    public void setDebuggerAttached(List<BOAccessoriesAttached> value) { this.debuggerAttached = value; }

    @JsonProperty("pluggedIn")
    public List<BOAccessoriesAttached> getPluggedIn() { return pluggedIn; }
    @JsonProperty("pluggedIn")
    public void setPluggedIn(List<BOAccessoriesAttached> value) { this.pluggedIn = value; }

    @JsonProperty("jailBroken")
    public List<BOAccessoriesAttached> getJailBroken() { return jailBroken; }
    @JsonProperty("jailBroken")
    public void setJailBroken(List<BOAccessoriesAttached> value) { this.jailBroken = value; }

    @JsonProperty("numberOfActiveProcessors")
    public List<BONumberOfA> getNumberOfActiveProcessors() { return numberOfActiveProcessors; }
    @JsonProperty("numberOfActiveProcessors")
    public void setNumberOfActiveProcessors(List<BONumberOfA> value) { this.numberOfActiveProcessors = value; }

    @JsonProperty("processorsUsage")
    public List<BOProcessorsUsage> getProcessorsUsage() { return processorsUsage; }
    @JsonProperty("processorsUsage")
    public void setProcessorsUsage(List<BOProcessorsUsage> value) { this.processorsUsage = value; }

    @JsonProperty("accessoriesAttached")
    public List<BOAccessoriesAttached> getAccessoriesAttached() { return accessoriesAttached; }
    @JsonProperty("accessoriesAttached")
    public void setAccessoriesAttached(List<BOAccessoriesAttached> value) { this.accessoriesAttached = value; }

    @JsonProperty("headphoneAttached")
    public List<BOAccessoriesAttached> getHeadphoneAttached() { return headphoneAttached; }
    @JsonProperty("headphoneAttached")
    public void setHeadphoneAttached(List<BOAccessoriesAttached> value) { this.headphoneAttached = value; }

    @JsonProperty("numberOfAttachedAccessories")
    public List<BONumberOfA> getNumberOfAttachedAccessories() { return numberOfAttachedAccessories; }
    @JsonProperty("numberOfAttachedAccessories")
    public void setNumberOfAttachedAccessories(List<BONumberOfA> value) { this.numberOfAttachedAccessories = value; }

    @JsonProperty("nameOfAttachedAccessories")
    public List<BONameOfAttachedAccessory> getNameOfAttachedAccessories() { return nameOfAttachedAccessories; }
    @JsonProperty("nameOfAttachedAccessories")
    public void setNameOfAttachedAccessories(List<BONameOfAttachedAccessory> value) { this.nameOfAttachedAccessories = value; }

    @JsonProperty("batteryLevel")
    public List<BOBatteryLevel> getBatteryLevel() { return batteryLevel; }
    @JsonProperty("batteryLevel")
    public void setBatteryLevel(List<BOBatteryLevel> value) { this.batteryLevel = value; }

    @JsonProperty("isCharging")
    public List<BOAccessoriesAttached> getIsCharging() { return isCharging; }
    @JsonProperty("isCharging")
    public void setIsCharging(List<BOAccessoriesAttached> value) { this.isCharging = value; }

    @JsonProperty("fullyCharged")
    public List<BOAccessoriesAttached> getFullyCharged() { return fullyCharged; }
    @JsonProperty("fullyCharged")
    public void setFullyCharged(List<BOAccessoriesAttached> value) { this.fullyCharged = value; }

    @JsonProperty("deviceOrientation")
    public List<BODeviceOrientation> getDeviceOrientation() { return deviceOrientation; }
    @JsonProperty("deviceOrientation")
    public void setDeviceOrientation(List<BODeviceOrientation> value) { this.deviceOrientation = value; }

    @JsonProperty("cfUUID")
    public List<BOCFUUID> getcfUUID() { return cfUUID; }
    @JsonProperty("cfUUID")
    public void setcfUUID(List<BOCFUUID> value) { this.cfUUID = value; }

    @JsonProperty("vendorID")
    public List<BOVendorID> getVendorID() { return vendorID; }
    @JsonProperty("vendorID")
    public void setVendorID(List<BOVendorID> value) { this.vendorID = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOSessionDeviceInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOSessionDeviceInfo fromJsonDictionary(HashMap<String,Object> jsonDict)  {

        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOSessionDeviceInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOSessionDeviceInfo.class);
        writer = mapper.writerFor(BOSessionDeviceInfo.class);
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
