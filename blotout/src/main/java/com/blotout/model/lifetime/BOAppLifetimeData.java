package com.blotout.model.lifetime;

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

public class BOAppLifetimeData {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAppSessionDataModel";

    @Nullable
    private static BOAppLifetimeData mBOLifetimeModelSharedInstance = null;

    private String appBundle;
    private String appID;
    private String date;
    private long lastServerSyncTimeStamp;
    private long allEventsSyncTimeStamp;

    private List<BOAppLifeTimeInfo> appLifeTimeInfo;

    @JsonProperty("appBundle")
    public String getAppBundle() {
        return appBundle;
    }

    @JsonProperty("appBundle")
    public void setAppBundle(String value) {
        this.appBundle = value;
    }

    @JsonProperty("appID")
    public String getAppID() {
        return appID;
    }

    @JsonProperty("appID")
    public void setAppID(String value) {
        this.appID = value;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String value) {
        this.date = value;
    }

    @JsonProperty("lastServerSyncTimeStamp")
    public long getLastServerSyncTimeStamp() {
        return lastServerSyncTimeStamp;
    }

    @JsonProperty("lastServerSyncTimeStamp")
    public void setLastServerSyncTimeStamp(long value) {
        this.lastServerSyncTimeStamp = value;
    }


    @JsonProperty("allEventsSyncTimeStamp")
    public long getAllEventsSyncTimeStamp() {
        return allEventsSyncTimeStamp;
    }

    @JsonProperty("allEventsSyncTimeStamp")
    public void setAllEventsSyncTimeStamp(long value) {
        this.allEventsSyncTimeStamp = value;
    }


    @JsonProperty("appLifeTimeInfo")
    public List<BOAppLifeTimeInfo> getAppLifeTimeInfo() {
        return appLifeTimeInfo;
    }

    @JsonProperty("appLifeTimeInfo")
    public void setAppLifeTimeInfo(List<BOAppLifeTimeInfo> value) {
        this.appLifeTimeInfo = value;
    }


    @Nullable
    public static BOAppLifetimeData sharedInstanceFromJSONDictionary(@Nullable HashMap<String,Object> dict) {

        if (dict== null) {
            return mBOLifetimeModelSharedInstance;
        }
        if(mBOLifetimeModelSharedInstance ==  null) {
            try {
                mBOLifetimeModelSharedInstance = BOAppLifetimeData.fromJsonDictionary(dict);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  mBOLifetimeModelSharedInstance;
    }

    @Nullable
    public static BOAppLifetimeData sharedInstanceFromJSONString(@Nullable String jsonString) {

        if (jsonString== null) {
            return mBOLifetimeModelSharedInstance;
        }
        if(mBOLifetimeModelSharedInstance ==  null) {
            try {
                mBOLifetimeModelSharedInstance = BOAppLifetimeData.fromJsonString(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  mBOLifetimeModelSharedInstance;
    }

    public  static void resetLifeTimeSharedInstanceToken(){
        mBOLifetimeModelSharedInstance = null;
    }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAppLifetimeData fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAppLifetimeData fromJsonDictionary(HashMap<String,Object> jsonDict) throws IOException {
        String jsonString = null;
        try{
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        } catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOAppLifetimeData obj) {
        String jsonString = "";
        try {
            return getObjectWriter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return jsonString;
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAppLifetimeData.class);
        writer = mapper.writerFor(BOAppLifetimeData.class);
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
