package com.blotout.model.session;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.*;

import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class BOAppStates {
    private static final String TAG = "BOAppStates";

    private boolean sentToServer;
    private List<BOApp> appLaunched;
    private List<BOApp> appActive;
    private List<BOApp> appResignActive;
    private List<BOApp> appInBackground;
    private List<BOApp> appInForeground;
    private List<BOApp> appBackgroundRefreshAvailable;
    private List<BOApp> appReceiveMemoryWarning;
    private List<BOApp> appSignificantTimeChange;
    private List<BOApp> appOrientationPortrait;
    private List<BOApp> appOrientationLandscape;
    private List<BOApp> appStatusbarFrameChange;
    private List<BOApp> appBackgroundRefreshStatusChange;
    private List<BOApp> appNotificationReceived;
    private List<BOApp> appNotificationViewed;
    private List<BOApp> appNotificationClicked;
    private List<BOSessionInfo> appSessionInfo;

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("appLaunched")
    public List<BOApp> getAppLaunched() { return appLaunched; }
    @JsonProperty("appLaunched")
    public void setAppLaunched(List<BOApp> value) { this.appLaunched = value; }

    @JsonProperty("appActive")
    public List<BOApp> getAppActive() { return appActive; }
    @JsonProperty("appActive")
    public void setAppActive(List<BOApp> value) { this.appActive = value; }

    @JsonProperty("appResignActive")
    public List<BOApp> getAppResignActive() { return appResignActive; }
    @JsonProperty("appResignActive")
    public void setAppResignActive(List<BOApp> value) { this.appResignActive = value; }

    @JsonProperty("appInBackground")
    public List<BOApp> getAppInBackground() { return appInBackground; }
    @JsonProperty("appInBackground")
    public void setAppInBackground(List<BOApp> value) { this.appInBackground = value; }

    @JsonProperty("appInForeground")
    public List<BOApp> getAppInForeground() { return appInForeground; }
    @JsonProperty("appInForeground")
    public void setAppInForeground(List<BOApp> value) { this.appInForeground = value; }

    @JsonProperty("appBackgroundRefreshAvailable")
    public List<BOApp> getAppBackgroundRefreshAvailable() { return appBackgroundRefreshAvailable; }
    @JsonProperty("appBackgroundRefreshAvailable")
    public void setAppBackgroundRefreshAvailable(List<BOApp> value) { this.appBackgroundRefreshAvailable = value; }

    @JsonProperty("appReceiveMemoryWarning")
    public List<BOApp> getAppReceiveMemoryWarning() { return appReceiveMemoryWarning; }
    @JsonProperty("appReceiveMemoryWarning")
    public void setAppReceiveMemoryWarning(List<BOApp> value) { this.appReceiveMemoryWarning = value; }

    @JsonProperty("appSignificantTimeChange")
    public List<BOApp> getAppSignificantTimeChange() { return appSignificantTimeChange; }
    @JsonProperty("appSignificantTimeChange")
    public void setAppSignificantTimeChange(List<BOApp> value) { this.appSignificantTimeChange = value; }

    @JsonProperty("appOrientationPortrait")
    public List<BOApp> getAppOrientationPortrait() { return appOrientationPortrait; }
    @JsonProperty("appOrientationPortrait")
    public void setAppOrientationPortrait(List<BOApp> value) { this.appOrientationPortrait = value; }

    @JsonProperty("appOrientationLandscape")
    public List<BOApp> getAppOrientationLandscape() { return appOrientationLandscape; }
    @JsonProperty("appOrientationLandscape")
    public void setAppOrientationLandscape(List<BOApp> value) { this.appOrientationLandscape = value; }

    @JsonProperty("appStatusbarFrameChange")
    public List<BOApp> getAppStatusbarFrameChange() { return appStatusbarFrameChange; }
    @JsonProperty("appStatusbarFrameChange")
    public void setAppStatusbarFrameChange(List<BOApp> value) { this.appStatusbarFrameChange = value; }

    @JsonProperty("appBackgroundRefreshStatusChange")
    public List<BOApp> getAppBackgroundRefreshStatusChange() { return appBackgroundRefreshStatusChange; }
    @JsonProperty("appBackgroundRefreshStatusChange")
    public void setAppBackgroundRefreshStatusChange(List<BOApp> value) { this.appBackgroundRefreshStatusChange = value; }

    @JsonProperty("appNotificationReceived")
    public List<BOApp> getAppNotificationReceived() { return appNotificationReceived; }
    @JsonProperty("appNotificationReceived")
    public void setAppNotificationReceived(List<BOApp> value) { this.appNotificationReceived = value; }

    @JsonProperty("appNotificationViewed")
    public List<BOApp> getAppNotificationViewed() { return appNotificationViewed; }
    @JsonProperty("appNotificationViewed")
    public void setAppNotificationViewed(List<BOApp> value) { this.appNotificationViewed = value; }

    @JsonProperty("appNotificationClicked")
    public List<BOApp> getAppNotificationClicked() { return appNotificationClicked; }
    @JsonProperty("appNotificationClicked")
    public void setAppNotificationClicked(List<BOApp> value) { this.appNotificationClicked = value; }

    @JsonProperty("appSessionInfo")
    public List<BOSessionInfo> getAppSessionInfo() { return appSessionInfo; }
    @JsonProperty("appSessionInfo")
    public void setAppSessionInfo(List<BOSessionInfo> value) { this.appSessionInfo = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAppStates fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAppStates fromJsonDictionary(HashMap<String,Object> jsonDict) {
       try{
           String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
            return null;
        }

    public static String toJsonString(BOAppStates obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAppStates.class);
        writer = mapper.writerFor(BOAppStates.class);
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
