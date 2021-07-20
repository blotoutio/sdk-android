package com.blotout.analytics;
/**
 * Created by Blotout on 03,November,2019
 */

import androidx.annotation.Nullable;

import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.HashMap;

public class BOGeo {

    private static final String TAG = "BOGeo";

    private String conc;
    private String couc;
    private String reg;
    private String city;
    private long zip;
    private double lat;
    private double geoLong;

    @JsonProperty("conc")
    public String getConc() { return conc; }
    @JsonProperty("conc")
    public void setConc(String value) { this.conc = value; }

    @JsonProperty("couc")
    public String getCouc() { return couc; }
    @JsonProperty("couc")
    public void setCouc(String value) { this.couc = value; }

    @JsonProperty("reg")
    public String getReg() { return reg; }
    @JsonProperty("reg")
    public void setReg(String value) { this.reg = value; }

    @JsonProperty("city")
    public String getCity() { return city; }
    @JsonProperty("city")
    public void setCity(String value) { this.city = value; }

    @JsonProperty("zip")
    public long getOSVersion() { return zip; }
    @JsonProperty("zip")
    public void setOSVersion(long value) { this.zip = value; }

    @JsonProperty("lat")
    public double getLat() { return lat; }
    @JsonProperty("lat")
    public void setDeviceManufacturer(double value) { this.lat = value; }

    @JsonProperty("long")
    public double getDeviceModel() { return geoLong; }
    @JsonProperty("long")
    public void setDeviceModel(double value) { this.geoLong = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOGeo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOGeo fromJsonDictionary(HashMap<String,Object> jsonDict)  {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        } catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return  null;
    }

    public static String toJsonString(BOGeo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOGeo.class);
        writer = mapper.writerFor(BOGeo.class);
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

