package com.blotout.util

object Constant {
    const val SDK_KEY = "sdk_key"
    const val SDK_END_POINT_URL = "SDK_END_POINT_URL"
    const val SDK_MANIFEST_SYNC_TIME = "SDK_MANIFEST_SYNC_TIME"
    const val BO_SDK_ROOT_DIRECTORY_NAME = "BOSDKRootDir"
    const val MANIFEST_FILE_NAME :String = "sdkManifest"


    const val BO_APPLICATION_OPENED = 11001
    const val BO_APPLICATION_INSTALLED = 11002;
    const val BO_APPLICATION_UPDATED = 11003;
    const val BO_PUSH_NOTIFICATION_TAPPED = 11004;
    const val BO_PUSH_NOTIFICATION_RECEIVED = 11005;
    const val BO_REGISTER_FOR_REMOTE_NOTIFICATION = 11006;
    const val BO_DEEP_LINK_OPENED = 11007;
    const val BO_APPLICATION_BACKGROUNDED = 11008;
    const val BO_APP_TRACKING = 11009;
    const val BO_TRANSACTION_COMPLETED = 11010;

    const val Api_Endpoint = "Api_Endpoint";
    const val EVENT_PATH = "Event_Path";
    const val Event_PHI_Public_Key = "PHI_Public_Key";
    const val Event_PII_Public_Key = "PII_Public_Key";
    const val Event_DeviceInfoGrain = "Event_Deviceinfo_Grain";
    const val Event_Push_System_Events = "SDK_Push_System_Events";

    const val BO_SYSTEM = "system";
    const val BO_CODIFIED = "codified";



    const val BO_DEV_EVENT_MAP_ID = 21001;
    const val BO_DEV_EVENT_CUSTOM_KEY= 21100;

    const val BO_SDK_REST_API_MANIFEST_PULL_PATH = "v1/manifest/pull";
    const val BO_SDK_REST_API_EVENTS_PUSH_PATH =  "v1/events/publish";

    const val BO_ANALYTICS_USER_UNIQUE_KEY =   "UserUniqueId";
    const val BO_VERSION_KEY = "BOVersionKey";

    const val BOSDK_MAJOR_VERSION = 0;
    const val BOSDK_MINOR_VERSION = 8;
    const val BOSDK_PATCH_VERSION = 0;




    const val BO_PII = "pii";
    const val BO_PHI = "phi";

    const val BO_EVENT_MAP_ID = "map_id";
    const val BO_EVENT_MAP_Provider = "map_provider";



    ///Platform Constants
    const val BO_Android_Phone = 11
    const val BO_Android_Tablet = 12
    const val BO_Android_TV = 13
    const val BO_iPhone = 14
    const val BO_iPad = 15
    const val BO_Mobile_Web = 16
    const val BO_Web_Application = 17
    const val BO_Apple_TV = 18
    const val BO_Apple_Watch = 19
    const val BO_Android_Watch = 20
    const val BO_Amazon_Fire_TV = 21
    const val BO_Amazon_Fire_Stick = 22
    const val BO_Roku_TV = 23
    const val BO_HbbTV = 24
    const val BO_Smart_Android_TV = 25

    const val BO_Smart_TV_All = 40
    const val BO_Android_All = 50
    const val BO_ios_All = 60
    const val BO_Web_All = 70

}