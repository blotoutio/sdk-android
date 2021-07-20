package com.blotout.model.lifetime;

import androidx.annotation.Nullable;

import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.HashMap;

public class BOAppLaunchInfo {

    private static final String TAG = "BOAppLaunchInfo";

    private String appVersion;
    private String launchReason;

    @JsonProperty("appVersion")
    public String getAppVersion() { return appVersion; }
    @JsonProperty("appVersion")
    public void setAppVersion(String value) { this.appVersion = value; }

    @JsonProperty("launchReason")
    public String getLaunchReason() { return launchReason; }
    @JsonProperty("launchReason")
    public void setLaunchReason(String value) { this.launchReason = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAppLaunchInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAppLaunchInfo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try{
            String jsonString = null;
             jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOAppLaunchInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAppLaunchInfo.class);
        writer = mapper.writerFor(BOAppLaunchInfo.class);
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
