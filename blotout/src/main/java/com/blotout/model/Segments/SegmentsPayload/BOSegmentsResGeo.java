package com.blotout.model.Segments.SegmentsPayload;

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

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOSegmentsResGeo {
    private static final String TAG = "BOSegmentsResGeo";

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
    public long getZip() { return zip; }
    @JsonProperty("zip")
    public void setZip(long value) { this.zip = value; }

    @JsonProperty("lat")
    public double getLat() { return lat; }
    @JsonProperty("lat")
    public void setLat(double value) { this.lat = value; }

    @JsonProperty("long")
    public double getGeoLong() { return geoLong; }
    @JsonProperty("long")
    public void setGeoLong(double value) { this.geoLong = value; }

       /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOSegmentsResGeo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOSegmentsResGeo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOSegmentsResGeo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOSegmentsResGeo.class);
        writer = mapper.writerFor(BOSegmentsResGeo.class);
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
