package com.blotout.model.lifetime;

import androidx.annotation.Nullable;

import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.HashMap;

public class BONetworkInfo {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BONetworkInfo";

    private boolean sentToServer;
    private String carrierName;
    private String carrierCountry;
    private String carrierMobileCountry;
    private String carrierISOCountryCode;
    private String carrierMobileNetworkCode;
    private boolean carrierAllowVOIP;
    private String currentIPAddress;
    private String externalIPAddress;
    private String cellIPAddress;
    private String cellNetMask;
    private String cellBroadcastAddress;
    private String wifiIPAddress;
    private String wifiNetMask;
    private String wifiBroadcastAddress;
    private String wifiRouterAddress;
    private String wifiSSID;
    private boolean connectedToWifi;
    private boolean connectedToCellNetwork;

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("carrierName")
    public String getCarrierName() { return carrierName; }
    @JsonProperty("carrierName")
    public void setCarrierName(String value) { this.carrierName = value; }

    @JsonProperty("carrierCountry")
    public String getCarrierCountry() { return carrierCountry; }
    @JsonProperty("carrierCountry")
    public void setCarrierCountry(String value) { this.carrierCountry = value; }

    @JsonProperty("carrierMobileCountry")
    public String getCarrierMobileCountry() { return carrierMobileCountry; }
    @JsonProperty("carrierMobileCountry")
    public void setCarrierMobileCountry(String value) { this.carrierMobileCountry = value; }

    @JsonProperty("carrierISOCountryCode")
    public String getCarrierISOCountryCode() { return carrierISOCountryCode; }
    @JsonProperty("carrierISOCountryCode")
    public void setCarrierISOCountryCode(String value) { this.carrierISOCountryCode = value; }

    @JsonProperty("carrierMobileNetworkCode")
    public String getCarrierMobileNetworkCode() { return carrierMobileNetworkCode; }
    @JsonProperty("carrierMobileNetworkCode")
    public void setCarrierMobileNetworkCode(String value) { this.carrierMobileNetworkCode = value; }

    @JsonProperty("carrierAllowVOIP")
    public boolean getCarrierAllowVOIP() { return carrierAllowVOIP; }
    @JsonProperty("carrierAllowVOIP")
    public void setCarrierAllowVOIP(boolean value) { this.carrierAllowVOIP = value; }

    @JsonProperty("currentIPAddress")
    public String getCurrentIPAddress() { return currentIPAddress; }
    @JsonProperty("currentIPAddress")
    public void setCurrentIPAddress(String value) { this.currentIPAddress = value; }

    @JsonProperty("externalIPAddress")
    public String getExternalIPAddress() { return externalIPAddress; }
    @JsonProperty("externalIPAddress")
    public void setExternalIPAddress(String value) { this.externalIPAddress = value; }

    @JsonProperty("cellIPAddress")
    public String getCellIPAddress() { return cellIPAddress; }
    @JsonProperty("cellIPAddress")
    public void setCellIPAddress(String value) { this.cellIPAddress = value; }

    @JsonProperty("cellNetMask")
    public String getCellNetMask() { return cellNetMask; }
    @JsonProperty("cellNetMask")
    public void setCellNetMask(String value) { this.cellNetMask = value; }

    @JsonProperty("cellBroadcastAddress")
    public String getCellBroadcastAddress() { return cellBroadcastAddress; }
    @JsonProperty("cellBroadcastAddress")
    public void setCellBroadcastAddress(String value) { this.cellBroadcastAddress = value; }

    @JsonProperty("wifiIPAddress")
    public String getWifiIPAddress() { return wifiIPAddress; }
    @JsonProperty("wifiIPAddress")
    public void setWifiIPAddress(String value) { this.wifiIPAddress = value; }

    @JsonProperty("wifiNetMask")
    public String getWifiNetMask() { return wifiNetMask; }
    @JsonProperty("wifiNetMask")
    public void setWifiNetMask(String value) { this.wifiNetMask = value; }

    @JsonProperty("wifiBroadcastAddress")
    public String getWifiBroadcastAddress() { return wifiBroadcastAddress; }
    @JsonProperty("wifiBroadcastAddress")
    public void setWifiBroadcastAddress(String value) { this.wifiBroadcastAddress = value; }

    @JsonProperty("wifiRouterAddress")
    public String getWifiRouterAddress() { return wifiRouterAddress; }
    @JsonProperty("wifiRouterAddress")
    public void setWifiRouterAddress(String value) { this.wifiRouterAddress = value; }

    @JsonProperty("wifiSSID")
    public String getWifiSSID() { return wifiSSID; }
    @JsonProperty("wifiSSID")
    public void setWifiSSID(String value) { this.wifiSSID = value; }

    @JsonProperty("connectedToWifi")
    public boolean getConnectedToWifi() { return connectedToWifi; }
    @JsonProperty("connectedToWifi")
    public void setConnectedToWifi(boolean value) { this.connectedToWifi = value; }

    @JsonProperty("connectedToCellNetwork")
    public boolean getConnectedToCellNetwork() { return connectedToCellNetwork; }
    @JsonProperty("connectedToCellNetwork")
    public void setConnectedToCellNetwork(boolean value) { this.connectedToCellNetwork = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BONetworkInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BONetworkInfo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try{
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
