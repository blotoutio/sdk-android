package com.blotout.model.session;

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

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOAppInfo {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAppInfo";

    private boolean sentToServer;
    private String mid;
    private long timeStamp;
    private  String version;
    private  int platform;
    private   String osName;
    private   String osVersion;
    private   String deviceMft;
    private   String deviceModel;
    private   String sdkVersion;
    private   boolean vpnStatus;
    private   boolean jbnStatus;
    private   boolean dcompStatus;
    private   boolean acompStatus;
    private   String name;
    private   String bundle;
    private   String language;
    private long launchTimeStamp;
    private long terminationTimeStamp;
    private long sessionsDuration;
    private long averageSessionsDuration;
    private   String launchReason;
    private BOCurrentLocation currentLocation;
    private String sessionId;
    private int timeZoneOffset;

    @JsonProperty("session_id")
    public String getSessionId() { return sessionId; }
    @JsonProperty("session_id")
    public void setSessionId(String value) { this.sessionId = value; }

    @JsonProperty("sentToServer")
    public boolean getAppVersion() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setAppVersion(boolean value) { this.sentToServer = value; }

    @JsonProperty("mid")
    public String getMid() { return mid; }
    @JsonProperty("mid")
    public void setMid(String value) { this.mid = value; }

    @JsonProperty("timeStamp")
    public long getTimeStamp() { return timeStamp; }
    @JsonProperty("timeStamp")
    public void setTimeStamp(long value) { this.timeStamp = value; }

    @JsonProperty("version")
    public String getVersion() { return version; }
    @JsonProperty("version")
    public void setVersion(String value) { this.version = value; }

    @JsonProperty("platform")
    public int getPlatform() { return platform; }
    @JsonProperty("platform")
    public void setPlatform(int value) { this.platform = value; }

    @JsonProperty("osName")
    public String getOsName() { return osName; }
    @JsonProperty("osName")
    public void setOsName(String value) { this.osName = value; }

    @JsonProperty("osVersion")
    public String getOsVersion() { return osVersion; }
    @JsonProperty("osVersion")
    public void setOsVersion(String value) { this.osVersion = value; }

    @JsonProperty("deviceMft")
    public String getDeviceMft() { return deviceMft; }
    @JsonProperty("deviceMft")
    public void setDeviceMft(String value) { this.deviceMft = value; }

    @JsonProperty("deviceModel")
    public String getDeviceModel() { return deviceModel; }
    @JsonProperty("deviceModel")
    public void setDeviceModel(String value) { this.deviceModel = value; }

    @JsonProperty("sdkVersion")
    public String getSdkVersion() { return sdkVersion; }
    @JsonProperty("sdkVersion")
    public void setSdkVersion(String value) { this.sdkVersion = value; }


    @JsonProperty("vpnStatus")
    public boolean getVpnStatus() { return vpnStatus; }
    @JsonProperty("vpnStatus")
    public void setVpnStatus(boolean value) { this.vpnStatus = value; }

    @JsonProperty("jbnStatus")
    public boolean getJbnStatus() { return jbnStatus; }
    @JsonProperty("jbnStatus")
    public void setJbnStatus(boolean value) { this.jbnStatus = value; }

    @JsonProperty("dcompStatus")
    public boolean getDcompStatus() { return dcompStatus; }
    @JsonProperty("dcompStatus")
    public void setDcompStatus(boolean value) { this.dcompStatus = value; }

    @JsonProperty("acompStatus")
    public boolean getAcompStatus() { return acompStatus; }
    @JsonProperty("acompStatus")
    public void setAcompStatus(boolean value) { this.acompStatus = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("language")
    public String getLanguage() { return language; }
    @JsonProperty("language")
    public void setLanguage(String value) { this.language = value; }

    @JsonProperty("bundle")
    public String getBundle() { return bundle; }
    @JsonProperty("bundle")
    public void setBundle(String value) { this.bundle = value; }

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

    @JsonProperty("timeZoneOffset")
    public int getTimeZoneOffset() { return timeZoneOffset; }
    @JsonProperty("timeZoneOffset")
    public void setTimeZoneOffset(int value) { this.timeZoneOffset = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAppInfo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAppInfo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOAppInfo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAppInfo.class);
        writer = mapper.writerFor(BOAppInfo.class);
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
