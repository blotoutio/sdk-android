package com.blotout.utilities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BONetworkConstants;
import com.blotout.storage.BOSharedPreferenceImpl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Blotout on 23,May,2020
 */
public class BODeviceDetection {

    private static final String TAG = "BODeviceDetection";
    public static final String BO_ANALYTICS_USER_UNIQUE_KEY = "com.blotout.sdk.Analytics.User.UniqueId";

    public static int getDevicePlatformCode() {

        Context context = BOSharedManager.getInstance().getContext();
        String userAgent = getDefaultUserAgentString(context);
        if (userAgent != null) {
            if (isTabletDevice(context)) {
                // public static final int  BO_Amazon_Fire_Stick = 22;
                if (isHbbTVDevice(userAgent)) {
                    return BONetworkConstants.BO_HbbTV;
                } else if (isRokuDevice(userAgent)) {
                    return BONetworkConstants.BO_Roku_TV;
                } else if (isAmazonFire(userAgent)) {
                    return BONetworkConstants.BO_Amazon_Fire_TV;
                } else if (isAndroidTV(userAgent)) {
                    return BONetworkConstants.BO_Android_TV;
                }

                return BONetworkConstants.BO_Android_TV;

            } else {
                if (isMobileDevice(userAgent)) {
                    return BONetworkConstants.BO_Android_Phone;
                } else {
                    return BONetworkConstants.BO_Android_All;
                }
                //public static final int  BO_Android_Watch = 20;
            }
        } else {
            return BONetworkConstants.BO_Android_All;
        }
    }

    public static boolean isAndroidTV(String userAgent) {
        final String androidTV = "TV".toLowerCase();
        return userAgent.toLowerCase().contains(androidTV);
    }

    public static boolean isMobileDevice(String userAgent) {
        final String Mobile = "Mobile".toLowerCase();
        return userAgent.toLowerCase().contains(Mobile);
    }

    public static boolean isHbbTVDevice(String userAgent) {
        final String HbbTV = "HbbTV".toLowerCase();
        return userAgent.toLowerCase().contains(HbbTV);
    }

    public static boolean isRokuDevice(String userAgent) {
        final String Roku = "Roku".toLowerCase();
        return userAgent.toLowerCase().contains(Roku);
    }

    public static boolean isMeizuDevice(String userAgent) {
        final String MEIZU = "meizu".toLowerCase();
        return userAgent.toLowerCase().contains(MEIZU);
    }

    /**
     * Returns true if the device manufacturer is LG.
     */
    public static boolean isLGEDevice(String userAgent) {
        final String LGE = "lge".toLowerCase();
        return userAgent.toLowerCase().contains(LGE);
    }

    /**
     * Returns true if the device manufacturer is Samsung.
     */
    public static boolean isSamsungDevice(String userAgent) {
        final String SAMSUNG = "samsung".toLowerCase();
        return userAgent.toLowerCase().contains(SAMSUNG);
    }

    public static boolean isAmazonFire(String userAgent) {
        final String AMAZON = "Amazon".toLowerCase();
        final String FIRE_TV = "AFTS".toLowerCase();

        return userAgent.toLowerCase().contains(AMAZON) || userAgent.toLowerCase().contains(FIRE_TV);
    }

