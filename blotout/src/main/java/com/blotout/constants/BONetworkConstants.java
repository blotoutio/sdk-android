package com.blotout.constants;

/**
 * Used to hold network specific constants
 */
public final class BONetworkConstants {

    public static final String BASE_URL = "https://sdk.blotout.io/sdk/";
    public static final String BASE_URL_STAGE = "https://stage-sdk.blotout.io/sdk/";
    public static final String BASE_URL_DEVELOPMENT = "https://api.blotout.io/sdk/";

    private static final String TAG = "BONetworkConstants";

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

    public static final String BO_GET = "GET";
    public static final String BO_POST = "POST";
    public static final String BO_PUT = "PUT";

    public static final String BO_CONTENT_TYPE = "Content-Type";
    public static final String BO_APPLICATION_JSON = "application/json";
    public static final String BO_TOKEN = "token";
    public static final String BO_ACCEPT = "Accept";
    public static final String BO_VERSION = "version";
    public static final String BO_VERSION_NAME = "v1";

    public static final String BO_EVENTS = "events";
    public static final String BO_PII = "pii";
    public static final String BO_PHI = "phi";
    public static final String BO_PHI_DATA = "phidata";
    public static final String BO_PII_DATA = "piidata";
    public static final String BO_EVENTS_TIME = "evt";
//    public static final String BO_EVENT_DAY_OCCURENCE_COUNT = "evdc";
//    public static final String BO_EVENT_CATEGORY = "evc";
    public static final String BO_EVENT_CATEGORY_SUBTYPE = "evcs";
    public static final String BO_MESSAGE_ID = "mid";
    public static final String BO_EVENT_NAME = "evn";
    public static final String BO_SCREEN_NAME = "scrn";
    public static final String BO_SCREEN_FROM = "scrfrm";
    public static final String BO_SCREEN_TO = "scrto";
    public static final String BO_PROPERTIES = "properties";
    public static final String BO_CODIFIED_INFO = "codifiedInfo";
    public static final String BO_OBJECT_TYPE = "objectType";
    public static final String BO_OBJECT_RECT = "objectRect";
    public static final String BO_OBJECT_SCREEN_RECT = "scrRect";
    public static final String BO_VALUE = "value";
    public static final String BO_NAVIGATION_SCREEN = "nvg";
    public static final String BO_NAVIGATION_TIME = "nvg_tm";
    public static final String BO_APP_NAVIGATION = "appNavigation";
    public static final String BO_AD_IDENTIFIER = "AdvertisingId";
    public static final String BO_AD_DO_NOT_TRACK = "AdDoNotTrack";
    public static final String BO_INSTALL_REFERRAR = "InstallReferrar";
    public static final String BO_PII_Event_SERVER = "piievn";
    public static final String BO_SESSION_ID = "session_id";
    public static final String BO_SDK_START = "sdk_start";
    public static final String BO_USER_ID = "userid";

    public static final String BO_CLIENT_TIMEZONE = "client_timezone";
    public static final String BO_EVENT_START_PERIOD = "event_start_period";
    public static final String BO_EVENT_END_PERIOD = "event_end_period";
    public static final String BO_TOTAL_SEESION_TIME = "total_session_time";
    public static final String BO_TOTAL_SESSION_COUNT = "total_session_count";
    public static final String BO_APP_BIRTH = "app_birth";
    public static final String BO_CUSTOM_KEY = "custom_key";


    //#pragma mark - Event Category
    public static final long BO_EVENT_SYSTEM_KEY = 10001;
    public static final long BO_EVENT_DEVELOPER_CODED_KEY = 20001;
    public static final long BO_EVENT_FUNNEL_KEY = 30001;
    public static final long BO_EVENT_RETENTION_KEY = 40001;
    public static final long BO_EVENT_EXCEPTION_KEY = 50001;
    public static final long BO_EVENT_CAMPAIGN_KEY = 60001;
    public static final long BO_EVENT_SEGMENT_KEY = 70001;

//#pragma mark - Event SYSYEM SUB EVENTS

