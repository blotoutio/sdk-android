package com.blotout.analytics;

/**
 * Created by Blotout on 03,November,2019
 */

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

public class BOEvent {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOEvent";
    private long ts;
    private long dayOccur;
    private long type;
    private String sType;
    private String msgID;
    private String campaignID;
    private String name;
    private String scrName;
    private Object[] properties;

    @JsonProperty("evt")
    public long getTs() { return ts; }
    @JsonProperty("evt")
    public void setTs(long value) { this.ts = value; }

    @JsonProperty("evdc")
    public long getDayOccur() { return dayOccur; }
    @JsonProperty("evdc")
    public void setDayOccur(long value) { this.dayOccur = value; }

    @JsonProperty("evc")
    public long getType() { return type; }
    @JsonProperty("evc")
    public void setType(long value) { this.type = value; }

    @JsonProperty("evcs")
    public String getSType() { return sType; }
    @JsonProperty("evcs")
    public void setSType(String value) { this.sType = value; }

    @JsonProperty("mid")
    public String getMsgID() { return msgID; }
    @JsonProperty("mid")
    public void setMsgID(String value) { this.msgID = value; }

    @JsonProperty("id")
    public String getCampaignID() { return campaignID; }
    @JsonProperty("id")
    public void setCampaignID(String value) { this.campaignID = value; }

    @JsonProperty("evn")
    public String getName() { return name; }
    @JsonProperty("evn")
    public void setName(String value) { this.name = value; }

    @JsonProperty("scrn")
    public String getScrName() { return scrName; }
    @JsonProperty("scrn")
    public void setScrName(String value) { this.scrName = value; }

    @JsonProperty("properties")
    public Object[] getProperties() { return properties; }
    @JsonProperty("properties")
    public void setProperties(Object[] value) { this.properties = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOEvent fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOEvent fromJsonDictionary(HashMap<String,Object> jsonDict)  {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOEvent obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOEvent.class);
        writer = mapper.writerFor(BOEvent.class);
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
