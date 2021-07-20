package com.blotout.referrerapi;


import static com.android.installreferrer.api.InstallReferrerClient.newBuilder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.blotout.constants.BOCommonConstants;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class BOInstallReferrerHelper implements InstallReferrerStateListener {
    private static final String TAG = BOCommonConstants.TAG_PREFIX + "InstallReferrerHelper";

    private static final String BO_KEY_REFERRER = "referrer";

    //TODO: need to changes as per new referrer API
    private static final String BO_REFERRER_FIRST_LAUNCH = "BO_REFERRER_FIRST_LAUNCH";
    private static final String BO_REFERRER_DATE = "BO_REFERRER_DATE";
    private static final String BO_REFERRER_DATA = "BO_REFERRER_DATA";

    private static BOInstallReferrerHelper mInstance;
    private Context mContext;
    private InstallReferrerClient mReferrerClient;


    private BOInstallReferrerHelper(Context applicationContext) {
        mContext = applicationContext;
        mReferrerClient = newBuilder(mContext).build();
        mReferrerClient.startConnection(this);
    }

    public static BOInstallReferrerHelper getInstance(@NonNull Context ctx) {
        if (mInstance == null) {
            synchronized (BOInstallReferrerHelper.class) {
                if (mInstance == null) {
                    mInstance = new BOInstallReferrerHelper(ctx.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public static BOInstallReferrerHelper getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        return mInstance;
    }

    @Override
    public void onInstallReferrerSetupFinished(int responseCode) {
        try {
            switch (responseCode) {
                case InstallReferrerClient.InstallReferrerResponse.OK:
                    try {

                        ReferrerDetails response = mReferrerClient.getInstallReferrer();

                        handleReferrer(response);
                        mReferrerClient.endConnection();
                    } catch (RemoteException e) {
                        Logger.INSTANCE.e("", e.toString());
                    }
                    break;
                case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                    Logger.INSTANCE.e(TAG, "2. InstallReferrer not supported");
                    break;
                case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                    Logger.INSTANCE.e(TAG, "3. Unable to connect to the service");
                    break;
                default:
                    Logger.INSTANCE.e(TAG, "4. responseCode not found.");

            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    private void handleReferrer(@NonNull ReferrerDetails response) {
       try {
            String referrerUrl = response.getInstallReferrer();
            long appInstallTime = response.getInstallBeginTimestampSeconds();
            long referrerTimeClick = response.getReferrerClickTimestampSeconds();
            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
            Logger.INSTANCE.e(TAG, "referrerUrl  " + referrerUrl);
            Logger.INSTANCE.e(TAG, "appInstallTime  " + appInstallTime);
            Logger.INSTANCE.e(TAG, "referrerTimeClick " + referrerTimeClick);
            Logger.INSTANCE.e(TAG, "instantExperienceLaunched  " + instantExperienceLaunched);


            if(appInstallTime==0){
                setReferrerDate(new Date().getTime());
            }else{
                Date date = new Date();
                date.setTime(appInstallTime);
                setReferrerDate(date.getTime());
            }

            setReferrerData(referrerUrl);
       } catch (Exception e) {
           Logger.INSTANCE.e(TAG, e.toString());
       }
    }

    @Override
    public void onInstallReferrerServiceDisconnected() {
        Logger.INSTANCE.e(TAG, "5. onInstallReferrerServiceDisconnected");

    }


    public static void setFirstLaunch(@NonNull Context context) {
        try {
            BOSharedPreferenceImpl sp = BOSharedPreferenceImpl.getInstance();
            if (sp.getString(BO_REFERRER_FIRST_LAUNCH) == null) {
                sp.saveLong(BO_REFERRER_FIRST_LAUNCH,new Date().getTime());
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    public static String getFirstLaunch() {
        try {
            Date date = new Date(BOSharedPreferenceImpl.getInstance().getLong(BO_REFERRER_FIRST_LAUNCH, new Date().getTime()));
            return DateFormat.getDateInstance().format(date) + " - " + new SimpleDateFormat("HH:mm:ss.SSS").format(date);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return "Undefined";
        }
    }

    public static boolean isReferrerDetected() {
        return BOSharedPreferenceImpl.getInstance().getLong(BO_REFERRER_DATE) > 0;
    }

    public static void setReferrerDate(long date) {
       try {
            BOSharedPreferenceImpl sp = BOSharedPreferenceImpl.getInstance();
            if (sp.getLong(BO_REFERRER_DATE) <= 0) {
                sp.saveLong(BO_REFERRER_DATE,date);
            }
       } catch (Exception e) {
           Logger.INSTANCE.e(TAG, e.toString());
       }
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    public static String getReferrerDate() {
        try {
            BOSharedPreferenceImpl sp = BOSharedPreferenceImpl.getInstance();
            if (sp.getLong(BO_REFERRER_DATE) <= 0) {
                return "Undefined";
            }

            Date date = new Date(sp.getLong(BO_REFERRER_DATE, new Date().getTime()));
            return DateFormat.getDateInstance().format(date) + " - " + new SimpleDateFormat("HH:mm:ss.SSS").format(date);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
            return "Undefined";
        }
    }

    public static void setReferrerData(String data) {
       try {
            BOSharedPreferenceImpl sp = BOSharedPreferenceImpl.getInstance();
            if (sp.getString(BO_REFERRER_DATA) == null) {
                sp.saveString(BO_REFERRER_DATA, data);
            }
       } catch (Exception e) {
           Logger.INSTANCE.e(TAG, e.toString());
       }
    }

    @Nullable
    public static String getReferrerDataRaw() {
       try {
            BOSharedPreferenceImpl sp = BOSharedPreferenceImpl.getInstance();
            if (sp.getString(BO_REFERRER_DATA) == null) {
                return "Undefined";
            }

            return sp.getString(BO_REFERRER_DATA, null);
       } catch (Exception e) {
           Logger.INSTANCE.e(TAG, e.toString());
           return "Undefined";
       }
    }

    @Nullable
    public static String getReferrerDataDecoded() {

        try {
        BOSharedPreferenceImpl sp = BOSharedPreferenceImpl.getInstance();
        String raw = sp.getString(BO_REFERRER_DATA, null);

        if (raw == null) {
            return null;
        }
            String url = URLDecoder.decode(raw, "utf-8");
            try {
                String url2x = URLDecoder.decode(url, "utf-8");
                if (raw.equals(url2x)) {
                    return null;
                }
                return url2x;
            } catch (UnsupportedEncodingException uee) {
                // not URL 2x encoded but URL encoded
                if (raw.equals(url)) {
                    return null;
                }
                return url;
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }


    interface InstallReferrerStateListener {
    }

    /* prepare Install Referrar Data */
    public HashMap<String,Object> getInstallReferrarData() {
        HashMap<String,Object> eventsData = new HashMap<String,Object>();
        eventsData.put("referrarDate",BOInstallReferrerHelper.getReferrerDate());
        eventsData.put("referrerDataRaw",BOInstallReferrerHelper.getReferrerDataRaw());
        eventsData.put("referrerDataDecoded",BOInstallReferrerHelper.getReferrerDataDecoded());

//        boolean isReferrerDetected = BOInstallReferrerHelper.isReferrerDetected();
//        String firstLaunch = BOInstallReferrerHelper.getFirstLaunch();
//        String referrerDate = BOInstallReferrerHelper.getReferrerDate();
//        String referrerDataRaw = BOInstallReferrerHelper.getReferrerDataRaw();
//        String referrerDataDecoded = BOInstallReferrerHelper.getReferrerDataDecoded();
        return eventsData;
    }
}


