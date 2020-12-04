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

public class BOChargeTransaction {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOChargeTransaction";

    private boolean sentToServer;
    private long timeStamp;
    private String transactionClassName;
    private HashMap<String,Object> transactionInfo;
    private String mid;
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

    @JsonProperty("transactionClassName")
    public String getTransactionClassName() { return transactionClassName; }
    @JsonProperty("transactionClassName")
    public void setTransactionClassName(String value) { this.transactionClassName = value; }

    @JsonProperty("transactionInfo")
    public HashMap<String,Object> getTransactionInfo() { return transactionInfo; }
    @JsonProperty("transactionInfo")
    public void setTransactionInfo(HashMap<String,Object> value) { this.transactionInfo = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOChargeTransaction fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOChargeTransaction fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        } catch(IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
       return null;
    }

    public static String toJsonString(BOChargeTransaction obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOChargeTransaction.class);
        writer = mapper.writerFor(BOChargeTransaction.class);
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
