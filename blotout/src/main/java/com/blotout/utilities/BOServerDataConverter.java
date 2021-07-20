package com.blotout.utilities;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.blotout.Controllers.BODeviceAndAppFraudController;
import com.blotout.Controllers.BOSDKManifestController;
import com.blotout.analytics.BOAnalyticsActivityLifecycleCallbacks;
import com.blotout.analytics.BOSharedManager;
import com.blotout.constants.BOCommonConstants;
import com.blotout.deviceinfo.device.DeviceInfo;
import com.blotout.events.BOAppSessionEvents;
import com.blotout.model.session.BOAppInfo;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.model.session.BOCurrentLocation;
import com.blotout.model.session.BOLocation;
import com.blotout.storage.BOSharedPreferenceImpl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Blotout on 03,May,2020
 */
public class BOServerDataConverter {
    private static final String TAG = "BOServerDataConverter";

    private static final int Continent = 1;
    private static final int Country = 2;
    private static final int Region = 3;
    private static final int City = 4;
    private static final int Postal_Address = 5;

    private static final int DeviceGrainHigh = 0;
    private static final int DeviceGrainMedium = 1;
    private static final int DeviceGrainAll = 2;

    private static HashMap<String,Object> appInfo = new HashMap<>();

    public static HashMap<String, Object> prepareMetaData() {
        try {

            HashMap<String, Object> appInfoCurrentDict = null;
            if (appInfo != null && (appInfo.values().size() > 0)) {
                appInfoCurrentDict = appInfo;
            }else{
                appInfoCurrentDict =  recordAppInformation();
            }

            HashMap<String, Object> metaData = new HashMap<>();
            if(appInfoCurrentDict != null) {
                metaData.put("plf", appInfoCurrentDict.get("platform"));
                metaData.put("appn", appInfoCurrentDict.get("bundle"));
                metaData.put("appv", appInfoCurrentDict.get("version"));
                metaData.put("osn", appInfoCurrentDict.get("osName"));
                metaData.put("osv", appInfoCurrentDict.get("osVersion"));
                metaData.put("dmft", appInfoCurrentDict.get("deviceMft"));
                metaData.put("dm", appInfoCurrentDict.get("deviceModel"));
                metaData.put("dcomp", appInfoCurrentDict.get("dcompStatus"));
                metaData.put("acomp", appInfoCurrentDict.get("acompStatus"));
                metaData.put("jbrkn", appInfoCurrentDict.get("jbnStatus"));
                metaData.put("vpn", appInfoCurrentDict.get("vpnStatus"));

                if (metaData.keySet().size() > 0) {
                    int deviceGrain = getDeviceGrain();
                    switch (deviceGrain) {
                        case DeviceGrainHigh:
                            metaData.remove("osn");
                            metaData.remove("osv");
                            metaData.remove("dmft");
                            metaData.remove("dm");
                            break;
                        case DeviceGrainMedium:
                            break;
                        case DeviceGrainAll:
                            break;
                    }
                }
            }
            return metaData;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }

    }

