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

public class BOAppGesture {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOAppGesture";

    private List<BODoubleTap> touchOrClick;
    private List<BODoubleTap> drag;
    private List<BODoubleTap> flick;
    private List<BODoubleTap> swipe;
    private List<BODoubleTap> doubleTap;
    private List<BODoubleTap> moreThanDoubleTap;
    private List<BODoubleTap> twoFingerTap;
    private List<BODoubleTap> moreThanTwoFingerTap;
    private List<BODoubleTap> pinch;
    private List<BODoubleTap> touchAndHold;
    private List<BODoubleTap> shake;
    private List<BODoubleTap> rotate;
    private List<BOScreenEdgePan> screenEdgePan;

    @JsonProperty("touchOrClick")
    public List<BODoubleTap> getTouchOrClick() { return touchOrClick; }
    @JsonProperty("touchOrClick")
    public void setTouchOrClick(List<BODoubleTap> value) { this.touchOrClick = value; }

    @JsonProperty("drag")
    public List<BODoubleTap> getDrag() { return drag; }
    @JsonProperty("drag")
    public void setDrag(List<BODoubleTap> value) { this.drag = value; }

    @JsonProperty("flick")
    public List<BODoubleTap> getFlick() { return flick; }
    @JsonProperty("flick")
    public void setFlick(List<BODoubleTap> value) { this.flick = value; }

    @JsonProperty("swipe")
    public List<BODoubleTap> getSwipe() { return swipe; }
    @JsonProperty("swipe")
    public void setSwipe(List<BODoubleTap> value) { this.swipe = value; }

    @JsonProperty("doubleTap")
    public List<BODoubleTap> getDoubleTap() { return doubleTap; }
    @JsonProperty("doubleTap")
    public void setDoubleTap(List<BODoubleTap> value) { this.doubleTap = value; }

    @JsonProperty("moreThanDoubleTap")
    public List<BODoubleTap> getMoreThanDoubleTap() { return moreThanDoubleTap; }
    @JsonProperty("moreThanDoubleTap")
    public void setMoreThanDoubleTap(List<BODoubleTap> value) { this.moreThanDoubleTap = value; }

    @JsonProperty("twoFingerTap")
    public List<BODoubleTap> getTwoFingerTap() { return twoFingerTap; }
    @JsonProperty("twoFingerTap")
    public void setTwoFingerTap(List<BODoubleTap> value) { this.twoFingerTap = value; }

    @JsonProperty("moreThanTwoFingerTap")
    public List<BODoubleTap> getMoreThanTwoFingerTap() { return moreThanTwoFingerTap; }
    @JsonProperty("moreThanTwoFingerTap")
    public void setMoreThanTwoFingerTap(List<BODoubleTap> value) { this.moreThanTwoFingerTap = value; }

    @JsonProperty("pinch")
    public List<BODoubleTap> getPinch() { return pinch; }
    @JsonProperty("pinch")
    public void setPinch(List<BODoubleTap> value) { this.pinch = value; }

    @JsonProperty("touchAndHold")
    public List<BODoubleTap> getTouchAndHold() { return touchAndHold; }
    @JsonProperty("touchAndHold")
    public void setTouchAndHold(List<BODoubleTap> value) { this.touchAndHold = value; }

    @JsonProperty("shake")
    public List<BODoubleTap> getShake() { return shake; }
    @JsonProperty("shake")
    public void setShake(List<BODoubleTap> value) { this.shake = value; }

    @JsonProperty("rotate")
    public List<BODoubleTap> getRotate() { return rotate; }
    @JsonProperty("rotate")
    public void setRotate(List<BODoubleTap> value) { this.rotate = value; }

    @JsonProperty("screenEdgePan")
    public List<BOScreenEdgePan> getScreenEdgePan() { return screenEdgePan; }
    @JsonProperty("screenEdgePan")
    public void setScreenEdgePan(List<BOScreenEdgePan> value) { this.screenEdgePan = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BOAppGesture fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BOAppGesture fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
        jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
        return getObjectReader().readValue(jsonString);
    }catch (IOException e) {
        Logger.INSTANCE.e(TAG, e.toString());
    }
        return null;
    }

    public static String toJsonString(BOAppGesture obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BOAppGesture.class);
        writer = mapper.writerFor(BOAppGesture.class);
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
