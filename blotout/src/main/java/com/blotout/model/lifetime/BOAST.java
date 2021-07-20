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

public class BOAST {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAST";

    private long timeStamp;
    private String averageSessionTime;
    private boolean sentToServer;
    private String mid;
    private HashMap<String,Object> dastInfo;
    private HashMap<String,Object> mastInfo;
    private HashMap<String,Object> wastInfo;

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("averageSessionTime")
    public String getAverageSessionTime() { return averageSessionTime; }
    @JsonProperty("averageSessionTime")
    public void setAverageSessionTime(String value) { this.averageSessionTime = value; }

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("mid")
    public String getMid() { return mid; }
    @JsonProperty("mid")
    public void setMid(String value) { this.mid = value; }

    @JsonProperty("dastInfo")
    public HashMap<String,Object> getDastInfo() { return dastInfo; }
    @JsonProperty("dastInfo")
    public void setDastInfo(HashMap<String,Object> value) { this.dastInfo = value; }

    @JsonProperty("mastInfo")
    public HashMap<String,Object> getMastInfo() { return mastInfo; }
    @JsonProperty("mastInfo")
    public void setMastInfo(HashMap<String,Object> value) { this.mastInfo = value; }

    @JsonProperty("wastInfo")
    public HashMap<String,Object> getWastInfo() { return wastInfo; }
    @JsonProperty("wastInfo")
    public void setWastInfo(HashMap<String,Object> value) { this.wastInfo = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAST fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAST fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
    }catch (IOException e) {
        Logger.INSTANCE.e(TAG, e.toString());
    }
        return null;
    }
    public static String toJsonString(BOAST obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAST.class);
        writer = mapper.writerFor(BOAST.class);
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
