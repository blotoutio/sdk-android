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

public class BOAppNavigation {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAppNavigation";

    private boolean sentToServer;
    private long timeStamp;
    private String from;
    private String to;
    private String action;
    private String actionObject;
    private String actionObjectTitle;
    private long actionTime;
    private boolean networkIndicatorVisible;
    private long timeSpent;
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

    @JsonProperty("from")
    public String getFrom() { return from; }
    @JsonProperty("from")
    public void setFrom(String value) { this.from = value; }

    @JsonProperty("to")
    public String getTo() { return to; }
    @JsonProperty("to")
    public void setTo(String value) { this.to = value; }

    @JsonProperty("action")
    public String getAction() { return action; }
    @JsonProperty("action")
    public void setAction(String value) { this.action = value; }

    @JsonProperty("actionObject")
    public String getActionObject() { return actionObject; }
    @JsonProperty("actionObject")
    public void setActionObject(String value) { this.actionObject = value; }

    @JsonProperty("actionObjectTitle")
    public String getActionObjectTitle() { return actionObjectTitle; }
    @JsonProperty("actionObjectTitle")
    public void setActionObjectTitle(String value) { this.actionObjectTitle = value; }

    @JsonProperty("actionTime")
    public long getActionTime() { return actionTime; }
    @JsonProperty("actionTime")
    public void setActionTime(long value) { this.actionTime = value; }

    @JsonProperty("networkIndicatorVisible")
    public boolean getNetworkIndicatorVisible() { return networkIndicatorVisible; }
    @JsonProperty("networkIndicatorVisible")
    public void setNetworkIndicatorVisible(boolean value) { this.networkIndicatorVisible = value; }

    @JsonProperty("timeSpent")
    public long getTimeSpent() { return timeSpent; }
    @JsonProperty("timeSpent")
    public void setTimeSpent(long value) { this.timeSpent = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAppNavigation fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAppNavigation fromJsonDictionary(HashMap<String,Object> jsonDict)  {
        try{
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOAppNavigation obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAppNavigation.class);
        writer = mapper.writerFor(BOAppNavigation.class);
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
