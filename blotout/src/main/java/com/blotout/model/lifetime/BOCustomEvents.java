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

public class BOCustomEvents {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOCustomEvents";

    private long timeStamp;
    private HashMap<String,Object> eventInfo;
    private String eventName;
    private String visibleClassName;
    private boolean sentToServer;
    private String mid;
    private String sessionId;

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("eventInfo")
    public HashMap<String,Object> getEventInfo() { return eventInfo; }
    @JsonProperty("eventInfo")
    public void setEventInfo(HashMap<String,Object> value) { this.eventInfo = value; }

    @JsonProperty("eventName")
    public String getEventName() { return eventName; }
    @JsonProperty("eventName")
    public void setEventName(String value) { this.eventName = value; }

    @JsonProperty("visibleClassName")
    public String getVisibleClassName() { return visibleClassName; }
    @JsonProperty("visibleClassName")
    public void setVisibleClassName(String value) { this.visibleClassName = value; }

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

    public static BOCustomEvents fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOCustomEvents fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
    }catch (IOException e) {
        Logger.INSTANCE.e(TAG, e.toString());
    }
        return null;
    }
    public static String toJsonString(BOCustomEvents obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOCustomEvents.class);
        writer = mapper.writerFor(BOCustomEvents.class);
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
