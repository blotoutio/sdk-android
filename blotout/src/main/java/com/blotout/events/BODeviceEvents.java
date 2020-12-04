package com.blotout.events;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.blotout.analytics.BOSharedManager;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.deviceinfo.ads.AdInfo;
import com.blotout.deviceinfo.device.DeviceInfo;
import com.blotout.io.BuildConfig;
import com.blotout.model.session.BOAccessoriesAttached;
import com.blotout.model.session.BOAdInfo;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.model.session.BOBatteryLevel;
import com.blotout.model.session.BOBroadcastAddress;
import com.blotout.model.session.BOCFUUID;
import com.blotout.model.session.BOConnectedTo;
import com.blotout.model.session.BODeviceOrientation;
import com.blotout.model.session.BOIPAddress;
import com.blotout.model.session.BOMemoryInfo;
import com.blotout.model.session.BONameOfAttachedAccessory;
import com.blotout.model.session.BONetMask;
import com.blotout.model.session.BONumberOfA;
import com.blotout.model.session.BOProcessorsUsage;
import com.blotout.model.session.BOSingleDaySessions;
import com.blotout.model.session.BOStorageInfo;
import com.blotout.model.session.BOVendorID;
import com.blotout.model.session.BOWifiRouterAddress;
import com.blotout.model.session.BOWifiSSID;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Blotout on 03,November,2019
 */
public class BODeviceEvents {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BODeviceEvents";
    private Context mContext;
    private DeviceInfo mDeviceInfo;
    private AdInfo mAdInfo;
    public boolean isEnabled;
    private static volatile BODeviceEvents instance;

