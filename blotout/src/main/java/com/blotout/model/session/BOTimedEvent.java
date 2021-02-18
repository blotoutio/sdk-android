package com.blotout.model.session;

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

public class BOTimedEvent {
    private static final String TAG = "BOTimedEvent";

    private boolean sentToServer;
    private long timeStamp;
    private String eventName;
    private long startTime;
    private String startVisibleClassName;
    private String endVisibleClassName;
    private long endTime;
    private long eventDuration;
    private HashMap<String,Object> timedEvenInfo;
    private String mid;
    private String sessionId;

    @JsonProperty("session_id")
    public String getSessionId() { return sessionId; }
    @JsonProperty("session_id")
    public void setSessionId(String value) { this.sessionId = value; }

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

    @JsonProperty("eventName")
    public String getEventName() { return eventName; }
    @JsonProperty("eventName")
    public void setEventName(String value) { this.eventName = value; }

    @JsonProperty("startTime")
    public long getStartTime() { return startTime; }
    @JsonProperty("startTime")
    public void setStartTime(long value) { this.startTime = value; }

    @JsonProperty("startVisibleClassName")
    public String getStartVisibleClassName() { return startVisibleClassName; }
    @JsonProperty("startVisibleClassName")
    public void setStartVisibleClassName(String value) { this.startVisibleClassName = value; }

    @JsonProperty("endVisibleClassName")
    public String getEndVisibleClassName() { return endVisibleClassName; }
    @JsonProperty("endVisibleClassName")
    public void setEndVisibleClassName(String value) { this.endVisibleClassName = value; }

    @JsonProperty("endTime")
    public long getEndTime() { return endTime; }
    @JsonProperty("endTime")
    public void setEndTime(long value) { this.endTime = value; }

    @JsonProperty("eventDuration")
    public long getEventDuration() { return eventDuration; }
    @JsonProperty("eventDuration")
    public void setEventDuration(long value) { this.eventDuration = value; }

    @JsonProperty("timedEvenInfo")
    public HashMap<String,Object> getTimedEvenInfo() { return timedEvenInfo; }
    @JsonProperty("timedEvenInfo")
    public void setTimedEvenInfo(HashMap<String,Object> value) { this.timedEvenInfo = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOTimedEvent fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOTimedEvent fromJsonDictionary(HashMap<String,Object> jsonDict)  {
       try {
           String jsonString = null;
           jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
           return getObjectReader().readValue(jsonString);
       }catch (IOException e) {
           Logger.INSTANCE.e(TAG, e.toString());
       }
        return null;
    }

    public static String toJsonString(BOTimedEvent obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOTimedEvent.class);
        writer = mapper.writerFor(BOTimedEvent.class);
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
