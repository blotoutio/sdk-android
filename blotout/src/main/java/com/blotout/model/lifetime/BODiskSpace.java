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

public class BODiskSpace {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BODiskSpace";

    private String diskSpace;
    private String unit;

    @JsonProperty("diskSpace")
    public String getDiskSpace() { return diskSpace; }
    @JsonProperty("diskSpace")
    public void setDiskSpace(String value) { this.diskSpace = value; }

    @JsonProperty("unit")
    public String getUnit() { return unit; }
    @JsonProperty("unit")
    public void setUnit(String value) { this.unit = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BODiskSpace fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BODiskSpace fromJsonDictionary(HashMap<String,Object> jsonDict)  {
       try {
           String jsonString = null;
           jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
           return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BODiskSpace obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BODiskSpace.class);
        writer = mapper.writerFor(BODiskSpace.class);
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
