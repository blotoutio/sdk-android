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

public class BOAppInstalled {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAppInstalled";

    private boolean isFirstLaunch;
    private long timeStamp;
    private HashMap<String,Object> appInstalledInfo;
    private boolean sentToServer;
    private String mid;
    private String sessionId;

    @JsonProperty("isFirstLaunch")
    public boolean getIsFirstLaunch() { return isFirstLaunch; }
    @JsonProperty("isFirstLaunch")
    public void setIsFirstLaunch(boolean value) { this.isFirstLaunch = value; }

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("appInstalledInfo")
    public HashMap<String,Object> getAppInstalledInfo() { return appInstalledInfo; }
    @JsonProperty("appInstalledInfo")
    public void setAppInstalledInfo(HashMap<String,Object> value) { this.appInstalledInfo = value; }

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("mid")
    public String getMid() { return mid; }
    @JsonProperty("mid")
    public void setMid(String value) { this.mid = value; }

    @JsonProperty("session_id")
    public String getSessionId() { return sessionId; }
    @JsonProperty("session_id")
    public void setSessionId(String value) { this.sessionId = value; }


    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAppInstalled fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAppInstalled fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOAppInstalled obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAppInstalled.class);
        writer = mapper.writerFor(BOAppInstalled.class);
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
