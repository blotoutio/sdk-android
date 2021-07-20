package com.blotout.model.FunnelAndCodifiedEvents.FunnelPayload;

/**
 * Created by Blotout on 07,December,2019
 */
import androidx.annotation.Nullable;

import com.blotout.constants.BOCommonConstants;
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
public class BOFunnelEvent {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOFunnelEvent";

    private String identifier;
    private String version;
    private String name;
    private long eventTime;
    private String dayOfAnalysis;
    private long daySessionCount;
    private String messageID;
    private boolean isaDayEvent;
    private boolean isTraversed;
    private long dayTraversedCount;
    private List<Long> visits;
    private List<Long>navigationTime;
    private boolean userReferral;
    private long userTraversedCount;
    private String prevTraversalDay;


    @JsonProperty("id")
    public String getIdentifier() { return identifier; }
    @JsonProperty("id")
    public void setIdentifier(String value) { this.identifier = value; }

    @JsonProperty("version")
    public String getVersion() { return version; }
    @JsonProperty("version")
    public void setVersion(String value) { this.version = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("event_time")
    public long getEventTime() { return eventTime; }
    @JsonProperty("event_time")
    public void setEventTime(long value) { this.eventTime = value; }

    @JsonProperty("day_of_analysis")
    public String getDayOfAnalysis() { return dayOfAnalysis; }
    @JsonProperty("day_of_analysis")
    public void setDayOfAnalysis(String value) { this.dayOfAnalysis = value; }

    @JsonProperty("day_session_count")
    public long getDaySessionCount() { return daySessionCount; }
    @JsonProperty("day_session_count")
    public void setDaySessionCount(long value) { this.daySessionCount = value; }

    @JsonProperty("message_id")
    public String getMessageID() { return messageID; }
    @JsonProperty("message_id")
    public void setMessageID(String value) { this.messageID = value; }

    @JsonProperty("isa_day_event")
    public boolean getISADayEvent() { return isaDayEvent; }
    @JsonProperty("isa_day_event")
    public void setISADayEvent(boolean value) { this.isaDayEvent = value; }

    @JsonProperty("is_traversed")
    public boolean getIsTraversed() { return isTraversed; }
    @JsonProperty("is_traversed")
    public void setIsTraversed(boolean value) { this.isTraversed = value; }

    @JsonProperty("day_traversed_count")
    public long getDayTraversedCount() { return dayTraversedCount; }
    @JsonProperty("day_traversed_count")
    public void setDayTraversedCount(long value) { this.dayTraversedCount = value; }

    @JsonProperty("visits")
    public List<Long> getVisits() { return visits; }
    @JsonProperty("visits")
    public void setVisits(List<Long> value) { this.visits = value; }

    @JsonProperty("navigation_time")
    public List<Long> getNavigationTime() { return navigationTime; }
    @JsonProperty("navigation_time")
    public void setNavigationTime(List<Long> value) { this.navigationTime = value; }

    @JsonProperty("user_referral")
    public boolean getUserReferral() { return userReferral; }
    @JsonProperty("user_referral")
    public void setUserReferral(boolean value) { this.userReferral = value; }

    @JsonProperty("user_traversed_count")
    public long getUserTraversedCount() { return userTraversedCount; }
    @JsonProperty("user_traversed_count")
    public void setUserTraversedCount(long value) { this.userTraversedCount = value; }

    @JsonProperty("prev_traversal_day")
    public String getPrevTraversalDay() { return prevTraversalDay; }
    @JsonProperty("prev_traversal_day")
    public void setPrevTraversalDay(String value) { this.prevTraversalDay = value; }


    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOFunnelEvent fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOFunnelEvent fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOFunnelEvent obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOFunnelEvent.class);
        writer = mapper.writerFor(BOFunnelEvent.class);
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
