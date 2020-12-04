package com.blotout.model.session;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.*;

import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class BOCrashDetail {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOCrashDetail";

    private String mid;
    private boolean sentToServer;
    private long timeStamp;
    private String name;
    private String reason;
    private HashMap<String,Object> info;
    private List<String> callStackSymbols;
    private List<Long> callStackReturnAddress;
    private String sessionId;

    @JsonProperty("sessionId")
    public String getSessionId() { return sessionId; }
    @JsonProperty("sessionId")
    public void setSessionId(String value) { this.sessionId = value; }

    @JsonProperty("mid")
    public String getMid() { return mid; }
    @JsonProperty("mid")
    public void setMid(String value) { this.mid = value; }

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("reason")
    public String getReason() { return reason; }
    @JsonProperty("reason")
    public void setReason(String value) { this.reason = value; }

    @JsonProperty("info")
    public HashMap<String,Object> getInfo() { return info; }
    @JsonProperty("info")
    public void setInfo(HashMap<String,Object> value) { this.info = value; }

    @JsonProperty("callStackSymbols")
    public List<String> getCallStackSymbols() { return callStackSymbols; }
    @JsonProperty("callStackSymbols")
    public void setCallStackSymbols(List<String> value) { this.callStackSymbols = value; }

    @JsonProperty("callStackReturnAddress")
    public List<Long> getCallStackReturnAddress() { return callStackReturnAddress; }
    @JsonProperty("callStackReturnAddress")
    public void setCallStackReturnAddress(List<Long> value) { this.callStackReturnAddress = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOCrashDetail fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOCrashDetail fromJsonDictionary(HashMap<String,Object> jsonDict)  {
        try{
            String jsonString = null;
          jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
          return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOCrashDetail obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOCrashDetail.class);
        writer = mapper.writerFor(BOCrashDetail.class);
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