    public static final long BO_EVENT_APP_INSTALLED_KEY = 11001;
    public static final long BO_EVENT_APP_UNINSTALLED_KEY = 11002;
    public static final long BO_EVENT_APP_LAUNCHED_KEY = 11003;
    public static final long BO_EVENT_APP_BACKGROUND_KEY = 11004;
    public static final long BO_EVENT_APP_FOREGROUND_KEY = 11005;
    public static final long BO_EVENT_APP_NOTIFICATION_RECEIVED_KEY = 11006;
    public static final long BO_EVENT_APP_NOTIFICATION_VIEWED_KEY = 11007;
    public static final long BO_EVENT_APP_NOTIFICATION_CLICKED_KEY = 11008;
    public static final long BO_EVENT_APP_PORTRAIT_ORIENTATION_KEY = 11009;
    public static final long BO_EVENT_APP_LANDSCAPE_ORIENTATION_KEY = 11010;
    public static final long BO_EVENT_APP_SESSION_START_KEY = 11011;
    public static final long BO_EVENT_APP_SESSION_END_KEY = 11012;
    public static final long BO_EVENT_APP_CLICK_TAP_KEY = 11013;
    public static final long BO_EVENT_APP_DOUBLE_TAP_KEY = 11014;
    public static final long BO_EVENT_APP_VIEW_KEY = 11015;
    public static final long BO_EVENT_APP_INSTALL_REFERRER = 11016;
    public static final long BO_EVENT_APP_RUN_TIME_EXCEPTION = 11017;
    public static final long BO_EVENT_APP_BOUNCE = 11018;
    public static final long BO_EVENT_APP_NAVIGATION = 11019;
    public static final long BO_EVENT_APP_DEVICE_INFO = 11020;
    public static final long BO_EVENT_APP_PERFORMANCE_INFO = 11021;
    public static final long BO_EVENT_APP_DO_NOT_TRACK = 11022;
    public static final long BO_EVENT_APP_DEEP_LINK = 11023;
    public static final long BO_EVENT_APP_SESSION_INFO = 11024;
    public static final long BO_EVENT_SDK_START = 11130;



    //#pragma mark - Event DEVELOPER CODED SUB EVENTS
    public static final long BO_DEV_EVENT_MAP_ID = 21001;
    public static final long BO_DEV_EVENT_CLICK_TAP_KEY = 21001;
    public static final long BO_DEV_EVENT_DOUBLE_CLICK_TAP_KEY = 21002;
    public static final long BO_DEV_EVENT_VIEW_KEY = 21003;
    public static final long BO_DEV_EVENT_ADD_TO_CART_KEY = 21004;
    public static final long BO_DEV_EVENT_GESTURE_KEY = 21005;
    public static final long BO_DEV_EVENT_SWIPE_UP_KEY = 21006;
    public static final long BO_DEV_EVENT_SWIPE_DOWN_KEY = 21007;
    public static final long BO_DEV_EVENT_SWIPE_LEFT_KEY = 21008;
    public static final long BO_DEV_EVENT_SWIPE_RIGHT_KEY = 21009;
    public static final long BO_DEV_EVENT_DRAG_KEY = 21010;
    public static final long BO_DEV_EVENT_FLICK_KEY = 21011;
    public static final long BO_DEV_EVENT_PINCH_KEY = 21012;
    public static final long BO_DEV_EVENT_LONG_PRESS_KEY = 21013;
    public static final long BO_DEV_EVENT_SHAKE_KEY = 21014;
    public static final long BO_DEV_EVENT_EDGE_PAN_GESTURE_KEY = 21015;
    public static final long BO_DEV_EVENT_CHARGE_TRANSACTION_BUTTON_KEY = 21016;
    public static final long BO_DEV_EVENT_CANCEL_BUTTON_KEY = 21017;
    public static final long BO_DEV_EVENT_APPLY_COUPAN_KEY = 21018;
    public static final long BO_DEV_EVENT_TIMED_KEY = 21019;
    public static final long BO_DEV_EVENT_CUSTOM_KEY = 21100;

    //#pragma mark - Event RETENTION CODED SUB EVENTS
    public static final long BO_RETEN_DAU_KEY = 41001;
    public static final long BO_RETEN_WAU_KEY = 41002;
    public static final long BO_RETEN_MAU_KEY = 41003;
    public static final long BO_RETEN_DPU_KEY = 41004;
    public static final long BO_RETEN_WPU_KEY = 41005;
    public static final long BO_RETEN_MPU_KEY = 41006;
    public static final long BO_RETEN_APP_INSTALL_KEY = 41007;
    public static final long BO_RETEN_APP_UNINSTALL_KEY = 41008;
    public static final long BO_RETEN_NUO_KEY = 41009;
    public static final long BO_RETEN_DAST_KEY = 41010;
    public static final long BO_RETEN_WAST_KEY = 41011;
    public static final long BO_RETEN_MAST_KEY = 41012;
    public static final long BO_RETEN_CUS_KEY1 = 41013;
    public static final long BO_RETEN_CUS_KEY2 = 41014;
    public static final long BO_RETEN_CUS_KEY3 = 41015;
    public static final long BO_RETEN_CUS_KEY4 = 41016;

