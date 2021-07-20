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

public class BOProcessorsUsage {
    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BOProcessorsUsage";

    private long processorID;
    private long usagePercentage;

    @JsonProperty("processorID")
    public long getProcessorID() { return processorID; }
    @JsonProperty("processorID")
    public void setProcessorID(long value) { this.processorID = value; }

    @JsonProperty("usagePercentage")
    public long getUsagePercentage() { return usagePercentage; }
    @JsonProperty("usagePercentage")
    public void setUsagePercentage(long value) { this.usagePercentage = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOProcessorsUsage fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOProcessorsUsage fromJsonDictionary(HashMap<String,Object> jsonDict) throws IOException {
        try {
            String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
    }catch (IOException e) {
        Logger.INSTANCE.e(TAG, e.toString());
    }
        return null;
    }
    public static String toJsonString(BOProcessorsUsage obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOProcessorsUsage.class);
        writer = mapper.writerFor(BOProcessorsUsage.class);
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
