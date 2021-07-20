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

public class BODoubleTap {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BODoubleTap";

    private boolean sentToServer;
    private long timeStamp;
    private String objectType;
    private String visibleClassName;
    private Map<String, Long> objectRect;
    private BOScreenRect screenRect;
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

    @JsonProperty("objectType")
    public String getObjectType() { return objectType; }
    @JsonProperty("objectType")
    public void setObjectType(String value) { this.objectType = value; }

    @JsonProperty("visibleClassName")
    public String getVisibleClassName() { return visibleClassName; }
    @JsonProperty("visibleClassName")
    public void setVisibleClassName(String value) { this.visibleClassName = value; }

    @JsonProperty("objectRect")
    public Map<String, Long> getObjectRect() { return objectRect; }
    @JsonProperty("objectRect")
    public void setObjectRect(Map<String, Long> value) { this.objectRect = value; }

    @JsonProperty("screenRect")
    public BOScreenRect getScreenRect() { return screenRect; }
    @JsonProperty("screenRect")
    public void setScreenRect(BOScreenRect value) { this.screenRect = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BODoubleTap fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BODoubleTap fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
             jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
             return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BODoubleTap obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BODoubleTap.class);
        writer = mapper.writerFor(BODoubleTap.class);
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