    //Funnel Events
    public static final long BO_Funnel_Received = 31001;
    public static final long BO_Funnel_Triggered = 31002;
    //Segments
    public static final long BO_Segment_Received = 71001;
    public static final long BO_Segment_Triggred = 71002;


    //#pragma mark - Event CAMPAIGN SUB EVENTS
    public static final long BO_CAMP_EVENT_RECEIVED_KEY = 61001;
    public static final long BO_CAMP_EVENT_TRIGGERED_KEY = 61002;
    public static final long BO_CAMP_EVENT_CONVERTED_KEY = 61003;
    public static final long BO_CAMP_EVENT_SYSTEM_NOTIFICATION_SHOW_KEY = 61004;
    public static final long BO_CAMP_EVENT_SYSTEM_NOTIFICATION_CLICK_KEY = 61005;
    public static final long BO_CAMP_EVENT_ALERT_SHOW_KEY = 61006;
    public static final long BO_CAMP_EVENT_ALERT_CLICK_KEY = 61007;
    public static final long BO_CAMP_EVENT_EMAIL_SENT_KEY = 61008;
    public static final long BO_CAMP_EVENT_SMS_SENT_KEY = 61009;

    public static final int BO_GREATER_THAN = 801;
    public static final int BO_GREATER_THAN_EQUALTO = 802;
    public static final int BO_LESS_THAN = 803;
    public static final int BO_LESS_THAN_EQUALTO = 804;
    public static final int BO_EQUAL = 805;
    public static final int BO_NOT_EQUAL = 806;
    public static final int BO_IN_RANGE = 807;
    public static final int BO_NOT_IN_RANGE = 808;
    public static final int BO_IN = 809;
    public static final int BO_NOT_IN = 810;
    public static final int BO_CONTAIN = 811;
    public static final int BO_NOT_CONTAIN = 812;
    public static final int BO_ANY = 813;
    public static final int BO_ALL = 814;
    public static final int BO_NONE = 815;

    ///Platform Constants
    public static final int BO_Android_Phone = 11;
    public static final int BO_Android_Tablet = 12;
    public static final int BO_Android_TV = 13;
    public static final int BO_iPhone = 14;
    public static final int BO_iPad = 15;
    public static final int BO_Mobile_Web = 16;
    public static final int BO_Web_Application = 17;
    public static final int BO_Apple_TV = 18;
    public static final int BO_Apple_Watch = 19;
    public static final int BO_Android_Watch = 20;
    public static final int BO_Amazon_Fire_TV = 21;
    public static final int BO_Amazon_Fire_Stick = 22;
    public static final int BO_Roku_TV = 23;
    public static final int BO_HbbTV = 24;
    public static final int BO_Smart_Android_TV = 25;

    public static final int BO_Smart_TV_All = 40;
    public static final int BO_Android_All = 50;
    public static final int BO_ios_All = 60;
    public static final int BO_Web_All = 70;

    //Funnel and Retention events
    public static final String BO_META = "meta";
    public static final String BO_PMETA="pmeta";
    public static final String BO_GEO="geo";

    //Developer codified events
    public static final String BO_ADD_TO_CART = "addToCart";
    public static final String BO_CHARGE_TRANSACTION="chargeTransaction";
    public static final String BO_SCREEN_EDGE_PAN="screenEdgePan";
    public static final String BO_View="view";
    public static final String BO_TOUCH_CLICK="touchClick";
    public static final String BO_DRAG="drag";
    public static final String BO_FLICK="flick";
    public static final String BO_SWIPE="swipe";
    public static final String BO_DOUBLE_TAP="doubleTap";
    public static final String BO_TWO_FINGER_TAP="twoFingerTap";
    public static final String BO_PINCH="pinch";
    public static final String BO_TOUCH_AND_HOLD="touchAndHold";
    public static final String BO_SHAKE="shake";

    //APPSTATE Events
    public static final String BO_APP_LAUNCHED="appLaunched";
    public static final String BO_RESIGN_ACTIVE="appResignActive";
    public static final String BO_APP_IN_BACKGROUND="appInBackground";
    public static final String BO_APP_IN_FOREGROUND="appInForeground";
    public static final String BO_APP_ORIENTATION_LANDSCAPE="appOrientationLandscape";
    public static final String BO_APP_ORIENTATION_PORTRAIT="appOrientationPortrait";
    public static final String BO_APP_NOTIFICATION_RECEIVED="appNotificationReceived";
    public static final String BO_APP_NOTIFICATION_VIEWED="appNotificationViewed";
    public static final String BO_APP_NOTIFICATION_CLICKED="appNotificationClicked";
    public static final String BO_APP_SESSION_INFO = "appSessionInfo";

