package com.blotout.utilities;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.USB_SERVICE;
import static android.content.Context.WIFI_SERVICE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import com.blotout.analytics.BOSharedManager;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;


public class BOCommonUtils {
    private static final String TAG = "BOCommonUtils";


    private static byte[] byteOutput;
    private static String stringOutput;

    //This method is used to convert string into base64 string
    public static String encodeToString(@NonNull String input) {
        byte[] inputBytes;
        try {
            if (!isEmpty(input)) {
                inputBytes = input.getBytes(StandardCharsets.UTF_8);
                stringOutput = Base64.encodeToString(inputBytes, Base64.DEFAULT);

            } else {
                Logger.INSTANCE.e(TAG, BOCommonConstants.EMPTY_STRING);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return stringOutput;
    }


    public static byte[] decodeBase64(String input) {

        try {
            if (!isEmpty(input)) {
                byteOutput = Base64.decode(input, Base64.DEFAULT);
            } else {
                Logger.INSTANCE.e(TAG, BOCommonConstants.EMPTY_STRING);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return byteOutput;
    }

    public static byte[] decodeBase64(@Nullable byte[] input) {

        try {
            if (input != null) {
                byteOutput = Base64.decode(input, Base64.DEFAULT);
            } else {
                Logger.INSTANCE.e(TAG, BOCommonConstants.EMPTY_STRING);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return byteOutput;
    }

    public static boolean isEmpty(@Nullable String str) {
        return str == null || str.trim().length() == 0;
    }


    @NonNull
    public static String getHashValue(@NonNull String deviceId) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytes = messageDigest.digest(deviceId.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte bytes : hashInBytes) {
                sb.append(String.format("%02x", bytes));
            }
            return sb.toString();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * Gets the percentage threashold to check whether to use internal memory of external memory in
     * case we have both
     *
     * @param usableSpace: usable internal memory available
     * @param totalSpace:  total internal memory
     * @return percentage in terms of threshold
     */
    public static float getMemoryThreshold(long usableSpace, long totalSpace) {
        long threasholdProportion = (long) ((float) usableSpace / totalSpace * 100);
        //long threasholdProportion = usableSpace / totalSpace;
        Logger.INSTANCE.e(TAG, String.valueOf(threasholdProportion));

        return threasholdProportion;
    }

    @NonNull
    public static String sizeFormatter(long size) {
        try {
            String suffix = null;
            if (size >= 1024) {
                suffix = "KB";
                size /= 1024;

                if (size >= 1024) {
                    suffix = "MB";
                    size /= 1024;

                    if (size >= 1024) {
                        suffix = "GB";
                        size /= 1024;
                    }
                }
            }

            StringBuilder stringBuilder = new StringBuilder(Long.toString(size));
            if (suffix != null) stringBuilder.append(" ").append(suffix);
            return stringBuilder.toString();
        } catch (Exception e) {
            //throw new AssertionError("Activity Not Found: " + e.toString());
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }


    @Nullable
    public static String loadJSONFromAsset(@NonNull Context context, @NonNull String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Logger.INSTANCE.e(TAG, ex.getMessage());
            return null;
        }
        return json;
    }

    @Nullable
    public static String getJsonStringFromHashMap(@Nullable HashMap<String, Object> dictionary) {

        if (dictionary == null)
            return null;
        try {
            String jsonStr = null;
            ObjectMapper mapper = new ObjectMapper();
            jsonStr = mapper.writeValueAsString(dictionary);
            return jsonStr;
        } catch (JsonProcessingException exception) {
            Logger.INSTANCE.e(TAG, exception.getMessage());
        }
        return null;
    }

    @Nullable
    public static HashMap<String, Object> getHashmapFromJsonString(@Nullable String jsonString) {
        if (jsonString == null)
            return null;

        try {
            HashMap<String, Object> retMap = null;
            ObjectMapper mapper = new ObjectMapper();
            retMap = mapper.readValue(jsonString, HashMap.class);
            return retMap;
        } catch (IOException exception) {
            Logger.INSTANCE.e(TAG, exception.getMessage());
        }
        return null;
    }

    @Nullable
    public static String lastPathComponent(@Nullable String fileName) {
        try {
            String lastPath = null;
            if (fileName != null) {
                lastPath = fileName.substring(fileName.lastIndexOf('/') + 1);
            }

            return lastPath;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    @Nullable
    public static String stringByDeletingPathExtension(@Nullable String fileName) {
        try {
            String lastPath = null;
            if (fileName != null) {
                lastPath = fileName.substring(0, fileName.lastIndexOf("."));
            }
            return lastPath;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }


    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths =
                {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
                        "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                        "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    public static boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }

    public static boolean getSensorEnabled(@NonNull Context context, int sensorType) {
        try {
            SensorManager sensorManager;
            Sensor sensor;

            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            //sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            sensor = sensorManager.getDefaultSensor(sensorType);
            return sensor.isWakeUpSensor();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    public static boolean isMultitaskEnabled(@NonNull Context context) {
        try {
            // This check ensures multitask i enabled
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return ((Activity) context).isInMultiWindowMode()
                        || ((Activity) context).isInPictureInPictureMode();
            }
            return false;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;

    }

    public static int getNumberOfActiveCores() {
        try {
            if (Build.VERSION.SDK_INT >= 17) {
                return Runtime.getRuntime().availableProcessors();
            } else {
                return getNumCoresOldPhones();
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return 1;
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    private static int getNumCoresOldPhones() {

        try {
            //Private Class to display only CPU devices in the directory listing
            class CpuFilter implements FileFilter {
                @Override
                public boolean accept(@NonNull File pathname) {
                    //Check if filename is "cpu", followed by a single digit number
                    return Pattern.matches("cpu[0-9]+", pathname.getName());
                }
            }

            try {
                //Get directory containing CPU info
                File dir = new File("/sys/devices/system/cpu/");
                //Filter to only list the devices we care about
                File[] files = dir.listFiles(new CpuFilter());
                //Return the number of cores (virtual CPU devices)
                return files.length;
            } catch (Exception e) {
                //Default to return 1 core
                return 1;
            }
        } catch (Exception e) {
            return 1;
        }

    }


    private void getCpuInfo(@NonNull Context context) {

        ActivityManager mgr = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = mgr.getRunningAppProcesses();
        Logger.INSTANCE.e("DEBUG", "Running processes:");
        for (Iterator i = processes.iterator(); i.hasNext(); ) {
            ActivityManager.RunningAppProcessInfo p = (ActivityManager.RunningAppProcessInfo) i.next();
            Logger.INSTANCE.e("DEBUG", "  process name: " + p.processName);
            Logger.INSTANCE.e("DEBUG", "     pid: " + p.pid);
            int[] pids = new int[1];
            pids[0] = p.pid;
            android.os.Debug.MemoryInfo[] MI = mgr.getProcessMemoryInfo(pids);
            Logger.INSTANCE.e("memory", "     dalvik private: " + MI[0].dalvikPrivateDirty);
            Logger.INSTANCE.e("memory", "     dalvik shared: " + MI[0].dalvikSharedDirty);
            Logger.INSTANCE.e("memory", "     dalvik pss: " + MI[0].dalvikPss);
            Logger.INSTANCE.e("memory", "     native private: " + MI[0].nativePrivateDirty);
            Logger.INSTANCE.e("memory", "     native shared: " + MI[0].nativeSharedDirty);
            Logger.INSTANCE.e("memory", "     native pss: " + MI[0].nativePss);
            Logger.INSTANCE.e("memory", "     other private: " + MI[0].otherPrivateDirty);
            Logger.INSTANCE.e("memory", "     other shared: " + MI[0].otherSharedDirty);
            Logger.INSTANCE.e("memory", "     other pss: " + MI[0].otherPss);

            Logger.INSTANCE.e("memory", "     total private dirty memory (KB): " + MI[0].getTotalPrivateDirty());
            Logger.INSTANCE.e("memory", "     total shared (KB): " + MI[0].getTotalSharedDirty());
            Logger.INSTANCE.e("memory", "     total pss: " + MI[0].getTotalPss());
        }


    }


    @NonNull
    public static List<Integer> getCPUInfoUsingActivityManager(@NonNull Context context) {
        List<Integer> list = new ArrayList<>();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                ACTIVITY_SERVICE);
        if(activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> pidsTask =
                    activityManager.getRunningAppProcesses();

            for (int i = 0; i < pidsTask.size(); i++) {
                //nameList.add(pidsTask.get(i).processName);
                list.add(pidsTask.get(i).uid);
                getCPUUsage(pidsTask.get(i).pid);

            }
        }
        return list;


    }

    @Nullable
    public static String getCPUUsage(int pid) {
        StringBuilder stringBuilder = new StringBuilder();
        Process process;
        try {
            String[] cmd = {
                    "sh",
                    "-c",
                    "top -m 1000 -d 1 -n 1 | grep \"" + pid + "\" "};
            process = Runtime.getRuntime().exec(cmd);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            Logger.INSTANCE.e(TAG, stringBuilder.toString());

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return null;
    }

    private void getCPUInfo() {
        try {

            String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder processBuilder = new ProcessBuilder(DATA);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            byte[] byteArry = new byte[1024];
            String output = "";
            while (inputStream.read(byteArry) != -1) {
                output = output + new String(byteArry);
            }
            inputStream.close();

            Logger.INSTANCE.e("CPU_INFO", output);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static boolean iSAccessoriesAttached(@NonNull Context context) {

        try {
            UsbManager m = (UsbManager) context.getSystemService(USB_SERVICE);
            HashMap<String, UsbDevice> usbDevices = m.getDeviceList();
            Collection<UsbDevice> ite = usbDevices.values();
            UsbDevice[] usbs = ite.toArray(new UsbDevice[]{});

            if (usbs.length > 0) {
                return true;
            }
//        for (UsbDevice usb : usbs) {
//            Logger.INSTANCE.d("Connected usb devices","Connected usb devices are "+ usb
//            .getDeviceName());
//        }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return false;
    }

    @NonNull
    public static List<String> getNameAccessoriesAttached(@NonNull Context context) {
        List<String> names = new ArrayList<>();

        try {
            UsbManager m = (UsbManager) context.getSystemService(USB_SERVICE);
            HashMap<String, UsbDevice> usbDevices = m.getDeviceList();
            Collection<UsbDevice> ite = usbDevices.values();
            UsbDevice[] usbs = ite.toArray(new UsbDevice[]{});

            for (UsbDevice usb : usbs) {
                names.add(usb.getDeviceName());
                Logger.INSTANCE.d("Connected usb devices", "Connected usb devices are " + usb
                        .getDeviceName());

            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return names;
    }

    public static int numberOfAccessoriesAttached(@NonNull Context context) {
        UsbDevice[] usbs = new UsbDevice[0];
        try {
            UsbManager m = (UsbManager) context.getSystemService(USB_SERVICE);
            HashMap<String, UsbDevice> usbDevices = m.getDeviceList();
            Collection<UsbDevice> ite = usbDevices.values();
            usbs = ite.toArray(new UsbDevice[]{});

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return usbs.length;
    }

    @Nullable
    public static String getWifiIpAddress(@Nullable Context context) {
        WifiInfo wifiInfo;
        String ipAdd = null;
        if (context != null) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(
                    WIFI_SERVICE);
            if (wifiManager != null) {
                wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    ipAdd = android.text.format.Formatter.formatIpAddress(wifiInfo.getIpAddress());
                }
            }
        } else {
            Logger.INSTANCE.e(TAG, "Unable to fetch ip");
        }
        return ipAdd;
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    public static String getCellularIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(
                    NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Logger.INSTANCE.e(TAG, ex.getMessage());
        }
        return "";
    }

    public static String getMacAddress(@Nullable Context context) {

        String defaultMac = "01:02:03:04:05:06";
        WifiInfo wifiInfo = null;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                List<NetworkInterface> networks = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface networkInterface : networks) {
                    if (!networkInterface.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = networkInterface.getHardwareAddress();
                    if (macBytes == null) {
                        defaultMac = "";
                    }

                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x", b)).append(":");
                    }

                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    defaultMac = sb.toString();
                }
            } else {

                if (context != null) {
                    WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(
                            WIFI_SERVICE);
                    if (wifiManager != null)
                        wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null && !TextUtils.isEmpty(wifiInfo.getMacAddress())) {
                        defaultMac = wifiInfo.getMacAddress();
                    }
                } else {
                    Logger.INSTANCE.e(TAG, "No mac address fetched");
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return defaultMac;
    }


    public static String getExternalIpAddress() {

        String externalIp = "";
        //TODO: need to work on this
//        try {
//            // Using https://icanhazip.com
//            //https://checkip.amazonaws.com/
//            String url = ""; //https://icanhazip.com
//            URL whatismyip = new URL(url);
//            BufferedReader in = null;
//            try {
//                in = new BufferedReader(new InputStreamReader(
//                        whatismyip.openStream()));
//                externalIp = in.readLine();
//                return externalIp;
//            } finally {
//                if (in != null) {
//                    try {
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            Logger.INSTANCE.e(TAG, e.getMessage());
//        }
        return externalIp;
    }

    @NonNull
    public static String getNetMask(@NonNull Context context) {

        String s_netmask = "";
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            DhcpInfo d = wifiManager.getDhcpInfo();
            String s_dns1 = "DNS 1: " + d.dns1;
            String s_dns2 = "DNS 2: " + d.dns2;
            String s_gateway = "Default Gateway: " + d.gateway;
            String s_ipAddress = "IP Address: " + d.ipAddress;
            String s_leaseDuration = "Lease Time: " + d.leaseDuration;
            s_netmask = "Subnet Mask: " + d.netmask;
            String s_serverAddress = "Server IP: " + d.serverAddress;
            return s_netmask;

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());

        }
        return s_netmask;
    }


    @Nullable
    public static String getCellBroadcastAddress() {
        //As the broadcast IP address is the current IP address but finishing with 255
        //getLocalIpAddress();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.INSTANCE.e(TAG, ex.getMessage());
        }
        return null;
    }

    @Nullable
    public static String getBroadcast() {
        try {
            System.setProperty("java.net.preferIPv4Stack", "true");
            for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces();
                 niEnum.hasMoreElements(); ) {
                NetworkInterface ni = niEnum.nextElement();
                if (ni != null && !ni.isLoopback()) {
                    for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                        if(interfaceAddress != null && interfaceAddress.getBroadcast()!= null) {
                            return interfaceAddress.getBroadcast().toString().substring(1);
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.INSTANCE.e(TAG, ex.getMessage());
        }
        return null;
    }

    @Nullable
    public static String getWifiBroadcastAddress(@NonNull Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(WIFI_SERVICE);
            DhcpInfo dhcp = wifi.getDhcpInfo();

            int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
            byte[] quads = new byte[4];
            for (int k = 0; k < 4; k++) {
                quads[k] = (byte) (broadcast >> (k * 8));
            }

            return InetAddress.getByAddress(quads).toString();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return null;
    }

    @Nullable
    public static String getRouterAddress(@NonNull Context context) {
        try {
            final WifiManager manager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            final DhcpInfo dhcp = manager.getDhcpInfo();
            return Formatter.formatIpAddress(dhcp.gateway);

        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return null;
    }


    @Nullable
    public static String getCurrentSsid(@NonNull Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if(connManager != null) {
            NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null && networkInfo.isConnected()) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(
                        Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                    if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                        ssid = connectionInfo.getSSID();
                    }
                }
            }
        }
        return ssid;
    }


    @NonNull
    public static String sizeFormatterWithUnit(long size) {
        if (size <= 0) {
            return "0";
        }

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int bytes = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("##0.##").format(size / Math.pow(1024, bytes)) + " "
                + units[bytes];
    }

    public static String sizeFormatterWithoutUnit(long size) {
        if (size <= 0) {
            return "0";
        }

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int bytes = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("##0.##").format(size / Math.pow(1024, bytes));
    }

    @NonNull
    public static String getUsedMemorySize() {
        String usedSizeString = "0";

        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
            usedSizeString = sizeFormatter(usedSize);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return usedSizeString;
    }

    public static long getUsedMemorySizeNumber() {
        long usedSizeString = 0;

        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
            usedSizeString = usedSize;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return usedSizeString;
    }

    @NonNull
    public static String getFreeMemorySize() {
        String freeSizeString = "0";

        long freeSize = 0L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            freeSizeString = sizeFormatter(freeSize);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return freeSizeString;
    }

    public static long getFreeMemorySizeLong() {
        long freeSizeString = 0;

        long freeSize = 0L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            freeSizeString = freeSize;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return freeSizeString;
    }

    public static int getStringMD5CustomIntSumWithCharIndexAdded(String event, boolean caseSensitive) {
        int sum = 0;
        try {
            event = caseSensitive ? event : event.toLowerCase();
            String stringToHash = event;
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(stringToHash.getBytes());
            byte[] digiest = messageDigest.digest();
            String md5String = convertToHex(digiest);


            for (int index = 0; index < md5String.length(); index++) {
                char c = md5String.charAt(index);
                if (isNumberChar(c)) {
                    sum = sum + intValueForChar(c);
                } else {
                    sum = sum + intValueForChar(c) + index;
                }
            }
        } catch (NoSuchAlgorithmException e) {

        }
        return sum;
    }

    @NonNull
    public static String convertToHex(@NonNull byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static boolean isNumberChar(char sChar) {
        boolean isNum = false;

        switch (sChar) {
            case '0':
                isNum = true;
                break;
            case '1':
                isNum = true;
                break;
            case '2':
                isNum = true;
                break;
            case '3':
                isNum = true;
                break;
            case '4':
                isNum = true;
                break;
            case '5':
                isNum = true;
                break;
            case '6':
                isNum = true;
                break;
            case '7':
                isNum = true;
                break;
            case '8':
                isNum = true;
                break;
            case '9':
                isNum = true;
                break;
        }
        return isNum;
    }

    public static int intValueForChar(char sChar) {

        switch (sChar) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;

            case '7':
                return 7;

            case '8':
                return 8;

            case '9':
                return 9;
            case ' ':
                return 10;

            case 'a':
                return 11;

            case 'b':
                return 12;

            case 'c':
                return 13;

            case 'd':
                return 14;
            case 'e':
                return 15;

            case 'f':
                return 16;

            case 'g':
                return 17;

            case 'h':
                return 18;

            case 'i':
                return 19;

            case 'j':
                return 20;

            case 'k':
                return 21;

            case 'l':
                return 22;

            case 'm':
                return 23;

            case 'n':
                return 24;

            case 'o':
                return 25;

            case 'p':
                return 26;

            case 'q':
                return 27;

            case 'r':
                return 28;

            case 's':
                return 29;

            case 't':
                return 30;

            case 'u':
                return 31;

            case 'v':
                return 32;

            case 'w':
                return 33;

            case 'x':
                return 34;
            case 'y':
                return 35;

            case 'z':
                return 36;

            case 'A':
                return 37;

            case 'B':
                return 38;

            case 'C':
                return 39;

            case 'D':
                return 40;

            case 'E':
                return 41;

            case 'F':
                return 42;

            case 'G':
                return 43;

            case 'H':
                return 44;

            case 'I':
                return 45;

            case 'J':
                return 46;

            case 'K':
                return 47;

            case 'L':
                return 48;

            case 'M':
                return 49;

            case 'N':
                return 50;

            case 'O':
                return 51;

            case 'P':
                return 52;

            case 'Q':
                return 53;

            case 'R':
                return 54;

            case 'S':
                return 55;

            case 'T':
                return 56;

            case 'U':
                return 57;

            case 'V':
                return 58;

            case 'W':
                return 59;

            case 'X':
                return 60;

            case 'Y':
                return 61;

            case 'Z':
                return 62;

            default:
                return (int) sChar;

        }
    }

    @NonNull
    public static int getAsciiCustomIntSum(String input, boolean isCaseSenstive) {
        input = isCaseSenstive ? input : input.toLowerCase();
        int sum = 0;
        if (BOCommonUtils.isPureAscii(input)) {
            for (int index = 0; index < input.length(); index++) {
                char c = input.charAt(index);
                sum = sum + intValueForChar(c);
            }
        }
        return sum;
    }

    @NonNull
    public static String generateMessageIDForEvent(String eventName, String eventCode, long eventTime) {
        return eventCode + BOCommonUtils.codeForCustomCodifiedEvent(eventName) + eventTime + BODateTimeUtils.get13DigitNumberObjTimeStamp();
    }

    @NonNull
    public static String getMessageIDForEventWithEventId(String eventName, long eventID) {
        return eventID + BOCommonUtils.codeForCustomCodifiedEvent(eventName) + eventName + BODateTimeUtils.get13DigitNumberObjTimeStamp();
    }

    @NonNull
    public static String getMessageIDForEvent(String eventName) {
        return "" + BOCommonUtils.getAsciiCustomIntSum(eventName, false) + BOCommonUtils.codeForCustomCodifiedEvent(eventName) + BOCommonUtils.getStringMD5CustomIntSumWithCharIndexAdded(eventName, false) + BODateTimeUtils.get13DigitNumberObjTimeStamp();
    }

    @Nullable
    public static Long codeForCustomCodifiedEvent(@Nullable String eventName) {
        try {
            String tempEventName = eventName.replaceAll(" ", "");
            if (eventName != null && !eventName.equals("") && !tempEventName.equals("")) {
                BOSharedPreferenceImpl analyticsRootUD = BOSharedPreferenceImpl.getInstance();
                String codifiedEventsString = analyticsRootUD.getString(BOCommonConstants.BO_ANALYTICS_ALL_DEV_CODIFIED_CUSTOM_EVENTS);
                HashMap<String, Object> allCustomEvents = BOCommonUtils.getHashmapFromJsonString(codifiedEventsString);

                if (allCustomEvents == null) {
                    allCustomEvents = new HashMap<>();
                    //allCustomEvents.put(eventName, BONetworkConstants.BO_DEV_EVENT_CUSTOM_KEY);
                    //analyticsRootUD.saveString(BOCommonConstants.BO_ANALYTICS_ALL_DEV_CODIFIED_CUSTOM_EVENTS, BOCommonUtils.getJsonStringFromHashMap(allCustomEvents));
                    //return BONetworkConstants.BO_DEV_EVENT_CUSTOM_KEY;
                }

                boolean isNameFound = allCustomEvents.containsKey(eventName);
                if (isNameFound) {
                    return Long.valueOf((Integer) allCustomEvents.get(eventName));
                } else {
                    int eventNameIntSum = 0;
                    boolean isAscii = BOCommonUtils.isPureAscii(eventName);
                    if (isAscii) {
                        eventNameIntSum = BOCommonUtils.getAsciiCustomIntSum(eventName, false);
                    } else {
                        eventNameIntSum = BOCommonUtils.getStringMD5CustomIntSumWithCharIndexAdded(eventName, false);
                    }
                    long eventNameIntSumModulo = eventNameIntSum % 9000;
                    long eventSubCode = BONetworkConstants.BO_DEV_EVENT_CUSTOM_KEY + eventNameIntSumModulo; //21100
                    Long eventSubCodeObj = eventSubCode;
                    //Test for sub code exist and above logic generated duplicate code then increment by 1 until unique & if greater than 500 then decrement by one until unique.
                    //if containsObject is having issues then move manual number to int and the int comaprision
                    if (allCustomEvents.values().contains(eventSubCodeObj)) {
                        Logger.INSTANCE.d(TAG, "codeForCustomCodifiedEvent: " + eventSubCodeObj);
                    }

                    while (allCustomEvents.values().contains(eventSubCodeObj)) {
                        eventNameIntSum = eventNameIntSum + 1;
                        eventNameIntSumModulo = eventNameIntSum % 9000;
                        eventSubCode = BONetworkConstants.BO_DEV_EVENT_CUSTOM_KEY + eventNameIntSumModulo; //21100
                        eventSubCodeObj = eventSubCode;
                    }
                    allCustomEvents.put(eventName, eventSubCodeObj);
                    analyticsRootUD.saveString(BOCommonConstants.BO_ANALYTICS_ALL_DEV_CODIFIED_CUSTOM_EVENTS, BOCommonUtils.getJsonStringFromHashMap(allCustomEvents));
                    return eventSubCodeObj;
                }
            }
            return Long.valueOf((Integer) 0);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return Long.valueOf((Integer) 0);
    }

    public static boolean isPureAscii(String input) {
        try {
            return Charset.forName("US-ASCII").newEncoder().canEncode(input);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    public static boolean checkVPN() {

        boolean vpnInUse = false;
        try {
            Context context = BOSharedManager.getInstance().getContext();
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(activeNetwork);
                vpnInUse = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
            } else {
                vpnInUse = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting();
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return vpnInUse;
    }

    public static HashMap<String, Object> lastObject(List<HashMap<String, Object>> list) {
        if (list != null && !list.isEmpty()) {
            return list.get(list.size() - 1);
        }

        return null;
    }

    public static String  getUUID(){
       String uniqueID = UUID.randomUUID().toString();
       return uniqueID;

    }

    /** Returns true if the application has the given permission. */
    public static boolean hasPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == PERMISSION_GRANTED;
    }

    /** Returns true if the application has the given feature. */
    public static boolean hasFeature(Context context, String feature) {
        return context.getPackageManager().hasSystemFeature(feature);
    }

    /** Returns the system service for the given string. */
    @SuppressWarnings("unchecked")
    public static <T> T getSystemService(Context context, String serviceConstant) {
        return (T) context.getSystemService(serviceConstant);
    }
    private static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }
    private static int getTrimmedLength(@NonNull CharSequence s) {
        int len = s.length();

        int start = 0;
        while (start < len && s.charAt(start) <= ' ') {
            start++;
        }

        int end = len;
        while (end > start && s.charAt(end - 1) <= ' ') {
            end--;
        }

        return end - start;
    }

    /** Returns true if the string is null, or empty (once trimmed). */
    public static boolean isNullOrEmpty(CharSequence text) {
        return isEmpty(text) || getTrimmedLength(text) == 0;
    }

    public static String getPasspharseKey() {
        try {
            String deviceID = BODeviceDetection.getDeviceId();
            StringBuilder newDeviceId = new StringBuilder();

            for (char item : deviceID.toCharArray()) {
                int code = item + 3;
                newDeviceId.append((char) (code));
            }

            //Shift each char by anonymus value and create a special cypher
//        for (char item : deviceID.toCharArray()) {
//            if((item >= 65 && item <= 90) ) {
//                int code = item + 3;
//                if(code > 90) {
//                    code = code - (90-64);
//                }
//                newDeviceId.append((char) (code));
//            } else if(item >= 97 && item <= 122) {
//                int code = item + 3;
//                if(code > 122) {
//                    code = code - (122-96);
//                }
//                newDeviceId.append((char) (code));
//            }
//        }
            return newDeviceId.toString();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return "";
        }
    }
}