    private static boolean isTabletDevice(Context activityContext) {
        try {
            boolean device_large = ((activityContext.getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_LARGE);

            if (device_large) {
                DisplayMetrics metrics = new DisplayMetrics();
                Activity activity = (Activity) activityContext;
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

                if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                        || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                        || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                        || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    public static String getDefaultUserAgentString(Context context) {
        if (Build.VERSION.SDK_INT >= 17) {
            return NewApiWrapper.getDefaultUserAgent(context);
        }

        try {
            Constructor<WebSettings> constructor = WebSettings.class.getDeclaredConstructor(Context.class, WebView.class);
            constructor.setAccessible(true);
            try {
                WebSettings settings = constructor.newInstance(context, null);
                return settings.getUserAgentString();
            } finally {
                constructor.setAccessible(false);
            }
        } catch (Exception e) {
            return new WebView(context).getSettings().getUserAgentString();
        }
    }

    @TargetApi(17)
    static class NewApiWrapper {
        static String getDefaultUserAgent(Context context) {
            return WebSettings.getDefaultUserAgent(context);
        }
    }

    private static final String EMULATOR_ANDROID_ID = "9774d56d682e549c";
    private static final String[] BAD_SERIAL_PATTERNS = {"1234567", "abcdef", "dead00beef", "unknown"};

    /**
     * Generating deviceId based on available data
     * @return deviceId string
     */
//    @SuppressLint({"HardwareIds", "MissingPermission"})
//    public static String getDeviceId(final Context context) {
//        String deviceId ="";
//
//        try {
//            deviceId = BOSharedPreferenceImpl.getInstance().getString(BO_ANALYTICS_USER_UNIQUE_KEY);
//            if (deviceId != null && deviceId.length() > 0) {
//                return deviceId;
//            } else {
//                /**
//                 * A hardware serial number, if available. Alphanumeric only, case-insensitive.
//                 */
//
//                final String androidSerialId = android.os.Build.SERIAL;
//                if (!TextUtils.isEmpty(androidSerialId) && !Build.UNKNOWN.equals(androidSerialId) && !isBadSerial(androidSerialId)) {
//                    deviceId = androidSerialId;
//                } else {
//                    /**
//                     * A 64-bit number (as a hex string) that is randomly
//                     * generated when the user first sets up the device and should remain
//                     * constant for the lifetime of the user's device. The value may
//                     * change if a factory reset is performed on the device.
//                     * <p class="note"><strong>Note:</strong> When a device has <a
//                     * href="{@docRoot}about/versions/android-4.2.html#MultipleUsers">multiple users</a>
//                     * (available on certain devices running Android 4.2 or higher), each user appears as a
//                     * completely separate device, so the {@code ANDROID_ID} value is unique to each
//                     * user.</p>
//                     */
//                    final String androidSecureId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//                    if (!TextUtils.isEmpty(androidSecureId) && !EMULATOR_ANDROID_ID.equals(androidSecureId) && !isBadDeviceId(androidSecureId)
//                            && androidSecureId.length() == EMULATOR_ANDROID_ID.length()) {
//                        deviceId = androidSecureId;
//                    } else {
//                        String telephonyDeviceId = null;
//                        try {
//                            /**
//                             * Returns the unique device ID, for example, the IMEI for GSM and the MEID
//                             * or ESN for CDMA phones. Return null if device ID is not available.
//                             *
//                             * <p>Requires Permission:
//                             *   {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
//                             */
//                            telephonyDeviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//                        } catch (RuntimeException e) {
//                            e.printStackTrace();
//                        }
//                        if (!TextUtils.isEmpty(telephonyDeviceId)) {
//                            deviceId = telephonyDeviceId;
//                        }
//                    }
//                }
//                if(deviceId != null && deviceId.length() >0) {
//                    deviceId = UUID.nameUUIDFromBytes(deviceId.getBytes()).toString();
//                    BOSharedPreferenceImpl.getInstance().saveString(BO_ANALYTICS_USER_UNIQUE_KEY, deviceId);
//                } else {
//                    deviceId = UUID.nameUUIDFromBytes(BOCommonUtils.getUniqueID().getBytes()).toString();
//                    BOSharedPreferenceImpl.getInstance().saveString(BO_ANALYTICS_USER_UNIQUE_KEY, deviceId);
//                }
//            }
//        }  catch (Exception e) {
//            Logger.INSTANCE.e(TAG, e.toString());
//        }
//
//        return deviceId;
//    }

    public static String getDeviceId() {
        //user id generation update =
        //Epoc 13 Digit Time at start +
        // Client SDK Token + UUID generate once +
        // 10 digit random number+ 10 digit random number +
        // Epoc 13 Digit time at the end = Input for SHA 512 or UUID function in case it takes

        String deviceId ="";
        try {

            deviceId = BOSharedPreferenceImpl.getInstance().getString(BO_ANALYTICS_USER_UNIQUE_KEY);
            if (deviceId != null && deviceId.length() > 0) {
                return deviceId;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                stringBuilder.append(BlotoutAnalytics_Internal.getInstance().prodBlotoutKey != null ? BlotoutAnalytics_Internal.getInstance().prodBlotoutKey
                        : BlotoutAnalytics_Internal.getInstance().testBlotoutKey);
                stringBuilder.append(UUID.nameUUIDFromBytes(BOCommonUtils.getUUID().getBytes()).toString());
                stringBuilder.append(generateNumber());
                stringBuilder.append(generateNumber());
                stringBuilder.append(BODateTimeUtils.get13DigitNumberObjTimeStamp());
                String guidString = BODeviceDetection.convertTo64CharUUID(BOEncryptionManager.sha256(stringBuilder.toString()));
                deviceId = guidString != null ? guidString : UUID.nameUUIDFromBytes(stringBuilder.toString().getBytes()).toString();
                //check for if SHA256 conversion failed
                deviceId = deviceId != null ? deviceId : BOCommonUtils.getUUID();
                BOSharedPreferenceImpl.getInstance().saveString(BO_ANALYTICS_USER_UNIQUE_KEY, deviceId);
            }

        }  catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return deviceId;
    }

    // Generates a random int with n digits
    private static long generateNumber() {
        return (long)(Math.random()*100000 + 3333300000L);
    }

    private static String convertTo64CharUUID(String stringToConvert) {
        try {
            if(stringToConvert != null && stringToConvert.length()>0) {
                String str = stringToConvert;
                ArrayList<Integer> lengthsOfPart = new ArrayList<>(Arrays.asList(16, 8, 8, 8, 24));
                ArrayList<String> parts = new ArrayList<String>();
                Integer range = 0;
                for (Integer i = 0; i < lengthsOfPart.size(); i++) {
                    String stringOfRange = str.substring(range, range + lengthsOfPart.get(i));
                    parts.add(stringOfRange);
                    range += lengthsOfPart.get(i);
                }

                String uuid64Char = TextUtils.join("-", parts);
                return uuid64Char;
            }
        }  catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return stringToConvert;
    }

    private String getUniqueUserId() {

        String uniqueId = BOSharedPreferenceImpl.getInstance().getString(BO_ANALYTICS_USER_UNIQUE_KEY);
        if (uniqueId != null) {
            return uniqueId;
        } else {
            uniqueId = BOCommonUtils.getUUID();
            BOSharedPreferenceImpl.getInstance().saveString(BO_ANALYTICS_USER_UNIQUE_KEY, uniqueId);
            return uniqueId;
        }
    }

    private static boolean isBadDeviceId(String id) {
        // empty or contains only spaces or 0
        return TextUtils.isEmpty(id) || TextUtils.isEmpty(id.replace('0', ' ').replace('-', ' ').trim());
    }

    private static boolean isBadSerial(String id) {
        if (!TextUtils.isEmpty(id)) {
            id = id.toLowerCase();
            for (String pattern : BAD_SERIAL_PATTERNS) {
                if (id.contains(pattern)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }
    /*
    Android Mobile User Agents
    Samsung Galaxy S9
    Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36
    Samsung Galaxy S8
    Mozilla/5.0 (Linux; Android 7.0; SM-G892A Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/60.0.3112.107 Mobile Safari/537.36
    Samsung Galaxy S7
    Mozilla/5.0 (Linux; Android 7.0; SM-G930VC Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/58.0.3029.83 Mobile Safari/537.36
    Samsung Galaxy S7 Edge
    Mozilla/5.0 (Linux; Android 6.0.1; SM-G935S Build/MMB29K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36
    Samsung Galaxy S6
    Mozilla/5.0 (Linux; Android 6.0.1; SM-G920V Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36
    Samsung Galaxy S6 Edge Plus
    Mozilla/5.0 (Linux; Android 5.1.1; SM-G928X Build/LMY47X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Mobile Safari/537.36
    Nexus 6P
    Mozilla/5.0 (Linux; Android 6.0.1; Nexus 6P Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Mobile Safari/537.36
    Sony Xperia XZ
    Mozilla/5.0 (Linux; Android 7.1.1; G8231 Build/41.2.A.0.219; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/59.0.3071.125 Mobile Safari/537.36
    Sony Xperia Z5
    Mozilla/5.0 (Linux; Android 6.0.1; E6653 Build/32.2.A.0.253) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36
    HTC One X10
    Mozilla/5.0 (Linux; Android 6.0; HTC One X10 Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36
    HTC One M9
    Mozilla/5.0 (Linux; Android 6.0; HTC One M9 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.3
    If you're wondering which devices are most common where you are, read our list of the world's most popular Android devices.

    Apple iPhone XR (Safari)
    Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1
    Apple iPhone XS (Chrome)
    Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/69.0.3497.105 Mobile/15E148 Safari/605.1
    Apple iPhone XS Max (Firefox)
    Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) FxiOS/13.2b11866 Mobile/16A366 Safari/605.1.15
    Apple iPhone X
    Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1
    Apple iPhone 8
    Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1
    Apple iPhone 8 Plus
    Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A5370a Safari/604.1
    Apple iPhone 7
    Mozilla/5.0 (iPhone9,3; U; CPU iPhone OS 10_0_1 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/14A403 Safari/602.1
    Apple iPhone 7 Plus
    Mozilla/5.0 (iPhone9,4; U; CPU iPhone OS 10_0_1 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/14A403 Safari/602.1
    Apple iPhone 6
    Mozilla/5.0 (Apple-iPhone7C2/1202.466; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543 Safari/419.3
    MS Windows Phone User Agents
    Microsoft Lumia 650
    Mozilla/5.0 (Windows Phone 10.0; Android 6.0.1; Microsoft; RM-1152) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Mobile Safari/537.36 Edge/15.15254
    Microsoft Lumia 550
    Mozilla/5.0 (Windows Phone 10.0; Android 4.2.1; Microsoft; RM-1127_16056) AppleWebKit/537.36(KHTML, like Gecko) Chrome/42.0.2311.135 Mobile Safari/537.36 Edge/12.10536
    Microsoft Lumia 950
    Mozilla/5.0 (Windows Phone 10.0; Android 4.2.1; Microsoft; Lumia 950) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Mobile Safari/537.36 Edge/13.1058
    Tablet User Agents
    Google Pixel C
    Mozilla/5.0 (Linux; Android 7.0; Pixel C Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/52.0.2743.98 Safari/537.36
    Sony Xperia Z4 Tablet
    Mozilla/5.0 (Linux; Android 6.0.1; SGP771 Build/32.2.A.0.253; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/52.0.2743.98 Safari/537.36
    Nvidia Shield Tablet K1
    Mozilla/5.0 (Linux; Android 6.0.1; SHIELD Tablet K1 Build/MRA58K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Safari/537.36
    Samsung Galaxy Tab S3
    Mozilla/5.0 (Linux; Android 7.0; SM-T827R4 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.116 Safari/537.36
    Samsung Galaxy Tab A
    Mozilla/5.0 (Linux; Android 5.0.2; SAMSUNG SM-T550 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/3.3 Chrome/38.0.2125.102 Safari/537.36
    Amazon Kindle Fire HDX 7
    Mozilla/5.0 (Linux; Android 4.4.3; KFTHWI Build/KTU84M) AppleWebKit/537.36 (KHTML, like Gecko) Silk/47.1.79 like Chrome/47.0.2526.80 Safari/537.36
    LG G Pad 7.0
    Mozilla/5.0 (Linux; Android 5.0.2; LG-V410/V41020c Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/34.0.1847.118 Safari/537.36
    If you're looking for a list of mobile browser user-agents, we've got them too - List Of Mobile Browser User-Agent Strings .

    Desktop User Agents
    Windows 10-based PC using Edge browser
    Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.246
    Chrome OS-based laptop using Chrome browser (Chromebook)
    Mozilla/5.0 (X11; CrOS x86_64 8172.45.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.64 Safari/537.36
    Mac OS X-based computer using a Safari browser
    Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/601.3.9 (KHTML, like Gecko) Version/9.0.2 Safari/601.3.9
    Windows 7-based PC using a Chrome browser
    Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36
    Linux-based PC using a Firefox browser
    Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:15.0) Gecko/20100101 Firefox/15.0.1
    Bring device intelligence to your web applications in minutes.
    For the web, native apps and mobile operator environments.

    Compare options and pricing

    Set Top Boxes User Agents
            Chromecast
    Mozilla/5.0 (CrKey armv7l 1.5.16041) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.0 Safari/537.36
    Roku Ultra
    Roku4640X/DVP-7.70 (297.70E04154A)
    Minix NEO X5
    Mozilla/5.0 (Linux; U; Android 4.2.2; he-il; NEO-X5-116A Build/JDQ39) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30
    Amazon 4K Fire TV
    Mozilla/5.0 (Linux; Android 5.1; AFTS Build/LMY47O) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/41.99900.2250.0242 Safari/537.36
    Google Nexus Player
    Dalvik/2.1.0 (Linux; U; Android 6.0.1; Nexus Player Build/MMB29T)
    Apple TV 5th Gen 4K
    AppleTV6,2/11.1
    Apple TV 4th Gen
    AppleTV5,3/9.1.1
    Game Consoles User Agents
    Nintendo Wii U
    Mozilla/5.0 (Nintendo WiiU) AppleWebKit/536.30 (KHTML, like Gecko) NX/3.0.4.2.12 NintendoBrowser/4.3.1.11264.US
    Xbox One S
    Mozilla/5.0 (Windows NT 10.0; Win64; x64; XBOX_ONE_ED) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393
    Xbox One
    Mozilla/5.0 (Windows Phone 10.0; Android 4.2.1; Xbox; Xbox One) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Mobile Safari/537.36 Edge/13.10586
    Playstation 4
    Mozilla/5.0 (PlayStation 4 3.11) AppleWebKit/537.73 (KHTML, like Gecko)
    Playstation Vita
    Mozilla/5.0 (PlayStation Vita 3.61) AppleWebKit/537.73 (KHTML, like Gecko) Silk/3.2
    Nintendo 3DS
    Mozilla/5.0 (Nintendo 3DS; U; ; en) Version/1.7412.EU
    Bots and Crawlers User Agents
    We've compiled a more in-depth list of User-Agent strings of the most active web crawlers and bots.

    Google bot
    Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)
    Bing bot
    Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)
    Yahoo! bot
    Mozilla/5.0 (compatible; Yahoo! Slurp; http://help.yahoo.com/help/us/ysearch/slurp)
    E Readers User Agents
    Amazon Kindle 4
    Mozilla/5.0 (X11; U; Linux armv7l like Android; en-us) AppleWebKit/531.2+ (KHTML, like Gecko) Version/5.0 Safari/533.2+ Kindle/3.0+
    Amazon Kindle 3
    Mozilla/5.0 (Linux; U; en-US) AppleWebKit/528.5+ (KHTML, like Gecko, Safari/528.5+) Version/4.0 Kindle/3.0 (screen 600x800; rotate)
    */
}
