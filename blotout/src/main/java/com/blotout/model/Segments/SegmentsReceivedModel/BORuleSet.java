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
public class BORuleSet {

    private static final String TAG = "BORuleSet";

    private List<BORule> rules;
    private String condition;

    @JsonProperty("rules")
    public List<BORule> getRules() { return rules; }
    @JsonProperty("rules")
    public void setRules(List<BORule> value) { this.rules = value; }

    @JsonProperty("condition")
    public String getCondition() { return condition; }
    @JsonProperty("condition")
    public void setCondition(String value) { this.condition = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BORuleSet fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BORuleSet fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BORuleSet obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BORuleSet.class);
        writer = mapper.writerFor(BORuleSet.class);
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
