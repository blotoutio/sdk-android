package com.blotout.model.session;

import androidx.annotation.Nullable;

import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOCommonEvent {
    private static final String TAG = "BOCommonEvent";


    private boolean sentToServer;
    private long timeStamp;
    private String eventName;
    private String visibleClassName;
    private HashMap<String,Object> eventInfo;
    private long eventSubCode;
    private long eventCode;
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

    @JsonProperty("eventName")
    public String getEventName() { return eventName; }
    @JsonProperty("eventName")
    public void setEventName(String value) { this.eventName = value; }

    @JsonProperty("visibleClassName")
    public String getVisibleClassName() { return visibleClassName; }
    @JsonProperty("visibleClassName")
    public void setVisibleClassName(String value) { this.visibleClassName = value; }

    @JsonProperty("eventInfo")
    public HashMap<String,Object> getEventInfo() { return eventInfo; }
    @JsonProperty("eventInfo")
    public void setEventInfo(HashMap<String,Object> value) { this.eventInfo = value; }

    @JsonProperty("eventSubCode")
    public long getEventSubCode() { return eventSubCode; }
    @JsonProperty("eventSubCode")
    public void setEventSubCode(long value) { this.eventSubCode = value; }

    @JsonProperty("eventCode")
    public long getEventCode() { return eventCode; }
    @JsonProperty("eventCode")
    public void setEventCode(long value) { this.eventCode = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOCommonEvent fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOCommonEvent fromJsonDictionary(HashMap<String,Object> jsonDict)  {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOCommonEvent obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOCommonEvent.class);
        writer = mapper.writerFor(BOCommonEvent.class);
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
