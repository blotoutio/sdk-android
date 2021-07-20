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

public class BOScreenEdgePan {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOScreenEdgePan";


    private boolean sentToServer;
    private long timeStamp;
    private String objectType;
    private String visibleClassName;
    private BOScreenRect screenRectFrom;
    private BOScreenRect screenRectTo;
    private String mid;

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

    @JsonProperty("objectType")
    public String getObjectType() { return objectType; }
    @JsonProperty("objectType")
    public void setObjectType(String value) { this.objectType = value; }

    @JsonProperty("visibleClassName")
    public String getVisibleClassName() { return visibleClassName; }
    @JsonProperty("visibleClassName")
    public void setVisibleClassName(String value) { this.visibleClassName = value; }

    @JsonProperty("screenRectFrom")
    public BOScreenRect getScreenRectFrom() { return screenRectFrom; }
    @JsonProperty("screenRectFrom")
    public void setScreenRectFrom(BOScreenRect value) { this.screenRectFrom = value; }

    @JsonProperty("screenRectTo")
    public BOScreenRect getScreenRectTo() { return screenRectTo; }
    @JsonProperty("screenRectTo")
    public void setScreenRectTo(BOScreenRect value) { this.screenRectTo = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOScreenEdgePan fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOScreenEdgePan fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOScreenEdgePan obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOScreenEdgePan.class);
        writer = mapper.writerFor(BOScreenEdgePan.class);
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
