package com.blotout.Controllers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Debug;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blotout.analytics.BOSharedManager;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.security.CryptoPrimitive.SIGNATURE;

/**
 * Created by Blotout on 07,December,2019
 */
public class BODeviceAndAppFraudController {

    private static final String TAG = "BODeviceAndAppFraudController";
    private static volatile BODeviceAndAppFraudController instance;

    private boolean isDeviceCompromisedCalculated;
    private boolean isDeviceCompromisedValue;

    private boolean isAppCompromisedCalculated;
    private boolean isAppCompromisedValue;

    public static BODeviceAndAppFraudController getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BODeviceAndAppFraudController.class) {
                if (instance == null) {
                    instance = new BODeviceAndAppFraudController();
                }
            }
        }
        return instance;
    }

    public boolean isDeviceJailbroken() {
        return isDeviceCompromised();
    }

    public boolean isDeviceCompromised() {
        if (this.isDeviceCompromisedCalculated) {
            Context context = BOSharedManager.getInstance().getContext();
            try {
                boolean isTestBuild = isTestBuild();
                boolean hasSuperuserAPK = checkRootPackages();
                boolean hasChainfiresupersu = hasChainfiresupersu(context);
                boolean hasSU = hasSU();
                Logger.INSTANCE.d("RootChecker", "isTestBuild: " + isTestBuild + " hasSuperuserAPK: " + hasSuperuserAPK + " hasChainfiresupersu: " + hasChainfiresupersu + " hasSU: " + hasSU);
                this.isDeviceCompromisedValue = isTestBuild || hasSuperuserAPK || hasChainfiresupersu || hasSU;
                this.isDeviceCompromisedCalculated = true;
                return this.isDeviceCompromisedValue;
            } catch (Exception e) {
                return this.isDeviceCompromisedValue;
            }
        } else {
            return this.isDeviceCompromisedValue;
        }
    }

    public boolean isAppCompromised() {
        if (isAppCompromisedCalculated) {
            try {
                Context context = BOSharedManager.getInstance().getContext();
                this.isAppCompromisedValue = isPackageInstalled("dylib_name", context) && isPackageInstalled("libcycript", context) && isDebuggable(context)
                        && detectDebugger();
                this.isAppCompromisedCalculated = true;
                return this.isAppCompromisedValue;
            } catch (Exception e) {
                return this.isAppCompromisedValue;
            }
        } else {
            return this.isAppCompromisedValue;
        }
    }

    /**************************************** Checker methods *************************************/
    private static boolean isDebuggable(Context context) {
        try {
            return ((context.getApplicationContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    private static boolean detectDebugger() {
        return Debug.isDebuggerConnected();
    }

    private static boolean detect_threadCpuTimeNanos() {
        try {
            long start = Debug.threadCpuTimeNanos();

            for (int i = 0; i < 1000000; ++i)
                continue;

            long stop = Debug.threadCpuTimeNanos();

            if (stop - start < 10000000) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    public boolean isTestBuild() {
        try {
            String buildTags = android.os.Build.TAGS;
            return buildTags != null && buildTags.contains("test-keys");
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    public boolean isRunningOnVM() {
        try {
            return BOCommonUtils.isEmulator();
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    private static boolean hasSuperuserAPK() {
        try {
            File file = new File("/system/app/Superuser.apk");
            return file.exists();
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkRootPackages() {
        try {
            String[] places = {
                    "/system/app/Superuser.apk",
                    "/system/etc/init.d/99SuperSUDaemon",
                    "/dev/com.koushikdutta.superuser.daemon/",
                    "/system/xbin/daemonsu"};
            for (String where : places) {
                if (new File(where).exists()) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static boolean hasChainfiresupersu(Context context) {
        try {
            return isPackageInstalled("eu.chainfire.supersu", context);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    private static boolean hasSU() {
        try {
            return findBinary("su") || executeCommand(new String[]{"/system/xbin/which", "su"}) || executeCommand(new String[]{"which", "su"});
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return false;
    }


    /**************************************** Helper methods **************************************/
    public static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static boolean findBinary(String binaryName) {
        String[] places = {
                "/sbin/",
                "/system/bin/",
                "/system/xbin/",
                "/data/local/xbin/",
                "/data/local/bin/",
                "/system/sd/xbin/",
                "/system/bin/failsafe/",
                "/data/local/",
                "/system/xbin/busybox"};
        for (String where : places) {
            if (new File(where + binaryName).exists()) {
                return true;
            }
        }
        return false;
    }

    private static boolean executeCommand(String[] command) {
        Process localProcess = null;
        BufferedReader in = null;
        try {
            localProcess = Runtime.getRuntime().exec(command);
            in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
            return (in.readLine() != null);
        } catch (Exception e) {
            return false;
        } finally {
            if (localProcess != null) localProcess.destroy();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Logger.INSTANCE.e("RootChecker", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean checkAppSignature(@NonNull Context context) {

        try {

            PackageInfo packageInfo = context.getPackageManager()

                    .getPackageInfo(context.getPackageName(),

                            PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {

                byte[] signatureBytes = signature.toByteArray();

                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);

                Logger.INSTANCE.d("REMOVE ME",
                        "Include this string as a value for SIGNATURE:" + currentSignature);

                //compare signatures

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (SIGNATURE.equals(currentSignature)) {
                        return true;
                    }
                }


            }

        } catch (Exception e) {
            return false;
        }

        return false;

    }

    public boolean isVPN() {
        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                Logger.INSTANCE.d(TAG, "IFACE NAME: " + iface);
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }

        return false;
    }

    public boolean isProxied() {
        try {
            String link = "https://blotout.io/";
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            return conn.usingProxy();
        } catch (Exception e) {

            return false;
        }
    }
}
