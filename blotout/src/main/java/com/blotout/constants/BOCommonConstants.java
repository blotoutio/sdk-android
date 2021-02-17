package com.blotout.constants;

public final class BOCommonConstants {

    private static final String TAG = "BOCommonConstants";

    public static final String BO_ANALYTICS_ROOT_USER_DEFAULTS_KEY   =   "com.blotout.sdk.Analytics.Root";
    public static final String BO_ANALYTICS_ROOT_USER_DEFAULTS_KEY_STAGE   =   "com.blotout.sdk.Analytics.Root_Stage";

    public static final String BO_ANALYTICS_ROOT_NEW_USER_DEFAULTS_KEY = "com.blotout.sdk.Analytics.Root.NewUser";
    public static final String BO_ANALYTICS_ROOT_NEW_USER_DEFAULTS_KEY_STAGE = "com.blotout.sdk.Analytics.Root.NewUser_Stage";

    public static final String BO_ANALYTICS_ROOT_SDK_LAUNCHED_DEFAULTS_KEY = "com.blotout.sdk.Analytics.Root.SdkLaunched";
    public static final String BO_ANALYTICS_ROOT_SDK_LAUNCHED_DEFAULTS_KEY_STAGE = "com.blotout.sdk.Analytics.Root.SdkLaunched_Stage";


    public static final String BO_ANALYTICS_SESSION_MODEL_DEFAULTS_KEY = "com.blotout.sdk.Analytics.Session_Model";
    public static final String BO_ANALYTICS_SESSION_MODEL_DEFAULTS_KEY_STAGE = "com.blotout.sdk.Analytics.Session_Model_Stage";

    public static final String BO_ANALYTICS_LIFETIME_MODEL_DEFAULTS_KEY = "com.blotout.sdk.Analytics.LifeTime_Model";
    public static final String BO_ANALYTICS_LIFETIME_MODEL_DEFAULTS_KEY_STAGE = "com.blotout.sdk.Analytics.LifeTime_Model_Stage";

    public static final String BO_ANALYTICS_DEV_EVENT_USER_DEFAULTS_KEY  = "com.blotout.sdk.Analytics.Dev_Event";
    public static final String BO_ANALYTICS_DEV_EVENT_USER_DEFAULTS_KEY_STAGE  = "com.blotout.sdk.Analytics.Dev_Event_Stage";

    public static final String BO_ANALYTICS_ROOT_SESSION_HISTORY_DEFAULTS_KEY = "com.blotout.sdk.Analytics.Root.Session_History";
    public static final String BO_ANALYTICS_ALL_DEV_CODIFIED_CUSTOM_EVENTS  =  "com.blotout.sdk.Analytics.Dev_Custom_Event";
    public static final String  BO_ANALYTICS_SEGMENT_LAST_SYNC_TIME_DEFAULTS_KEY = "segment_last_sync_time";
    public static final String BO_ANALYTICS_FUNNEL_LAST_UPDATE_TIME_DEFAULTS_KEY = "funnel_last_update_time";
    public static final String    BO_ANALYTICS_FUNNEL_LAST_SYNC_TIME_DEFAULTS_KEY  ="funnel_last_sync_time";
    public static final String    BO_ANALYTICS_FUNNEL_APP_LAUNCH_PREV_DAY_DEFAULTS_KEY = "funnel_app_launch_prev_day";

    public static final String    BO_ANALYTICS_FUNNEL_USER_TRAVERSAL_COUNT_DEFAULTS_KEY = "funnel_user_traversal_count";
    public static final String    BO_ANALYTICS_FUNNEL_USER_TRAVERSAL_PREV_DAY_DEFAULTS_KEY  = "funnel_user_traversal_prev_day";

    public static final String    BO_ANALYTICS_SDK_MANIFEST_LAST_DATE_SYNC_DEFAULTS_KEY =  "sdk_manifest_last_sync_date";

    public static final String    BO_ANALYTICS_SDK_MANIFEST_LAST_TIMESTAMP_SYNC_DEFAULTS_KEY = "sdk_manifest_last_sync_timestamp";

    public static final String     BO_ANALYTICS_ROOT_USER_DEFAULTS_PREVIOUS_DAY_APP_INFO       ="com.blotout.sdk.Analytics.pDay.AppInfo";

    public static final String BO_ANALYTICS_USER_BIRTH_TIME_STAMP_KEY = "com.blotout.sdk.Analytics.Root.UserBirthTimeStamp";
    public static final String BO_ANALYTICS_USER_BIRTH_TIME_STAMP_KEY_STAGE = "com.blotout.sdk.Analytics.Root.UserBirthTimeStamp_Stage";

    public static final String BO_ANALYTICS_APP_REFERRAL_SEND_KEY = "com.blotout.sdk.Analytics.Root.AppReferralSend";


