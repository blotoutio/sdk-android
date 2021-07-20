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

public class BOOtherID {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOOtherID";

    private String idName;
    private String idValue;

    @JsonProperty("idName")
    public String getidName() { return idName; }
    @JsonProperty("idName")
    public void setidName(String value) { this.idName = value; }

    @JsonProperty("idValue")
    public String getidValue() { return idValue; }
    @JsonProperty("idValue")
    public void setidValue(String value) { this.idValue = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOOtherID fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOOtherID fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOOtherID obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOOtherID.class);
        writer = mapper.writerFor(BOOtherID.class);
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
