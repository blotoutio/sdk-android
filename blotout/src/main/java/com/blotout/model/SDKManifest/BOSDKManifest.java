package com.blotout.model.SDKManifest;

/**
 * Created by Blotout on 07,December,2019
 */
import androidx.annotation.Nullable;

import com.blotout.network.jobs.Priority;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BOSDKManifest {

    private static final String TAG = "BOSDKManifest";

    private List<BOManifestVariable> variables;

    @JsonProperty("variables")
    public List<BOManifestVariable> getVariables() { return variables; }
    @JsonProperty("variables")
    public void setVariable(List<BOManifestVariable> variables) { this.variables = variables; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    @Nullable
    public static BOSDKManifest fromJsonString(String json) {
        try {
            return getObjectReader().readValue(json);
        }catch (Exception e) {

        }
        return null;
    }

    @Nullable
    public static BOSDKManifest fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    @Nullable
    public static String toJsonString(BOSDKManifest obj)  {
        try {
            return getObjectWriter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return null;
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOSDKManifest.class);
        writer = mapper.writerFor(BOSDKManifest.class);
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

