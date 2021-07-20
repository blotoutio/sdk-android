package com.blotout.deviceinfo.location;

import android.content.*;
import android.location.*;

import androidx.annotation.*;

import com.blotout.io.*;

/**
 * Created by Blotout on 2020-01-08.
 *
 * ernitinjai@gmail.com
 */
public class LocationUtils {

    /**
     * Check if the gps provider is enabled or not
     *
     * @param context any context
     * @return true if gps provider is enabled, false otherwise
     */
    public static boolean isGpsProviderEnabled(@NonNull Context context) {
        return isProviderEnabled(context, LocationManager.GPS_PROVIDER);

    }

    /**
     * Check if the network provider is enabled or not
     *
     * @param context any context
     * @return true if the network provider is enabled, false otherwise
     */
    public static boolean isNetworkProviderEnabled(@NonNull Context context) {
        return isProviderEnabled(context, LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Check if the passive provider is enabled or not
     *
     * @param context any context
     * @return true if the passive provider is enabled, false otherwise
     */
    public static boolean isPassiveProviderEnabled(@NonNull Context context) {
        return isProviderEnabled(context, LocationManager.PASSIVE_PROVIDER);
    }



    /**
     * Check if the provider is enabled of not
     *
     * @param context  any context
     * @param provider the provider to check
     * @return true if the provider is enabled, false otherwise
     */
    private static boolean isProviderEnabled(@NonNull Context context, @NonNull String provider) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(provider);
    }

}
