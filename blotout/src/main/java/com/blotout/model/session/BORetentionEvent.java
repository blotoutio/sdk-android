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

public class BORetentionEvent {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BORetentionEvent";

    private boolean sentToServer;
    private BODau dau;
    private BODpu dpu;
    private BOAppInstalled appInstalled;
    private BONewUser newUser;
    private BODast dast;
    private List<BOCustomEvent> customEvents;
    private String mid;

    @JsonProperty("mid")
    public String getMid() { return mid; }
    @JsonProperty("mid")
    public void setMid(String value) { this.mid = value; }

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("dau")
    public BODau getDau() { return dau; }
    @JsonProperty("dau")
    public void setDau(BODau value) { this.dau = value; }

    @JsonProperty("dpu")
    public BODpu getDpu() { return dpu; }
    @JsonProperty("dpu")
    public void setDpu(BODpu value) { this.dpu = value; }

    @JsonProperty("appInstalled")
    public BOAppInstalled getAppInstalled() { return appInstalled; }
    @JsonProperty("appInstalled")
    public void setAppInstalled(BOAppInstalled value) { this.appInstalled = value; }

    @JsonProperty("newUser")
    public BONewUser getNewUser() { return newUser; }
    @JsonProperty("newUser")
    public void setNewUser(BONewUser value) { this.newUser = value; }

    @JsonProperty("dast")
    public BODast getDast() { return dast; }
    @JsonProperty("dast")
    public void setDast(BODast value) { this.dast = value; }

    @JsonProperty("customEvents")
    public List<BOCustomEvent> getCustomEvents() { return customEvents; }
    @JsonProperty("customEvents")
    public void setCustomEvents(List<BOCustomEvent> value) { this.customEvents = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BORetentionEvent fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BORetentionEvent fromJsonDictionary(HashMap<String,Object> jsonDict)  {

       try{
           String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BORetentionEvent obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BORetentionEvent.class);
        writer = mapper.writerFor(BORetentionEvent.class);
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
