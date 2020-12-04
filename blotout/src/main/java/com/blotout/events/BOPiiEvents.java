package com.blotout.events;

import android.*;
import android.content.*;
import android.location.*;

import androidx.annotation.*;

import com.blotout.analytics.*;
import com.blotout.constants.BONetworkConstants;
import com.blotout.deviceinfo.device.*;
import com.blotout.deviceinfo.device.DeviceInfo;
import com.blotout.deviceinfo.location.*;
import com.blotout.model.session.*;
import com.blotout.utilities.*;

import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Created by Blotout on 03,November,2019
 */
public class BOPiiEvents extends LocationTracker {

    private static volatile BOPiiEvents instance;
    public boolean isEnabled;
    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public static BOPiiEvents getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOPiiEvents.class) {
                if (instance == null) {
                    instance = new BOPiiEvents(BOSharedManager.getInstance().getContext());
                }
            }
        }
        return instance;
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    private BOPiiEvents(@NotNull Context context) {
        super(context);
    }



    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void startCollectingUserLocationEvent(){
        if(this.isEnabled) {
            startListening();
        }
    }


    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void stopCollectingUserLocationEvent(){
        stopListening();
    }

    @Override
    public void onLocationFound(@NonNull  Location location) {
        if (BOAEvents.isSessionModelInitialised) {
            String userActivity;

            if (location.getSpeed() > 10.0) {
                userActivity = "driving";
            } else if ((location.getSpeed() > 2.0) && (location.getSpeed() < 10.0)) {
                userActivity = "running";
            } else if ((location.getSpeed() > 0.1) && (location.getSpeed() < 2.0)) {
                userActivity = "walking";
            }
            userActivity = "static";

            com.blotout.deviceinfo.location.DeviceLocation addressInfo = getAddressFromLocation(location.getLatitude(), location.getLongitude());

            BONonPIILocation boNonPIILocation = new BONonPIILocation();
            boNonPIILocation.setCity(addressInfo.getCity());
            boNonPIILocation.setCountry(addressInfo.getCountryCode());
            boNonPIILocation.setState(addressInfo.getState());
            boNonPIILocation.setZip(addressInfo.getPostalCode());
            boNonPIILocation.setActivity(userActivity);
            boNonPIILocation.setSource(location.getProvider());
            boNonPIILocation.setSentToServer(false);
            boNonPIILocation.setMid(BOCommonUtils.getMessageIDForEvent("NonPIILocation"));
            boNonPIILocation.setSessionId(BOSharedManager.getInstance().sessionId);


            BOPiiLocation boPIILocation = new BOPiiLocation();

            boPIILocation.setLatitude(location.getLatitude());
            boPIILocation.setLongitude(location.getLongitude());
            boPIILocation.setMid(BOCommonUtils.getMessageIDForEvent("PIILocation"));
            boPIILocation.setSentToServer(false);
            boPIILocation.setSessionId(BOSharedManager.getInstance().sessionId);

            BOLocation boLocation = new BOLocation();
            boLocation.setNonPIILocation(boNonPIILocation);
            boLocation.setPiiLocation(boPIILocation);
            boLocation.setSentToServer(false);
            boLocation.setMid(BOCommonUtils.getMessageIDForEvent("PiiNonPiiLocation"));
            boLocation.setTimeStamp(BODateTimeUtils.get13DigitNumberObjTimeStamp());
            boLocation.setSessionId(BOSharedManager.getInstance().sessionId);

            List<BOLocation> existingLocations = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions().getLocation();
            existingLocations.add(boLocation);
            BOSingleDaySessions existingSingleDaySessions = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).getSingleDaySessions();
            existingSingleDaySessions.setLocation(existingLocations);
            BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null).setSingleDaySessions(existingSingleDaySessions);

        }
    }

    @Override
    public void onTimeout() {

    }
}
