package com.blotout.model.Segments.SegmentsPayload;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.*;

import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOSegmentsResSegment {
    private static final String TAG = "BOSegmentsResSegment";

    private long identifier;
    private long eventTime;
    private String messageID;

    @JsonProperty("id")
    public long getIdentifier() { return identifier; }
    @JsonProperty("id")
    public void setIdentifier(long value) { this.identifier = value; }

    @JsonProperty("event_time")
    public long getEventTime() { return eventTime; }
    @JsonProperty("event_time")
    public void setEventTime(long value) { this.eventTime = value; }

    @JsonProperty("message_id")
    public String getMessageID() { return messageID; }
    @JsonProperty("message_id")
    public void setMessageID(String value) { this.messageID = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOSegmentsResSegment fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOSegmentsResSegment fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOSegmentsResSegment obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOSegmentsResSegment.class);
        writer = mapper.writerFor(BOSegmentsResSegment.class);
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
