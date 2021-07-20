package com.blotout.model.FunnelAndCodifiedEvents.EventsGetRequest;

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

@JsonIgnoreProperties(ignoreUnknown = true)
public class BOAMetaEventsGet {
    private static final String TAG = "BOAMetaEventsGet";

    private long plf;
    private String appn;
    private String osv;
    private String osn;
    private String dmft;
    private String dm;
    private boolean acomp;
    private boolean dcomp;
    private String appv;
    private boolean jbrkn;
    private boolean vpn;



    @JsonProperty("appv")
    public String getAppv() { return appv; }
    @JsonProperty("appv")
    public void setAppv(String value) { this.appv = value; }

    @JsonProperty("jbrkn")
    public boolean getJbrkn() { return jbrkn; }
    @JsonProperty("jbrkn")
    public void setJbrkn(boolean value) { this.jbrkn = value; }

    @JsonProperty("vpn")
    public boolean getVpn() { return vpn; }
    @JsonProperty("vpn")
    public void setVpn(boolean value) { this.vpn = value; }

    @JsonProperty("plf")
    public long getPlf() { return plf; }
    @JsonProperty("plf")
    public void setPlf(long value) { this.plf = value; }

    @JsonProperty("acomp")
    public boolean getAcomp() { return acomp; }
    @JsonProperty("acomp")
    public void setAcomp(boolean value) { this.acomp = value; }

    @JsonProperty("dcomp")
    public boolean getDcomp() { return dcomp; }
    @JsonProperty("dcomp")
    public void setDcomp(boolean value) { this.dcomp = value; }

    @JsonProperty("appn")
    public String getAppn() { return appn; }
    @JsonProperty("appn")
    public void setAppn(String value) { this.appn = value; }

    @JsonProperty("osv")
    public String getOsv() { return osv; }
    @JsonProperty("osv")
    public void setOsv(String value) { this.osv = value; }

    @JsonProperty("osn")
    public String getOsn() { return osn; }
    @JsonProperty("osn")
    public void setOsn(String value) { this.osn = value; }

    @JsonProperty("dmft")
    public String getDmft() { return dmft; }
    @JsonProperty("dmft")
    public void setDmft(String value) { this.dmft = value; }

    @JsonProperty("dm")
    public String getDm() { return dm; }
    @JsonProperty("dm")
    public void setDm(String value) { this.dm = value; }


    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAMetaEventsGet fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAMetaEventsGet fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }
    public static String toJsonString(BOAMetaEventsGet obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAMetaEventsGet.class);
        writer = mapper.writerFor(BOAMetaEventsGet.class);
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
