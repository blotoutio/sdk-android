package com.blotout.model.session;

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

public class BOListUpdated {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOListUpdated";

    private boolean sentToServer;
    private long timeStamp;
    private String listClassName;
    private HashMap<String,Object> updatesInfo;
    private String mid;

    @JsonProperty("mid")
    public String getMid() { return mid; }
    @JsonProperty("mid")
    public void setMid(String value) { this.mid = value; }

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("listClassName")
    public String getListClassName() { return listClassName; }
    @JsonProperty("listClassName")
    public void setListClassName(String value) { this.listClassName = value; }

    @JsonProperty("updatesInfo")
    public HashMap<String,Object> getUpdatesInfo() { return updatesInfo; }
    @JsonProperty("updatesInfo")
    public void setUpdatesInfo(HashMap<String,Object> value) { this.updatesInfo = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOListUpdated fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOListUpdated fromJsonDictionary(HashMap<String,Object> jsonDict)  {
       try {
           String jsonString = null;
           jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
           return getObjectReader().readValue(jsonString);
       }catch (IOException e) {
           Logger.INSTANCE.e(TAG, e.toString());
       }
        return null;
    }

    public static String toJsonString(BOListUpdated obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOListUpdated.class);
        writer = mapper.writerFor(BOListUpdated.class);
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
