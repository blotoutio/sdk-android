package com.blotout.model.session;

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

public class BODeveloperCodified {

    private static final String TAG = "BODeveloperCodified";

    private List<BODoubleTap> touchClick;
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
    private List<BOView> view;
    private List<BOAddToCart> addToCart;
    private List<BOChargeTransaction> chargeTransaction;
    private List<BOListUpdated> listUpdated;
    private List<BOTimedEvent> timedEvent;
    private List<BOCustomEvent> customEvent;
    private List<BOCustomEvent> piiEvent;
    private List<BOCustomEvent> phiEvent;

    @JsonProperty("touchClick")
    public List<BODoubleTap> getTouchClick() { return touchClick; }
    @JsonProperty("touchClick")
    public void setTouchClick(List<BODoubleTap> value) { this.touchClick = value; }

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

    @JsonProperty("view")
    public List<BOView> getView() { return view; }
    @JsonProperty("view")
    public void setView(List<BOView> value) { this.view = value; }

    @JsonProperty("addToCart")
    public List<BOAddToCart> getAddToCart() { return addToCart; }
    @JsonProperty("addToCart")
    public void setAddToCart(List<BOAddToCart> value) { this.addToCart = value; }

    @JsonProperty("chargeTransaction")
    public List<BOChargeTransaction> getChargeTransaction() { return chargeTransaction; }
    @JsonProperty("chargeTransaction")
    public void setChargeTransaction(List<BOChargeTransaction> value) { this.chargeTransaction = value; }

    @JsonProperty("listUpdated")
    public List<BOListUpdated> getListUpdated() { return listUpdated; }
    @JsonProperty("listUpdated")
    public void setListUpdated(List<BOListUpdated> value) { this.listUpdated = value; }

    @JsonProperty("timedEvent")
    public List<BOTimedEvent> getTimedEvent() { return timedEvent; }
    @JsonProperty("timedEvent")
    public void setTimedEvent(List<BOTimedEvent> value) { this.timedEvent = value; }

    @JsonProperty("customEvent")
    public List<BOCustomEvent> getCustomEvent() { return customEvent; }
    @JsonProperty("customEvent")
    public void setCustomEvent(List<BOCustomEvent> value) { this.customEvent = value; }

    @JsonProperty("piiEvent")
    public List<BOCustomEvent> getPiiEvent() { return piiEvent; }
    @JsonProperty("piiEvent")
    public void setPiiEvent(List<BOCustomEvent> value) { this.piiEvent = value; }

    @JsonProperty("phiEvent")
    public List<BOCustomEvent> getPhiEvent() { return phiEvent; }
    @JsonProperty("phiEvent")
    public void setPhiEvent(List<BOCustomEvent> value) { this.phiEvent = value; }

     /*
        Model to json and vice-versa Conversion Methods
     */

    public static BODeveloperCodified fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    @Nullable
    public static BODeveloperCodified fromJsonDictionary(HashMap<String,Object> jsonDict) {
        try {
            String jsonString = null;
            jsonString = BOCommonUtils.getJsonStringFromHashMap(jsonDict);
            return getObjectReader().readValue(jsonString);
        }catch (IOException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    public static String toJsonString(BODeveloperCodified obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(BODeveloperCodified.class);
        writer = mapper.writerFor(BODeveloperCodified.class);
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
