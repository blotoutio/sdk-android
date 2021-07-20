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

public class BONetworkInfo {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BONetworkInfo";

    private List<BOIPAddress> currentIPAddress;
    private List<BOIPAddress> externalIPAddress;
    private List<BOIPAddress> cellIPAddress;
    private List<BONetMask> cellNetMask;
    private List<BOBroadcastAddress> cellBroadcastAddress;
    private List<BOIPAddress> wifiIPAddress;
    private List<BONetMask> wifiNetMask;
    private List<BOBroadcastAddress> wifiBroadcastAddress;
    private List<BOWifiRouterAddress> wifiRouterAddress;
    private List<BOWifiSSID> wifiSSID;
    private List<BOConnectedTo> connectedToWifi;
    private List<BOConnectedTo> connectedToCellNetwork;

    @JsonProperty("currentIPAddress")
    public List<BOIPAddress> getCurrentIPAddress() { return currentIPAddress; }
    @JsonProperty("currentIPAddress")
    public void setCurrentIPAddress(List<BOIPAddress> value) { this.currentIPAddress = value; }

    @JsonProperty("externalIPAddress")
    public List<BOIPAddress> getExternalIPAddress() { return externalIPAddress; }
    @JsonProperty("externalIPAddress")
    public void setExternalIPAddress(List<BOIPAddress> value) { this.externalIPAddress = value; }

    @JsonProperty("cellIPAddress")
    public List<BOIPAddress> getCellIPAddress() { return cellIPAddress; }
    @JsonProperty("cellIPAddress")
    public void setCellIPAddress(List<BOIPAddress> value) { this.cellIPAddress = value; }

    @JsonProperty("cellNetMask")
    public List<BONetMask> getCellNetMask() { return cellNetMask; }
    @JsonProperty("cellNetMask")
    public void setCellNetMask(List<BONetMask> value) { this.cellNetMask = value; }

    @JsonProperty("cellBroadcastAddress")
    public List<BOBroadcastAddress> getCellBroadcastAddress() { return cellBroadcastAddress; }
    @JsonProperty("cellBroadcastAddress")
    public void setCellBroadcastAddress(List<BOBroadcastAddress> value) { this.cellBroadcastAddress = value; }

    @JsonProperty("wifiIPAddress")
    public List<BOIPAddress> getWifiIPAddress() { return wifiIPAddress; }
    @JsonProperty("wifiIPAddress")
    public void setWifiIPAddress(List<BOIPAddress> value) { this.wifiIPAddress = value; }

    @JsonProperty("wifiNetMask")
    public List<BONetMask> getWifiNetMask() { return wifiNetMask; }
    @JsonProperty("wifiNetMask")
    public void setWifiNetMask(List<BONetMask> value) { this.wifiNetMask = value; }

    @JsonProperty("wifiBroadcastAddress")
    public List<BOBroadcastAddress> getWifiBroadcastAddress() { return wifiBroadcastAddress; }
    @JsonProperty("wifiBroadcastAddress")
    public void setWifiBroadcastAddress(List<BOBroadcastAddress> value) { this.wifiBroadcastAddress = value; }

    @JsonProperty("wifiRouterAddress")
    public List<BOWifiRouterAddress> getWifiRouterAddress() { return wifiRouterAddress; }
    @JsonProperty("wifiRouterAddress")
    public void setWifiRouterAddress(List<BOWifiRouterAddress> value) { this.wifiRouterAddress = value; }

    @JsonProperty("wifiSSID")
    public List<BOWifiSSID> getWifiSSID() { return wifiSSID; }
    @JsonProperty("wifiSSID")
    public void setWifiSSID(List<BOWifiSSID> value) { this.wifiSSID = value; }

    @JsonProperty("connectedToWifi")
    public List<BOConnectedTo> getConnectedToWifi() { return connectedToWifi; }
    @JsonProperty("connectedToWifi")
    public void setConnectedToWifi(List<BOConnectedTo> value) { this.connectedToWifi = value; }

    @JsonProperty("connectedToCellNetwork")
    public List<BOConnectedTo> getConnectedToCellNetwork() { return connectedToCellNetwork; }
    @JsonProperty("connectedToCellNetwork")
    public void setConnectedToCellNetwork(List<BOConnectedTo> value) { this.connectedToCellNetwork = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BONetworkInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BONetworkInfo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
                jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
                return getObjectReader().readValue(jsonString);
            }catch (IOException e) {
                Logger.INSTANCE.e(TAG, e.toString());
            }
        return null;
    }

    public static String toJsonString(BONetworkInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BONetworkInfo.class);
        writer = mapper.writerFor(BONetworkInfo.class);
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