    //This is used for app release management
    private static final String MAJOR = "1";
    private static final String MINOR = "0";
    private static final String PATCH = "0";
    public static final String SDK_VERSION = MAJOR + "." + MINOR + "." + PATCH;
    public static final String TAG_PREFIX = "BOSDK:";

    public static final String NO_EXTERNAL_MEMORY = "External Memory is not available";
    public static final String DOWNLOAD_START = "Downloading File started";
    public static final String DOWNLOAD_DONE = "Successfully Downloaded";
    public static final String DOWNLOAD_ERROR = "Error while downloading the file";

    public static final String EMPTY_STRING = "Given input should not be null or empty";
    public static final String DIRECTORY_EXIST = "Directory is already exist";
    public static final String DIRECTORY_NEWLY_CREATED = "Directory is created";

    public static final String EMPTY_CONTEXT = "Application Context Should not be Empty";


    public static final int KILOBYTE = 1024;
    public static final int MEGABYTE = KILOBYTE * 1024;

    public static final String FOLDER_PATH = "Blotout";
    public static final String BO_SDK_ROOT_DIRECTORY_NAME ="BOSDKRootDir";
    public static final String BO_SDK_ROOT_DIRECTORY_NAME_STAGE ="BOSDKRootDir_Stage";

    public static final String GET_EXTERNAL_DIR = "get_external_file_dir_root";
    public static final String GET_ROOT_DIR = "get_internal_file_dir_root";
    public static final String GET_ROOT_DIR_STAGE = "get_internal_file_dir_root_stage";
    public static final String BO_SDK_Launch_Test_DirectoryName = "BOSDKLaunchTestDir";

    public static final String BO_SDK_Volatile_ROOT_DIRECTORY_NAME    = "BOVolatileRootDirectory";
    public static final String BO_SDK_Volatile_ROOT_DIRECTORY_NAME_STAGE    = "BOVolatileRootDirectory_Stage";

    public static final String BO_SDK_NonVolatile_ROOT_DIRECTORY_NAME ="BONonVolatileRootDirectory";
    public static final String BO_SDK_NonVolatile_ROOT_DIRECTORY_NAME_STAGE ="BONonVolatileRootDirectory_Stage";

    public static final String BO_Network_Promise_Download_Directory_Name ="BOFNetworkPromiseDownloads";
    public static final String BO_Foundation_Directory_Name  ="BOFoundationDirectory";
    public static final String BO_Analytics_Directory_Name ="BOAnalyticsDirectory";

    public static final String BO_ANALYTICS_CURRENT_LOCATION_DICT = "geo_ip_current_location";

    public static final int BYTE_TO_GB = 1024 * 1024 * 1024;
    public static final int MEMORY_THRESHOLD = 20;

    public static final String BO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String BO_TIME_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final String BO_VERSION_KEY = "BOVersionKey";
    public static final String BO_BUILD_KEY = "BOBuildKey";

    public static final int BO_DEFAULT_EVENT_PUSH_TIME = 10*1000;
    //Default initial delay values, in MiliSeconds
    public static final int BO_ANALYTICS_POST_INIT_NETWORK_DELAY  = 10*1000;
    public static final int BO_ANALYTICS_EVENT_SYNC_NETWORK_DELAY = 10*1000;
    public static final int BO_ANALYTICS_FUNNEL_SYNC_NETWORK_DELAY = 10*1000;
    public static final int BO_ANALYTICS_SEGMENT_SYNC_NETWORK_DELAY = 10*1000;

