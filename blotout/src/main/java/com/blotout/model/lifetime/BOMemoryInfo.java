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

public class BOMemoryInfo {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOMemoryInfo";

    private boolean sentToServer;
    private long totalRAM;
    private long userMemory;
    private long wireMemory;
    private long activeMemory;
    private long inActiveMemory;
    private long freeMemory;
    private long purgeableMemory;

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("totalRAM")
    public long getTotalRAM() { return totalRAM; }
    @JsonProperty("totalRAM")
    public void setTotalRAM(long value) { this.totalRAM = value; }

    @JsonProperty("userMemory")
    public long getUserMemory() { return userMemory; }
    @JsonProperty("userMemory")
    public void setUserMemory(long value) { this.userMemory = value; }

    @JsonProperty("wireMemory")
    public long getWireMemory() { return wireMemory; }
    @JsonProperty("wireMemory")
    public void setWireMemory(long value) { this.wireMemory = value; }

    @JsonProperty("activeMemory")
    public long getActiveMemory() { return activeMemory; }
    @JsonProperty("activeMemory")
    public void setActiveMemory(long value) { this.activeMemory = value; }

    @JsonProperty("inActiveMemory")
    public long getInActiveMemory() { return inActiveMemory; }
    @JsonProperty("inActiveMemory")
    public void setInActiveMemory(long value) { this.inActiveMemory = value; }

    @JsonProperty("freeMemory")
    public long getFreeMemory() { return freeMemory; }
    @JsonProperty("freeMemory")
    public void setFreeMemory(long value) { this.freeMemory = value; }

    @JsonProperty("purgeableMemory")
    public long getPurgeableMemory() { return purgeableMemory; }
    @JsonProperty("purgeableMemory")
    public void setPurgeableMemory(long value) { this.purgeableMemory = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOMemoryInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOMemoryInfo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOMemoryInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOMemoryInfo.class);
        writer = mapper.writerFor(BOMemoryInfo.class);
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
