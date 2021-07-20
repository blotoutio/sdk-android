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

public class BORetentionEvent {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BORetentionEvent";
    private boolean sentToServer;
    private BODau dau;
    private BOWau wau;
    private BOMau mau;
    private BODpu dpu;
    private BOWpu wpu;
    private BOMpu mpu;
    private BOAppInstalled appInstalled;
    private BONewUser newUser;
    private BOAST dast;
    private BOAST wast;
    private BOAST mast;
    private BOCustomEvents customEvents;

    @JsonProperty("sentToServer")
    public boolean getSentToServer() { return sentToServer; }
    @JsonProperty("sentToServer")
    public void setSentToServer(boolean value) { this.sentToServer = value; }

    @JsonProperty("DAU")
    public BODau getDau() { return dau; }
    @JsonProperty("DAU")
    public void setDau(BODau value) { this.dau = value; }

    @JsonProperty("WAU")
    public BOWau getWau() { return wau; }
    @JsonProperty("WAU")
    public void setWau(BOWau value) { this.wau = value; }

    @JsonProperty("MAU")
    public BOMau getMau() { return mau; }
    @JsonProperty("MAU")
    public void setMau(BOMau value) { this.mau = value; }

    @JsonProperty("DPU")
    public BODpu getDpu() { return dpu; }
    @JsonProperty("DPU")
    public void setDpu(BODpu value) { this.dpu = value; }

    @JsonProperty("WPU")
    public BOWpu getWpu() { return wpu; }
    @JsonProperty("WPU")
    public void setWpu(BOWpu value) { this.wpu = value; }

    @JsonProperty("MPU")
    public BOMpu getMpu() { return mpu; }
    @JsonProperty("MPU")
    public void setMpu(BOMpu value) { this.mpu = value; }

    @JsonProperty("appInstalled")
    public BOAppInstalled getAppInstalled() { return appInstalled; }
    @JsonProperty("appInstalled")
    public void setAppInstalled(BOAppInstalled value) { this.appInstalled = value; }

    @JsonProperty("newUser")
    public BONewUser getNewUser() { return newUser; }
    @JsonProperty("newUser")
    public void setNewUser(BONewUser value) { this.newUser = value; }

    @JsonProperty("DAST")
    public BOAST getDast() { return dast; }
    @JsonProperty("DAST")
    public void setDast(BOAST value) { this.dast = value; }

    @JsonProperty("WAST")
    public BOAST getWast() { return wast; }
    @JsonProperty("WAST")
    public void setWast(BOAST value) { this.wast = value; }

    @JsonProperty("MAST")
    public BOAST getMast() { return mast; }
    @JsonProperty("MAST")
    public void setMast(BOAST value) { this.mast = value; }

    @JsonProperty("customEvents")
    public BOCustomEvents getCustomEvents() { return customEvents; }
    @JsonProperty("customEvents")
    public void setCustomEvents(BOCustomEvents value) { this.customEvents = value; }

    /*
        Model to json and vice-versa Conversion Methods
     */

    public static BORetentionEvent fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BORetentionEvent fromJsonDictionary(HashMap<String,Object> jsonDict) throws IOException {
       try {
           String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
    }catch (IOException e) {
        Logger.INSTANCE.e(TAG, e.toString());
    }
        return null;
    }
    public static String toJsonString(BORetentionEvent obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BORetentionEvent.class);
        writer = mapper.writerFor(BORetentionEvent.class);
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
