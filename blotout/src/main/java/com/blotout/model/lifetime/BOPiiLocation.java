package com.blotout.model.lifetime;

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

public class BOPiiLocation {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOPiiLocation";

    private double lat;
    private double piiLocationLong;

    @JsonProperty("latitude")
    public double getLat() { return lat; }
    @JsonProperty("latitude")
    public void setLat(double value) { this.lat = value; }

    @JsonProperty("longitude")
    public double getPiiLocationLong() { return piiLocationLong; }
    @JsonProperty("longitude")
    public void setPiiLocationLong(double value) { this.piiLocationLong = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOPiiLocation fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOPiiLocation fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try{
            String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
    }catch (IOException e) {
        Logger.INSTANCE.e(TAG, e.toString());
    }
        return null;
    }
    public static String toJsonString(BOPiiLocation obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOPiiLocation.class);
        writer = mapper.writerFor(BOPiiLocation.class);
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
