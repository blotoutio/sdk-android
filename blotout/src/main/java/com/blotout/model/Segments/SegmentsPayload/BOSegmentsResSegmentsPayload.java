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
public class BOSegmentsResSegmentsPayload {

    private static final String TAG = "BOSegmentsResSegmentsPayload";

    private BOSegmentsResMeta meta;
    private BOSegmentsResGeo geo;
    private List<BOSegmentsResSegment> segments;

    @JsonProperty("meta")
    public BOSegmentsResMeta getMeta() { return meta; }
    @JsonProperty("meta")
    public void setMeta(BOSegmentsResMeta value) { this.meta = value; }

    @JsonProperty("geo")
    public BOSegmentsResGeo getGeo() { return geo; }
    @JsonProperty("geo")
    public void setGeo(BOSegmentsResGeo value) { this.geo = value; }

    @JsonProperty("segments")
    public List<BOSegmentsResSegment> getSegments() { return segments; }
    @JsonProperty("segments")
    public void setSegments(List<BOSegmentsResSegment> value) { this.segments = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOSegmentsResSegmentsPayload fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOSegmentsResSegmentsPayload fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOSegmentsResSegmentsPayload obj) {
        try {
            return getObjectWriter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOSegmentsResSegmentsPayload.class);
        writer = mapper.writerFor(BOSegmentsResSegmentsPayload.class);
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
