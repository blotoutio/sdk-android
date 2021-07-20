package com.blotout.model.session;

import androidx.annotation.Nullable;

import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by ankuradhikari on 19,July,2020
 */
public class BOAdInfo {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAdInfo";

    private boolean sentToServer;
    private long timeStamp;
    private String mid;
    private String  advertisingId;
    private boolean  isAdDoNotTrack;

    @JsonProperty("mid")
    public String getMid() { return mid; }
    @JsonProperty("mid")
    public void setMid(String value) { this.mid = value; }

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("isAdDoNotTrack")
    public boolean getAdDoNotTrack() { return isAdDoNotTrack; }
    @JsonProperty("isAdDoNotTrack")
    public void setAdDoNotTrack(boolean value) { this.isAdDoNotTrack = value; }

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("advertisingId")
    public String getAdvertisingId() { return advertisingId; }
    @JsonProperty("advertisingId")
    public void setAdvertisingId(String value) { this.advertisingId = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAdInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAdInfo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try{
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOAdInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAdInfo.class);
        writer = mapper.writerFor(BOAdInfo.class);
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
