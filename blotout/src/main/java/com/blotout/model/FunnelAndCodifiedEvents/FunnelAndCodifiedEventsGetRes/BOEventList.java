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
public class BOEventList {

    private static final String TAG = "BOEventList";

    private String eventCategory;
    private String eventCategorySubtype;
    private String eventSubcategory;
    private String eventName;
    private String screen;
    private List[] properties;
    private String condition;

    @JsonProperty("event_category")
    public String getEventCategory() { return eventCategory; }
    @JsonProperty("event_category")
    public void setEventCategory(String value) { this.eventCategory = value; }

    @JsonProperty("condition")
    public String getCondition() { return condition; }
    @JsonProperty("condition")
    public void setCondition(String value) { this.condition = value; }

    @JsonProperty("subEventCategory")
    public String getEventCategorySubtype() { return eventCategorySubtype; }
    @JsonProperty("subEventCategory")
    public void setEventCategorySubtype(String value) { this.eventCategorySubtype = value; }

    @JsonProperty("event_subcategory")
    public String getEventSubcategory() { return eventSubcategory; }
    @JsonProperty("event_subcategory")
    public void setEventSubcategory(String value) { this.eventSubcategory = value; }

    @JsonProperty("event_name")
    public String getEventName() { return eventName; }
    @JsonProperty("event_name")
    public void setEventName(String value) { this.eventName = value; }

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

    public static BOEventList fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOEventList fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOEventList obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOEventList.class);
        writer = mapper.writerFor(BOEventList.class);
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
