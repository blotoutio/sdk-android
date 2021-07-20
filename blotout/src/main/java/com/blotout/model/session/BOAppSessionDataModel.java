package com.blotout.model.session;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.HashMap;

import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class BOAppSessionDataModel {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAppSessionDataModel";

    private String appBundle;
    private String date;
    private BOSingleDaySessions singleDaySessions;
    @Nullable
    private static BOAppSessionDataModel mBOASessionModelSharedInstance = null;

    @JsonProperty("appBundle")
    public String getAppBundle() { return appBundle; }
    @JsonProperty("appBundle")
    public void setAppBundle(String value) { this.appBundle = value; }

    @JsonProperty("date")
    public String getDate() { return date; }
    @JsonProperty("date")
    public void setDate(String value) { this.date = value; }

    @JsonProperty("singleDaySessions")
    public BOSingleDaySessions getSingleDaySessions() { return singleDaySessions; }
    @JsonProperty("singleDaySessions")
    public void setSingleDaySessions(BOSingleDaySessions value) { this.singleDaySessions = value; }

    @Nullable
    public static BOAppSessionDataModel sharedInstanceFromJSONDictionary(@Nullable HashMap<String,Object> dict) {

        if (dict== null) {
            return mBOASessionModelSharedInstance;
        }
        if(mBOASessionModelSharedInstance ==  null) {
            try {
                mBOASessionModelSharedInstance = BOAppSessionDataModel.fromJsonDictionary(dict);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  mBOASessionModelSharedInstance;
    }

    @Nullable
    public static BOAppSessionDataModel sharedInstanceFromJSONString(@Nullable String jsonString) {

        if (jsonString== null) {
            return mBOASessionModelSharedInstance;
        }
        if(mBOASessionModelSharedInstance ==  null) {
            try {
                mBOASessionModelSharedInstance = BOAppSessionDataModel.fromJsonString(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  mBOASessionModelSharedInstance;
    }

    public static void resetDailySessionSharedInstanceToken(){
        mBOASessionModelSharedInstance = null;
    }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAppSessionDataModel fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAppSessionDataModel fromJsonDictionary(HashMap<String,Object> jsonDict) throws IOException {
        String jsonString = null;
        try{
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        } catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOAppSessionDataModel obj) {
       String jsonString = "";
        try {
            return getObjectWriter().writeValueAsString(obj);
        } catch(JsonProcessingException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return jsonString;
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAppSessionDataModel.class);
        writer = mapper.writerFor(BOAppSessionDataModel.class);
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
