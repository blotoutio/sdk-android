package com.blotout.model.FunnelAndCodifiedEvents.EventsGetRequest;

/**
 * Created by Blotout on 07,December,2019
 */

import androidx.annotation.Nullable;

import com.blotout.constants.BOCommonConstants;
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

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOEventsGetRequest {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOEventsGetRequest";

    private BOAEventsGet boaEventsGet;
    private BOAGeoEventsGet boaGeoEventsGet;
    private BOAMetaEventsGet boaMetaEventsGet;

    @JsonProperty("BOAEventsGet")
    public BOAEventsGet getBoaEventsGet() { return boaEventsGet; }
    @JsonProperty("BOAEventsGet")
    public void setBoaEventsGet(BOAEventsGet value) { this.boaEventsGet = value; }

    @JsonProperty("BOAGeoEventsGet")
    public BOAGeoEventsGet getBoaGeoEventsGet() { return boaGeoEventsGet; }
    @JsonProperty("BOAGeoEventsGet")
    public void setBoaGeoEventsGet(BOAGeoEventsGet value) { this.boaGeoEventsGet = value; }

    @JsonProperty("BOAMetaEventsGet")
    public BOAMetaEventsGet getBoaMetaEventsGet() { return boaMetaEventsGet; }
    @JsonProperty("BOAMetaEventsGet")
    public void setBoaMetaEventsGet(BOAMetaEventsGet value) { this.boaMetaEventsGet = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOEventsGetRequest fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOEventsGetRequest fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOEventsGetRequest obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOEventsGetRequest.class);
        writer = mapper.writerFor(BOEventsGetRequest.class);
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