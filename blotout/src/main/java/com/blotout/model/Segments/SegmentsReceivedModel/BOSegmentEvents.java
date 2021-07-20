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
public class BOSegmentEvents {
    private static final String TAG = "BOSegmentEvents";

    private BOSegmentsGeo geo;
    private List<BOSegment> segments;

    @JsonProperty("geo")
    public BOSegmentsGeo getGeo() { return geo; }
    @JsonProperty("geo")
    public void setGeo(BOSegmentsGeo value) { this.geo = value; }

    @JsonProperty("segments")
    public List<BOSegment> getSegments() { return segments; }
    @JsonProperty("segments")
    public void setSegments(List<BOSegment> value) { this.segments = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    @Nullable
    public static BOSegmentEvents fromJsonString(String json) {
        try {
            return getObjectReader().readValue(json);
        } catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    @Nullable
    public static BOSegmentEvents fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    @Nullable
    public static String toJsonString(BOSegmentEvents obj)  {
        try {
            return getObjectWriter().writeValueAsString(obj);
        }catch (JsonProcessingException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return null;
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOSegmentEvents.class);
        writer = mapper.writerFor(BOSegmentEvents.class);
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
