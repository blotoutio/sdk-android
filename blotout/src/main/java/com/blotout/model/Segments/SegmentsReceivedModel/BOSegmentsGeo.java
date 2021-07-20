package com.blotout.model.Segments.SegmentsReceivedModel;

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
public class BOSegmentsGeo {

    private static final String TAG = "BOSegmentsGeo";

    private String continentCode;
    private String countryCode;
    private String regionCode;
    private String city;
    private long zip;
    private double latitude;
    private double longitude;

    @JsonProperty("conc")
    public String getContinentCode() { return continentCode; }
    @JsonProperty("conc")
    public void setContinentCode(String value) { this.continentCode = value; }

    @JsonProperty("couc")
    public String getCountryCode() { return countryCode; }
    @JsonProperty("couc")
    public void setCountryCode(String value) { this.countryCode = value; }

    @JsonProperty("reg")
    public String getRegionCode() { return regionCode; }
    @JsonProperty("reg")
    public void setRegionCode(String value) { this.regionCode = value; }

    @JsonProperty("city")
    public String getCity() { return city; }
    @JsonProperty("city")
    public void setCity(String value) { this.city = value; }

    @JsonProperty("zip")
    public long getZip() { return zip; }
    @JsonProperty("zip")
    public void setZip(long value) { this.zip = value; }

    @JsonProperty("lat")
    public double getLatitude() { return latitude; }
    @JsonProperty("lat")
    public void setLatitude(double value) { this.latitude = value; }

    @JsonProperty("long")
    public double getLongitude() { return longitude; }
    @JsonProperty("long")
    public void setLongitude(double value) { this.longitude = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOSegmentsGeo fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOSegmentsGeo fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOSegmentsGeo obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOSegmentsGeo.class);
        writer = mapper.writerFor(BOSegmentsGeo.class);
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
