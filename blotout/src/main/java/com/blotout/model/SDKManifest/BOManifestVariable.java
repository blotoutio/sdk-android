package com.blotout.model.SDKManifest;

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

/**
 * Created by Blotout on 22,February,2020
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOManifestVariable {

    private static final String TAG = "BOManifestVariable";

    private long variableId;
    private String value;
    private int variableDataType;
    private String  variableName;
    private boolean isEditable;

    @JsonProperty("variableId")
    public long getVariableId() { return variableId;}
    @JsonProperty("variableId")
    public void setVariableId(long variableId) { this.variableId = variableId; }

    @JsonProperty("value")
    public String getValue() { return value; }
    @JsonProperty("value")
    public void setValue(String value) { this.value = value; }

    @JsonProperty("variableName")
    public String getVariableName() { return variableName; }
    @JsonProperty("variableName")
    public void setVariableName(String variableName) { this.variableName = variableName; }

    @JsonProperty("variableDataType")
    public int getVariableDataType() { return variableDataType; }
    @JsonProperty("variableDataType")
    public void setVariableDataType(int variableDataType) { this.variableDataType = variableDataType; }

    @JsonProperty("isEditable")
    public boolean getIsEditable() { return isEditable; }
    @JsonProperty("isEditable")
    public void setIsEditable(boolean isEditable) { this.isEditable = isEditable; }

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
    public static BOManifestVariable fromJsonDictionary(HashMap<String,Object> jsonDict) {
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
    public static String toJsonString(BOManifestVariable obj)  {
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
        reader = mapper.reader(BOManifestVariable.class);
        writer = mapper.writerFor(BOManifestVariable.class);
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