    public static final String BO_SINGLE_DAY_SESSIONS = "singleDaySessions";
    public static final String BO_APP_INFO = "appInfo";
    public static final String BO_BUNDLE_ID = "bundleId";
    public static final String BO_LAST_UPDATED_TIME =  "lastUpdatedTime";
    public static final String BO_SENT_TO_SERVER =  "sentToServer";
    public static final String BO_TIME_STAMP =  "timeStamp";
    public static final String BO_VISIBLE_CLASS_NAME =  "visibleClassName";
    public static final String BO_DATE =  "date";
    public static final String BO_EVENT_SUB_CODE =  "eventSubCode";
    public static final String BO_EVENT_CODE =  "eventCode";
    public static final String BO_EVENT_NAME =  "eventName";
    public static final String BO_EVENT_INFO =  "eventInfo";
    public static final String BO_AVERAGE_SESSION_DURATION =  "averageSessionsDuration";
    public static final String BO_STATUS =  "status";
    public static final String BO_SESSION_DURATION =  "sessionsDuration";
    public static final String BO_TERMINATION_TIME_STAMP =  "terminationTimeStamp";
    public static final String BO_LAUNCH_TIME_STAMP =  "launchTimeStamp";
    public static final String BO_START_VISIBLE_CLASS_NAME =  "startVisibleClassName";
    public static final String BO_END_VISIBLE_CLASS_NAME =  "endVisibleClassName";
    public static final String BO_EVENT_START_INFO =  "eventStartInfo";
    public static final String BO_EVENT_DURATION =  "eventDuration";
    public static final String BO_EVENT_START_TIME_REFERENCE =  "eventStartTimeReference";
    public static final String BO_EVENT_START_TIME =  "startTime";
    public static final String BO_EVENT_END_TIME =  "endTime";
    public static final String BO_TIMED_EVENT_INFO =  "timedEvenInfo";
    public static final String BO_DAU =  "DAU";
    public static final String BO_DAU_INFO =  "dauInfo";
    public static final String BO_DPU =  "DPU";
    public static final String BO_DPU_INFO =  "dpuInfo";
    public static final String BO_APP_INSTALLED =  "AppInstalled";
    public static final String BO_APP_INSTALLED_INFO =  "appInstalledInfo";
    public static final String BO_IS_FIRST_LAUNCH =  "isFirstLaunch";
    public static final String BO_IS_NEW_USER =  "isNewUser";
    public static final String BO_THE_NEW_USER_INFO =  "theNewUserInfo";
    public static final String BO_NEW_USER =  "NewUser";
    public static final String BO_DAST =  "DAST";
    public static final String BO_AVERAGE_SESSION_TIME =  "averageSessionTime";
    public static final String BO_PAYLOAD =  "payload";
    public static final String BO_DAST_INFO =  "dastInfo";
    public static final String BO_MAST_INFO =  "mastInfo";
    public static final String BO_WAST_INFO =  "wastInfo";
    public static final String BO_MAST =  "MAST";
    public static final String BO_WAST =  "WAST";
    public static final String BO_MAU_INFO =  "mauInfo";
    public static final String BO_WAU_INFO =  "wauInfo";
    public static final String BO_MAU =  "MAU";
    public static final String BO_WAU =  "WAU";
    public static final String BO_MPU =  "MPU";
    public static final String BO_WPU =  "WPU";
    public static final String BO_MPU_INFO =  "mpuInfo";
    public static final String BO_WPU_INFO =  "wpuInfo";
    public static final String BO_APP_LAUNCHED =  "AppLaunched";
    public static final String BO_NAME = "name";
    public static final String BO_PLATFORM ="platform";
    public static final String BO_LANGUAGE ="language";
    public static final String BO_BUNDLE ="bundle";
    public static final String BO_SDK_VERSION ="sdkVersion";
    public static final String BO_OS_NAME ="osName";
    public static final String BO_OS_VERSION ="osVersion";
    public static final String BO_DEVICE_MFT ="deviceMft";
    public static final String BO_DEVICE_MODEL ="deviceModel";
    public static final String BO_VPN_STATUS ="vpnStatus";
    public static final String BO_JBN_STATUS ="jbnStatus";
    public static final String BO_DCOMP_STATUS ="dcompStatus";
    public static final String BO_ACOMP_STATUS ="acompStatus";
    public static final String BO_CURRENT_LOCATION ="currentLocation";
    public static final String BO_APP_IN_BACKGROUND ="AppInBackground";
    public static final String BO_APP_IN_FOREGROUND ="AppInForeground";
    public static final String BO_APP_BECOME_ACTIVE ="AppBecomeActive";
    public static final String BO_APP_RESIGN_ACTIVE ="AppResignActive";
    public static final String BO_APP_MEMORY_WARNING ="AppMemoryWarning";
    public static final String BO_APP_SIGNIFICANT_TIME_CHANGE ="AppSignificantTimeChange";
    public static final String BO_STATUS_BAR_FRAME_CHANGED ="StatusBarFrameChanged";
    public static final String BO_APP_TAKEN_SCREEN_SHOT ="AppTakenScreenShot";
    public static final String BO_NUMBER ="number";
    public static final String BO_PROCESSOR_ID ="processorID";
    public static final String BO_USAGE_PERCENTAGE ="usagePercentage";
    public static final String BO_NAMES ="names";
    public static final String BO_PERCENTAGE ="percentage";
    public static final String BO_ORIENTATION ="orientation";
    public static final String BO_DEVICE_ORIENTATION ="DeviceOrientation";
    public static final String BO_PII_EVENT ="piiEvent";
    public static final String BO_TIME_ZONE_OFFSET ="timeZoneOffset";
    public static final String BO_START = "start";
    public static final String BO_END = "end";
    public static final String BO_DURATION = "duration";

    public static final int BO_EVENT_TYPE_SESSION = 0;
    public static final int BO_EVENT_TYPE_SESSION_WITH_TIME = 1;
    public static final int BO_EVENT_TYPE_PII = 2;
    public static final int BO_EVENT_TYPE_PHI = 3;
    public static final int BO_EVENT_TYPE_START_TIMED_EVENT = 4;
    public static final int BO_EVENT_TYPE_END_TIMED_EVENT = 5;
    public static final int BO_EVENT_TYPE_RETENTION_EVENT = 6;

    public static final String  BO_EVENT_MAP_ID = "map_id";
    public static final String  BO_EVENT_MAP_Provider = "map_provider";

}
