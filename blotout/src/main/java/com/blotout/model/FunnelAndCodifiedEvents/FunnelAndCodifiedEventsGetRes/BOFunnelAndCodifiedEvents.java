package com.blotout.model.FunnelAndCodifiedEvents.FunnelAndCodifiedEventsGetRes;

/**
 * Created by Blotout on 07,December,2019
 */
import androidx.annotation.Nullable;

import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class BOFunnelAndCodifiedEvents {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOFunnelAndCodifiedEvents";
    private BOGeoFunnelAndCodifed geo;
    private List<BOEventsCodified> eventsCodified;
    private List<BOEventsFunnel> eventsFunnel;

    @JsonProperty("geo")
    public BOGeoFunnelAndCodifed getGeo() { return geo; }
    @JsonProperty("geo")
    public void setGeo(BOGeoFunnelAndCodifed value) { this.geo = value; }


    @JsonProperty("events_codified")
    public List<BOEventsCodified> getEventsCodified() { return eventsCodified; }
    @JsonProperty("events_codified")
    public void setEventsCodified(List<BOEventsCodified> value) { this.eventsCodified = value; }

    @JsonProperty("funnels")
    public List<BOEventsFunnel> getEventsFunnel() { return eventsFunnel; }
    @JsonProperty("funnels")
    public void setEventsFunnel(List<BOEventsFunnel> value) { this.eventsFunnel = value; }


    /*
        Model to json and vice-versa Conversion Methods
     */

    @Nullable
    public static BOFunnelAndCodifiedEvents fromJsonString(String json) {

        try {
            return getObjectReader().readValue(json);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return null;
    }

    @Nullable
    public static BOFunnelAndCodifiedEvents fromJsonDictionary(HashMap<String,Object> jsonDict) {
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
    public static String toJsonString(BOFunnelAndCodifiedEvents obj) {
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
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);;
        reader = mapper.reader(BOFunnelAndCodifiedEvents.class);
        writer = mapper.writerFor(BOFunnelAndCodifiedEvents.class);
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

