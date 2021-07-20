package com.blotout.model.Segments.SegmentsReceivedModel;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.*;

import com.blotout.model.Segments.SegmentsPayload.BOSegmentsResGeo;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BORule {
    private static final String TAG = "BORule";

    private Long segmentID;
    private Long subEventCategory;
    private String operatorKey;
    private String eventName;
    private String key;
    private List<String> value;
    private String condition;
    private List<BORule> rules;

    @JsonProperty("segmentId")
    public Long getSegmentID() { return segmentID; }
    @JsonProperty("segmentId")
    public void setSegmentID(Long value) { this.segmentID = value; }

    @JsonProperty("event_subcategory")
    public Long getSubEventCategory() { return subEventCategory; }
    @JsonProperty("event_subcategory")
    public void setSubEventCategory(Long value) { this.subEventCategory = value; }

    @JsonProperty("operator")
    public Long getOperatorKey() { return Long.parseLong(operatorKey); }
    @JsonProperty("operator")
    public void setOperatorKey(String value) { this.operatorKey = value; }

    @JsonProperty("event_name")
    public String getEventName() { return eventName; }
    @JsonProperty("event_name")
    public void setEventName(String value) { this.eventName = value; }

    @JsonProperty("key")
    public String getKey() { return key; }
    @JsonProperty("key")
    public void setKey(String value) { this.key = value; }

    @JsonProperty("value")
    public List<String> getValue() { return value; }
    @JsonProperty("value")
    public void setValue(List<String> value) { this.value = value; }

    @JsonProperty("condition")
    public String getCondition() { return condition; }
    @JsonProperty("condition")
    public void setCondition(String value) { this.condition = value; }

    @JsonProperty("rules")
    public List<BORule> getRules() { return rules; }
    @JsonProperty("rules")
    public void setRules(List<BORule> value) { this.rules = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BORule fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BORule fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BORule obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BORule.class);
        writer = mapper.writerFor(BORule.class);
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
