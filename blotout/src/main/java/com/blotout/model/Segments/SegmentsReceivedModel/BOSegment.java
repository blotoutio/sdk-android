package com.blotout.model.Segments.SegmentsReceivedModel;

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
public class BOSegment {
    private static final String TAG = "BOSegment";

    private Long identifier;
    private String name;
    private long state;
    private long createdTime;
    private BORuleSet ruleset;
    private boolean status;
    private long updateTime;

    @JsonProperty("id")
    public Long getIdentifier() { return identifier; }
    @JsonProperty("id")
    public void setIdentifier(Long value) { this.identifier = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("state")
    public long getState() { return state; }
    @JsonProperty("state")
    public void setState(long value) { this.state = value; }

    @JsonProperty("createdTime")
    public long getCreatedTime() { return createdTime; }
    @JsonProperty("createdTime")
    public void setCreatedTime(long value) { this.createdTime = value; }

    @JsonProperty("ruleset")
    public BORuleSet getRuleset() { return ruleset; }
    @JsonProperty("ruleset")
    public void setRuleset(BORuleSet value) { this.ruleset = value; }

    @JsonProperty("status")
    public boolean getStatus() { return status; }
    @JsonProperty("status")
    public void setStatus(boolean value) { this.status = value; }

    @JsonProperty("updateTime")
    public long getUpdateTime() { return updateTime; }
    @JsonProperty("updateTime")
    public void setUpdateTime(long value) { this.updateTime = value; }



     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOSegment fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOSegment fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOSegment obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOSegment.class);
        writer = mapper.writerFor(BOSegment.class);
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
