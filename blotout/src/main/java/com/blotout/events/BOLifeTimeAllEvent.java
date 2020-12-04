package com.blotout.events;
import androidx.annotation.Nullable;

import com.blotout.analytics.BOAnalyticsActivityLifecycleCallbacks;
import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.model.lifetime.BOAST;
import com.blotout.model.lifetime.BOAppInstalled;
import com.blotout.model.lifetime.BOAppLifeTimeInfo;
import com.blotout.model.lifetime.BOAppLifetimeData;
import com.blotout.model.lifetime.BOCustomEvents;
import com.blotout.model.lifetime.BODau;
import com.blotout.model.lifetime.BOMau;
import com.blotout.model.lifetime.BOMpu;
import com.blotout.model.lifetime.BONewUser;
import com.blotout.model.lifetime.BOWau;
import com.blotout.model.lifetime.BOWpu;
import com.blotout.model.session.BOAppInfo;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.storage.BOFileSystemManager;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Blotout on 03,November,2019
 */
public class BOLifeTimeAllEvent {

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOLifeTimeAllEvent";
    private static volatile BOLifeTimeAllEvent instance;
    private static boolean isOnLaunchMethodCalled = false;

    public static BOLifeTimeAllEvent getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOLifeTimeAllEvent.class) {
                if(instance == null) {
                    instance = new BOLifeTimeAllEvent();
                }
            }
        }
        return instance;
    }

    @Nullable
    public HashMap<String,Object> appLifeTimeDefaultSingleDayDict (){
        HashMap<String,Object> lifeTimeInfo = new HashMap<>();
        try {
            lifeTimeInfo.put("sentToServer",false);
            lifeTimeInfo.put("dateAndTime",BODateTimeUtils.getStringFromDate(BODateTimeUtils.getCurrentDate(),BOCommonConstants.BO_TIME_DATE_FORMAT));
            lifeTimeInfo.put("timeStamp", BODateTimeUtils.get13DigitNumberObjTimeStamp());
            lifeTimeInfo.put("appInstallInfo",null);
            lifeTimeInfo.put("appUpdatesInfo",null);
            lifeTimeInfo.put("appLaunchInfo",null);
            lifeTimeInfo.put("blotoutSDKsInfo",null);
            lifeTimeInfo.put("appLanguagesSupported",null);
            lifeTimeInfo.put("appSupportShakeToEdit",null);
            lifeTimeInfo.put("appSupportRemoteNotifications",null);
            lifeTimeInfo.put("appCategory",null);
            lifeTimeInfo.put("deviceInfo",getDeviceInfo());
            lifeTimeInfo.put("networkInfo",null);
            lifeTimeInfo.put("storageInfo",null);
            lifeTimeInfo.put("memoryInfo",null);
            lifeTimeInfo.put("location",getLocationInfo());
            lifeTimeInfo.put("retentionEvent",appLifeTimeDefaultRetentionInfo());
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return lifeTimeInfo;
    }

    @Nullable
    public HashMap<String,Object> getDeviceInfo () {
        HashMap<String,Object> deviceInfo = new HashMap<>();
        try {
        deviceInfo.put("sentToServer",null);
        deviceInfo.put("timeStamp",null);
        deviceInfo.put("multitaskingEnabled",null);
        deviceInfo.put("proximitySensorEnabled",null);
        deviceInfo.put("debuggerAttached",null);
        deviceInfo.put("pluggedIn",null);
        deviceInfo.put("jailBroken",null);
        deviceInfo.put("numberOfActiveProcessors",null);
        deviceInfo.put("processorsUsage",null);
        deviceInfo.put("accessoriesAttached",null);
        deviceInfo.put("headphoneAttached",null);
        deviceInfo.put("numberOfAttachedAccessories",null);
        deviceInfo.put("nameOfAttachedAccessories",null);
        deviceInfo.put("batteryLevelPercentage",null);
        deviceInfo.put("isCharging",null);
        deviceInfo.put("fullyCharged",null);
        deviceInfo.put("deviceOrientation",null);
        deviceInfo.put("deviceModel",null);
        deviceInfo.put("deviceName",null);
        deviceInfo.put("systemName",null);
        deviceInfo.put("systemVersion",null);
        deviceInfo.put("systemDeviceTypeUnformatted",null);
        deviceInfo.put("systemDeviceTypeFormatted",null);
        deviceInfo.put("deviceScreenWidth",null);
        deviceInfo.put("deviceScreenHeight",null);
        deviceInfo.put("appUIWidth",null);
        deviceInfo.put("appUIHeight",null);
        deviceInfo.put("screenBrightness",null);
        deviceInfo.put("stepCountingAvailable",null);
        deviceInfo.put("distanceAvailbale",null);
        deviceInfo.put("floorCountingAvailable",null);
        deviceInfo.put("numberOfProcessors",null);
        deviceInfo.put("country",null);
        deviceInfo.put("Language",null);
        deviceInfo.put("timeZone",null);
        deviceInfo.put("currency",null);
        deviceInfo.put("clipboardContent",null);
        deviceInfo.put("cfUUID",null);
        deviceInfo.put("vendorID",null);
        deviceInfo.put("doNotTrackEnabled",null);
        deviceInfo.put("advertisingID",null);
        deviceInfo.put("otherIDs",null);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return deviceInfo;
    }

    @Nullable
    public HashMap<String,Object> getLocationInfo () {
      try {
        HashMap<String,Object> locationInfo = new HashMap<>();
        locationInfo.put("sentToServer",false);
        locationInfo.put("timeStamp", BODateTimeUtils.get13DigitNumberObjTimeStamp());

        HashMap<String,Object> piiLocation = new HashMap<>();
        piiLocation.put("latitude",null);
        piiLocation.put("longitude", null);
        locationInfo.put("piiLocation",piiLocation);

        HashMap<String,Object> nonPIILocation = new HashMap<>();
        nonPIILocation.put("city",null);
        nonPIILocation.put("state", null);
        nonPIILocation.put("zip", null);
        nonPIILocation.put("country", null);
        nonPIILocation.put("activity", null);
        nonPIILocation.put("source", null);
        locationInfo.put("nonPIILocation",nonPIILocation);

        return locationInfo;
      }catch (Exception e) {
          Logger.INSTANCE.e(TAG, e.getMessage());
          return null;
      }
    }
    @Nullable
    public HashMap<String,Object> appLifeTimeDefaultRetentionInfo (){

        HashMap<String,Object> retentionInfo = new HashMap<>();
        try {
            retentionInfo.put("sentToServer",false);
            retentionInfo.put("DAU",null);
            retentionInfo.put("WAU",null);
            retentionInfo.put("MAU",null);
            retentionInfo.put("DPU",null);
            retentionInfo.put("WPU",null);
            retentionInfo.put("MPU",null);
            retentionInfo.put("appInstalled",null);
            retentionInfo.put("newUser",null);
            retentionInfo.put("DAST",null);
            retentionInfo.put("WAST",null);
            retentionInfo.put("MAST",null);
            retentionInfo.put("customEvents",null);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return retentionInfo;
    }

    public  void setAppLifeTimeSystemInfoOnAppLaunch() {
        try {
        if (BOAEvents.isAppLifeModelInitialised) {
            //TODO: get ad tracking  string
            boolean isAdTrackingEnabled = false;

            BOAppLifetimeData lifeSessionModel = BOAppLifetimeData.sharedInstanceFromJSONDictionary(null);

            boolean appLifeTimeRequireNewDayModelEntry = true;
            if (lifeSessionModel != null) {
                if (lifeSessionModel.getAppLifeTimeInfo() != null && lifeSessionModel.getAppLifeTimeInfo().size() > 0) {
                    String dateTime = null;
                    BOAppLifeTimeInfo info = lifeSessionModel.getAppLifeTimeInfo().get(lifeSessionModel.getAppLifeTimeInfo().size()-1);
                    dateTime = info.getDateAndTime();

                    boolean isSameDay = BODateTimeUtils.isDayMonthAndYearSameOfDate(BODateTimeUtils.getCurrentDate(),dateTime, BOCommonConstants.BO_TIME_DATE_FORMAT);
                    if (isSameDay) {
                        appLifeTimeRequireNewDayModelEntry = false;
                    }
                }
            }

            if (lifeSessionModel != null && appLifeTimeRequireNewDayModelEntry) {
                BOAppLifeTimeInfo singleDayInfo = BOAppLifeTimeInfo.fromJsonDictionary(appLifeTimeDefaultSingleDayDict());
                isOnLaunchMethodCalled = true;
                //TODO: fill the value
                singleDayInfo.getDeviceInfo().setDoNotTrackEnabled(!isAdTrackingEnabled);
                singleDayInfo.getDeviceInfo().setAdvertisingID("");
                singleDayInfo.getDeviceInfo().setcfUUID("");
                singleDayInfo.getDeviceInfo().setVendorID("");

                List<BOAppLifeTimeInfo> appLifeTimeInfoArr = lifeSessionModel.getAppLifeTimeInfo();
                appLifeTimeInfoArr.add(singleDayInfo);

                lifeSessionModel.setAppLifeTimeInfo(appLifeTimeInfoArr);
            }

            String sessionDate = BODateTimeUtils.getStringFromDate(new Date(), BOCommonConstants.BO_DATE_FORMAT);
            lifeSessionModel.setDate(sessionDate);

            setLifeTimeRetentionEventsOnAppLaunch();

        }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    public void setLifeTimeRetentionEventsOnAppLaunch() {
      try {
        if (BOAEvents.isAppLifeModelInitialised) {
            isOnLaunchMethodCalled = true;
            BOAppLifeTimeInfo info = null;

            BOAppLifetimeData lifeSessionModel = BOAppLifetimeData.sharedInstanceFromJSONDictionary(null);
            if (lifeSessionModel.getAppLifeTimeInfo() != null && !lifeSessionModel.getAppLifeTimeInfo().isEmpty()) {
                info = lifeSessionModel.getAppLifeTimeInfo().get(lifeSessionModel.getAppLifeTimeInfo().size()-1);
            }

            if(info != null) {

                BODau lifeDAU = info.getRetentionEvent().getDau();
                if (lifeDAU != null) {
                    info.getRetentionEvent().setDau(null);
                }

                boolean isNewValidWau = true;
                for (int wauCount = lifeSessionModel.getAppLifeTimeInfo().size(); wauCount > 0; wauCount--) {
                    int wauCountArrIndex = wauCount - 1;
                    BOAppLifeTimeInfo lifeTimeWauDayModel = lifeSessionModel.getAppLifeTimeInfo().get(wauCountArrIndex);
                    BOWau wauEvent = lifeTimeWauDayModel.getRetentionEvent().getWau();
                    if (wauEvent != null) {
                        int lastWauWeekNumber = BODateTimeUtils.weekOfYearForDateInterval(wauEvent.getTimeStamp());
                        int currentWeekNumber = BODateTimeUtils.weekOfMonth(BODateTimeUtils.getCurrentDate());
                        if (lastWauWeekNumber == currentWeekNumber) {
                            isNewValidWau = false;
                            break;
                        }
                    }
                }
                if (isNewValidWau) {
                    HashMap<String, Object> appwau = new HashMap<>();
                    appwau.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    appwau.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_WAU));
                    appwau.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    appwau.put(BOCommonConstants.BO_WAU_INFO, null);
                    appwau.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                    BOWau wau = BOWau.fromJsonDictionary(appwau);
                    info.getRetentionEvent().setWau(wau);
                }


                //Ideally this should be just once as monthly file is getting created and then same file for whole month much have it
                boolean isNewValidMau = true;
                for (int mauCount = lifeSessionModel.getAppLifeTimeInfo().size(); mauCount > 0; mauCount--) {
                    int mauCountArrIndex = mauCount - 1;
                    BOAppLifeTimeInfo lifeTimeMauDayModel = lifeSessionModel.getAppLifeTimeInfo().get(mauCountArrIndex);
                    BOMau mauEvent = lifeTimeMauDayModel.getRetentionEvent().getMau();
                    if (mauEvent != null) {
                        int lastMauMonthNumber = BODateTimeUtils.monthOfYearForDateInterval(mauEvent.getTimeStamp());
                        int currentMonthNumber = BODateTimeUtils.monthOfYear(BODateTimeUtils.getCurrentDate());
                        if (lastMauMonthNumber == currentMonthNumber) {
                            isNewValidMau = false;
                            break;
                        }
                    }
                }
                if (isNewValidMau) {
                    HashMap<String, Object> appmau = new HashMap<>();
                    appmau.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    appmau.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_MAU));
                    appmau.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    appmau.put(BOCommonConstants.BO_MAU_INFO, null);
                    appmau.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                    BOMau mau = BOMau.fromJsonDictionary(appmau);
                    info.getRetentionEvent().setMau(mau);
                }
                //Daily, Weekly and Monthly paying users logic pending
                if (BlotoutAnalytics_Internal.getInstance().isPayingUser) {
                    recordPayingUser(info);
                }
            }
        }
      }catch (Exception e) {
          Logger.INSTANCE.e(TAG, e.getMessage());
      }
    }

    private void recordPayingUser(BOAppLifeTimeInfo info) {
        BOAppLifetimeData lifeSessionModel = BOAppLifetimeData.sharedInstanceFromJSONDictionary(null);
        boolean isNewValidWpu = true;
        for (int wpuCount = lifeSessionModel.getAppLifeTimeInfo().size(); wpuCount > 0; wpuCount--) {
            int wpuCountArrIndex = wpuCount -1;
            BOAppLifeTimeInfo lifeTimeWauDayModel = lifeSessionModel.getAppLifeTimeInfo().get(wpuCountArrIndex);
            BOWpu wpuEvent = lifeTimeWauDayModel.getRetentionEvent().getWpu();
            if (wpuEvent != null) {
                int lastWpuWeekNumber = BODateTimeUtils.weekOfYearForDateInterval(wpuEvent.getTimeStamp());
                int currentWeekNumber = BODateTimeUtils.weekOfMonth(BODateTimeUtils.getCurrentDate());
                if (lastWpuWeekNumber == currentWeekNumber) {
                    isNewValidWpu = false;
                    break;
                }
            }
        }
        if (isNewValidWpu) {
            HashMap<String, Object> appwpu = new HashMap<>();
            appwpu.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
            appwpu.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_WPU));
            appwpu.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
            appwpu.put(BOCommonConstants.BO_WPU_INFO, null);
            appwpu.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

            BOWpu wpu = BOWpu.fromJsonDictionary(appwpu);
            info.getRetentionEvent().setWpu(wpu);
        }
        //Ideally this should be just once as monthly file is getting created and then same file for whole month much have it
        boolean isNewValidMpu = true;
        for (int mpuCount = lifeSessionModel.getAppLifeTimeInfo().size(); mpuCount > 0; mpuCount--) {
            int mpuCountArrIndex = mpuCount -1;
            BOAppLifeTimeInfo lifeTimeMauDayModel = lifeSessionModel.getAppLifeTimeInfo().get(mpuCountArrIndex);
            BOMpu mpuEvent = lifeTimeMauDayModel.getRetentionEvent().getMpu();
            if (mpuEvent != null) {
                int lastMpuMonthNumber = BODateTimeUtils.monthOfYearForDateInterval(mpuEvent.getTimeStamp());
                int currentMonthNumber = BODateTimeUtils.monthOfYear(BODateTimeUtils.getCurrentDate());
                if (lastMpuMonthNumber == currentMonthNumber) {
                    isNewValidMpu = false;
                    break;
                }
            }
        }
        if (isNewValidMpu) {
            HashMap<String, Object> appmpu = new HashMap<>();
            appmpu.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
            appmpu.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_MPU));
            appmpu.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
            appmpu.put(BOCommonConstants.BO_MPU_INFO, null);
            appmpu.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

            BOMpu mpu = BOMpu.fromJsonDictionary(appmpu);
            info.getRetentionEvent().setMpu(mpu);
        }
    }

     private  void  recordIfAppFirstLaunch () {
       try {
         boolean isSDKFirstLaunch = BOSharedPreferenceImpl.getInstance().isSDKFirstLaunched();
         //If it is not first SDK launch then can't be App launch
         if (BOAEvents.isAppLifeModelInitialised && !isSDKFirstLaunch && isOnLaunchMethodCalled) {
             Boolean isAppFirstLaunch = BOSharedPreferenceImpl.getInstance().isSDKFirstLaunched();
             if (!isAppFirstLaunch) {
                 //Check for reinstall case
                 BOAppLifeTimeInfo info = null;
                 BOAppLifetimeData lifeSessionModel = BOAppLifetimeData.sharedInstanceFromJSONDictionary(null);

                 if (lifeSessionModel.getAppLifeTimeInfo() != null && !lifeSessionModel.getAppLifeTimeInfo().isEmpty()) {
                     info = lifeSessionModel.getAppLifeTimeInfo().get(lifeSessionModel.getAppLifeTimeInfo().size()-1);
                 }

                 if(info != null) {

                     HashMap<String, Object> appInstalled = new HashMap<>();
                     appInstalled.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                     appInstalled.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_APP_INSTALLED));
                     appInstalled.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                     appInstalled.put(BOCommonConstants.BO_IS_FIRST_LAUNCH, isAppFirstLaunch);
                     appInstalled.put(BOCommonConstants.BO_APP_INSTALLED_INFO, null);
                     appInstalled.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                     BOAppInstalled app = BOAppInstalled.fromJsonDictionary(appInstalled);


                     info.getRetentionEvent().setAppInstalled(app);
                 }
             }

         }
       }catch (Exception e) {
           Logger.INSTANCE.e(TAG, e.getMessage());
       }
     }

     private void recordNewUser() {
        try {
        if (BOAEvents.isAppLifeModelInitialised && isOnLaunchMethodCalled) {
             //Implement share file app logic also for devices available
             boolean isNewUserInstallCheck =  BOFileSystemManager.getInstance().isFirstLaunchBOSDKFileSystemCheck();
             if (isNewUserInstallCheck) {
                 BOAppLifetimeData lifeSessionModel = BOAppLifetimeData.sharedInstanceFromJSONDictionary(null);
                 BOAppLifeTimeInfo info = null;

                 if (lifeSessionModel.getAppLifeTimeInfo() != null && !lifeSessionModel.getAppLifeTimeInfo().isEmpty()) {
                     info = lifeSessionModel.getAppLifeTimeInfo().get(lifeSessionModel.getAppLifeTimeInfo().size()-1);
                 }

                 if(info != null) {
                     HashMap<String, Object> newUser = new HashMap<>();
                     newUser.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                     newUser.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_NEW_USER));
                     newUser.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                     newUser.put(BOCommonConstants.BO_IS_NEW_USER, isNewUserInstallCheck);
                     newUser.put(BOCommonConstants.BO_THE_NEW_USER_INFO, null);
                     newUser.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                     BONewUser theNewUser = BONewUser.fromJsonDictionary(newUser);

                     info.getRetentionEvent().setNewUser(theNewUser);
                 }

             }else{
                 //Think about case when after 12 months, we want to check onboarding date
                 //As first launch file might have been archived, so need to store new user on boarding time somewhere safely
             }
         }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
     }

     private static String getSessionDirectoryPath(){
        try {
            String rootDirectory = BOFileSystemManager.getInstance().getBOSDKRootDirectory();
            return  BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(rootDirectory +"/"+"SessionData");
        }catch (Exception e) {
                Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return null;
    }

     private static String getSyncedDirectoryPath(){
        try {
                String sessionDataDir =  getSessionDirectoryPath();
                return BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(sessionDataDir+"/"+"syncedFiles");
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return null;
    }

    private static String getNotSyncedDirectoryPath(){
        try {
                String sessionDataDir =  getSessionDirectoryPath();
                 return BOFileSystemManager.getInstance().createDirectoryIfRequiredAndReturnPath(sessionDataDir+"/"+"notSyncedFiles");
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return null;
    }

     private List<String> getAllSessionFilesForTheWeek(int weekOfYear){

        try {
            //NSInteger weekOfYear = [BOAUtilities weekOfYearForDate:currentDate];
            //NSInteger lastWeekOfYear = weekNumber;
            List<String> allSyncedSessionFile = BOFileSystemManager.getInstance().getAllFilesWithExtension(getSyncedDirectoryPath(),"txt");
            List<String> allNonSyncedSessionFile = BOFileSystemManager.getInstance().getAllFilesWithExtension(getNotSyncedDirectoryPath(),"txt");

            List<String> syncedfilePathToConsider = new ArrayList<>();
            List<String> nonSyncedfilePathToConsider = new ArrayList<>();

            List<String> allfilePathToConsider = new ArrayList<>();

            for (String singleSyncedfilePath : allSyncedSessionFile) {
                String fileName = BOCommonUtils.lastPathComponent(singleSyncedfilePath);
                String fileDate = BOCommonUtils.stringByDeletingPathExtension(fileName);
                int fileWeekOfYear = BODateTimeUtils.weekOfYearForDateString(fileDate);
                if (fileWeekOfYear == weekOfYear) {
                 syncedfilePathToConsider.add(singleSyncedfilePath);
                 allfilePathToConsider.add(singleSyncedfilePath);
                }
                Logger.INSTANCE.d(TAG, "fileName" + fileName + "&& fileDate-"+ fileDate);
            }
            for (String singleNonSyncedfilePath : allNonSyncedSessionFile) {
                String fileName = BOCommonUtils.lastPathComponent(singleNonSyncedfilePath);
                String fileDate = BOCommonUtils.stringByDeletingPathExtension(fileName);
                int fileWeekOfYear = BODateTimeUtils.weekOfYearForDateString(fileDate);
                if (fileWeekOfYear == weekOfYear) {
                    nonSyncedfilePathToConsider.add(singleNonSyncedfilePath);
                    allfilePathToConsider.add(singleNonSyncedfilePath);
                }
                Logger.INSTANCE.d(TAG, "fileName" + fileName + "&& fileDate-"+ fileDate);
            }
            return ((allfilePathToConsider.size() > 0)) ? allfilePathToConsider : null;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }

        return null;
    }

     private long getWASTForTheWeek(int weekOfYear){

        try {
                List<String> allFilesPath =  getAllSessionFilesForTheWeek(weekOfYear);
                if (allFilesPath == null ||  (allFilesPath.size() <= 0)) {
                    return -1;
                }
                long totalWST = 0;
                long actualWAST = 0;
                for (String singleFilePath : allFilesPath) {

                    String jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(singleFilePath);
                    if (jsonString != null && !jsonString.equals("")) {
                        HashMap<String,Object> singleSessionDic = BOCommonUtils.getHashmapFromJsonString(jsonString);
                        if (singleSessionDic != null) {
                            BOAppSessionDataModel appSessionData = BOAppSessionDataModel.fromJsonDictionary(singleSessionDic);
                            //Daily avergae last object has proper average value so far
                            List<BOAppInfo> appInfoList = appSessionData.getSingleDaySessions().getAppInfo();
                            BOAppInfo appInfo = null;
                            if(appInfoList != null && appInfoList.size() >0) {
                                appInfo = appInfoList.get(appInfoList.size()-1);
                            }
                            if(appInfo != null) {
                                long dailyAverage = appInfo.getAverageSessionsDuration();
                                totalWST = totalWST + dailyAverage;
                            }
                        }
                    }
                }
                //Devide by zero is not possible as check on top of function takes care of that
                actualWAST = totalWST / allFilesPath.size();
                return (actualWAST > 0) ? actualWAST : -1;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
         return -1;
    }

    private List<String> getAllSessionFilesForTheMonth(int monthOfyear){

        try {
            List<String> allSyncedSessionFile = BOFileSystemManager.getInstance().getAllFilesWithExtension(getSyncedDirectoryPath(),"txt");
            List<String> allNonSyncedSessionFile = BOFileSystemManager.getInstance().getAllFilesWithExtension(getNotSyncedDirectoryPath(),"txt");

            List<String> syncedfilePathToConsider = new ArrayList<>();
            List<String> nonSyncedfilePathToConsider = new ArrayList<>();

            List<String> allfilePathToConsider = new ArrayList<>();

            for (String singleSyncedfilePath : allSyncedSessionFile) {
                String fileName = BOCommonUtils.lastPathComponent(singleSyncedfilePath);
                String fileDate = BOCommonUtils.stringByDeletingPathExtension(fileName);
                int fileMonthOfYear = BODateTimeUtils.monthOfYearForDateString(fileDate);
                if (fileMonthOfYear == monthOfyear) {
                    syncedfilePathToConsider.add(singleSyncedfilePath);
                    allfilePathToConsider.add(singleSyncedfilePath);
                }
                Logger.INSTANCE.d(TAG, "fileName" + fileName + "&& fileDate-"+ fileDate);
            }

            for (String singleNonSyncedfilePath : allNonSyncedSessionFile) {
                String fileName = BOCommonUtils.lastPathComponent(singleNonSyncedfilePath);
                String fileDate = BOCommonUtils.stringByDeletingPathExtension(fileName);
                int fileWeekOfYear = BODateTimeUtils.monthOfYearForDateString(fileDate);
                if (fileWeekOfYear == monthOfyear) {
                    nonSyncedfilePathToConsider.add(singleNonSyncedfilePath);
                    allfilePathToConsider.add(singleNonSyncedfilePath);
                }
                Logger.INSTANCE.d(TAG, "fileName" + fileName + "&& fileDate-"+ fileDate);
            }
            return ((allfilePathToConsider.size() > 0)) ? allfilePathToConsider : null;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }

        return null;
    }

    private long getMASTForTheMonth(int monthOfYear){

        try {
            List<String> allFilesPath =  getAllSessionFilesForTheMonth(monthOfYear);
            if (allFilesPath == null || (allFilesPath.size() <= 0)) {
                return -1;
            }
            long totalMAST = 0;
            long actualMAST = 0;
            for (String singleFilePath : allFilesPath) {

                String jsonString = BOFileSystemManager.getInstance().readContentOfFileAtPath(singleFilePath);
                if (jsonString != null && !jsonString.equals("")) {
                    HashMap<String,Object> singleSessionDic = BOCommonUtils.getHashmapFromJsonString(jsonString);
                    if (singleSessionDic != null) {
                        BOAppSessionDataModel appSessionData = BOAppSessionDataModel.fromJsonDictionary(singleSessionDic);
                        //Daily avergae last object has proper average value so far
                        List<BOAppInfo> appInfoList = appSessionData.getSingleDaySessions().getAppInfo();
                        BOAppInfo appInfo = null;
                        if(appInfoList != null && appInfoList.size() >0) {
                            appInfo = appInfoList.get(appInfoList.size()-1);
                        }
                        if(appInfo != null) {
                            long dailyAverage = appInfo.getAverageSessionsDuration();
                            totalMAST = totalMAST + dailyAverage;
                        }
                    }
                }
            }
            //Devide by zero is not possible as check on top of function takes care of that
            actualMAST = totalMAST / allFilesPath.size();
            return (actualMAST > 0) ? actualMAST : -1;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return -1;
    }

    private long lastWeekWAST(Date currentDate){
        try {
                int weekOfYear = BODateTimeUtils.weekOfYear(currentDate);
                int lastWeekOfYear = weekOfYear - 1;
                long actualWAST = (lastWeekOfYear >= 0) ? getWASTForTheWeek(lastWeekOfYear) : -1;
                return actualWAST;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return -1;
    }

   private List<String> lastMonthAllFiles(Date currentDate){
    try {
            int monthOfYear = BODateTimeUtils.monthOfYear(currentDate);
            int lastMonthOfYear = monthOfYear - 1;
            List<String> allfilePathToConsider = (lastMonthOfYear >= 0) ? getAllSessionFilesForTheMonth(lastMonthOfYear) : null;
            return allfilePathToConsider;
    }catch (Exception e) {
        Logger.INSTANCE.e(TAG, e.getMessage());
    }
        return null;
    }

    private  long lastMonthMAST(Date currentDate){
        try {
                int monthOfYear = BODateTimeUtils.monthOfYear(currentDate);
                int lastMonthOfYear = monthOfYear - 1;
                long actualMAST = (lastMonthOfYear >= 0) ? getMASTForTheMonth(lastMonthOfYear): -1;
                return actualMAST;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return -1;
    }

    private boolean isWASTAlreadySetForLastWeek(){
        boolean isWASTAlreadySet = false;
        try {
                BOAppLifetimeData lifeSessionModel = BOAppLifetimeData.sharedInstanceFromJSONDictionary(null);

                //Reversing order loop will be better optimised in this case
                //            for (BOAAppLifeTimeInfo *singleInfoLT in lifeSessionModel.appLifeTimeInfo) {
                //
                //            }
                //lifeSessionModel.appLifeTimeInfo.count - 2 : -2 because count -1 will be current day object
                for (int wastIndex = (lifeSessionModel.getAppLifeTimeInfo().size() - 2); wastIndex >=0; wastIndex --) {
                    if ((wastIndex >= 0)) {
                        BOAppLifeTimeInfo singleInfoLT =  lifeSessionModel.getAppLifeTimeInfo().get(wastIndex);
                        if(singleInfoLT != null) {
                            boolean isSameWeek = BODateTimeUtils.isWeekSameOfDate(BODateTimeUtils.getDateFromString(singleInfoLT.getDateAndTime(), BOCommonConstants.BO_TIME_DATE_FORMAT), BODateTimeUtils.getCurrentDate());

                            if (isSameWeek) {
                                if (singleInfoLT.getRetentionEvent() != null && singleInfoLT.getRetentionEvent().getWast() != null) {
                                    isWASTAlreadySet = true;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }else{
                        break;
                    }
                }
                return isWASTAlreadySet;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return true;
    }

    private boolean isMASTAlreadySetForLastWeek(){
        boolean isMASTAlreadySet = false;
        try {
            BOAppLifetimeData lifeSessionModel = BOAppLifetimeData.sharedInstanceFromJSONDictionary(null);

            //Reversing order loop will be better optimised in this case
            //            for (BOAAppLifeTimeInfo *singleInfoLT in lifeSessionModel.appLifeTimeInfo) {
            //
            //            }
            //lifeSessionModel.appLifeTimeInfo.count - 2 : -2 because count -1 will be current day object
            for (int mastIndex = 0; mastIndex < lifeSessionModel.getAppLifeTimeInfo().size(); mastIndex ++) {
                if ((mastIndex >= 0)) {
                    BOAppLifeTimeInfo singleInfoLT =  lifeSessionModel.getAppLifeTimeInfo().get(mastIndex);
                    if(singleInfoLT != null) {
                        boolean isSameMonth = BODateTimeUtils.isMonthSameOfDate(BODateTimeUtils.getDateFromString(singleInfoLT.getDateAndTime(), BOCommonConstants.BO_TIME_DATE_FORMAT), BODateTimeUtils.getCurrentDate());

                        if (isSameMonth) {
                            if (singleInfoLT.getRetentionEvent() != null && singleInfoLT.getRetentionEvent().getMast() != null) {
                                isMASTAlreadySet = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
            return isMASTAlreadySet;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return true;
    }


    public  void recordWASTWithPayload(long averageTime, @Nullable HashMap<String,Object> eventInfo) {

        try {
            if (BOAEvents.isAppLifeModelInitialised && isOnLaunchMethodCalled) {

            boolean isWASTAlreadySet = isWASTAlreadySetForLastWeek();
            if (isWASTAlreadySet) {
                return;
            }

            long wastInt = lastWeekWAST(BODateTimeUtils.getCurrentDate());
            if (wastInt == -1) {
                return;
            }
            if ((eventInfo != null && (eventInfo.keySet().size()>0))) {
                HashMap<String,Object> newEventInfo = new HashMap<>(eventInfo);
                newEventInfo.put(BOCommonConstants.BO_DATE,BODateTimeUtils.getStringFromDate(BODateTimeUtils.getCurrentDate(),BOCommonConstants.BO_DATE_FORMAT));
                eventInfo = newEventInfo;
            }

            Object averageSessionInfo = (eventInfo != null && (eventInfo.keySet().size()>0))  ? eventInfo : null;
            List<BOAppLifeTimeInfo> appLifeTimeInfo = BOAppLifetimeData.sharedInstanceFromJSONDictionary(null).getAppLifeTimeInfo();
                if(appLifeTimeInfo != null && appLifeTimeInfo.size() >0) {
                    HashMap<String, Object> wast = new HashMap<>();
                    wast.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    wast.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_WAST));
                    wast.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    wast.put(BOCommonConstants.BO_AVERAGE_SESSION_TIME, wastInt);
                    wast.put(BOCommonConstants.BO_DAST_INFO, null);
                    wast.put(BOCommonConstants.BO_MAST_INFO, null);
                    wast.put(BOCommonConstants.BO_WAST_INFO, averageSessionInfo);
                    wast.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                    BOAST wastObj = BOAST.fromJsonDictionary(wast);

                    BOAppLifeTimeInfo appInfo = appLifeTimeInfo.get(appLifeTimeInfo.size()-1);
                    appInfo.getRetentionEvent().setWast(wastObj);
                }
            }
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }
    public  void recordMASTWithPayload(long averageTime, @Nullable HashMap<String,Object> eventInfo){
       try {
        if (BOAEvents.isAppLifeModelInitialised && isOnLaunchMethodCalled) {
                boolean isMASTAlreadySet = isMASTAlreadySetForLastWeek();
                if (isMASTAlreadySet) {
                    return;
                }

                long mastInt = lastMonthMAST(BODateTimeUtils.getCurrentDate());
                if (mastInt == -1) {
                    return;
                }

                if ((eventInfo != null && (eventInfo.keySet().size()>0))) {
                    HashMap<String,Object> newEventInfo = new HashMap<>(eventInfo);
                    newEventInfo.put(BOCommonConstants.BO_DATE,BODateTimeUtils.getStringFromDate(BODateTimeUtils.getCurrentDate(),BOCommonConstants.BO_DATE_FORMAT));
                    eventInfo = newEventInfo;
                }

                Object averageSessionInfo = (eventInfo != null && (eventInfo.keySet().size()>0))  ? eventInfo : null;
                List<BOAppLifeTimeInfo> appLifeTimeInfo = BOAppLifetimeData.sharedInstanceFromJSONDictionary(null).getAppLifeTimeInfo();
                if(appLifeTimeInfo != null && appLifeTimeInfo.size() >0) {
                    HashMap<String, Object> mast = new HashMap<>();
                    mast.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    mast.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(BOCommonConstants.BO_MAST));
                    mast.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    mast.put(BOCommonConstants.BO_AVERAGE_SESSION_TIME, mastInt);
                    mast.put(BOCommonConstants.BO_DAST_INFO, null);
                    mast.put(BOCommonConstants.BO_MAST_INFO, averageSessionInfo);
                    mast.put(BOCommonConstants.BO_WAST_INFO, null);
                    mast.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                    BOAST mastObj = BOAST.fromJsonDictionary(mast);

                    BOAppLifeTimeInfo appInfo = appLifeTimeInfo.get(appLifeTimeInfo.size()-1);
                    appInfo.getRetentionEvent().setMast(mastObj);
                }
            }

       }catch (Exception e) {
           Logger.INSTANCE.e(TAG, e.getMessage());
       }
    }
    public  void recordCustomEventsWithNamewithPayload( String eventName, @Nullable HashMap<String,Object> eventInfo) {
        try {
            if (BOAEvents.isAppLifeModelInitialised && isOnLaunchMethodCalled) {
                Object customEventInfo = (eventInfo != null && (eventInfo.keySet().size() > 0)) ? eventInfo : null;
                BOAppLifeTimeInfo info = null;
                BOAppLifetimeData lifeSessionModel = BOAppLifetimeData.sharedInstanceFromJSONDictionary(null);

                if (lifeSessionModel.getAppLifeTimeInfo() != null && !lifeSessionModel.getAppLifeTimeInfo().isEmpty()) {
                    info = lifeSessionModel.getAppLifeTimeInfo().get(lifeSessionModel.getAppLifeTimeInfo().size() - 1);
                }
                if (info != null) {
                    HashMap<String, Object> dast = new HashMap<>();
                    dast.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                    dast.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent(eventName));
                    dast.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                    dast.put(BOCommonConstants.BO_EVENT_INFO, customEventInfo);
                    dast.put(BOCommonConstants.BO_EVENT_NAME, eventName);
                    dast.put(BOCommonConstants.BO_VISIBLE_CLASS_NAME, BOAnalyticsActivityLifecycleCallbacks.getInstance().activityName);
                    dast.put(BONetworkConstants.BO_SESSION_ID, BOSharedManager.getInstance().sessionId);

                    BOCustomEvents customEvents = BOCustomEvents.fromJsonDictionary(dast);
                    info.getRetentionEvent().setCustomEvents(customEvents);
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }
}