    public static HashMap<String, Object> prepareGeoData() {
        //Check for latest session location from Geo-IP and use. in case location found has time stamp after sync then use the last location known else latest
        try {

            //Return No Geo Event in case of firstParty container
            if(BOSDKManifestController.getInstance().sdkModeDeployment == BOSDKManifestController.BO_DEPLOYMENT_MODE_FIRST_PARTY) {
                return null;
            }

            List<BOAppInfo> appInfo = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppInfo();
            HashMap<String, Object> geoInfo = new HashMap<>();

            if (appInfo != null && !appInfo.isEmpty()) {

                List<BOAppInfo> sessionInfo = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getAppInfo();
                BOCurrentLocation ipGeoLocation = null;
                if (sessionInfo != null && sessionInfo.size() > 0) {
                    BOAppInfo info = sessionInfo.get(sessionInfo.size() - 1);
                    ipGeoLocation = info.getCurrentLocation();
                }

                List<BOLocation> location = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getLocation();
                BOLocation gpsGeoLocation = null;
                if (location != null && location.size() > 0) {
                    gpsGeoLocation = location.get(location.size() - 1);
                }

                //Above location has one concern, appInfo is being set to object at the termination, so will get previous location always.
                if (ipGeoLocation != null && ipGeoLocation.getCountry() != null && !ipGeoLocation.getCountry().equals("")) {
                    //based on IP and geo-ip response get from server or local store, static for certain time
                    geoInfo.put("conc", ipGeoLocation.getContinentCode());
                    geoInfo.put("couc", ipGeoLocation.getCountry());
                    geoInfo.put("reg", ipGeoLocation.getState());
                    geoInfo.put("city", ipGeoLocation.getCity());
                    geoInfo.put("zip", ipGeoLocation.getZip());
                    //geoInfo.put("lat", ipGeoLocation.getLatitude());
                    //geoInfo.put("long", ipGeoLocation.getLongitude());

                } else if (gpsGeoLocation != null && gpsGeoLocation.getNonPIILocation().getCountry() != null && !gpsGeoLocation.getNonPIILocation().getCountry().equals("")) {

                    geoInfo.put("conc", gpsGeoLocation.getNonPIILocation().getSource());
                    geoInfo.put("couc", gpsGeoLocation.getNonPIILocation().getCountry());
                    geoInfo.put("reg", gpsGeoLocation.getNonPIILocation().getState());
                    geoInfo.put("city", gpsGeoLocation.getNonPIILocation().getCity());
                    geoInfo.put("zip", gpsGeoLocation.getNonPIILocation().getZip());
                    //geoInfo.put("lat", gpsGeoLocation.getPiiLocation().getLatitude());
                    //geoInfo.put("long", gpsGeoLocation.getPiiLocation().getLongitude());
                } else {
                    HashMap<String, Object> currentKnownLocation = BOAppSessionEvents.getInstance().getGeoIPAndPublish(false);
                    if(currentKnownLocation != null && currentKnownLocation.keySet().size() >0) {
                        geoInfo.put("conc", currentKnownLocation.get("continentCode"));
                        geoInfo.put("couc", currentKnownLocation.get("country"));
                        geoInfo.put("reg", currentKnownLocation.get("state"));
                        geoInfo.put("city", currentKnownLocation.get("city"));
                        geoInfo.put("zip", currentKnownLocation.get("zip"));
                        //geoInfo.put("lat", currentKnownLocation.get("latitude"));
                        //geoInfo.put("long", currentKnownLocation.get("longitude"));
                    }
                }
            } else {
                HashMap<String, Object> currentKnownLocation = BOAppSessionEvents.getInstance().getGeoIPAndPublish(false);
                if(currentKnownLocation != null && currentKnownLocation.keySet().size() >0) {
                    geoInfo.put("conc", currentKnownLocation.get("continentCode"));
                    geoInfo.put("couc", currentKnownLocation.get("country"));
                    geoInfo.put("reg", currentKnownLocation.get("state"));
                    geoInfo.put("city", currentKnownLocation.get("city"));
                    geoInfo.put("zip", currentKnownLocation.get("zip"));
                    //geoInfo.put("lat", currentKnownLocation.get("latitude"));
                    //geoInfo.put("long", currentKnownLocation.get("longitude"));
                }
            }

                if(geoInfo.keySet().size() > 0) {
                    int geoGrain = getGeoGrain();
                    switch (geoGrain) {
                        case Continent:
                            geoInfo.remove("country");
                            geoInfo.remove("state");
                            geoInfo.remove("city");
                            geoInfo.remove("zip");
                            break;
                        case Country:
                            geoInfo.remove("state");
                            geoInfo.remove("city");
                            geoInfo.remove("zip");
                            break;
                        case Region:
                            geoInfo.remove("city");
                            break;
                        case City:
                            geoInfo.remove("zip");
                            break;
                        case Postal_Address:
                            break;
                    }
                }
            return geoInfo;
     }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return null;
        }
    }

    private static int getGeoGrain() {

        int geoGrain = BOSDKManifestController.getInstance().eventGEOLocationGrain;
        if(geoGrain >0) {
            return geoGrain;
        }

        return 0;
    }

    private static int getDeviceGrain() {

        int deviceGrain = BOSDKManifestController.getInstance().eventDeviceInfoGrain;
        if(deviceGrain >0) {
            return deviceGrain;
        }

        return 0;
    }

    public static HashMap<String, Object> preparePreviousMetaData(BOAppSessionDataModel sessionData) {

         try {

            HashMap<String, Object> appInfoCurrentDict = BOAppSessionEvents.getInstance().sessionAppInfo;
            if (!(appInfoCurrentDict != null && (appInfoCurrentDict.values().size() > 0))) {

               BOAppSessionEvents.getInstance().recordAppInformation();
                appInfoCurrentDict = BOAppSessionEvents.getInstance().sessionAppInfo;
            }
            BOAppInfo appInfoCurrent = BOAppInfo.fromJsonDictionary(appInfoCurrentDict);

            //can find previous day based on sessionData but using default as short term solution.

            String previousAppInfoDictStr = BOSharedPreferenceImpl.getInstance().getString(BOCommonConstants.BO_ANALYTICS_ROOT_USER_DEFAULTS_PREVIOUS_DAY_APP_INFO);
            HashMap<String, Object> previousAppInfoDict = BOCommonUtils.getHashmapFromJsonString(previousAppInfoDictStr);

            BOAppInfo appInfoPrevious = null;
            if (previousAppInfoDict != null && previousAppInfoDict.values().size() > 0) {
                appInfoPrevious =  BOAppInfo.fromJsonDictionary(previousAppInfoDict);
            }

                if (appInfoPrevious != null && appInfoCurrent != null) {
                    HashMap<String, Object> metaData = new HashMap<>();
                    metaData.put("plf", null);
                    metaData.put("appn", appInfoPrevious.getName().equals(appInfoCurrent.getName()) ? null : appInfoPrevious.getName());
                    metaData.put("osn", appInfoPrevious.getOsName().equals(appInfoCurrent.getOsName()) ? null : appInfoPrevious.getOsName());
                    metaData.put("osv", appInfoPrevious.getOsVersion().equals(appInfoCurrent.getOsVersion()) ? null : appInfoPrevious.getOsVersion());
                    metaData.put("dmft", null);
                    metaData.put("dm", null);
                    metaData.put("dcomp", appInfoPrevious.getDcompStatus() != appInfoCurrent.getDcompStatus() ? null : appInfoPrevious.getDcompStatus());
                    metaData.put("acomp", appInfoPrevious.getAcompStatus() != appInfoCurrent.getAcompStatus() ? null : appInfoPrevious.getAcompStatus());
                    metaData.put("jbrkn",BODeviceAndAppFraudController.getInstance().isDeviceJailbroken());
                    metaData.put("appv", appInfoPrevious.getVersion().equals(appInfoCurrent.getVersion()) ? null : appInfoPrevious.getVersion());
                    metaData.put("vpn", appInfoPrevious.getVpnStatus() != appInfoCurrent.getVpnStatus() ? null : appInfoPrevious.getVpnStatus());

                    if(metaData.keySet().size() > 0) {
                        int deviceGrain = getDeviceGrain();
                        switch (deviceGrain) {
                            case DeviceGrainHigh:
                                metaData.remove("osn");
                                metaData.remove("osv");
                                metaData.remove("dmft");
                                metaData.remove("dm");
                                break;
                            case DeviceGrainMedium:
                                break;
                            case DeviceGrainAll:
                                break;
                        }
                    }

                    for (String metaInfoKey : metaData.keySet()) {
                        Object metaVal = metaData.get(metaInfoKey);
                        if (metaVal != null) {
                            metaData.remove(metaInfoKey);
                        }
                    }

                    return metaData;
                }
            }catch( Exception e) {
             Logger.INSTANCE.e(TAG, e.toString());
             return null;
         }
         return null;
    }

    private static HashMap<String,Object> recordAppInformation() {

        long launchTimeStamp = BODateTimeUtils.get13DigitNumberObjTimeStamp();
        appInfo.put("launchTimeStamp",launchTimeStamp);

        PackageInfo packageInfo = BOAnalyticsActivityLifecycleCallbacks.getPackageInfo(BOSharedManager.getInstance().getContext());
        String currentVersion = packageInfo.versionName;
        int currentBuild = packageInfo.versionCode;
        appInfo.put("version",currentVersion+currentBuild);
        String  sdkVersion = BOCommonConstants.SDK_VERSION;
        appInfo.put("sdkVersion",sdkVersion);

        String bundleIdentifier = BOSharedManager.getInstance().getContext().getPackageName();
        appInfo.put("bundle",bundleIdentifier);

        final String packageName = bundleIdentifier;
        PackageManager packageManager= BOSharedManager.getInstance().getContext().getPackageManager();
        String appName = "";
        try {
            appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appInfo.put("name",appName);

        DeviceInfo mDeviceInfo = new DeviceInfo(BOSharedManager.getInstance().getContext());
        appInfo.put("language",mDeviceInfo.getLanguage());

        appInfo.put("platform", BODeviceDetection.getDevicePlatformCode());

        //get OS name
        String fieldName = null;
        int fieldValue = -1;
        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            fieldName = field.getName();
            try {
                fieldValue = field.getInt(new Object());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fieldValue == Build.VERSION.SDK_INT) {
                break;
            }
        }
        appInfo.put("osName", fieldName);
        appInfo.put("osVersion", Build.VERSION.RELEASE);
        appInfo.put("deviceMft", Build.MANUFACTURER);
        appInfo.put("deviceModel", Build.MODEL);
        appInfo.put("vpnStatus", BOCommonUtils.checkVPN());
        appInfo.put("jbnStatus", BODeviceAndAppFraudController.getInstance().isDeviceJailbroken());
        appInfo.put("dcompStatus", BODeviceAndAppFraudController.getInstance().isDeviceCompromised());
        appInfo.put("acompStatus", BODeviceAndAppFraudController.getInstance().isAppCompromised());

        return appInfo;
    }
}
