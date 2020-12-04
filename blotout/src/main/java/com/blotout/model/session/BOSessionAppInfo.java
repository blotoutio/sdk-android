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

public class BOSessionAppInfo {

    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BOSessionAppInfo";

    private String mid;
    private boolean sentToServer;
    private long timeStamp;
    private String version;
    private String sdkVersion;
    private String name;
    private String bundle;
    private String language;
    private long launchTimeStamp;
    private long terminationTimeStamp;
    private long sessionsDuration;
    private long averageSessionsDuration;
    private String launchReason;
    private BOCurrentLocation currentLocation;
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

    @JsonProperty("version")
    public String getVersion() { return version; }
    @JsonProperty("version")
    public void setVersion(String value) { this.version = value; }

    @JsonProperty("sdkVersion")
    public String getsdkVersion() { return sdkVersion; }
    @JsonProperty("sdkVersion")
    public void setsdkVersion(String value) { this.sdkVersion = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("bundle")
    public String getBundle() { return bundle; }
    @JsonProperty("bundle")
    public void setBundle(String value) { this.bundle = value; }

    @JsonProperty("language")
    public String getLanguage() { return language; }
    @JsonProperty("language")
    public void setLanguage(String value) { this.language = value; }

    @JsonProperty("launchTimeStamp")
    public long getLaunchTimeStamp() { return launchTimeStamp; }
    @JsonProperty("launchTimeStamp")
    public void setLaunchTimeStamp(long value) { this.launchTimeStamp = value; }

    @JsonProperty("terminationTimeStamp")
    public long getTerminationTimeStamp() { return terminationTimeStamp; }
    @JsonProperty("terminationTimeStamp")
    public void setTerminationTimeStamp(long value) { this.terminationTimeStamp = value; }

    @JsonProperty("sessionsDuration")
    public long getSessionsDuration() { return sessionsDuration; }
    @JsonProperty("sessionsDuration")
    public void setSessionsDuration(long value) { this.sessionsDuration = value; }

    @JsonProperty("averageSessionsDuration")
    public long getAverageSessionsDuration() { return averageSessionsDuration; }
    @JsonProperty("averageSessionsDuration")
    public void setAverageSessionsDuration(long value) { this.averageSessionsDuration = value; }

    @JsonProperty("launchReason")
    public String getLaunchReason() { return launchReason; }
    @JsonProperty("launchReason")
    public void setLaunchReason(String value) { this.launchReason = value; }

    @JsonProperty("currentLocation")
    public BOCurrentLocation getCurrentLocation() { return currentLocation; }
    @JsonProperty("currentLocation")
    public void setCurrentLocation(BOCurrentLocation value) { this.currentLocation = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOSessionAppInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOSessionAppInfo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try{
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOSessionAppInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOSessionAppInfo.class);
        writer = mapper.writerFor(BOSessionAppInfo.class);
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