    //SEGMENT AND FUNNEL
    public static final String BO_SEGMENT_TRIGGERED =  "segmentTriggered";
    public static final String BO_SEGMENT_RECEIVED =  "segmentReceived";
    public static final String BO_FUNNEL_TRIGGERED =  "funnelTriggered";
    public static final String BO_FUNNEL_RECEIVED =  "funnelReceived";

    //Device Event Name
    public static final String BO_EVENT_MULTITASKING_ENABLED = "multitaskingEnabled";
    public static final String BO_EVENT_PROXIMITY_SENSOR_ENABLED = "proximitySensorEnabled";
    public static final String BO_EVENT_DEBUGGER_ATTACHED ="debuggerAttached";
    public static final String BO_EVENT_PLUGGEDIN = "pluggedIn";
    public static final String BO_EVENT_JAIL_BROKEN = "jailBroken";
    public static final String BO_EVENT_NUMBER_OF_ACTIVE_PROCESSORS = "numberOfActiveProcessors";
    public static final String BO_EVENT_PROCESSORS_USAGE = "processorsUsage";
    public static final String BO_EVENT_ACCESSORIES_ATTACHED = "accessoriesAttached";
    public static final String BO_EVENT_HEADPHONE_ATTACHED = "headphoneAttached";
    public static final String BO_EVENT_NUMBER_OF_ATTACHED_ACCESSORIES = "numberOfAttachedAccessories";
    public static final String BO_EVENT_NAME_OF_ATTACHED_ACCESSORIES = "nameOfAttachedAccessories";
    public static final String BO_EVENT_BATTERY_LEVEL = "batteryLevel";
    public static final String BO_EVENT_IS_CHARGING = "isCharging";
    public static final String BO_EVENT_FULLY_CHARGED = "fullyCharged";

    //Storage Events
    public static final String BO_EVENT_MEMORY_INFO = "MemoryInfo";
    public static final String BO_EVENT_STORAGE_INFO = "StorageInfo";
    public static final String BO_EVENT_UNIT = "unit";
    public static final String BO_EVENT_TOTAL_DISK_SPACE = "totalDiskSpace";
    public static final String BO_EVENT_USED_DISK_SPACE = "usedDiskSpace";
    public static final String BO_EVENT_FREE_DISK_SPACE = "freeDiskSpace";
    public static final String BO_EVENT_TOTAL_RAM = "totalRAM";
    public static final String BO_EVENT_USED_MEMORY = "usedMemory";
    public static final String BO_EVENT_WIRED_MEMORY = "wiredMemory";
    public static final String BO_EVENT_ACTIVE_MEMORY = "activeMemory";
    public static final String BO_EVENT_INACTIVE_MEMORY = "inActiveMemory";
    public static final String BO_EVENT_FREE_MEMORY = "freeMemory";
    public static final String BO_EVENT_PURGEABLE_MEMORY = "purgeableMemory";
    public static final String BO_EVENT_AT_MEMORY_WARNING = "atMemoryWarning";

    ////PII DATA
    public static final String BO_EVENT_CFUU_ID = "cfUUID";
    public static final String BO_EVENT_VENDOR_ID = "vendorID";

    public static final String BO_EVENT_CURRENT_IP_ADDRESS = "currentIPAddress";
    public static final String BO_EVENT_EXTERNAL_IP_ADDRESS = "externalIPAddress";
    public static final String BO_EVENT_CELL_IP_ADDRESS = "cellIPAddress";
    public static final String BO_EVENT_CELL_NETMASK = "cellNetMask";
    public static final String BO_EVENT_CELL_BROADCAST_ADDRESS = "cellBroadcastAddress";
    public static final String BO_EVENT_WIFI_IP_ADDRESS = "wifiIPAddress";
    public static final String BO_EVENT_WIFI_NET_MASK = "wifiNetMask";
    public static final String BO_EVENT_WIFI_BROADCAST_ADDRESS = "wifiBroadcastAddress";
    public static final String BO_EVENT_WIFI_ROUTER_ADDRESS = "wifiRouterAddress";
    public static final String BO_EVENT_WIFI_SSID = "wifiSSID";
    public static final String BO_EVENT_CONNECTED_WIFI = "connectedToWifi";
    public static final String BO_EVENT_CONNECTED_TO_CELL_NETWORK = "connectedToCellNetwork";

    public static final String BO_EVENT_AD_INFO = "AdInfo";
    public static final String BO_KEY = "key";
    public static final String BO_DATA = "data";
    public static final String BO_IV = "iv";



}
