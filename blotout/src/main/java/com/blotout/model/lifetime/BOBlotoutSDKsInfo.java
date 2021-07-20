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

public class BOBlotoutSDKsInfo {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOBlotoutSDKsInfo";
    private String sdkVersion;
    private String sdkName;
    private String sdkBundle;
    private String appVersion;
    private String appName;
    private String appBundle;

    @JsonProperty("sdkVersion")
    public String getsdkVersion() { return sdkVersion; }
    @JsonProperty("sdkVersion")
    public void setsdkVersion(String value) { this.sdkVersion = value; }

    @JsonProperty("sdkName")
    public String getsdkName() { return sdkName; }
    @JsonProperty("sdkName")
    public void setsdkName(String value) { this.sdkName = value; }

    @JsonProperty("sdkBundle")
    public String getsdkBundle() { return sdkBundle; }
    @JsonProperty("sdkBundle")
    public void setsdkBundle(String value) { this.sdkBundle = value; }

    @JsonProperty("appVersion")
    public String getAppVersion() { return appVersion; }
    @JsonProperty("appVersion")
    public void setAppVersion(String value) { this.appVersion = value; }

    @JsonProperty("appName")
    public String getAppName() { return appName; }
    @JsonProperty("appName")
    public void setAppName(String value) { this.appName = value; }

    @JsonProperty("appBundle")
    public String getAppBundle() { return appBundle; }
    @JsonProperty("appBundle")
    public void setAppBundle(String value) { this.appBundle = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOBlotoutSDKsInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOBlotoutSDKsInfo fromJsonDictionary(HashMap<String,Object> jsonDict) throws IOException {
      try{
          String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
    }catch (IOException e) {
        Logger.INSTANCE.e(TAG, e.toString());
    }
        return null;
    }
    public static String toJsonString(BOBlotoutSDKsInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOBlotoutSDKsInfo.class);
        writer = mapper.writerFor(BOBlotoutSDKsInfo.class);
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
