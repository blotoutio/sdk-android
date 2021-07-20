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
import java.util.Date;
import java.util.HashMap;

public class BOPendingEvents {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAddToCart";


    private int eventType;
    private String eventName;
    private HashMap<String,Object> eventInfo;
    private Date eventTime;

    @JsonProperty("eventType")
    public int getEventType() { return eventType; }
    @JsonProperty("eventType")
    public void setEventType(int value) { this.eventType = value; }

    @JsonProperty("eventName")
    public String getEventName() { return eventName; }
    @JsonProperty("eventName")
    public void setEventName(String value) { this.eventName = value; }

    @JsonProperty("eventInfo")
    public HashMap<String,Object> getEventInfo() { return eventInfo; }
    @JsonProperty("eventInfo")
    public void setEventInfo(HashMap<String,Object> value) { this.eventInfo = value; }

    @JsonProperty("eventTime")
    public Date getEventTime() { return eventTime; }
    @JsonProperty("eventTime")
    public void setEventTime(Date value) { this.eventTime = value; }


     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOPendingEvents fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOPendingEvents fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        } catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOPendingEvents obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOPendingEvents.class);
        writer = mapper.writerFor(BOPendingEvents.class);
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
