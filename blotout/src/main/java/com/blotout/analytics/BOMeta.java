package com.blotout.analytics;
/**
 * Created by Blotout on 03,November,2019
 */

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

public class BOMeta {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOMeta";

    private long platform;
    private String appNamespace;
    private String appVersion;
    private String osVersion;
    private String osName;
    private String deviceManufacturer;
    private String deviceModel;

    @JsonProperty("plf")
    public long getPlatform() { return platform; }
    @JsonProperty("plf")
    public void setPlatform(long value) { this.platform = value; }

    @JsonProperty("appn")
    public String getAppNamespace() { return appNamespace; }
    @JsonProperty("appn")
    public void setAppNamespace(String value) { this.appNamespace = value; }

    @JsonProperty("appv")
    public String getAppVersion() { return appVersion; }
    @JsonProperty("appv")
    public void setAppVersion(String value) { this.appVersion = value; }

    @JsonProperty("osn")
    public String getOsName() { return osName; }
    @JsonProperty("osn")
    public void setOsNameName(String value) { this.osName = value; }

    @JsonProperty("osv")
    public String getOSVersion() { return osVersion; }
    @JsonProperty("osv")
    public void setOSVersion(String value) { this.osVersion = value; }

    @JsonProperty("dmft")
    public String getDeviceManufacturer() { return deviceManufacturer; }
    @JsonProperty("dmft")
    public void setDeviceManufacturer(String value) { this.deviceManufacturer = value; }

    @JsonProperty("dm")
    public String getDeviceModel() { return deviceModel; }
    @JsonProperty("dm")
    public void setDeviceModel(String value) { this.deviceModel = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOMeta fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOMeta fromJsonDictionary(HashMap<String,Object> jsonDict)  {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        } catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return  null;
    }

    public static String toJsonString(BOMeta obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOMeta.class);
        writer = mapper.writerFor(BOMeta.class);
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

