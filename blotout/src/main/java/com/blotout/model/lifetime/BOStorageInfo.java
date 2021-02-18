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

public class BOStorageInfo {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOStorageInfo";

    private boolean sentToServer;
    private BODiskSpace totalDiskSpace;
    private BODiskSpace usedDiskSpace;
    private BODiskSpace freeDiskSpace;
    private String mid;
    private String sessionId;

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("totalDiskSpace")
    public BODiskSpace getTotalDiskSpace() { return totalDiskSpace; }
    @JsonProperty("totalDiskSpace")
    public void setTotalDiskSpace(BODiskSpace value) { this.totalDiskSpace = value; }

    @JsonProperty("usedDiskSpace")
    public BODiskSpace getUsedDiskSpace() { return usedDiskSpace; }
    @JsonProperty("usedDiskSpace")
    public void setUsedDiskSpace(BODiskSpace value) { this.usedDiskSpace = value; }

    @JsonProperty("freeDiskSpace")
    public BODiskSpace getFreeDiskSpace() { return freeDiskSpace; }
    @JsonProperty("freeDiskSpace")
    public void setFreeDiskSpace(BODiskSpace value) { this.freeDiskSpace = value; }

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

    public static BOStorageInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOStorageInfo fromJsonDictionary(HashMap<String,Object> jsonDict) throws IOException {
       try {
           String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
    }catch (IOException e) {
        Logger.INSTANCE.e(TAG, e.toString());
    }
        return null;
    }
    public static String toJsonString(BOStorageInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOStorageInfo.class);
        writer = mapper.writerFor(BOStorageInfo.class);
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
