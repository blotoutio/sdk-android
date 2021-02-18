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

public class BOAddToCart {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAddToCart";

    private boolean sentToServer;
    private long timeStamp;
    private String cartClassName;
    private HashMap<String,Object> additionalInfo;
    private String mid;
    private String sessionId;

    @JsonProperty("session_id")
    public String getSessionId() { return sessionId; }
    @JsonProperty("session_id")
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

    @JsonProperty("cartClassName")
    public String getCartClassName() { return cartClassName; }
    @JsonProperty("cartClassName")
    public void setCartClassName(String value) { this.cartClassName = value; }

    @JsonProperty("additionalInfo")
    public HashMap<String,Object> getAdditionalInfo() { return additionalInfo; }
    @JsonProperty("additionalInfo")
    public void setAdditionalInfo(HashMap<String,Object> value) { this.additionalInfo = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAddToCart fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAddToCart fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        } catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOAddToCart obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAddToCart.class);
        writer = mapper.writerFor(BOAddToCart.class);
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
