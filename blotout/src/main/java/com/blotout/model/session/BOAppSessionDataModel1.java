//// Converter.java
//
//// To use this code, add the following Maven dependency to your project:
////
////     com.fasterxml.jackson.core : jackson-databind : 2.9.0
////
//// Import this package:
////
////     import com.blotout.model.AppSessionDataModel.Converter;
////
//// Then you can deserialize a JSON string with
////
////     Empty data = Converter.fromJsonString(jsonString);
//
//package com.blotout.model.session;
//import java.util.*;
//import java.io.IOException;
//import com.fasterxml.jackson.databind.*;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.annotation.*;
//
///**
// * Created by Blotout on 22,October,2019
// */
//
//class Converter {
//    // Serialize/deserialize helpers
//
//    public static BOAppSessionDataModel1 fromJsonString(String json) throws IOException {
//        return getObjectReader().readValue(json);
//    }
//
//    public static String toJsonString(BOAppSessionDataModel1 obj) throws JsonProcessingException {
//        return getObjectWriter().writeValueAsString(obj);
//    }
//
//    private static ObjectReader reader;
//    private static ObjectWriter writer;
//
//    private static void instantiateMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        reader = mapper.reader(BOAppSessionDataModel1.class);
//        writer = mapper.writerFor(BOAppSessionDataModel1.class);
//    }
//
//    private static ObjectReader getObjectReader() {
//        if (reader == null) instantiateMapper();
//        return reader;
//    }
//
//    private static ObjectWriter getObjectWriter() {
//        if (writer == null) instantiateMapper();
//        return writer;
//    }
//}
//
//// Empty.java
//public class BOAppSessionDataModel1 {
//
//    public static BOAppSessionDataModel1 fromJsonString(String json) throws IOException {
//        return getObjectReader().readValue(json);
//    }
//
//    public static String toJsonString(BOAppSessionDataModel1 obj) throws JsonProcessingException {
//        return getObjectWriter().writeValueAsString(obj);
//    }
//
//    private static ObjectReader reader;
//    private static ObjectWriter writer;
//
//    private static void instantiateMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        reader = mapper.reader(BOAppSessionDataModel1.class);
//        writer = mapper.writerFor(BOAppSessionDataModel1.class);
//    }
//
//    private static ObjectReader getObjectReader() {
//        if (reader == null) instantiateMapper();
//        return reader;
//    }
//
//    private static ObjectWriter getObjectWriter() {
//        if (writer == null) instantiateMapper();
//        return writer;
//    }
//
//    private String appBundle;
//    private String date;
//    private SingleDaySessions singleDaySessions;
//
//    @JsonProperty("appBundle")
//    public String getAppBundle() { return appBundle; }
//    @JsonProperty("appBundle")
//    public void setAppBundle(String value) { this.appBundle = value; }
//
//    @JsonProperty("date")
//    public String getDate() { return date; }
//    @JsonProperty("date")
//    public void setDate(String value) { this.date = value; }
//
//    @JsonProperty("singleDaySessions")
//    public SingleDaySessions getSingleDaySessions() { return singleDaySessions; }
//    @JsonProperty("singleDaySessions")
//    public void setSingleDaySessions(SingleDaySessions value) { this.singleDaySessions = value; }
//}
//
//// BOSingleDaySessions.java
//class SingleDaySessions {
//    private boolean sentToServer;
//    private List<Long> systemUptime;
//    private long lastServerSyncTimeStamp;
//    private long allEventsSyncTimeStamp;
//    private List<AppInfo> appInfo;
//    private UbiAutoDetected ubiAutoDetected;
//    private DeveloperCodified developerCodified;
//    private AppStates appStates;
//    private DeviceInfo deviceInfo;
//    private NetworkInfo networkInfo;
//    private List<StorageInfo> storageInfo;
//    private List<MemoryInfo> memoryInfo;
//    private List<Location> location;
//    private List<CrashDetail> crashDetails;
//    private RetentionEvent retentionEvent;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("systemUptime")
//    public List<Long> getSystemUptime() { return systemUptime; }
//    @JsonProperty("systemUptime")
//    public void setSystemUptime(List<Long> value) { this.systemUptime = value; }
//
//    @JsonProperty("lastServerSyncTimeStamp")
//    public long getLastServerSyncTimeStamp() { return lastServerSyncTimeStamp; }
//    @JsonProperty("lastServerSyncTimeStamp")
//    public void setLastServerSyncTimeStamp(long value) { this.lastServerSyncTimeStamp = value; }
//
//    @JsonProperty("allEventsSyncTimeStamp")
//    public long getAllEventsSyncTimeStamp() { return allEventsSyncTimeStamp; }
//    @JsonProperty("allEventsSyncTimeStamp")
//    public void setAllEventsSyncTimeStamp(long value) { this.allEventsSyncTimeStamp = value; }
//
//    @JsonProperty("appInfo")
//    public List<AppInfo> getAppInfo() { return appInfo; }
//    @JsonProperty("appInfo")
//    public void setAppInfo(List<AppInfo> value) { this.appInfo = value; }
//
//    @JsonProperty("ubiAutoDetected")
//    public UbiAutoDetected getUbiAutoDetected() { return ubiAutoDetected; }
//    @JsonProperty("ubiAutoDetected")
//    public void setUbiAutoDetected(UbiAutoDetected value) { this.ubiAutoDetected = value; }
//
//    @JsonProperty("developerCodified")
//    public DeveloperCodified getDeveloperCodified() { return developerCodified; }
//    @JsonProperty("developerCodified")
//    public void setDeveloperCodified(DeveloperCodified value) { this.developerCodified = value; }
//
//    @JsonProperty("appStates")
//    public AppStates getAppStates() { return appStates; }
//    @JsonProperty("appStates")
//    public void setAppStates(AppStates value) { this.appStates = value; }
//
//    @JsonProperty("deviceInfo")
//    public DeviceInfo getDeviceInfo() { return deviceInfo; }
//    @JsonProperty("deviceInfo")
//    public void setDeviceInfo(DeviceInfo value) { this.deviceInfo = value; }
//
//    @JsonProperty("networkInfo")
//    public NetworkInfo getNetworkInfo() { return networkInfo; }
//    @JsonProperty("networkInfo")
//    public void setNetworkInfo(NetworkInfo value) { this.networkInfo = value; }
//
//    @JsonProperty("storageInfo")
//    public List<StorageInfo> getStorageInfo() { return storageInfo; }
//    @JsonProperty("storageInfo")
//    public void setStorageInfo(List<StorageInfo> value) { this.storageInfo = value; }
//
//    @JsonProperty("memoryInfo")
//    public List<MemoryInfo> getMemoryInfo() { return memoryInfo; }
//    @JsonProperty("memoryInfo")
//    public void setMemoryInfo(List<MemoryInfo> value) { this.memoryInfo = value; }
//
//    @JsonProperty("location")
//    public List<Location> getLocation() { return location; }
//    @JsonProperty("location")
//    public void setLocation(List<Location> value) { this.location = value; }
//
//    @JsonProperty("crashDetails")
//    public List<CrashDetail> getCrashDetails() { return crashDetails; }
//    @JsonProperty("crashDetails")
//    public void setCrashDetails(List<CrashDetail> value) { this.crashDetails = value; }
//
//    @JsonProperty("retentionEvent")
//    public RetentionEvent getRetentionEvent() { return retentionEvent; }
//    @JsonProperty("retentionEvent")
//    public void setRetentionEvent(RetentionEvent value) { this.retentionEvent = value; }
//}
//
//// BOSessionAppInfo.java
//class AppInfo {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String version;
//    private String sdkVersion;
//    private String name;
//    private String bundle;
//    private String language;
//    private long launchTimeStamp;
//    private long terminationTimeStamp;
//    private long sessionsDuration;
//    private long averageSessionsDuration;
//    private String launchReason;
//    private CurrentLocation currentLocation;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("version")
//    public String getVersion() { return version; }
//    @JsonProperty("version")
//    public void setVersion(String value) { this.version = value; }
//
//    @JsonProperty("sdkVersion")
//    public String getSDKVersion() { return sdkVersion; }
//    @JsonProperty("sdkVersion")
//    public void setSDKVersion(String value) { this.sdkVersion = value; }
//
//    @JsonProperty("name")
//    public String getName() { return name; }
//    @JsonProperty("name")
//    public void setName(String value) { this.name = value; }
//
//    @JsonProperty("bundle")
//    public String getBundle() { return bundle; }
//    @JsonProperty("bundle")
//    public void setBundle(String value) { this.bundle = value; }
//
//    @JsonProperty("language")
//    public String getLanguage() { return language; }
//    @JsonProperty("language")
//    public void setLanguage(String value) { this.language = value; }
//
//    @JsonProperty("launchTimeStamp")
//    public long getLaunchTimeStamp() { return launchTimeStamp; }
//    @JsonProperty("launchTimeStamp")
//    public void setLaunchTimeStamp(long value) { this.launchTimeStamp = value; }
//
//    @JsonProperty("terminationTimeStamp")
//    public long getTerminationTimeStamp() { return terminationTimeStamp; }
//    @JsonProperty("terminationTimeStamp")
//    public void setTerminationTimeStamp(long value) { this.terminationTimeStamp = value; }
//
//    @JsonProperty("sessionsDuration")
//    public long getSessionsDuration() { return sessionsDuration; }
//    @JsonProperty("sessionsDuration")
//    public void setSessionsDuration(long value) { this.sessionsDuration = value; }
//
//    @JsonProperty("averageSessionsDuration")
//    public long getAverageSessionsDuration() { return averageSessionsDuration; }
//    @JsonProperty("averageSessionsDuration")
//    public void setAverageSessionsDuration(long value) { this.averageSessionsDuration = value; }
//
//    @JsonProperty("launchReason")
//    public String getLaunchReason() { return launchReason; }
//    @JsonProperty("launchReason")
//    public void setLaunchReason(String value) { this.launchReason = value; }
//
//    @JsonProperty("currentLocation")
//    public CurrentLocation getCurrentLocation() { return currentLocation; }
//    @JsonProperty("currentLocation")
//    public void setCurrentLocation(CurrentLocation value) { this.currentLocation = value; }
//}
//
//// CurrentLocation.java
//class CurrentLocation {
//    private String city;
//    private String state;
//    private String country;
//    private String zip;
//
//    @JsonProperty("city")
//    public String getCity() { return city; }
//    @JsonProperty("city")
//    public void setCity(String value) { this.city = value; }
//
//    @JsonProperty("state")
//    public String getState() { return state; }
//    @JsonProperty("state")
//    public void setState(String value) { this.state = value; }
//
//    @JsonProperty("country")
//    public String getCountry() { return country; }
//    @JsonProperty("country")
//    public void setCountry(String value) { this.country = value; }
//
//    @JsonProperty("zip")
//    public String getZip() { return zip; }
//    @JsonProperty("zip")
//    public void setZip(String value) { this.zip = value; }
//}
//
//// BOAppStates.java
//class AppStates {
//    private boolean sentToServer;
//    private List<App> appLaunched;
//    private List<App> appActive;
//    private List<App> appResignActive;
//    private List<App> appInBackground;
//    private List<App> appInForeground;
//    private List<App> appBackgroundRefreshAvailable;
//    private List<App> appReceiveMemoryWarning;
//    private List<App> appSignificantTimeChange;
//    private List<App> appOrientationPortrait;
//    private List<App> appOrientationLandscape;
//    private List<App> appStatusbarFrameChange;
//    private List<App> appBackgroundRefreshStatusChange;
//    private List<App> appNotificationReceived;
//    private List<App> appNotificationViewed;
//    private List<App> appNotificationClicked;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("appLaunched")
//    public List<App> getAppLaunched() { return appLaunched; }
//    @JsonProperty("appLaunched")
//    public void setAppLaunched(List<App> value) { this.appLaunched = value; }
//
//    @JsonProperty("appActive")
//    public List<App> getAppActive() { return appActive; }
//    @JsonProperty("appActive")
//    public void setAppActive(List<App> value) { this.appActive = value; }
//
//    @JsonProperty("appResignActive")
//    public List<App> getAppResignActive() { return appResignActive; }
//    @JsonProperty("appResignActive")
//    public void setAppResignActive(List<App> value) { this.appResignActive = value; }
//
//    @JsonProperty("appInBackground")
//    public List<App> getAppInBackground() { return appInBackground; }
//    @JsonProperty("appInBackground")
//    public void setAppInBackground(List<App> value) { this.appInBackground = value; }
//
//    @JsonProperty("appInForeground")
//    public List<App> getAppInForeground() { return appInForeground; }
//    @JsonProperty("appInForeground")
//    public void setAppInForeground(List<App> value) { this.appInForeground = value; }
//
//    @JsonProperty("appBackgroundRefreshAvailable")
//    public List<App> getAppBackgroundRefreshAvailable() { return appBackgroundRefreshAvailable; }
//    @JsonProperty("appBackgroundRefreshAvailable")
//    public void setAppBackgroundRefreshAvailable(List<App> value) { this.appBackgroundRefreshAvailable = value; }
//
//    @JsonProperty("appReceiveMemoryWarning")
//    public List<App> getAppReceiveMemoryWarning() { return appReceiveMemoryWarning; }
//    @JsonProperty("appReceiveMemoryWarning")
//    public void setAppReceiveMemoryWarning(List<App> value) { this.appReceiveMemoryWarning = value; }
//
//    @JsonProperty("appSignificantTimeChange")
//    public List<App> getAppSignificantTimeChange() { return appSignificantTimeChange; }
//    @JsonProperty("appSignificantTimeChange")
//    public void setAppSignificantTimeChange(List<App> value) { this.appSignificantTimeChange = value; }
//
//    @JsonProperty("appOrientationPortrait")
//    public List<App> getAppOrientationPortrait() { return appOrientationPortrait; }
//    @JsonProperty("appOrientationPortrait")
//    public void setAppOrientationPortrait(List<App> value) { this.appOrientationPortrait = value; }
//
//    @JsonProperty("appOrientationLandscape")
//    public List<App> getAppOrientationLandscape() { return appOrientationLandscape; }
//    @JsonProperty("appOrientationLandscape")
//    public void setAppOrientationLandscape(List<App> value) { this.appOrientationLandscape = value; }
//
//    @JsonProperty("appStatusbarFrameChange")
//    public List<App> getAppStatusbarFrameChange() { return appStatusbarFrameChange; }
//    @JsonProperty("appStatusbarFrameChange")
//    public void setAppStatusbarFrameChange(List<App> value) { this.appStatusbarFrameChange = value; }
//
//    @JsonProperty("appBackgroundRefreshStatusChange")
//    public List<App> getAppBackgroundRefreshStatusChange() { return appBackgroundRefreshStatusChange; }
//    @JsonProperty("appBackgroundRefreshStatusChange")
//    public void setAppBackgroundRefreshStatusChange(List<App> value) { this.appBackgroundRefreshStatusChange = value; }
//
//    @JsonProperty("appNotificationReceived")
//    public List<App> getAppNotificationReceived() { return appNotificationReceived; }
//    @JsonProperty("appNotificationReceived")
//    public void setAppNotificationReceived(List<App> value) { this.appNotificationReceived = value; }
//
//    @JsonProperty("appNotificationViewed")
//    public List<App> getAppNotificationViewed() { return appNotificationViewed; }
//    @JsonProperty("appNotificationViewed")
//    public void setAppNotificationViewed(List<App> value) { this.appNotificationViewed = value; }
//
//    @JsonProperty("appNotificationClicked")
//    public List<App> getAppNotificationClicked() { return appNotificationClicked; }
//    @JsonProperty("appNotificationClicked")
//    public void setAppNotificationClicked(List<App> value) { this.appNotificationClicked = value; }
//}
//
//// BOApp.java
//
//class App {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String visibleClassName;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("visibleClassName")
//    public String getVisibleClassName() { return visibleClassName; }
//    @JsonProperty("visibleClassName")
//    public void setVisibleClassName(String value) { this.visibleClassName = value; }
//}
//
//// CrashDetail.java
//class CrashDetail {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String name;
//    private String reason;
//    private Info info;
//    private List<String> callStackSymbols;
//    private List<Long> callStackReturnAddress;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("name")
//    public String getName() { return name; }
//    @JsonProperty("name")
//    public void setName(String value) { this.name = value; }
//
//    @JsonProperty("reason")
//    public String getReason() { return reason; }
//    @JsonProperty("reason")
//    public void setReason(String value) { this.reason = value; }
//
//    @JsonProperty("info")
//    public Info getInfo() { return info; }
//    @JsonProperty("info")
//    public void setInfo(Info value) { this.info = value; }
//
//    @JsonProperty("callStackSymbols")
//    public List<String> getCallStackSymbols() { return callStackSymbols; }
//    @JsonProperty("callStackSymbols")
//    public void setCallStackSymbols(List<String> value) { this.callStackSymbols = value; }
//
//    @JsonProperty("callStackReturnAddress")
//    public List<Long> getCallStackReturnAddress() { return callStackReturnAddress; }
//    @JsonProperty("callStackReturnAddress")
//    public void setCallStackReturnAddress(List<Long> value) { this.callStackReturnAddress = value; }
//}
//
//// BOInfo.java
//@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
//class Info {
//}
//
//// BODeveloperCodified.java
//class DeveloperCodified {
//    private List<DoubleTap> touchClick;
//    private List<DoubleTap> drag;
//    private List<DoubleTap> flick;
//    private List<DoubleTap> swipe;
//    private List<DoubleTap> doubleTap;
//    private List<DoubleTap> moreThanDoubleTap;
//    private List<DoubleTap> twoFingerTap;
//    private List<DoubleTap> moreThanTwoFingerTap;
//    private List<DoubleTap> pinch;
//    private List<DoubleTap> touchAndHold;
//    private List<DoubleTap> shake;
//    private List<DoubleTap> rotate;
//    private List<ScreenEdgePan> screenEdgePan;
//    private List<View> view;
//    private List<AddToCart> addToCart;
//    private List<ChargeTransaction> chargeTransaction;
//    private List<ListUpdated> listUpdated;
//    private List<TimedEvent> timedEvent;
//    private List<CustomEvent> customEvent;
//
//    @JsonProperty("touchClick")
//    public List<DoubleTap> getTouchClick() { return touchClick; }
//    @JsonProperty("touchClick")
//    public void setTouchClick(List<DoubleTap> value) { this.touchClick = value; }
//
//    @JsonProperty("drag")
//    public List<DoubleTap> getDrag() { return drag; }
//    @JsonProperty("drag")
//    public void setDrag(List<DoubleTap> value) { this.drag = value; }
//
//    @JsonProperty("flick")
//    public List<DoubleTap> getFlick() { return flick; }
//    @JsonProperty("flick")
//    public void setFlick(List<DoubleTap> value) { this.flick = value; }
//
//    @JsonProperty("swipe")
//    public List<DoubleTap> getSwipe() { return swipe; }
//    @JsonProperty("swipe")
//    public void setSwipe(List<DoubleTap> value) { this.swipe = value; }
//
//    @JsonProperty("doubleTap")
//    public List<DoubleTap> getDoubleTap() { return doubleTap; }
//    @JsonProperty("doubleTap")
//    public void setDoubleTap(List<DoubleTap> value) { this.doubleTap = value; }
//
//    @JsonProperty("moreThanDoubleTap")
//    public List<DoubleTap> getMoreThanDoubleTap() { return moreThanDoubleTap; }
//    @JsonProperty("moreThanDoubleTap")
//    public void setMoreThanDoubleTap(List<DoubleTap> value) { this.moreThanDoubleTap = value; }
//
//    @JsonProperty("twoFingerTap")
//    public List<DoubleTap> getTwoFingerTap() { return twoFingerTap; }
//    @JsonProperty("twoFingerTap")
//    public void setTwoFingerTap(List<DoubleTap> value) { this.twoFingerTap = value; }
//
//    @JsonProperty("moreThanTwoFingerTap")
//    public List<DoubleTap> getMoreThanTwoFingerTap() { return moreThanTwoFingerTap; }
//    @JsonProperty("moreThanTwoFingerTap")
//    public void setMoreThanTwoFingerTap(List<DoubleTap> value) { this.moreThanTwoFingerTap = value; }
//
//    @JsonProperty("pinch")
//    public List<DoubleTap> getPinch() { return pinch; }
//    @JsonProperty("pinch")
//    public void setPinch(List<DoubleTap> value) { this.pinch = value; }
//
//    @JsonProperty("touchAndHold")
//    public List<DoubleTap> getTouchAndHold() { return touchAndHold; }
//    @JsonProperty("touchAndHold")
//    public void setTouchAndHold(List<DoubleTap> value) { this.touchAndHold = value; }
//
//    @JsonProperty("shake")
//    public List<DoubleTap> getShake() { return shake; }
//    @JsonProperty("shake")
//    public void setShake(List<DoubleTap> value) { this.shake = value; }
//
//    @JsonProperty("rotate")
//    public List<DoubleTap> getRotate() { return rotate; }
//    @JsonProperty("rotate")
//    public void setRotate(List<DoubleTap> value) { this.rotate = value; }
//
//    @JsonProperty("screenEdgePan")
//    public List<ScreenEdgePan> getScreenEdgePan() { return screenEdgePan; }
//    @JsonProperty("screenEdgePan")
//    public void setScreenEdgePan(List<ScreenEdgePan> value) { this.screenEdgePan = value; }
//
//    @JsonProperty("view")
//    public List<View> getView() { return view; }
//    @JsonProperty("view")
//    public void setView(List<View> value) { this.view = value; }
//
//    @JsonProperty("addToCart")
//    public List<AddToCart> getAddToCart() { return addToCart; }
//    @JsonProperty("addToCart")
//    public void setAddToCart(List<AddToCart> value) { this.addToCart = value; }
//
//    @JsonProperty("chargeTransaction")
//    public List<ChargeTransaction> getChargeTransaction() { return chargeTransaction; }
//    @JsonProperty("chargeTransaction")
//    public void setChargeTransaction(List<ChargeTransaction> value) { this.chargeTransaction = value; }
//
//    @JsonProperty("listUpdated")
//    public List<ListUpdated> getListUpdated() { return listUpdated; }
//    @JsonProperty("listUpdated")
//    public void setListUpdated(List<ListUpdated> value) { this.listUpdated = value; }
//
//    @JsonProperty("timedEvent")
//    public List<TimedEvent> getTimedEvent() { return timedEvent; }
//    @JsonProperty("timedEvent")
//    public void setTimedEvent(List<TimedEvent> value) { this.timedEvent = value; }
//
//    @JsonProperty("customEvent")
//    public List<CustomEvent> getCustomEvent() { return customEvent; }
//    @JsonProperty("customEvent")
//    public void setCustomEvent(List<CustomEvent> value) { this.customEvent = value; }
//}
//
//// BOAddToCart.java
// class AddToCart {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String cartClassName;
//    private Info additionalInfo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("cartClassName")
//    public String getCartClassName() { return cartClassName; }
//    @JsonProperty("cartClassName")
//    public void setCartClassName(String value) { this.cartClassName = value; }
//
//    @JsonProperty("additionalInfo")
//    public Info getAdditionalInfo() { return additionalInfo; }
//    @JsonProperty("additionalInfo")
//    public void setAdditionalInfo(Info value) { this.additionalInfo = value; }
//}
//
//// ChargeTransaction.java
//class ChargeTransaction {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String transactionClassName;
//    private Info transactionInfo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("transactionClassName")
//    public String getTransactionClassName() { return transactionClassName; }
//    @JsonProperty("transactionClassName")
//    public void setTransactionClassName(String value) { this.transactionClassName = value; }
//
//    @JsonProperty("transactionInfo")
//    public Info getTransactionInfo() { return transactionInfo; }
//    @JsonProperty("transactionInfo")
//    public void setTransactionInfo(Info value) { this.transactionInfo = value; }
//}
//
//// CustomEvent.java
//class CustomEvent {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String eventName;
//    private String visibleClassName;
//    private Info eventInfo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("eventName")
//    public String getEventName() { return eventName; }
//    @JsonProperty("eventName")
//    public void setEventName(String value) { this.eventName = value; }
//
//    @JsonProperty("visibleClassName")
//    public String getVisibleClassName() { return visibleClassName; }
//    @JsonProperty("visibleClassName")
//    public void setVisibleClassName(String value) { this.visibleClassName = value; }
//
//    @JsonProperty("eventInfo")
//    public Info getEventInfo() { return eventInfo; }
//    @JsonProperty("eventInfo")
//    public void setEventInfo(Info value) { this.eventInfo = value; }
//}
//
//// BODoubleTap.java
//
//class DoubleTap {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String objectType;
//    private String visibleClassName;
//    private Map<String, Long> objectRect;
//    private ScreenRect screenRect;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("objectType")
//    public String getObjectType() { return objectType; }
//    @JsonProperty("objectType")
//    public void setObjectType(String value) { this.objectType = value; }
//
//    @JsonProperty("visibleClassName")
//    public String getVisibleClassName() { return visibleClassName; }
//    @JsonProperty("visibleClassName")
//    public void setVisibleClassName(String value) { this.visibleClassName = value; }
//
//    @JsonProperty("objectRect")
//    public Map<String, Long> getObjectRect() { return objectRect; }
//    @JsonProperty("objectRect")
//    public void setObjectRect(Map<String, Long> value) { this.objectRect = value; }
//
//    @JsonProperty("screenRect")
//    public ScreenRect getScreenRect() { return screenRect; }
//    @JsonProperty("screenRect")
//    public void setScreenRect(ScreenRect value) { this.screenRect = value; }
//}
//
//// BOScreenRect.java
//class ScreenRect {
//    private long screenX;
//    private long screenY;
//
//    @JsonProperty("screenX")
//    public long getScreenX() { return screenX; }
//    @JsonProperty("screenX")
//    public void setScreenX(long value) { this.screenX = value; }
//
//    @JsonProperty("screenY")
//    public long getScreenY() { return screenY; }
//    @JsonProperty("screenY")
//    public void setScreenY(long value) { this.screenY = value; }
//}
//
//// BOListUpdated.java
//
//class ListUpdated {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String listClassName;
//    private Info updatesInfo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("listClassName")
//    public String getListClassName() { return listClassName; }
//    @JsonProperty("listClassName")
//    public void setListClassName(String value) { this.listClassName = value; }
//
//    @JsonProperty("updatesInfo")
//    public Info getUpdatesInfo() { return updatesInfo; }
//    @JsonProperty("updatesInfo")
//    public void setUpdatesInfo(Info value) { this.updatesInfo = value; }
//}
//
//// BOScreenEdgePan.java
//class ScreenEdgePan {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String objectType;
//    private String visibleClassName;
//    private ScreenRect screenRectFrom;
//    private ScreenRect screenRectTo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("objectType")
//    public String getObjectType() { return objectType; }
//    @JsonProperty("objectType")
//    public void setObjectType(String value) { this.objectType = value; }
//
//    @JsonProperty("visibleClassName")
//    public String getVisibleClassName() { return visibleClassName; }
//    @JsonProperty("visibleClassName")
//    public void setVisibleClassName(String value) { this.visibleClassName = value; }
//
//    @JsonProperty("screenRectFrom")
//    public ScreenRect getScreenRectFrom() { return screenRectFrom; }
//    @JsonProperty("screenRectFrom")
//    public void setScreenRectFrom(ScreenRect value) { this.screenRectFrom = value; }
//
//    @JsonProperty("screenRectTo")
//    public ScreenRect getScreenRectTo() { return screenRectTo; }
//    @JsonProperty("screenRectTo")
//    public void setScreenRectTo(ScreenRect value) { this.screenRectTo = value; }
//}
//
//// BOTimedEvent.java
//class TimedEvent {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String eventName;
//    private long startTime;
//    private String startVisibleClassName;
//    private String endVisibleClassName;
//    private long endTime;
//    private long eventDuration;
//    private Info timedEvenInfo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("eventName")
//    public String getEventName() { return eventName; }
//    @JsonProperty("eventName")
//    public void setEventName(String value) { this.eventName = value; }
//
//    @JsonProperty("startTime")
//    public long getStartTime() { return startTime; }
//    @JsonProperty("startTime")
//    public void setStartTime(long value) { this.startTime = value; }
//
//    @JsonProperty("startVisibleClassName")
//    public String getStartVisibleClassName() { return startVisibleClassName; }
//    @JsonProperty("startVisibleClassName")
//    public void setStartVisibleClassName(String value) { this.startVisibleClassName = value; }
//
//    @JsonProperty("endVisibleClassName")
//    public String getEndVisibleClassName() { return endVisibleClassName; }
//    @JsonProperty("endVisibleClassName")
//    public void setEndVisibleClassName(String value) { this.endVisibleClassName = value; }
//
//    @JsonProperty("endTime")
//    public long getEndTime() { return endTime; }
//    @JsonProperty("endTime")
//    public void setEndTime(long value) { this.endTime = value; }
//
//    @JsonProperty("eventDuration")
//    public long getEventDuration() { return eventDuration; }
//    @JsonProperty("eventDuration")
//    public void setEventDuration(long value) { this.eventDuration = value; }
//
//    @JsonProperty("timedEvenInfo")
//    public Info getTimedEvenInfo() { return timedEvenInfo; }
//    @JsonProperty("timedEvenInfo")
//    public void setTimedEvenInfo(Info value) { this.timedEvenInfo = value; }
//}
//
//// BOView.java
// class View {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String viewClassName;
//    private Info viewInfo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("viewClassName")
//    public String getViewClassName() { return viewClassName; }
//    @JsonProperty("viewClassName")
//    public void setViewClassName(String value) { this.viewClassName = value; }
//
//    @JsonProperty("viewInfo")
//    public Info getViewInfo() { return viewInfo; }
//    @JsonProperty("viewInfo")
//    public void setViewInfo(Info value) { this.viewInfo = value; }
//}
//
//// BOSessionDeviceInfo.java
//class DeviceInfo {
//    private List<AccessoriesAttached> multitaskingEnabled;
//    private List<AccessoriesAttached> proximitySensorEnabled;
//    private List<AccessoriesAttached> debuggerAttached;
//    private List<AccessoriesAttached> pluggedIn;
//    private List<AccessoriesAttached> jailBroken;
//    private List<NumberOfA> numberOfActiveProcessors;
//    private List<ProcessorsUsage> processorsUsage;
//    private List<AccessoriesAttached> accessoriesAttached;
//    private List<AccessoriesAttached> headphoneAttached;
//    private List<NumberOfA> numberOfAttachedAccessories;
//    private List<BONameOfAttachedAccessory> nameOfAttachedAccessories;
//    private List<BatteryLevel> batteryLevel;
//    private List<AccessoriesAttached> isCharging;
//    private List<AccessoriesAttached> fullyCharged;
//    private List<DeviceOrientation> deviceOrientation;
//    private List<CFUUID> cfUUID;
//    private List<VendorID> vendorID;
//
//    @JsonProperty("multitaskingEnabled")
//    public List<AccessoriesAttached> getMultitaskingEnabled() { return multitaskingEnabled; }
//    @JsonProperty("multitaskingEnabled")
//    public void setMultitaskingEnabled(List<AccessoriesAttached> value) { this.multitaskingEnabled = value; }
//
//    @JsonProperty("proximitySensorEnabled")
//    public List<AccessoriesAttached> getProximitySensorEnabled() { return proximitySensorEnabled; }
//    @JsonProperty("proximitySensorEnabled")
//    public void setProximitySensorEnabled(List<AccessoriesAttached> value) { this.proximitySensorEnabled = value; }
//
//    @JsonProperty("debuggerAttached")
//    public List<AccessoriesAttached> getDebuggerAttached() { return debuggerAttached; }
//    @JsonProperty("debuggerAttached")
//    public void setDebuggerAttached(List<AccessoriesAttached> value) { this.debuggerAttached = value; }
//
//    @JsonProperty("pluggedIn")
//    public List<AccessoriesAttached> getPluggedIn() { return pluggedIn; }
//    @JsonProperty("pluggedIn")
//    public void setPluggedIn(List<AccessoriesAttached> value) { this.pluggedIn = value; }
//
//    @JsonProperty("jailBroken")
//    public List<AccessoriesAttached> getJailBroken() { return jailBroken; }
//    @JsonProperty("jailBroken")
//    public void setJailBroken(List<AccessoriesAttached> value) { this.jailBroken = value; }
//
//    @JsonProperty("numberOfActiveProcessors")
//    public List<NumberOfA> getNumberOfActiveProcessors() { return numberOfActiveProcessors; }
//    @JsonProperty("numberOfActiveProcessors")
//    public void setNumberOfActiveProcessors(List<NumberOfA> value) { this.numberOfActiveProcessors = value; }
//
//    @JsonProperty("processorsUsage")
//    public List<ProcessorsUsage> getProcessorsUsage() { return processorsUsage; }
//    @JsonProperty("processorsUsage")
//    public void setProcessorsUsage(List<ProcessorsUsage> value) { this.processorsUsage = value; }
//
//    @JsonProperty("accessoriesAttached")
//    public List<AccessoriesAttached> getAccessoriesAttached() { return accessoriesAttached; }
//    @JsonProperty("accessoriesAttached")
//    public void setAccessoriesAttached(List<AccessoriesAttached> value) { this.accessoriesAttached = value; }
//
//    @JsonProperty("headphoneAttached")
//    public List<AccessoriesAttached> getHeadphoneAttached() { return headphoneAttached; }
//    @JsonProperty("headphoneAttached")
//    public void setHeadphoneAttached(List<AccessoriesAttached> value) { this.headphoneAttached = value; }
//
//    @JsonProperty("numberOfAttachedAccessories")
//    public List<NumberOfA> getNumberOfAttachedAccessories() { return numberOfAttachedAccessories; }
//    @JsonProperty("numberOfAttachedAccessories")
//    public void setNumberOfAttachedAccessories(List<NumberOfA> value) { this.numberOfAttachedAccessories = value; }
//
//    @JsonProperty("nameOfAttachedAccessories")
//    public List<BONameOfAttachedAccessory> getNameOfAttachedAccessories() { return nameOfAttachedAccessories; }
//    @JsonProperty("nameOfAttachedAccessories")
//    public void setNameOfAttachedAccessories(List<BONameOfAttachedAccessory> value) { this.nameOfAttachedAccessories = value; }
//
//    @JsonProperty("batteryLevel")
//    public List<BatteryLevel> getBatteryLevel() { return batteryLevel; }
//    @JsonProperty("batteryLevel")
//    public void setBatteryLevel(List<BatteryLevel> value) { this.batteryLevel = value; }
//
//    @JsonProperty("isCharging")
//    public List<AccessoriesAttached> getIsCharging() { return isCharging; }
//    @JsonProperty("isCharging")
//    public void setIsCharging(List<AccessoriesAttached> value) { this.isCharging = value; }
//
//    @JsonProperty("fullyCharged")
//    public List<AccessoriesAttached> getFullyCharged() { return fullyCharged; }
//    @JsonProperty("fullyCharged")
//    public void setFullyCharged(List<AccessoriesAttached> value) { this.fullyCharged = value; }
//
//    @JsonProperty("deviceOrientation")
//    public List<DeviceOrientation> getDeviceOrientation() { return deviceOrientation; }
//    @JsonProperty("deviceOrientation")
//    public void setDeviceOrientation(List<DeviceOrientation> value) { this.deviceOrientation = value; }
//
//    @JsonProperty("cfUUID")
//    public List<CFUUID> getCFUUID() { return cfUUID; }
//    @JsonProperty("cfUUID")
//    public void setCFUUID(List<CFUUID> value) { this.cfUUID = value; }
//
//    @JsonProperty("vendorID")
//    public List<VendorID> getVendorID() { return vendorID; }
//    @JsonProperty("vendorID")
//    public void setVendorID(List<VendorID> value) { this.vendorID = value; }
//}
//
//// BOAccessoriesAttached.java
//
//class AccessoriesAttached {
//    private boolean sentToServer;
//    private boolean status;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("status")
//    public boolean getStatus() { return status; }
//    @JsonProperty("status")
//    public void setStatus(boolean value) { this.status = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BOBatteryLevel.java
//class BatteryLevel {
//    private boolean sentToServer;
//    private long percentage;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("percentage")
//    public long getPercentage() { return percentage; }
//    @JsonProperty("percentage")
//    public void setPercentage(long value) { this.percentage = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// CFUUID.java
//
//class CFUUID {
//    private boolean sentToServer;
//    private String cfUUID;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("cfUUID")
//    public String getCFUUID() { return cfUUID; }
//    @JsonProperty("cfUUID")
//    public void setCFUUID(String value) { this.cfUUID = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BODeviceOrientation.java
//
//class DeviceOrientation {
//    private boolean sentToServer;
//    private String orientation;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("orientation")
//    public String getOrientation() { return orientation; }
//    @JsonProperty("orientation")
//    public void setOrientation(String value) { this.orientation = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BONameOfAttachedAccessory.java
//
//class BONameOfAttachedAccessory {
//    private boolean sentToServer;
//    private List<String> names;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("names")
//    public List<String> getNames() { return names; }
//    @JsonProperty("names")
//    public void setNames(List<String> value) { this.names = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BONumberOfA.java
//
//class NumberOfA {
//    private boolean sentToServer;
//    private long number;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("number")
//    public long getNumber() { return number; }
//    @JsonProperty("number")
//    public void setNumber(long value) { this.number = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BOProcessorsUsage.java
//
//class ProcessorsUsage {
//    private boolean sentToServer;
//    private long processorID;
//    private long usagePercentage;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("processorID")
//    public long getProcessorID() { return processorID; }
//    @JsonProperty("processorID")
//    public void setProcessorID(long value) { this.processorID = value; }
//
//    @JsonProperty("usagePercentage")
//    public long getUsagePercentage() { return usagePercentage; }
//    @JsonProperty("usagePercentage")
//    public void setUsagePercentage(long value) { this.usagePercentage = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BOVendorID.java
//
//class VendorID {
//    private String vendorID;
//    private boolean sentToServer;
//    private long timeStamp;
//
//    @JsonProperty("vendorID")
//    public String getVendorID() { return vendorID; }
//    @JsonProperty("vendorID")
//    public void setVendorID(String value) { this.vendorID = value; }
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BOLocation.java
//
// class Location {
//    private boolean sentToServer;
//    private long timeStamp;
//    private PiiLocation piiLocation;
//    private NonPIILocation nonPIILocation;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("piiLocation")
//    public PiiLocation getPiiLocation() { return piiLocation; }
//    @JsonProperty("piiLocation")
//    public void setPiiLocation(PiiLocation value) { this.piiLocation = value; }
//
//    @JsonProperty("nonPIILocation")
//    public NonPIILocation getNonPIILocation() { return nonPIILocation; }
//    @JsonProperty("nonPIILocation")
//    public void setNonPIILocation(NonPIILocation value) { this.nonPIILocation = value; }
//}
//
//// BONonPIILocation.java
//
//class NonPIILocation {
//    private boolean sentToServer;
//    private String city;
//    private String state;
//    private String zip;
//    private String country;
//    private String activity;
//    private String source;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("city")
//    public String getCity() { return city; }
//    @JsonProperty("city")
//    public void setCity(String value) { this.city = value; }
//
//    @JsonProperty("state")
//    public String getState() { return state; }
//    @JsonProperty("state")
//    public void setState(String value) { this.state = value; }
//
//    @JsonProperty("zip")
//    public String getZip() { return zip; }
//    @JsonProperty("zip")
//    public void setZip(String value) { this.zip = value; }
//
//    @JsonProperty("country")
//    public String getCountry() { return country; }
//    @JsonProperty("country")
//    public void setCountry(String value) { this.country = value; }
//
//    @JsonProperty("activity")
//    public String getActivity() { return activity; }
//    @JsonProperty("activity")
//    public void setActivity(String value) { this.activity = value; }
//
//    @JsonProperty("source")
//    public String getSource() { return source; }
//    @JsonProperty("source")
//    public void setSource(String value) { this.source = value; }
//}
//
//// BOPiiLocation.java
//
//class PiiLocation {
//    private boolean sentToServer;
//    private String latitude;
//    private String longitude;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("latitude")
//    public String getLatitude() { return latitude; }
//    @JsonProperty("latitude")
//    public void setLatitude(String value) { this.latitude = value; }
//
//    @JsonProperty("longitude")
//    public String getLongitude() { return longitude; }
//    @JsonProperty("longitude")
//    public void setLongitude(String value) { this.longitude = value; }
//}
//
//// BOMemoryInfo.java
//
//class MemoryInfo {
//    private boolean sentToServer;
//    private long timeStamp;
//    private long totalRAM;
//    private long usedMemory;
//    private long wiredMemory;
//    private long activeMemory;
//    private long inActiveMemory;
//    private long freeMemory;
//    private long purgeableMemory;
//    private boolean atMeoryWarning;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("totalRAM")
//    public long getTotalRAM() { return totalRAM; }
//    @JsonProperty("totalRAM")
//    public void setTotalRAM(long value) { this.totalRAM = value; }
//
//    @JsonProperty("usedMemory")
//    public long getUsedMemory() { return usedMemory; }
//    @JsonProperty("usedMemory")
//    public void setUsedMemory(long value) { this.usedMemory = value; }
//
//    @JsonProperty("wiredMemory")
//    public long getWiredMemory() { return wiredMemory; }
//    @JsonProperty("wiredMemory")
//    public void setWiredMemory(long value) { this.wiredMemory = value; }
//
//    @JsonProperty("activeMemory")
//    public long getActiveMemory() { return activeMemory; }
//    @JsonProperty("activeMemory")
//    public void setActiveMemory(long value) { this.activeMemory = value; }
//
//    @JsonProperty("inActiveMemory")
//    public long getInActiveMemory() { return inActiveMemory; }
//    @JsonProperty("inActiveMemory")
//    public void setInActiveMemory(long value) { this.inActiveMemory = value; }
//
//    @JsonProperty("freeMemory")
//    public long getFreeMemory() { return freeMemory; }
//    @JsonProperty("freeMemory")
//    public void setFreeMemory(long value) { this.freeMemory = value; }
//
//    @JsonProperty("purgeableMemory")
//    public long getPurgeableMemory() { return purgeableMemory; }
//    @JsonProperty("purgeableMemory")
//    public void setPurgeableMemory(long value) { this.purgeableMemory = value; }
//
//    @JsonProperty("atMeoryWarning")
//    public boolean getAtMeoryWarning() { return atMeoryWarning; }
//    @JsonProperty("atMeoryWarning")
//    public void setAtMeoryWarning(boolean value) { this.atMeoryWarning = value; }
//}
//
//// BONetworkInfo.java
//
// class NetworkInfo {
//    private List<IPAddress> currentIPAddress;
//    private List<IPAddress> externalIPAddress;
//    private List<IPAddress> cellIPAddress;
//    private List<NetMask> cellNetMask;
//    private List<BroadcastAddress> cellBroadcastAddress;
//    private List<IPAddress> wifiIPAddress;
//    private List<NetMask> wifiNetMask;
//    private List<BroadcastAddress> wifiBroadcastAddress;
//    private List<WifiRouterAddress> wifiRouterAddress;
//    private List<WifiSSID> wifiSSID;
//    private List<ConnectedTo> connectedToWifi;
//    private List<ConnectedTo> connectedToCellNetwork;
//
//    @JsonProperty("currentIPAddress")
//    public List<IPAddress> getCurrentIPAddress() { return currentIPAddress; }
//    @JsonProperty("currentIPAddress")
//    public void setCurrentIPAddress(List<IPAddress> value) { this.currentIPAddress = value; }
//
//    @JsonProperty("externalIPAddress")
//    public List<IPAddress> getExternalIPAddress() { return externalIPAddress; }
//    @JsonProperty("externalIPAddress")
//    public void setExternalIPAddress(List<IPAddress> value) { this.externalIPAddress = value; }
//
//    @JsonProperty("cellIPAddress")
//    public List<IPAddress> getCellIPAddress() { return cellIPAddress; }
//    @JsonProperty("cellIPAddress")
//    public void setCellIPAddress(List<IPAddress> value) { this.cellIPAddress = value; }
//
//    @JsonProperty("cellNetMask")
//    public List<NetMask> getCellNetMask() { return cellNetMask; }
//    @JsonProperty("cellNetMask")
//    public void setCellNetMask(List<NetMask> value) { this.cellNetMask = value; }
//
//    @JsonProperty("cellBroadcastAddress")
//    public List<BroadcastAddress> getCellBroadcastAddress() { return cellBroadcastAddress; }
//    @JsonProperty("cellBroadcastAddress")
//    public void setCellBroadcastAddress(List<BroadcastAddress> value) { this.cellBroadcastAddress = value; }
//
//    @JsonProperty("wifiIPAddress")
//    public List<IPAddress> getWifiIPAddress() { return wifiIPAddress; }
//    @JsonProperty("wifiIPAddress")
//    public void setWifiIPAddress(List<IPAddress> value) { this.wifiIPAddress = value; }
//
//    @JsonProperty("wifiNetMask")
//    public List<NetMask> getWifiNetMask() { return wifiNetMask; }
//    @JsonProperty("wifiNetMask")
//    public void setWifiNetMask(List<NetMask> value) { this.wifiNetMask = value; }
//
//    @JsonProperty("wifiBroadcastAddress")
//    public List<BroadcastAddress> getWifiBroadcastAddress() { return wifiBroadcastAddress; }
//    @JsonProperty("wifiBroadcastAddress")
//    public void setWifiBroadcastAddress(List<BroadcastAddress> value) { this.wifiBroadcastAddress = value; }
//
//    @JsonProperty("wifiRouterAddress")
//    public List<WifiRouterAddress> getWifiRouterAddress() { return wifiRouterAddress; }
//    @JsonProperty("wifiRouterAddress")
//    public void setWifiRouterAddress(List<WifiRouterAddress> value) { this.wifiRouterAddress = value; }
//
//    @JsonProperty("wifiSSID")
//    public List<WifiSSID> getWifiSSID() { return wifiSSID; }
//    @JsonProperty("wifiSSID")
//    public void setWifiSSID(List<WifiSSID> value) { this.wifiSSID = value; }
//
//    @JsonProperty("connectedToWifi")
//    public List<ConnectedTo> getConnectedToWifi() { return connectedToWifi; }
//    @JsonProperty("connectedToWifi")
//    public void setConnectedToWifi(List<ConnectedTo> value) { this.connectedToWifi = value; }
//
//    @JsonProperty("connectedToCellNetwork")
//    public List<ConnectedTo> getConnectedToCellNetwork() { return connectedToCellNetwork; }
//    @JsonProperty("connectedToCellNetwork")
//    public void setConnectedToCellNetwork(List<ConnectedTo> value) { this.connectedToCellNetwork = value; }
//}
//
//// BroadcastAddress.java
//
//class BroadcastAddress {
//    private boolean sentToServer;
//    private String broadcastAddress;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("broadcastAddress")
//    public String getBroadcastAddress() { return broadcastAddress; }
//    @JsonProperty("broadcastAddress")
//    public void setBroadcastAddress(String value) { this.broadcastAddress = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BOIPAddress.java
//
//class IPAddress {
//    private boolean sentToServer;
//    private String ipAddress;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("ipAddress")
//    public String getIPAddress() { return ipAddress; }
//    @JsonProperty("ipAddress")
//    public void setIPAddress(String value) { this.ipAddress = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BONetMask.java
//
//class NetMask {
//    private boolean sentToServer;
//    private String netmask;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("netmask")
//    public String getNetmask() { return netmask; }
//    @JsonProperty("netmask")
//    public void setNetmask(String value) { this.netmask = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// ConnectedTo.java
//
//class ConnectedTo {
//    private boolean sentToServer;
//    private boolean isConnected;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("isConnected")
//    public boolean getIsConnected() { return isConnected; }
//    @JsonProperty("isConnected")
//    public void setIsConnected(boolean value) { this.isConnected = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BOWifiRouterAddress.java
//
// class WifiRouterAddress {
//    private boolean sentToServer;
//    private String routerAddress;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("routerAddress")
//    public String getRouterAddress() { return routerAddress; }
//    @JsonProperty("routerAddress")
//    public void setRouterAddress(String value) { this.routerAddress = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BOWifiSSID.java
//
// class WifiSSID {
//    private boolean sentToServer;
//    private String ssid;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("ssid")
//    public String getSSID() { return ssid; }
//    @JsonProperty("ssid")
//    public void setSSID(String value) { this.ssid = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BORetentionEvent.java
//
// class RetentionEvent {
//    private boolean sentToServer;
//    private Dau dau;
//    private Dpu dpu;
//    private AppInstalled appInstalled;
//    private NewUser newUser;
//    private Dast dast;
//    private List<CustomEvent> customEvents;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("dau")
//    public Dau getDau() { return dau; }
//    @JsonProperty("dau")
//    public void setDau(Dau value) { this.dau = value; }
//
//    @JsonProperty("dpu")
//    public Dpu getDpu() { return dpu; }
//    @JsonProperty("dpu")
//    public void setDpu(Dpu value) { this.dpu = value; }
//
//    @JsonProperty("appInstalled")
//    public AppInstalled getAppInstalled() { return appInstalled; }
//    @JsonProperty("appInstalled")
//    public void setAppInstalled(AppInstalled value) { this.appInstalled = value; }
//
//    @JsonProperty("newUser")
//    public NewUser getNewUser() { return newUser; }
//    @JsonProperty("newUser")
//    public void setNewUser(NewUser value) { this.newUser = value; }
//
//    @JsonProperty("DAST")
//    public Dast getDast() { return dast; }
//    @JsonProperty("DAST")
//    public void setDast(Dast value) { this.dast = value; }
//
//    @JsonProperty("customEvents")
//    public List<CustomEvent> getCustomEvents() { return customEvents; }
//    @JsonProperty("customEvents")
//    public void setCustomEvents(List<CustomEvent> value) { this.customEvents = value; }
//}
//
//// BOAppInstalled.java
// class AppInstalled {
//    private boolean sentToServer;
//    private boolean isFirstLaunch;
//    private long timeStamp;
//    private Info appInstalledInfo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("isFirstLaunch")
//    public boolean getIsFirstLaunch() { return isFirstLaunch; }
//    @JsonProperty("isFirstLaunch")
//    public void setIsFirstLaunch(boolean value) { this.isFirstLaunch = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("appInstalledInfo")
//    public Info getAppInstalledInfo() { return appInstalledInfo; }
//    @JsonProperty("appInstalledInfo")
//    public void setAppInstalledInfo(Info value) { this.appInstalledInfo = value; }
//}
//
//// Dast.java
//
// class Dast {
//    private boolean sentToServer;
//    private long timeStamp;
//    private long averageSessionTime;
//    private Info payload;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("averageSessionTime")
//    public long getAverageSessionTime() { return averageSessionTime; }
//    @JsonProperty("averageSessionTime")
//    public void setAverageSessionTime(long value) { this.averageSessionTime = value; }
//
//    @JsonProperty("payload")
//    public Info getPayload() { return payload; }
//    @JsonProperty("payload")
//    public void setPayload(Info value) { this.payload = value; }
//}
//
//// BODau.java
//
// class Dau {
//    private boolean sentToServer;
//    private long timeStamp;
//    private Info dauInfo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("dauInfo")
//    public Info getDauInfo() { return dauInfo; }
//    @JsonProperty("dauInfo")
//    public void setDauInfo(Info value) { this.dauInfo = value; }
//}
//
//// BODpu.java
//class Dpu {
//    private boolean sentToServer;
//    private long timeStamp;
//    private Info dpuInfo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("dpuInfo")
//    public Info getDpuInfo() { return dpuInfo; }
//    @JsonProperty("dpuInfo")
//    public void setDpuInfo(Info value) { this.dpuInfo = value; }
//}
//
//// BONewUser.java
// class NewUser {
//    private boolean sentToServer;
//    private boolean isNewUser;
//    private long timeStamp;
//    private Info newUserInfo;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("isNewUser")
//    public boolean getIsNewUser() { return isNewUser; }
//    @JsonProperty("isNewUser")
//    public void setIsNewUser(boolean value) { this.isNewUser = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("newUserInfo")
//    public Info getNewUserInfo() { return newUserInfo; }
//    @JsonProperty("newUserInfo")
//    public void setNewUserInfo(Info value) { this.newUserInfo = value; }
//}
//
//// BOStorageInfo.java
//
// class StorageInfo {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String unit;
//    private String totalDiskSpace;
//    private String usedDiskSpace;
//    private String freeDiskSpace;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("unit")
//    public String getUnit() { return unit; }
//    @JsonProperty("unit")
//    public void setUnit(String value) { this.unit = value; }
//
//    @JsonProperty("totalDiskSpace")
//    public String getTotalDiskSpace() { return totalDiskSpace; }
//    @JsonProperty("totalDiskSpace")
//    public void setTotalDiskSpace(String value) { this.totalDiskSpace = value; }
//
//    @JsonProperty("usedDiskSpace")
//    public String getUsedDiskSpace() { return usedDiskSpace; }
//    @JsonProperty("usedDiskSpace")
//    public void setUsedDiskSpace(String value) { this.usedDiskSpace = value; }
//
//    @JsonProperty("freeDiskSpace")
//    public String getFreeDiskSpace() { return freeDiskSpace; }
//    @JsonProperty("freeDiskSpace")
//    public void setFreeDiskSpace(String value) { this.freeDiskSpace = value; }
//}
//
//// BOUbiAutoDetected.java
//
// class UbiAutoDetected {
//    private List<ScreenShotsTaken> screenShotsTaken;
//    private List<AppNavigation> appNavigation;
//    private AppGesture appGesture;
//
//    @JsonProperty("screenShotsTaken")
//    public List<ScreenShotsTaken> getScreenShotsTaken() { return screenShotsTaken; }
//    @JsonProperty("screenShotsTaken")
//    public void setScreenShotsTaken(List<ScreenShotsTaken> value) { this.screenShotsTaken = value; }
//
//    @JsonProperty("appNavigation")
//    public List<AppNavigation> getAppNavigation() { return appNavigation; }
//    @JsonProperty("appNavigation")
//    public void setAppNavigation(List<AppNavigation> value) { this.appNavigation = value; }
//
//    @JsonProperty("appGesture")
//    public AppGesture getAppGesture() { return appGesture; }
//    @JsonProperty("appGesture")
//    public void setAppGesture(AppGesture value) { this.appGesture = value; }
//}
//
//// BOAppGesture.java
//
// class AppGesture {
//    private List<DoubleTap> touchOrClick;
//    private List<DoubleTap> drag;
//    private List<DoubleTap> flick;
//    private List<DoubleTap> swipe;
//    private List<DoubleTap> doubleTap;
//    private List<DoubleTap> moreThanDoubleTap;
//    private List<DoubleTap> twoFingerTap;
//    private List<DoubleTap> moreThanTwoFingerTap;
//    private List<DoubleTap> pinch;
//    private List<DoubleTap> touchAndHold;
//    private List<DoubleTap> shake;
//    private List<DoubleTap> rotate;
//    private List<ScreenEdgePan> screenEdgePan;
//
//    @JsonProperty("touchOrClick")
//    public List<DoubleTap> getTouchOrClick() { return touchOrClick; }
//    @JsonProperty("touchOrClick")
//    public void setTouchOrClick(List<DoubleTap> value) { this.touchOrClick = value; }
//
//    @JsonProperty("drag")
//    public List<DoubleTap> getDrag() { return drag; }
//    @JsonProperty("drag")
//    public void setDrag(List<DoubleTap> value) { this.drag = value; }
//
//    @JsonProperty("flick")
//    public List<DoubleTap> getFlick() { return flick; }
//    @JsonProperty("flick")
//    public void setFlick(List<DoubleTap> value) { this.flick = value; }
//
//    @JsonProperty("swipe")
//    public List<DoubleTap> getSwipe() { return swipe; }
//    @JsonProperty("swipe")
//    public void setSwipe(List<DoubleTap> value) { this.swipe = value; }
//
//    @JsonProperty("doubleTap")
//    public List<DoubleTap> getDoubleTap() { return doubleTap; }
//    @JsonProperty("doubleTap")
//    public void setDoubleTap(List<DoubleTap> value) { this.doubleTap = value; }
//
//    @JsonProperty("moreThanDoubleTap")
//    public List<DoubleTap> getMoreThanDoubleTap() { return moreThanDoubleTap; }
//    @JsonProperty("moreThanDoubleTap")
//    public void setMoreThanDoubleTap(List<DoubleTap> value) { this.moreThanDoubleTap = value; }
//
//    @JsonProperty("twoFingerTap")
//    public List<DoubleTap> getTwoFingerTap() { return twoFingerTap; }
//    @JsonProperty("twoFingerTap")
//    public void setTwoFingerTap(List<DoubleTap> value) { this.twoFingerTap = value; }
//
//    @JsonProperty("moreThanTwoFingerTap")
//    public List<DoubleTap> getMoreThanTwoFingerTap() { return moreThanTwoFingerTap; }
//    @JsonProperty("moreThanTwoFingerTap")
//    public void setMoreThanTwoFingerTap(List<DoubleTap> value) { this.moreThanTwoFingerTap = value; }
//
//    @JsonProperty("pinch")
//    public List<DoubleTap> getPinch() { return pinch; }
//    @JsonProperty("pinch")
//    public void setPinch(List<DoubleTap> value) { this.pinch = value; }
//
//    @JsonProperty("touchAndHold")
//    public List<DoubleTap> getTouchAndHold() { return touchAndHold; }
//    @JsonProperty("touchAndHold")
//    public void setTouchAndHold(List<DoubleTap> value) { this.touchAndHold = value; }
//
//    @JsonProperty("shake")
//    public List<DoubleTap> getShake() { return shake; }
//    @JsonProperty("shake")
//    public void setShake(List<DoubleTap> value) { this.shake = value; }
//
//    @JsonProperty("rotate")
//    public List<DoubleTap> getRotate() { return rotate; }
//    @JsonProperty("rotate")
//    public void setRotate(List<DoubleTap> value) { this.rotate = value; }
//
//    @JsonProperty("screenEdgePan")
//    public List<ScreenEdgePan> getScreenEdgePan() { return screenEdgePan; }
//    @JsonProperty("screenEdgePan")
//    public void setScreenEdgePan(List<ScreenEdgePan> value) { this.screenEdgePan = value; }
//}
//
//// BOAppNavigation.java
//
// class AppNavigation {
//    private boolean sentToServer;
//    private long timeStamp;
//    private String from;
//    private String to;
//    private String action;
//    private String actionObject;
//    private String actionObjectTitle;
//    private long actionTime;
//    private boolean networkIndicatorVisible;
//    private long timeSpent;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("from")
//    public String getFrom() { return from; }
//    @JsonProperty("from")
//    public void setFrom(String value) { this.from = value; }
//
//    @JsonProperty("to")
//    public String getTo() { return to; }
//    @JsonProperty("to")
//    public void setTo(String value) { this.to = value; }
//
//    @JsonProperty("action")
//    public String getAction() { return action; }
//    @JsonProperty("action")
//    public void setAction(String value) { this.action = value; }
//
//    @JsonProperty("actionObject")
//    public String getActionObject() { return actionObject; }
//    @JsonProperty("actionObject")
//    public void setActionObject(String value) { this.actionObject = value; }
//
//    @JsonProperty("actionObjectTitle")
//    public String getActionObjectTitle() { return actionObjectTitle; }
//    @JsonProperty("actionObjectTitle")
//    public void setActionObjectTitle(String value) { this.actionObjectTitle = value; }
//
//    @JsonProperty("actionTime")
//    public long getActionTime() { return actionTime; }
//    @JsonProperty("actionTime")
//    public void setActionTime(long value) { this.actionTime = value; }
//
//    @JsonProperty("networkIndicatorVisible")
//    public boolean getNetworkIndicatorVisible() { return networkIndicatorVisible; }
//    @JsonProperty("networkIndicatorVisible")
//    public void setNetworkIndicatorVisible(boolean value) { this.networkIndicatorVisible = value; }
//
//    @JsonProperty("timeSpent")
//    public long getTimeSpent() { return timeSpent; }
//    @JsonProperty("timeSpent")
//    public void setTimeSpent(long value) { this.timeSpent = value; }
//}
//
//// BOScreenShotsTaken.java
// class ScreenShotsTaken {
//    private String currentView;
//    private long timeStamp;
//    private boolean sentToServer;
//
//    @JsonProperty("currentView")
//    public String getCurrentView() { return currentView; }
//    @JsonProperty("currentView")
//    public void setCurrentView(String value) { this.currentView = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//}
