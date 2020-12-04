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

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOCustomEvent {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOCustomEvent";


    private boolean sentToServer;
    private long timeStamp;
    private String eventName;
    private String visibleClassName;
    private HashMap<String,Object> eventInfo;
    private long eventSubCode;
    private String mid;
    private String sessionId;

    @JsonProperty("sessionId")
    public String getSessionId() { return sessionId; }
    @JsonProperty("sessionId")
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


     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOCustomEvent fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOCustomEvent fromJsonDictionary(HashMap<String,Object> jsonDict)  {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOCustomEvent obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOCustomEvent.class);
        writer = mapper.writerFor(BOCustomEvent.class);
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
