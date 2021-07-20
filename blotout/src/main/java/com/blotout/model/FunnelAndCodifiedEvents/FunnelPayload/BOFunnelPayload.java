package com.blotout.model.FunnelAndCodifiedEvents.FunnelPayload;

/**
 * Created by Blotout on 07,December,2019
 */

import androidx.annotation.Nullable;

import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOFunnelPayload {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOFunnelPayload";

    private BOFunnelMeta meta;
    private BOFunnelGeo geo;
    private List<BOFunnelEvent> fevents;

    @JsonProperty("meta")
    public BOFunnelMeta getMeta() { return meta; }
    @JsonProperty("meta")
    public void setMeta(BOFunnelMeta value) { this.meta = value; }

    @JsonProperty("geo")
    public BOFunnelGeo getGeo() { return geo; }
    @JsonProperty("geo")
    public void setGeo(BOFunnelGeo value) { this.geo = value; }

    @JsonProperty("fevents")
    public List<BOFunnelEvent> getFevents() { return fevents; }
    @JsonProperty("fevents")
    public void setFevents(List<BOFunnelEvent> value) { this.fevents = value; }


    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOFunnelPayload fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOFunnelPayload fromJsonDictionary(HashMap<String,Object> jsonDict) {
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
    public static String toJsonString(BOFunnelPayload obj) {
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
        reader = mapper.reader(BOFunnelPayload.class);
        writer = mapper.writerFor(BOFunnelPayload.class);
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


