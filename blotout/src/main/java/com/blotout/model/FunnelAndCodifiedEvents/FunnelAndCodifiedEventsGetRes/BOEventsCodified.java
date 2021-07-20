package com.blotout.model.FunnelAndCodifiedEvents.FunnelAndCodifiedEventsGetRes;

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
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOEventsCodified {

    private static final String TAG = "BOEventsCodified";


    private String eventCategory;
    private String eventCategorySubtype;
    private String eventName;
    private long operation;
    private String version;
    private String createdTime;
    private String screen;
    private List[] properties;

    @JsonProperty("event_category")
    public String getEventCategory() { return eventCategory; }
    @JsonProperty("event_category")
    public void setEventCategory(String value) { this.eventCategory = value; }

    @JsonProperty("event_category_subtype")
    public String getEventCategorySubtype() { return eventCategorySubtype; }
    @JsonProperty("event_category_subtype")
    public void setEventCategorySubtype(String value) { this.eventCategorySubtype = value; }

    @JsonProperty("event_name")
    public String getEventName() { return eventName; }
    @JsonProperty("event_name")
    public void setEventName(String value) { this.eventName = value; }

    @JsonProperty("operation")
    public long getOperation() { return operation; }
    @JsonProperty("operation")
    public void setOperation(long value) { this.operation = value; }

    @JsonProperty("version")
    public String getVersion() { return version; }
    @JsonProperty("version")
    public void setVersion(String value) { this.version = value; }

    @JsonProperty("createdTime")
    public String getCreatedTime() { return createdTime; }
    @JsonProperty("createdTime")
    public void setCreatedTime(String value) { this.createdTime = value; }

    @JsonProperty("screen")
    public String getScreen() { return screen; }
    @JsonProperty("screen")
    public void setScreen(String value) { this.screen = value; }

    @JsonProperty("properties")
    public List[] getProperties() { return properties; }
    @JsonProperty("properties")
    public void setProperties(List[] value) { this.properties = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOEventsCodified fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOEventsCodified fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOEventsCodified obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOEventsCodified.class);
        writer = mapper.writerFor(BOEventsCodified.class);
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
