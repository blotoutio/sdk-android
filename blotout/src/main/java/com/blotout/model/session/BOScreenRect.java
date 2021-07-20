package com.blotout.model.session;

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

public class BOScreenRect {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOScreenRect";

    private long screenX;
    private long screenY;

    @JsonProperty("screenX")
    public long getScreenX() { return screenX; }
    @JsonProperty("screenX")
    public void setScreenX(long value) { this.screenX = value; }

    @JsonProperty("screenY")
    public long getScreenY() { return screenY; }
    @JsonProperty("screenY")
    public void setScreenY(long value) { this.screenY = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOScreenRect fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOScreenRect fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try{
            String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOScreenRect obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOScreenRect.class);
        writer = mapper.writerFor(BOScreenRect.class);
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
