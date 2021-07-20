package com.blotout.model.session;

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

public class BOUbiAutoDetected {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOUbiAutoDetected";

    private List<BOScreenShotsTaken> screenShotsTaken;
    private List<BOAppNavigation> appNavigation;
    private BOAppGesture appGesture;

    @JsonProperty("screenShotsTaken")
    public List<BOScreenShotsTaken> getScreenShotsTaken() { return screenShotsTaken; }
    @JsonProperty("screenShotsTaken")
    public void setScreenShotsTaken(List<BOScreenShotsTaken> value) { this.screenShotsTaken = value; }

    @JsonProperty("appNavigation")
    public List<BOAppNavigation> getAppNavigation() { return appNavigation; }
    @JsonProperty("appNavigation")
    public void setAppNavigation(List<BOAppNavigation> value) { this.appNavigation = value; }

    @JsonProperty("appGesture")
    public BOAppGesture getAppGesture() { return appGesture; }
    @JsonProperty("appGesture")
    public void setAppGesture(BOAppGesture value) { this.appGesture = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOUbiAutoDetected fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOUbiAutoDetected fromJsonDictionary(HashMap<String,Object> jsonDict) {
       try{
           String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BOUbiAutoDetected obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOUbiAutoDetected.class);
        writer = mapper.writerFor(BOUbiAutoDetected.class);
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
