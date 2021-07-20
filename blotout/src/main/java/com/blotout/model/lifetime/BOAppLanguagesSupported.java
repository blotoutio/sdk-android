package com.blotout.model.lifetime;

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

public class BOAppLanguagesSupported {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAppLanguagesSupported";
    private String languageName;
    private String languageCode;

    @JsonProperty("languageName")
    public String getLanguageName() { return languageName; }
    @JsonProperty("languageName")
    public void setLanguageName(String value) { this.languageName = value; }

    @JsonProperty("languageCode")
    public String getLanguageCode() { return languageCode; }
    @JsonProperty("languageCode")
    public void setLanguageCode(String value) { this.languageCode = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAppLanguagesSupported fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAppLanguagesSupported fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try{
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
           return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOAppLanguagesSupported obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAppLanguagesSupported.class);
        writer = mapper.writerFor(BOAppLanguagesSupported.class);
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
