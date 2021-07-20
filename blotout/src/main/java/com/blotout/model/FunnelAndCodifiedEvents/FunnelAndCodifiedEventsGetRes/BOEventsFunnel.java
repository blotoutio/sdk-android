package com.blotout.model.FunnelAndCodifiedEvents.FunnelAndCodifiedEventsGetRes;

import androidx.annotation.Nullable;

import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOEventsFunnel {

    private static final String TAG = "BOEventsFunnel";

    private String identifier;
    private String name;
    private boolean state;
    private boolean status;
    private String version;
    private String createdTime;
    private String updateTime;
    private BOGeoFunnelAndCodifed geoFunnelAndCodifed;
    private List<BOEventList> eventList;

    @JsonProperty("id")
    public String getIdentifier() { return identifier; }
    @JsonProperty("id")
    public void setIdentifier(String value) { this.identifier = value; }

    @JsonProperty("state")
    public boolean getState() { return state; }
    @JsonProperty("state")
    public void setState(boolean value) { this.state = value; }

    @JsonProperty("status")
    public boolean getStatus() { return status; }
    @JsonProperty("status")
    public void setStatus(boolean value) { this.status = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("version")
    public String getVersion() { return version; }
    @JsonProperty("version")
    public void setVersion(String value) { this.version = value; }

    @JsonProperty("createdTime")
    public String getCreatedTime() { return createdTime; }
    @JsonProperty("createdTime")
    public void setCreatedTime(String value) { this.createdTime = value; }

    @JsonProperty("updateTime")
    public String getUpdateTime() { return updateTime; }
    @JsonProperty("updateTime")
    public void setUpdateTime(String value) { this.updateTime = value; }

    @JsonProperty("geo")
    public BOGeoFunnelAndCodifed getGeoFunnelAndCodifed() { return geoFunnelAndCodifed; }
    @JsonProperty("geo")
    public void setGeoFunnelAndCodifed(BOGeoFunnelAndCodifed value) { this.geoFunnelAndCodifed = value; }

    @JsonProperty("event_list")
    public List<BOEventList> getEventList() { return eventList; }
    @JsonProperty("event_list")
    public void setEventList(List<BOEventList> value) { this.eventList = value; }


    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOEventsFunnel fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOEventsFunnel fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOEventsFunnel obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOEventsFunnel.class);
        writer = mapper.writerFor(BOEventsFunnel.class);
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
