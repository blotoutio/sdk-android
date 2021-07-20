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

public class BOLocation {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOLocation";

    private boolean sentToServer;
    private long timeStamp;
    private BOPiiLocation piiLocation;
    private BONonPIILocation nonPIILocation;

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("piiLocation")
    public BOPiiLocation getPiiLocation() { return piiLocation; }
    @JsonProperty("piiLocation")
    public void setPiiLocation(BOPiiLocation value) { this.piiLocation = value; }

    @JsonProperty("nonPIILocation")
    public BONonPIILocation getNonPIILocation() { return nonPIILocation; }
    @JsonProperty("nonPIILocation")
    public void setNonPIILocation(BONonPIILocation value) { this.nonPIILocation = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOLocation fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOLocation fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try{
            String jsonString = null;
           jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
           return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOLocation obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOLocation.class);
        writer = mapper.writerFor(BOLocation.class);
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