    public static BODeviceEvents getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BODeviceEvents.class) {
                if (instance == null) {
                    instance = new BODeviceEvents();
                }
            }
        }
        return instance;
    }

    private BODeviceEvents() {
        mContext = BOSharedManager.getInstance().getContext();
        mDeviceInfo = new DeviceInfo(mContext);
        mAdInfo = new AdInfo(mContext);
    }

    public void recordDeviceEvents() {
        try {
        if (this.isEnabled) {

            BOSingleDaySessions singalDaySessions =  BOAppSessionDataModel.sharedInstanceFromJSONDictionary(
                        null).getSingleDaySessions();
            if(singalDaySessions != null) {
                HashMap<String, Object> stringObjectHashMap = new HashMap<>();
                stringObjectHashMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                stringObjectHashMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                stringObjectHashMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("MultiTaskingState"));
                stringObjectHashMap.put(BOCommonConstants.BO_STATUS, false); // BOCommonUtils.isMultitaskEnabled(mContext));
                stringObjectHashMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAccessoriesAttached boAccessoriesAttached = BOAccessoriesAttached.fromJsonDictionary(
                        stringObjectHashMap);
                List<BOAccessoriesAttached> multitaskingEnabledList =
                        singalDaySessions.getDeviceInfo().getMultitaskingEnabled();
                multitaskingEnabledList.add(boAccessoriesAttached);
                singalDaySessions.getDeviceInfo().setMultitaskingEnabled(
                        multitaskingEnabledList);


                // existingProximitySensors
                HashMap<String, Object> proximityHashMap = new HashMap<>();
                proximityHashMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                proximityHashMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("ProximitySensorState"));
                proximityHashMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                proximityHashMap.put(BOCommonConstants.BO_STATUS,
                        BOCommonUtils.getSensorEnabled(mContext, Sensor.TYPE_PROXIMITY));
                proximityHashMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAccessoriesAttached boProximitySensor = BOAccessoriesAttached.fromJsonDictionary(
                        proximityHashMap);
                List<BOAccessoriesAttached> boProximitySensorList =
                        singalDaySessions.getDeviceInfo().getProximitySensorEnabled();
                boProximitySensorList.add(boProximitySensor);
                singalDaySessions.getDeviceInfo().setProximitySensorEnabled(
                        boProximitySensorList);


                // debugger attached
                HashMap<String, Object> debuggerAttachedMap = new HashMap<>();
                debuggerAttachedMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                debuggerAttachedMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("DebuggerAttachedState"));
                debuggerAttachedMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                debuggerAttachedMap.put(BOCommonConstants.BO_STATUS, (BuildConfig.DEBUG));
                debuggerAttachedMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAccessoriesAttached boAccessoriesAttachedDebugger =
                        BOAccessoriesAttached.fromJsonDictionary(
                                debuggerAttachedMap);
                List<BOAccessoriesAttached> boAccessoriesAttachedDebuggerList =
                        singalDaySessions.getDeviceInfo().getDebuggerAttached();
                boAccessoriesAttachedDebuggerList.add(boAccessoriesAttachedDebugger);
                singalDaySessions.getDeviceInfo().setDebuggerAttached(
                        boAccessoriesAttachedDebuggerList);


                // plugged in
                HashMap<String, Object> pluggedInMap = new HashMap<>();
                pluggedInMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                pluggedInMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("PluggedIn"));
                pluggedInMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                pluggedInMap.put(BOCommonConstants.BO_STATUS, mDeviceInfo.isPhoneCharging());
                pluggedInMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAccessoriesAttached boAccessoriesAttachedPluggedIn =
                        BOAccessoriesAttached.fromJsonDictionary(
                                pluggedInMap);
                List<BOAccessoriesAttached> boAccessoriesAttachedPluggedInList =
                        singalDaySessions.getDeviceInfo().getPluggedIn();
                boAccessoriesAttachedPluggedInList.add(boAccessoriesAttachedPluggedIn);
                singalDaySessions.getDeviceInfo().setPluggedIn(
                        boAccessoriesAttachedPluggedInList);


                // jail broken/rooted devices check
                HashMap<String, Object> rootedDeviceMap = new HashMap<>();
                rootedDeviceMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                rootedDeviceMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("JailBroken"));
                rootedDeviceMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                rootedDeviceMap.put(BOCommonConstants.BO_STATUS, mDeviceInfo.isDeviceRooted());
                rootedDeviceMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAccessoriesAttached boAccessoriesAttachedRootedDevice =
                        BOAccessoriesAttached.fromJsonDictionary(
                                rootedDeviceMap);
                List<BOAccessoriesAttached> boAccessoriesAttachedRootedDeviceList =
                        singalDaySessions.getDeviceInfo().getJailBroken();
                boAccessoriesAttachedRootedDeviceList.add(boAccessoriesAttachedRootedDevice);
                singalDaySessions.getDeviceInfo().setJailBroken(
                        boAccessoriesAttachedRootedDeviceList);


                // Number of active processor cores
                HashMap<String, Object> noOfActiveCoresMap = new HashMap<>();
                noOfActiveCoresMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                noOfActiveCoresMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("ActiveProcessor"));
                noOfActiveCoresMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                noOfActiveCoresMap.put(BOCommonConstants.BO_NUMBER, BOCommonUtils.getNumberOfActiveCores());
                noOfActiveCoresMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BONumberOfA boNumberOfA = BONumberOfA.fromJsonDictionary(
                        noOfActiveCoresMap);
                List<BONumberOfA> boNumberOfASList =
                        singalDaySessions.getDeviceInfo().getNumberOfActiveProcessors();
                boNumberOfASList.add(boNumberOfA);
                singalDaySessions.getDeviceInfo().setNumberOfActiveProcessors(
                        boNumberOfASList);


                // BOProcessorsUsage

                ActivityManager activityManager = (ActivityManager) mContext.getSystemService(
                        Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> pidsTask =
                        activityManager.getRunningAppProcesses();

                for (int i = 0; i < pidsTask.size(); i++) {

                    HashMap<String, Object> processorUsageMap = new HashMap<>();
                    processorUsageMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    processorUsageMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("ProcessorPercentage"));
                    processorUsageMap.put(BOCommonConstants.BO_TIME_STAMP,
                            BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    ActivityManager.RunningAppProcessInfo processInfo = pidsTask.get(i);
                    processorUsageMap.put(BOCommonConstants.BO_PROCESSOR_ID, processInfo.pid);
                    //TODO: need to get cpu usage percentage.Hardcoded as 10
                    processorUsageMap.put(BOCommonConstants.BO_USAGE_PERCENTAGE, 10);
                    processorUsageMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                    BOProcessorsUsage boProcessorsUsage = BOProcessorsUsage.fromJsonDictionary(
                            processorUsageMap);
                    List<BOProcessorsUsage> boProcessorsUsagesList =
                            singalDaySessions.getDeviceInfo().getProcessorsUsage();
                    boProcessorsUsagesList.add(boProcessorsUsage);
                    singalDaySessions.getDeviceInfo().setProcessorsUsage(
                            boProcessorsUsagesList);
                }


                // getAccessoriesAttached
                HashMap<String, Object> accessoriesAttachedMap = new HashMap<>();
                accessoriesAttachedMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                accessoriesAttachedMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                accessoriesAttachedMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("AccessoriesAttached"));
                accessoriesAttachedMap.put(BOCommonConstants.BO_STATUS, BOCommonUtils.iSAccessoriesAttached(mContext));
                accessoriesAttachedMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAccessoriesAttached accessoriesAttached = BOAccessoriesAttached.fromJsonDictionary(
                        accessoriesAttachedMap);
                List<BOAccessoriesAttached> accessoriesAttachedList =
                        singalDaySessions.getDeviceInfo().getAccessoriesAttached();
                accessoriesAttachedList.add(accessoriesAttached);
                singalDaySessions.getDeviceInfo().setAccessoriesAttached(
                        accessoriesAttachedList);

                // headphoneAttached
                HashMap<String, Object> headphoneAttachedMap = new HashMap<>();
                headphoneAttachedMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                headphoneAttachedMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                headphoneAttachedMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("HeadphoneAttached"));
                headphoneAttachedMap.put(BOCommonConstants.BO_STATUS, BOCommonUtils.iSAccessoriesAttached(mContext));
                headphoneAttachedMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAccessoriesAttached headphoneAttached = BOAccessoriesAttached.fromJsonDictionary(
                        headphoneAttachedMap);
                List<BOAccessoriesAttached> headphoneAttachedList =
                        singalDaySessions.getDeviceInfo().getHeadphoneAttached();
                headphoneAttachedList.add(headphoneAttached);
                singalDaySessions.getDeviceInfo().setHeadphoneAttached(
                        accessoriesAttachedList);

                // numberAttachedAccessories
                HashMap<String, Object> noOfAccessoriesAttachedMap = new HashMap<>();
                noOfAccessoriesAttachedMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                noOfAccessoriesAttachedMap.put(BOCommonConstants.BO_TIME_STAMP,
                        BODateTimeUtils.get13DigitNumberObjTimeStamp());
                noOfAccessoriesAttachedMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("NumberOfAccessories"));
                noOfAccessoriesAttachedMap.put(BOCommonConstants.BO_NUMBER,
                        BOCommonUtils.getNameAccessoriesAttached(mContext).size());
                noOfAccessoriesAttachedMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BONumberOfA boNumberOfA1 = BONumberOfA.fromJsonDictionary(
                        noOfAccessoriesAttachedMap);
                List<BONumberOfA> boNumberOfASList1 =
                        singalDaySessions.getDeviceInfo().getNumberOfAttachedAccessories();
                boNumberOfASList1.add(boNumberOfA1);
                singalDaySessions.getDeviceInfo().setNumberOfAttachedAccessories(
                        boNumberOfASList1);


                // numberAttachedAccessories
                HashMap<String, Object> nameOfAttachedAccessoriesMap = new HashMap<>();
                nameOfAttachedAccessoriesMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                nameOfAttachedAccessoriesMap.put(BOCommonConstants.BO_TIME_STAMP,
                        BODateTimeUtils.get13DigitNumberObjTimeStamp());
                nameOfAttachedAccessoriesMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("NameOfAccessory"));
                nameOfAttachedAccessoriesMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                nameOfAttachedAccessoriesMap.put(BOCommonConstants.BO_NAMES,
                        BOCommonUtils.getNameAccessoriesAttached(mContext));

                BONameOfAttachedAccessory nameOfAttachedAccessories =
                        BONameOfAttachedAccessory.fromJsonDictionary(
                                nameOfAttachedAccessoriesMap);
                List<BONameOfAttachedAccessory> nameOfAttachedAccessoriesList =
                        singalDaySessions.getDeviceInfo().getNameOfAttachedAccessories();
                nameOfAttachedAccessoriesList.add(nameOfAttachedAccessories);
                singalDaySessions.getDeviceInfo().setNameOfAttachedAccessories(
                        nameOfAttachedAccessoriesList);


                // BOBatteryLevel
                HashMap<String, Object> batteryLevelMap = new HashMap<>();
                batteryLevelMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                batteryLevelMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                batteryLevelMap.put(BOCommonConstants.BO_PERCENTAGE, mDeviceInfo.getBatteryPercent());
                batteryLevelMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("BatteryLevel"));
                batteryLevelMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOBatteryLevel batteryLevel = BOBatteryLevel.fromJsonDictionary(
                        batteryLevelMap);
                List<BOBatteryLevel> batteryLevelList =
                        singalDaySessions.getDeviceInfo().getBatteryLevel();
                batteryLevelList.add(batteryLevel);
                singalDaySessions.getDeviceInfo().setBatteryLevel(
                        batteryLevelList);


                // Battery charging
                HashMap<String, Object> batteryChargingMap = new HashMap<>();
                batteryChargingMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                batteryChargingMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                batteryChargingMap.put(BOCommonConstants.BO_STATUS, mDeviceInfo.isPhoneCharging());
                batteryChargingMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("BatteryCharging"));
                batteryChargingMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAccessoriesAttached batteryCharging = BOAccessoriesAttached.fromJsonDictionary(
                        batteryChargingMap);
                List<BOAccessoriesAttached> batteryChargingList =
                        singalDaySessions.getDeviceInfo().getIsCharging();
                batteryChargingList.add(batteryCharging);
                singalDaySessions.getDeviceInfo().setIsCharging(
                        batteryChargingList);


                // Battery fully charged
                HashMap<String, Object> fullyChargedBatteryMap = new HashMap<>();
                fullyChargedBatteryMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                fullyChargedBatteryMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                fullyChargedBatteryMap.put(BOCommonConstants.BO_STATUS, (mDeviceInfo.getBatteryPercent() == 100));
                fullyChargedBatteryMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("BatteryFullCharged"));
                fullyChargedBatteryMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAccessoriesAttached fullyChargedBattery = BOAccessoriesAttached.fromJsonDictionary(
                        fullyChargedBatteryMap);
                List<BOAccessoriesAttached> fullyChargedBatteryList =
                        singalDaySessions.getDeviceInfo().getFullyCharged();
                fullyChargedBatteryList.add(fullyChargedBattery);
                singalDaySessions.getDeviceInfo().setFullyCharged(
                        fullyChargedBatteryList);


                // Device orientation
                HashMap<String, Object> deviceOrientationMap = new HashMap<>();
                deviceOrientationMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                deviceOrientationMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                deviceOrientationMap.put(BOCommonConstants.BO_ORIENTATION,
                        mContext.getResources().getConfiguration().orientation);
                deviceOrientationMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_DEVICE_ORIENTATION));
                deviceOrientationMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BODeviceOrientation boDeviceOrientation = BODeviceOrientation.fromJsonDictionary(
                        deviceOrientationMap);
                List<BODeviceOrientation> deviceOrientationList =
                        singalDaySessions.getDeviceInfo().getDeviceOrientation();
                deviceOrientationList.add(boDeviceOrientation);
                singalDaySessions.getDeviceInfo().setDeviceOrientation(
                        deviceOrientationList);


                // UUID:
                HashMap<String, Object> bocfuuidMap = new HashMap<>();
                bocfuuidMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                bocfuuidMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                bocfuuidMap.put("cfUUID",
                        mDeviceInfo.getAndroidId());
                bocfuuidMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("CFUUID"));
                bocfuuidMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOCFUUID bocfuuid = BOCFUUID.fromJsonDictionary(
                        bocfuuidMap);
                List<BOCFUUID> bocfuuidList =
                        singalDaySessions.getDeviceInfo().getcfUUID();
                bocfuuidList.add(bocfuuid);
                singalDaySessions.getDeviceInfo().setcfUUID(
                        bocfuuidList);

                // vendorID
                HashMap<String, Object> boVendorIDMap = new HashMap<>();
                boVendorIDMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                boVendorIDMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                // val manufacturer = Build.MANUFACTURER + val model = Build.MODEL
                boVendorIDMap.put("vendorID", mDeviceInfo.getDeviceName());
                boVendorIDMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("VendorID"));
                boVendorIDMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOVendorID boVendorID = BOVendorID.fromJsonDictionary(
                        boVendorIDMap);
                List<BOVendorID> boVendorIDList =
                        singalDaySessions.getDeviceInfo().getVendorID();
                boVendorIDList.add(boVendorID);
                singalDaySessions.getDeviceInfo().setVendorID(
                        boVendorIDList);
            }
        }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordExternalIpAddress() {
        try {
            BOSingleDaySessions singalDaySessions =  BOAppSessionDataModel.sharedInstanceFromJSONDictionary(
                    null).getSingleDaySessions();
            if(singalDaySessions != null) {
                // external ip address
                HashMap<String, Object> boExternalIpAddressMap = new HashMap<>();
                boExternalIpAddressMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                boExternalIpAddressMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                boExternalIpAddressMap.put("ipAddress", BOCommonUtils.getExternalIpAddress());
                boExternalIpAddressMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("ExternalIPAddress"));
                boExternalIpAddressMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);
                BOIPAddress boExternalIp = BOIPAddress.fromJsonDictionary(
                        boExternalIpAddressMap);
                List<BOIPAddress> boExternalIpList =
                        singalDaySessions.getNetworkInfo().getExternalIPAddress();
                boExternalIpList.add(boExternalIp);
                singalDaySessions.getNetworkInfo().setExternalIPAddress(
                        boExternalIpList);
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordNetworkEvents() {
        // ip address
        try {

            recordExternalIpAddress();

            BOSingleDaySessions singalDaySessions =  BOAppSessionDataModel.sharedInstanceFromJSONDictionary(
                    null).getSingleDaySessions();
           if(singalDaySessions != null) {
               HashMap<String, Object> boIpAddressMap = new HashMap<>();
               boIpAddressMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               boIpAddressMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());

               String ipAddress = TextUtils.isEmpty(BOCommonUtils.getIPAddress(true))
                       ? BOCommonUtils.getIPAddress(false) :
                       BOCommonUtils.getIPAddress(true);
               boIpAddressMap.put("ipAddress", ipAddress);
               boIpAddressMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("CurrentIPAddress"));
               boIpAddressMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               BOIPAddress boIpAddress = BOIPAddress.fromJsonDictionary(
                       boIpAddressMap);
               List<BOIPAddress> boIpAddressList =
                       singalDaySessions.getNetworkInfo().getCurrentIPAddress();
               boIpAddressList.add(boIpAddress);
               singalDaySessions.getNetworkInfo().setCurrentIPAddress(
                       boIpAddressList);


               // cellIPAddress
               HashMap<String, Object> cellIPAddressMap = new HashMap<>();
               cellIPAddressMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               cellIPAddressMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
               cellIPAddressMap.put("ipAddress", BOCommonUtils.getCellularIPAddress());
               cellIPAddressMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("CellIPAddress"));
               cellIPAddressMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               BOIPAddress cellIPAddress = BOIPAddress.fromJsonDictionary(
                       cellIPAddressMap);
               List<BOIPAddress> cellIPAddressList =
                       singalDaySessions.getNetworkInfo().getCellIPAddress();
               cellIPAddressList.add(cellIPAddress);
               singalDaySessions.getNetworkInfo().setCellIPAddress(
                       cellIPAddressList);


               // netmask
               HashMap<String, Object> netmaskMap = new HashMap<>();
               netmaskMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               netmaskMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
               netmaskMap.put("netmask", BOCommonUtils.getNetMask(mContext));
               netmaskMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("CellNetMask"));
               netmaskMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               BONetMask netmask = BONetMask.fromJsonDictionary(
                       netmaskMap);
               List<BONetMask> netmaskList =
                       singalDaySessions.getNetworkInfo().getCellNetMask();
               netmaskList.add(netmask);
               singalDaySessions.getNetworkInfo().setCellNetMask(netmaskList);


               // cellBroadcastAddress
               HashMap<String, Object> broadcastAddressMap = new HashMap<>();
               broadcastAddressMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               broadcastAddressMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
               broadcastAddressMap.put("broadcastAddress", BOCommonUtils.getCellBroadcastAddress());
               broadcastAddressMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("CellBroadcastAddress"));
               broadcastAddressMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               BOBroadcastAddress broadcastAddress = BOBroadcastAddress.fromJsonDictionary(
                       broadcastAddressMap);
               List<BOBroadcastAddress> broadcastAddressList = singalDaySessions.getNetworkInfo().getCellBroadcastAddress();
               broadcastAddressList.add(broadcastAddress);
               singalDaySessions.getNetworkInfo().setCellBroadcastAddress(
                       broadcastAddressList);


               // wifiIPAddress
               HashMap<String, Object> wifiIPAddressMap = new HashMap<>();
               wifiIPAddressMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               wifiIPAddressMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
               wifiIPAddressMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("WifiIPAddress"));
               wifiIPAddressMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               boIpAddressMap.put("ipAddress", mDeviceInfo.getWifiMacAddress(mContext));

               BOIPAddress wifiIPAddress = BOIPAddress.fromJsonDictionary(
                       wifiIPAddressMap);
               List<BOIPAddress> wifiIPAddressList = singalDaySessions.getNetworkInfo().getWifiIPAddress();
               wifiIPAddressList.add(wifiIPAddress);
               singalDaySessions.getNetworkInfo().setWifiIPAddress(
                       wifiIPAddressList);


               // wifinetmask
               HashMap<String, Object> wifinetmaskMap = new HashMap<>();
               wifinetmaskMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               wifinetmaskMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
               wifinetmaskMap.put("netmask", BOCommonUtils.getNetMask(mContext));
               wifinetmaskMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("WifiNetMask"));
               wifinetmaskMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               BONetMask wifinetmask = BONetMask.fromJsonDictionary(
                       wifinetmaskMap);
               List<BONetMask> wifinetmaskMapList =
                       singalDaySessions.getNetworkInfo().getWifiNetMask();
               wifinetmaskMapList.add(wifinetmask);
               singalDaySessions.getNetworkInfo().setWifiNetMask(wifinetmaskMapList);


               // wifiBroadcastAddress
               HashMap<String, Object> wifiBroadcastAddressMap = new HashMap<>();
               wifiBroadcastAddressMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               wifiBroadcastAddressMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
               //TODO: need tp re-verify
               wifiBroadcastAddressMap.put("broadcastAddress",
                       BOCommonUtils.getWifiBroadcastAddress(mContext));
               wifiBroadcastAddressMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("WifiBroadcastAddress"));
               wifiBroadcastAddressMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               BOBroadcastAddress wifiBroadcastAddress = BOBroadcastAddress.fromJsonDictionary(
                       wifiBroadcastAddressMap);
               List<BOBroadcastAddress> wifiBroadcastAddressList =
                       singalDaySessions.getNetworkInfo().getWifiBroadcastAddress();
               wifiBroadcastAddressList.add(wifiBroadcastAddress);
               singalDaySessions.getNetworkInfo().setWifiBroadcastAddress(
                       wifiBroadcastAddressList);


               // routerAddress
               HashMap<String, Object> routerAddressMap = new HashMap<>();
               routerAddressMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               routerAddressMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
               //TODO: need to re-verify
               routerAddressMap.put("routerAddress", BOCommonUtils.getRouterAddress(mContext));
               routerAddressMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("WifiRouterAddress"));
               routerAddressMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               BOWifiRouterAddress boWifiRouterAddress = BOWifiRouterAddress.fromJsonDictionary(
                       routerAddressMap);
               List<BOWifiRouterAddress> routerAddressesList =
                       singalDaySessions.getNetworkInfo().getWifiRouterAddress();
               routerAddressesList.add(boWifiRouterAddress);
               singalDaySessions.getNetworkInfo().setWifiRouterAddress(
                       routerAddressesList);


               // ssid
               HashMap<String, Object> ssidMap = new HashMap<>();
               ssidMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               ssidMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
               //Write logic for generate based on LanPing library later
               ssidMap.put("ssid", BOCommonUtils.getCurrentSsid(mContext));
               ssidMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("WifiSSID"));
               ssidMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               BOWifiSSID ssid = BOWifiSSID.fromJsonDictionary(
                       ssidMap);
               List<BOWifiSSID> ssidList =
                       singalDaySessions.getNetworkInfo().getWifiSSID();
               ssidList.add(ssid);
               singalDaySessions.getNetworkInfo().setWifiSSID(ssidList);

               //connectedToWifi
               HashMap<String, Object> connectedToWifiMap = new HashMap<>();
               connectedToWifiMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               connectedToWifiMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
               connectedToWifiMap.put("isConnected",
                       (mDeviceInfo.isWifiEnabled() && mDeviceInfo.isNetworkAvailable()));
               connectedToWifiMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("ConnectedToWifi"));
               connectedToWifiMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               BOConnectedTo connectedTo = BOConnectedTo.fromJsonDictionary(
                       connectedToWifiMap);
               List<BOConnectedTo> connectedToWifiList =
                       singalDaySessions.getNetworkInfo().getConnectedToWifi();
               connectedToWifiList.add(connectedTo);
               singalDaySessions.getNetworkInfo().setConnectedToWifi(
                       connectedToWifiList);


               //connected to cellular network
               HashMap<String, Object> connectedToCellMap = new HashMap<>();
               connectedToCellMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
               connectedToCellMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
               connectedToCellMap.put("isConnected",
                       (!mDeviceInfo.isWifiEnabled() && mDeviceInfo.isNetworkAvailable()));
               connectedToCellMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("ConnectedToCellNet"));
               connectedToCellMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

               BOConnectedTo connectedToCell = BOConnectedTo.fromJsonDictionary(
                       connectedToCellMap);
               List<BOConnectedTo> connectedToCellList =
                       singalDaySessions.getNetworkInfo().getConnectedToWifi();
               connectedToCellList.add(connectedToCell);
               singalDaySessions.getNetworkInfo().setConnectedToWifi(
                       connectedToCellList);
           }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordStorageEvents() {
        try {
            BOSingleDaySessions singalDaySessions =  BOAppSessionDataModel.sharedInstanceFromJSONDictionary(
                    null).getSingleDaySessions();
            if(singalDaySessions != null) {
                //BOStorageInfo
                HashMap<String, Object> bOStorageInfoMap = new HashMap<>();
                bOStorageInfoMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                bOStorageInfoMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                bOStorageInfoMap.put("totalDiskSpace", mDeviceInfo.getTotalInternalMemorySize());
                bOStorageInfoMap.put("usedDiskSpace", (mDeviceInfo.getTotalInternalMemorySize()
                        - mDeviceInfo.getAvailableInternalMemorySize()));
                bOStorageInfoMap.put("freeDiskSpace", mDeviceInfo.getAvailableInternalMemorySize());
                bOStorageInfoMap.put("unit", "GB");
                bOStorageInfoMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("StorageInfo"));
                bOStorageInfoMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);


                BOStorageInfo bOStorageInf = BOStorageInfo.fromJsonDictionary(
                        bOStorageInfoMap);
                List<BOStorageInfo> storageInfoList = singalDaySessions.getStorageInfo();
                storageInfoList.add(bOStorageInf);
                singalDaySessions.setStorageInfo(
                        storageInfoList);
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void recordMemoryEvents(){
            try {
                BOSingleDaySessions singalDaySessions =  BOAppSessionDataModel.sharedInstanceFromJSONDictionary(
                        null).getSingleDaySessions();
                if(singalDaySessions != null) {
                    //BOMemoryInfo
                    HashMap<String, Object> bOMemoryInfoMap = new HashMap<>();
                    bOMemoryInfoMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    bOMemoryInfoMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    bOMemoryInfoMap.put("atMemoryWarning", false);
                    bOMemoryInfoMap.put("totalRAM", (mDeviceInfo.getTotalRAM()));
                    bOMemoryInfoMap.put("usedMemory", BOCommonUtils.getUsedMemorySizeNumber());
                    bOMemoryInfoMap.put("freeMemory", BOCommonUtils.getFreeMemorySizeLong());
                    //--TODO Need clarity on this from team.
                    bOMemoryInfoMap.put("wiredMemory", "0");
                    bOMemoryInfoMap.put("activeMemory", "0");
                    bOMemoryInfoMap.put("inActiveMemory", "0");
                    bOMemoryInfoMap.put("purgeableMemory", "0");
                    bOMemoryInfoMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("MemoryRAMInfo"));
                    bOMemoryInfoMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);
                    //---


                    BOMemoryInfo boMemoryInfo = BOMemoryInfo.fromJsonDictionary(
                            bOMemoryInfoMap);
                    List<BOMemoryInfo> boMemoryInfoList =
                            singalDaySessions.getMemoryInfo();
                    boMemoryInfoList.add(boMemoryInfo);
                    singalDaySessions.setMemoryInfo(
                            boMemoryInfoList);
                }
            }catch (Exception e) {
                Logger.INSTANCE.e(TAG, e.getMessage());
            }

    }

    public void stopDeviceEvents() {

    }

    public void recordAdInformation() {
        try {
            BOSingleDaySessions singalDaySessions = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(
                    null).getSingleDaySessions();

            if (singalDaySessions != null) {
                HashMap<String, Object> stringObjectHashMap = new HashMap<>();
                stringObjectHashMap.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                stringObjectHashMap.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                stringObjectHashMap.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("AdInfo"));
                stringObjectHashMap.put("advertisingId",mAdInfo.getAd().getAdvertisingId());
                stringObjectHashMap.put("isAdDoNotTrack", mAdInfo.getAd().isAdDoNotTrack());
                stringObjectHashMap.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                BOAdInfo adInfo = BOAdInfo.fromJsonDictionary(stringObjectHashMap);
                List<BOAdInfo> adInfoList = singalDaySessions.getAdInfo();
                adInfoList.add(adInfo);
                singalDaySessions.setAdInfo(adInfoList);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }
}
