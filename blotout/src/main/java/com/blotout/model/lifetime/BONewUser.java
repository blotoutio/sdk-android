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

public class BONewUser {

    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BONewUser";

    private String mid;
    private boolean isNewUser;
    private long timeStamp;
    private HashMap<String,Object> theNewUserInfo;
    private boolean sentToServer;


    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("isNewUser")
    public boolean getIsNewUser() { return isNewUser; }
    @JsonProperty("isNewUser")
    public void setIsNewUser(boolean value) { this.isNewUser = value; }

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("theNewUserInfo")
    public HashMap<String,Object> getTheNewUserInfo() { return theNewUserInfo; }
    @JsonProperty("theNewUserInfo")
    public void setTheNewUserInfo(HashMap<String,Object> value) { this.theNewUserInfo = value; }

    @JsonProperty("mid")
    public String getMid() { return mid; }
    @JsonProperty("mid")
    public void setMid(String value) { this.mid = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BONewUser fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BONewUser fromJsonDictionary(HashMap<String,Object> jsonDict) {
       try {
           String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
       }catch (IOException e) {
           Logger.INSTANCE.e(TAG, e.toString());
       }
        return null;
    }
    public static String toJsonString(BONewUser obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BONewUser.class);
        writer = mapper.writerFor(BONewUser.class);
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
