//// Converter.java
//
//// To use this code, add the following Maven dependency to your project:
////
////     com.fasterxml.jackson.core : jackson-databind : 2.9.0
////
//// Import this package:
////
////     import com.blotout.model.AppLifeTimeModel.Converter;
////
//// Then you can deserialize a JSON string with
////
////     Empty data = Converter.fromJsonString(jsonString);
//
//package com.blotout.model.lifetime;
//
//import java.util.*;
//import java.io.IOException;
//import com.fasterxml.jackson.databind.*;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.annotation.*;
///**
// * Created by Blotout on 22,October,2019
// */
//
//class Converter {
//    // Serialize/deserialize helpers
//
//    public static BOAppLifetimeData1 fromJsonString(String json) throws IOException {
//        return getObjectReader().readValue(json);
//    }
//
//    public static String toJsonString(BOAppLifetimeData1 obj) throws JsonProcessingException {
//        return getObjectWriter().writeValueAsString(obj);
//    }
//
//    private static ObjectReader reader;
//    private static ObjectWriter writer;
//
//    private static void instantiateMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        reader = mapper.reader(BOAppLifetimeData1.class);
//        writer = mapper.writerFor(BOAppLifetimeData1.class);
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
//public class BOAppLifetimeData1 {
//    private Object appBundle;
//    private Object appID;
//    private Object lastServerSyncTimeStamp;
//    private Object allEventsSyncTimeStamp;
//    private List<AppLifeTimeInfo> appLifeTimeInfo;
//    private String date;
//
//    @JsonProperty("appBundle")
//    public Object getAppBundle() { return appBundle; }
//    @JsonProperty("appBundle")
//    public void setAppBundle(Object value) { this.appBundle = value; }
//
//    @JsonProperty("appID")
//    public Object getAppID() { return appID; }
//    @JsonProperty("appID")
//    public void setAppID(Object value) { this.appID = value; }
//
//    @JsonProperty("lastServerSyncTimeStamp")
//    public Object getLastServerSyncTimeStamp() { return lastServerSyncTimeStamp; }
//    @JsonProperty("lastServerSyncTimeStamp")
//    public void setLastServerSyncTimeStamp(Object value) { this.lastServerSyncTimeStamp = value; }
//
//    @JsonProperty("allEventsSyncTimeStamp")
//    public Object getAllEventsSyncTimeStamp() { return allEventsSyncTimeStamp; }
//    @JsonProperty("allEventsSyncTimeStamp")
//    public void setAllEventsSyncTimeStamp(Object value) { this.allEventsSyncTimeStamp = value; }
//
//    @JsonProperty("appLifeTimeInfo")
//    public List<AppLifeTimeInfo> getAppLifeTimeInfo() { return appLifeTimeInfo; }
//    @JsonProperty("appLifeTimeInfo")
//    public void setAppLifeTimeInfo(List<AppLifeTimeInfo> value) { this.appLifeTimeInfo = value; }
//
//    @JsonProperty("date")
//    public String getDate() { return date; }
//    @JsonProperty("date")
//    public void setDate(String value) { this.date = value; }
//}
//
//// BOAppLifeTimeInfo.java
//class AppLifeTimeInfo {
//    private long timeStamp;
//    private boolean sentToServer;
//    private Object appUpdatesInfo;
//    private DeviceInfo deviceInfo;
//    private Object memoryInfo;
//    private Object networkInfo;
//    private Object blotoutSDKsInfo;
//    private Location location;
//    private Object appLanguagesSupported;
//    private Object appSupportShakeToEdit;
//    private Object appInstallInfo;
//    private Object appLaunchInfo;
//    private Object storageInfo;
//    private Object appSupportRemoteNotifications;
//    private RetentionEvent retentionEvent;
//    private Object appCategory;
//    private String dateAndTime;
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
//
//    @JsonProperty("appUpdatesInfo")
//    public Object getAppUpdatesInfo() { return appUpdatesInfo; }
//    @JsonProperty("appUpdatesInfo")
//    public void setAppUpdatesInfo(Object value) { this.appUpdatesInfo = value; }
//
//    @JsonProperty("deviceInfo")
//    public DeviceInfo getDeviceInfo() { return deviceInfo; }
//    @JsonProperty("deviceInfo")
//    public void setDeviceInfo(DeviceInfo value) { this.deviceInfo = value; }
//
//    @JsonProperty("memoryInfo")
//    public Object getMemoryInfo() { return memoryInfo; }
//    @JsonProperty("memoryInfo")
//    public void setMemoryInfo(Object value) { this.memoryInfo = value; }
//
//    @JsonProperty("networkInfo")
//    public Object getNetworkInfo() { return networkInfo; }
//    @JsonProperty("networkInfo")
//    public void setNetworkInfo(Object value) { this.networkInfo = value; }
//
//    @JsonProperty("blotoutSDKsInfo")
//    public Object getBlotoutSDKsInfo() { return blotoutSDKsInfo; }
//    @JsonProperty("blotoutSDKsInfo")
//    public void setBlotoutSDKsInfo(Object value) { this.blotoutSDKsInfo = value; }
//
//    @JsonProperty("location")
//    public Location getLocation() { return location; }
//    @JsonProperty("location")
//    public void setLocation(Location value) { this.location = value; }
//
//    @JsonProperty("appLanguagesSupported")
//    public Object getAppLanguagesSupported() { return appLanguagesSupported; }
//    @JsonProperty("appLanguagesSupported")
//    public void setAppLanguagesSupported(Object value) { this.appLanguagesSupported = value; }
//
//    @JsonProperty("appSupportShakeToEdit")
//    public Object getAppSupportShakeToEdit() { return appSupportShakeToEdit; }
//    @JsonProperty("appSupportShakeToEdit")
//    public void setAppSupportShakeToEdit(Object value) { this.appSupportShakeToEdit = value; }
//
//    @JsonProperty("appInstallInfo")
//    public Object getAppInstallInfo() { return appInstallInfo; }
//    @JsonProperty("appInstallInfo")
//    public void setAppInstallInfo(Object value) { this.appInstallInfo = value; }
//
//    @JsonProperty("appLaunchInfo")
//    public Object getAppLaunchInfo() { return appLaunchInfo; }
//    @JsonProperty("appLaunchInfo")
//    public void setAppLaunchInfo(Object value) { this.appLaunchInfo = value; }
//
//    @JsonProperty("storageInfo")
//    public Object getStorageInfo() { return storageInfo; }
//    @JsonProperty("storageInfo")
//    public void setStorageInfo(Object value) { this.storageInfo = value; }
//
//    @JsonProperty("appSupportRemoteNotifications")
//    public Object getAppSupportRemoteNotifications() { return appSupportRemoteNotifications; }
//    @JsonProperty("appSupportRemoteNotifications")
//    public void setAppSupportRemoteNotifications(Object value) { this.appSupportRemoteNotifications = value; }
//
//    @JsonProperty("retentionEvent")
//    public RetentionEvent getRetentionEvent() { return retentionEvent; }
//    @JsonProperty("retentionEvent")
//    public void setRetentionEvent(RetentionEvent value) { this.retentionEvent = value; }
//
//    @JsonProperty("appCategory")
//    public Object getAppCategory() { return appCategory; }
//    @JsonProperty("appCategory")
//    public void setAppCategory(Object value) { this.appCategory = value; }
//
//    @JsonProperty("dateAndTime")
//    public String getDateAndTime() { return dateAndTime; }
//    @JsonProperty("dateAndTime")
//    public void setDateAndTime(String value) { this.dateAndTime = value; }
//}
//
//// BOSessionDeviceInfo.java
//class DeviceInfo {
//    private Object deviceModel;
//    private Object systemName;
//    private Object processorsUsage;
//    private Object fullyCharged;
//    private Object appUIHeight;
//    private Object otherIDs;
//    private Object deviceScreenHeight;
//    private Object country;
//    private Object currency;
//    private Object batteryLevelPercentage;
//    private Object language;
//    private Object floorCountingAvailable;
//    private Object timeZone;
//    private Object stepCountingAvailable;
//    private Object pluggedIn;
//    private Object accessoriesAttached;
//    private Object nameOfAttachedAccessories;
//    private Object multitaskingEnabled;
//    private Object sentToServer;
//    private Object clipboardContent;
//    private Object numberOfAttachedAccessories;
//    private Object deviceScreenWidth;
//    private Object systemVersion;
//    private Object headphoneAttached;
//    private Object screenBrightness;
//    private Object debuggerAttached;
//    private Object distanceAvailbale;
//    private Object timeStamp;
//    private Object appUIWidth;
//    private String cfUUID;
//    private Object deviceName;
//    private Object systemDeviceTypeUnformatted;
//    private Object jailBroken;
//    private Object systemDeviceTypeFormatted;
//    private String vendorID;
//    private Object numberOfProcessors;
//    private Object isCharging;
//    private boolean doNotTrackEnabled;
//    private Object advertisingID;
//    private Object numberOfActiveProcessors;
//    private Object proximitySensorEnabled;
//    private Object deviceOrientation;
//
//    @JsonProperty("deviceModel")
//    public Object getDeviceModel() { return deviceModel; }
//    @JsonProperty("deviceModel")
//    public void setDeviceModel(Object value) { this.deviceModel = value; }
//
//    @JsonProperty("systemName")
//    public Object getSystemName() { return systemName; }
//    @JsonProperty("systemName")
//    public void setSystemName(Object value) { this.systemName = value; }
//
//    @JsonProperty("processorsUsage")
//    public Object getProcessorsUsage() { return processorsUsage; }
//    @JsonProperty("processorsUsage")
//    public void setProcessorsUsage(Object value) { this.processorsUsage = value; }
//
//    @JsonProperty("fullyCharged")
//    public Object getFullyCharged() { return fullyCharged; }
//    @JsonProperty("fullyCharged")
//    public void setFullyCharged(Object value) { this.fullyCharged = value; }
//
//    @JsonProperty("appUIHeight")
//    public Object getAppUIHeight() { return appUIHeight; }
//    @JsonProperty("appUIHeight")
//    public void setAppUIHeight(Object value) { this.appUIHeight = value; }
//
//    @JsonProperty("otherIDs")
//    public Object getOtherIDs() { return otherIDs; }
//    @JsonProperty("otherIDs")
//    public void setOtherIDs(Object value) { this.otherIDs = value; }
//
//    @JsonProperty("deviceScreenHeight")
//    public Object getDeviceScreenHeight() { return deviceScreenHeight; }
//    @JsonProperty("deviceScreenHeight")
//    public void setDeviceScreenHeight(Object value) { this.deviceScreenHeight = value; }
//
//    @JsonProperty("country")
//    public Object getCountry() { return country; }
//    @JsonProperty("country")
//    public void setCountry(Object value) { this.country = value; }
//
//    @JsonProperty("currency")
//    public Object getCurrency() { return currency; }
//    @JsonProperty("currency")
//    public void setCurrency(Object value) { this.currency = value; }
//
//    @JsonProperty("batteryLevelPercentage")
//    public Object getBatteryLevelPercentage() { return batteryLevelPercentage; }
//    @JsonProperty("batteryLevelPercentage")
//    public void setBatteryLevelPercentage(Object value) { this.batteryLevelPercentage = value; }
//
//    @JsonProperty("Language")
//    public Object getLanguage() { return language; }
//    @JsonProperty("Language")
//    public void setLanguage(Object value) { this.language = value; }
//
//    @JsonProperty("floorCountingAvailable")
//    public Object getFloorCountingAvailable() { return floorCountingAvailable; }
//    @JsonProperty("floorCountingAvailable")
//    public void setFloorCountingAvailable(Object value) { this.floorCountingAvailable = value; }
//
//    @JsonProperty("timeZone")
//    public Object getTimeZone() { return timeZone; }
//    @JsonProperty("timeZone")
//    public void setTimeZone(Object value) { this.timeZone = value; }
//
//    @JsonProperty("stepCountingAvailable")
//    public Object getStepCountingAvailable() { return stepCountingAvailable; }
//    @JsonProperty("stepCountingAvailable")
//    public void setStepCountingAvailable(Object value) { this.stepCountingAvailable = value; }
//
//    @JsonProperty("pluggedIn")
//    public Object getPluggedIn() { return pluggedIn; }
//    @JsonProperty("pluggedIn")
//    public void setPluggedIn(Object value) { this.pluggedIn = value; }
//
//    @JsonProperty("accessoriesAttached")
//    public Object getAccessoriesAttached() { return accessoriesAttached; }
//    @JsonProperty("accessoriesAttached")
//    public void setAccessoriesAttached(Object value) { this.accessoriesAttached = value; }
//
//    @JsonProperty("nameOfAttachedAccessories")
//    public Object getNameOfAttachedAccessories() { return nameOfAttachedAccessories; }
//    @JsonProperty("nameOfAttachedAccessories")
//    public void setNameOfAttachedAccessories(Object value) { this.nameOfAttachedAccessories = value; }
//
//    @JsonProperty("multitaskingEnabled")
//    public Object getMultitaskingEnabled() { return multitaskingEnabled; }
//    @JsonProperty("multitaskingEnabled")
//    public void setMultitaskingEnabled(Object value) { this.multitaskingEnabled = value; }
//
//    @JsonProperty("sentToServer")
//    public Object getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(Object value) { this.sentToServer = value; }
//
//    @JsonProperty("clipboardContent")
//    public Object getClipboardContent() { return clipboardContent; }
//    @JsonProperty("clipboardContent")
//    public void setClipboardContent(Object value) { this.clipboardContent = value; }
//
//    @JsonProperty("numberOfAttachedAccessories")
//    public Object getNumberOfAttachedAccessories() { return numberOfAttachedAccessories; }
//    @JsonProperty("numberOfAttachedAccessories")
//    public void setNumberOfAttachedAccessories(Object value) { this.numberOfAttachedAccessories = value; }
//
//    @JsonProperty("deviceScreenWidth")
//    public Object getDeviceScreenWidth() { return deviceScreenWidth; }
//    @JsonProperty("deviceScreenWidth")
//    public void setDeviceScreenWidth(Object value) { this.deviceScreenWidth = value; }
//
//    @JsonProperty("systemVersion")
//    public Object getSystemVersion() { return systemVersion; }
//    @JsonProperty("systemVersion")
//    public void setSystemVersion(Object value) { this.systemVersion = value; }
//
//    @JsonProperty("headphoneAttached")
//    public Object getHeadphoneAttached() { return headphoneAttached; }
//    @JsonProperty("headphoneAttached")
//    public void setHeadphoneAttached(Object value) { this.headphoneAttached = value; }
//
//    @JsonProperty("screenBrightness")
//    public Object getScreenBrightness() { return screenBrightness; }
//    @JsonProperty("screenBrightness")
//    public void setScreenBrightness(Object value) { this.screenBrightness = value; }
//
//    @JsonProperty("debuggerAttached")
//    public Object getDebuggerAttached() { return debuggerAttached; }
//    @JsonProperty("debuggerAttached")
//    public void setDebuggerAttached(Object value) { this.debuggerAttached = value; }
//
//    @JsonProperty("distanceAvailbale")
//    public Object getDistanceAvailbale() { return distanceAvailbale; }
//    @JsonProperty("distanceAvailbale")
//    public void setDistanceAvailbale(Object value) { this.distanceAvailbale = value; }
//
//    @JsonProperty("timeStamp")
//    public Object getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(Object value) { this.timeStamp = value; }
//
//    @JsonProperty("appUIWidth")
//    public Object getAppUIWidth() { return appUIWidth; }
//    @JsonProperty("appUIWidth")
//    public void setAppUIWidth(Object value) { this.appUIWidth = value; }
//
//    @JsonProperty("cfUUID")
//    public String getCFUUID() { return cfUUID; }
//    @JsonProperty("cfUUID")
//    public void setCFUUID(String value) { this.cfUUID = value; }
//
//    @JsonProperty("deviceName")
//    public Object getDeviceName() { return deviceName; }
//    @JsonProperty("deviceName")
//    public void setDeviceName(Object value) { this.deviceName = value; }
//
//    @JsonProperty("systemDeviceTypeUnformatted")
//    public Object getSystemDeviceTypeUnformatted() { return systemDeviceTypeUnformatted; }
//    @JsonProperty("systemDeviceTypeUnformatted")
//    public void setSystemDeviceTypeUnformatted(Object value) { this.systemDeviceTypeUnformatted = value; }
//
//    @JsonProperty("jailBroken")
//    public Object getJailBroken() { return jailBroken; }
//    @JsonProperty("jailBroken")
//    public void setJailBroken(Object value) { this.jailBroken = value; }
//
//    @JsonProperty("systemDeviceTypeFormatted")
//    public Object getSystemDeviceTypeFormatted() { return systemDeviceTypeFormatted; }
//    @JsonProperty("systemDeviceTypeFormatted")
//    public void setSystemDeviceTypeFormatted(Object value) { this.systemDeviceTypeFormatted = value; }
//
//    @JsonProperty("vendorID")
//    public String getVendorID() { return vendorID; }
//    @JsonProperty("vendorID")
//    public void setVendorID(String value) { this.vendorID = value; }
//
//    @JsonProperty("numberOfProcessors")
//    public Object getNumberOfProcessors() { return numberOfProcessors; }
//    @JsonProperty("numberOfProcessors")
//    public void setNumberOfProcessors(Object value) { this.numberOfProcessors = value; }
//
//    @JsonProperty("isCharging")
//    public Object getIsCharging() { return isCharging; }
//    @JsonProperty("isCharging")
//    public void setIsCharging(Object value) { this.isCharging = value; }
//
//    @JsonProperty("doNotTrackEnabled")
//    public boolean getDoNotTrackEnabled() { return doNotTrackEnabled; }
//    @JsonProperty("doNotTrackEnabled")
//    public void setDoNotTrackEnabled(boolean value) { this.doNotTrackEnabled = value; }
//
//    @JsonProperty("advertisingID")
//    public Object getAdvertisingID() { return advertisingID; }
//    @JsonProperty("advertisingID")
//    public void setAdvertisingID(Object value) { this.advertisingID = value; }
//
//    @JsonProperty("numberOfActiveProcessors")
//    public Object getNumberOfActiveProcessors() { return numberOfActiveProcessors; }
//    @JsonProperty("numberOfActiveProcessors")
//    public void setNumberOfActiveProcessors(Object value) { this.numberOfActiveProcessors = value; }
//
//    @JsonProperty("proximitySensorEnabled")
//    public Object getProximitySensorEnabled() { return proximitySensorEnabled; }
//    @JsonProperty("proximitySensorEnabled")
//    public void setProximitySensorEnabled(Object value) { this.proximitySensorEnabled = value; }
//
//    @JsonProperty("deviceOrientation")
//    public Object getDeviceOrientation() { return deviceOrientation; }
//    @JsonProperty("deviceOrientation")
//    public void setDeviceOrientation(Object value) { this.deviceOrientation = value; }
//}
//
//// BOLocation.java
//class Location {
//    private long timeStamp;
//    private NonPIILocation nonPIILocation;
//    private PiiLocation piiLocation;
//    private boolean sentToServer;
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("nonPIILocation")
//    public NonPIILocation getNonPIILocation() { return nonPIILocation; }
//    @JsonProperty("nonPIILocation")
//    public void setNonPIILocation(NonPIILocation value) { this.nonPIILocation = value; }
//
//    @JsonProperty("piiLocation")
//    public PiiLocation getPiiLocation() { return piiLocation; }
//    @JsonProperty("piiLocation")
//    public void setPiiLocation(PiiLocation value) { this.piiLocation = value; }
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//}
//
//// BONonPIILocation.java
//
// class NonPIILocation {
//    private Object activity;
//    private Object state;
//    private Object country;
//    private Object city;
//    private Object zip;
//    private Object source;
//
//    @JsonProperty("activity")
//    public Object getActivity() { return activity; }
//    @JsonProperty("activity")
//    public void setActivity(Object value) { this.activity = value; }
//
//    @JsonProperty("state")
//    public Object getState() { return state; }
//    @JsonProperty("state")
//    public void setState(Object value) { this.state = value; }
//
//    @JsonProperty("country")
//    public Object getCountry() { return country; }
//    @JsonProperty("country")
//    public void setCountry(Object value) { this.country = value; }
//
//    @JsonProperty("city")
//    public Object getCity() { return city; }
//    @JsonProperty("city")
//    public void setCity(Object value) { this.city = value; }
//
//    @JsonProperty("zip")
//    public Object getZip() { return zip; }
//    @JsonProperty("zip")
//    public void setZip(Object value) { this.zip = value; }
//
//    @JsonProperty("source")
//    public Object getSource() { return source; }
//    @JsonProperty("source")
//    public void setSource(Object value) { this.source = value; }
//}
//
//// BOPiiLocation.java
//
// class PiiLocation {
//    private Object longitude;
//    private Object latitude;
//
//    @JsonProperty("longitude")
//    public Object getLongitude() { return longitude; }
//    @JsonProperty("longitude")
//    public void setLongitude(Object value) { this.longitude = value; }
//
//    @JsonProperty("latitude")
//    public Object getLatitude() { return latitude; }
//    @JsonProperty("latitude")
//    public void setLatitude(Object value) { this.latitude = value; }
//}
//
//// BORetentionEvent.java
//
// class RetentionEvent {
//    private AST mast;
//    private boolean sentToServer;
//    private AST wast;
//    private Mau mau;
//    private NewUser newUser;
//    private Object customEvents;
//    private AST dast;
//    private Object wpu;
//    private Object dpu;
//    private Object mpu;
//    private Wau wau;
//    private AppInstalled appInstalled;
//    private Dau dau;
//
//    @JsonProperty("MAST")
//    public AST getMast() { return mast; }
//    @JsonProperty("MAST")
//    public void setMast(AST value) { this.mast = value; }
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("WAST")
//    public AST getWast() { return wast; }
//    @JsonProperty("WAST")
//    public void setWast(AST value) { this.wast = value; }
//
//    @JsonProperty("MAU")
//    public Mau getMau() { return mau; }
//    @JsonProperty("MAU")
//    public void setMau(Mau value) { this.mau = value; }
//
//    @JsonProperty("newUser")
//    public NewUser getNewUser() { return newUser; }
//    @JsonProperty("newUser")
//    public void setNewUser(NewUser value) { this.newUser = value; }
//
//    @JsonProperty("customEvents")
//    public Object getCustomEvents() { return customEvents; }
//    @JsonProperty("customEvents")
//    public void setCustomEvents(Object value) { this.customEvents = value; }
//
//    @JsonProperty("DAST")
//    public AST getDast() { return dast; }
//    @JsonProperty("DAST")
//    public void setDast(AST value) { this.dast = value; }
//
//    @JsonProperty("WPU")
//    public Object getWpu() { return wpu; }
//    @JsonProperty("WPU")
//    public void setWpu(Object value) { this.wpu = value; }
//
//    @JsonProperty("DPU")
//    public Object getDpu() { return dpu; }
//    @JsonProperty("DPU")
//    public void setDpu(Object value) { this.dpu = value; }
//
//    @JsonProperty("MPU")
//    public Object getMPU() { return mpu; }
//    @JsonProperty("MPU")
//    public void setMPU(Object value) { this.mpu = value; }
//
//    @JsonProperty("WAU")
//    public Wau getWau() { return wau; }
//    @JsonProperty("WAU")
//    public void setWau(Wau value) { this.wau = value; }
//
//    @JsonProperty("appInstalled")
//    public AppInstalled getAppInstalled() { return appInstalled; }
//    @JsonProperty("appInstalled")
//    public void setAppInstalled(AppInstalled value) { this.appInstalled = value; }
//
//    @JsonProperty("DAU")
//    public Dau getDau() { return dau; }
//    @JsonProperty("DAU")
//    public void setDau(Dau value) { this.dau = value; }
//}
//
//// BOAppInstalled.java
//
// class AppInstalled {
//    private long timeStamp;
//    private boolean isFirstLaunch;
//    private Object appInstalledInfo;
//    private boolean sentToServer;
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("isFirstLaunch")
//    public boolean getIsFirstLaunch() { return isFirstLaunch; }
//    @JsonProperty("isFirstLaunch")
//    public void setIsFirstLaunch(boolean value) { this.isFirstLaunch = value; }
//
//    @JsonProperty("appInstalledInfo")
//    public Object getAppInstalledInfo() { return appInstalledInfo; }
//    @JsonProperty("appInstalledInfo")
//    public void setAppInstalledInfo(Object value) { this.appInstalledInfo = value; }
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//}
//
//// AST.java
// class AST {
//    private long timeStamp;
//    private Object mastInfo;
//    private Object dastInfo;
//    private long averageSessionTime;
//    private Object wastInfo;
//    private boolean sentToServer;
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//
//    @JsonProperty("mastInfo")
//    public Object getMastInfo() { return mastInfo; }
//    @JsonProperty("mastInfo")
//    public void setMastInfo(Object value) { this.mastInfo = value; }
//
//    @JsonProperty("dastInfo")
//    public Object getDastInfo() { return dastInfo; }
//    @JsonProperty("dastInfo")
//    public void setDastInfo(Object value) { this.dastInfo = value; }
//
//    @JsonProperty("averageSessionTime")
//    public long getAverageSessionTime() { return averageSessionTime; }
//    @JsonProperty("averageSessionTime")
//    public void setAverageSessionTime(long value) { this.averageSessionTime = value; }
//
//    @JsonProperty("wastInfo")
//    public Object getWastInfo() { return wastInfo; }
//    @JsonProperty("wastInfo")
//    public void setWastInfo(Object value) { this.wastInfo = value; }
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//}
//
//// BODau.java
//
// class Dau {
//    private boolean sentToServer;
//    private Object dauInfo;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("dauInfo")
//    public Object getDauInfo() { return dauInfo; }
//    @JsonProperty("dauInfo")
//    public void setDauInfo(Object value) { this.dauInfo = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// Mau.java
//
// class Mau {
//    private boolean sentToServer;
//    private Object mauInfo;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("mauInfo")
//    public Object getMauInfo() { return mauInfo; }
//    @JsonProperty("mauInfo")
//    public void setMauInfo(Object value) { this.mauInfo = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
//
//// BONewUser.java
//
// class NewUser {
//    private boolean isNewUser;
//    private long timeStamp;
//    private Object newUserInfo;
//    private boolean sentToServer;
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
//    public Object getNewUserInfo() { return newUserInfo; }
//    @JsonProperty("newUserInfo")
//    public void setNewUserInfo(Object value) { this.newUserInfo = value; }
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//}
//
//// Wau.java
//
// class Wau {
//    private boolean sentToServer;
//    private Object wauInfo;
//    private long timeStamp;
//
//    @JsonProperty("sentToServer")
//    public boolean getSentToServer() { return sentToServer; }
//    @JsonProperty("sentToServer")
//    public void setSentToServer(boolean value) { this.sentToServer = value; }
//
//    @JsonProperty("wauInfo")
//    public Object getWauInfo() { return wauInfo; }
//    @JsonProperty("wauInfo")
//    public void setWauInfo(Object value) { this.wauInfo = value; }
//
//    @JsonProperty("timeStamp")
//    public long getTimeStamp() { return timeStamp; }
//    @JsonProperty("timeStamp")
//    public void setTimeStamp(long value) { this.timeStamp = value; }
//}
