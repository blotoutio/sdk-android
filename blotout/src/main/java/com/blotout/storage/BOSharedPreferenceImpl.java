package com.blotout.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class BOSharedPreferenceImpl implements IBOPreference {
    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOSharedPreferenceImpl";


    private static final String PREFERENCE_NAME = "bo_preference";
    private static final String PREFERENCE_NAME_STAGE = "bo_preference_stage";

    private static BOSharedPreferenceImpl mPrefHelper;
    private SharedPreferences mSharedPref;

    @Nullable
    private SharedPreferences.Editor mEditor;
    private boolean mBulkDataUpdate = false;

    private BOSharedPreferenceImpl(@NonNull Context context) {
        if(BlotoutAnalytics_Internal.getInstance().isProductionMode) {
            mSharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        } else {
            mSharedPref = context.getSharedPreferences(PREFERENCE_NAME_STAGE, Context.MODE_PRIVATE);
        }
    }

    public static BOSharedPreferenceImpl getInstance(@NonNull Context context) {
        if (mPrefHelper == null) {
            mPrefHelper = new BOSharedPreferenceImpl(context.getApplicationContext());
        }
        return mPrefHelper;
    }

    public static BOSharedPreferenceImpl getInstance() {
        if (mPrefHelper != null) {
            return mPrefHelper;
        }
        Logger.INSTANCE.e(TAG,"Please use getInstance(context) for atleast once.");
        return mPrefHelper;
    }


    @Override
    public void saveString(@Nullable String key, @Nullable String value) {
        if (key != null && value != null) {
            if (!key.trim().isEmpty() && !value.trim().isEmpty()) {
//                if(BlotoutAnalytics_Internal.getInstance().isDataEncryptionEnabled) {
//                    value = BlotoutAnalytics_Internal.getInstance().getEncryptionManager().encrypt(value);
//                }
                doEdit();
                mEditor.putString(key, value);
                doCommit();
            } else {
                logMsg();
            }
        } else {
            logMsg();
        }
    }


    @Override
    public void saveInteger(@Nullable String key, int value) {
        if (key != null) {
            if (!key.trim().isEmpty()) {
                doEdit();
                mEditor.putInt(key, value);
                doCommit();
            } else {
                logMsg();
            }
        } else {
            logMsg();
        }
    }


    @Override
    public void saveBoolean(@Nullable String key, boolean value) {
        if (key != null) {
            if (!key.trim().isEmpty()) {
                doEdit();
                mEditor.putBoolean(key, value);
                doCommit();
            } else {
                logMsg();
            }
        } else {
            logMsg();
        }
    }


    @Override
    public void saveFloat(@Nullable String key, float value) {
        if (key != null) {
            if (!key.trim().isEmpty()) {
                doEdit();
                mEditor.putFloat(key, value);
                doCommit();
            } else {
                logMsg();
            }
        } else {
            logMsg();
        }
    }


    @Override
    public void saveDouble(@Nullable String key, double value) {
        if (key != null) {
            if (!key.trim().isEmpty()) {
                doEdit();
                mEditor.putString(key, String.valueOf(value));
                doCommit();
            } else {
                logMsg();
            }
        } else {
            logMsg();
        }
    }


    @Override
    public void saveLong(@Nullable String key, long value) {
        if (key != null) {
            if (!key.trim().isEmpty()) {
                doEdit();
                mEditor.putLong(key, value);
                doCommit();
            } else {
                logMsg();
            }
        } else {
            logMsg();
        }
    }

    @Nullable
    @Override
    public String getString(String key) {
        return mSharedPref.getString(key, null);
    }


    @Override
    public void removeKey(@Nullable String key) {

        if (key != null) {
            if (!key.trim().isEmpty()) {
                doEdit();
                mEditor.remove(key);
                doCommit();
            } else {
                logMsg();
            }
        } else {
            logMsg();
        }
    }

    @Nullable
    @Override
    public String getString(String key, String value) {
//        if(BlotoutAnalytics_Internal.getInstance().isDataEncryptionEnabled) {
//            String str = mSharedPref.getString(key, value);
//            return BlotoutAnalytics_Internal.getInstance().getEncryptionManager().decryptString(str);
//        } else {
            return mSharedPref.getString(key, value);
        //}
    }


    @Override
    public int getInt(String key) {
        return mSharedPref.getInt(key, 0);
    }


    @Override
    public int getInt(String key, int value) {
        return mSharedPref.getInt(key, value);
    }


    @Override
    public boolean getBoolean(String key) {
        return mSharedPref.getBoolean(key, false);
    }


    @Override
    public boolean getBoolean(String key, boolean value) {
        return mSharedPref.getBoolean(key, value);
    }


    @Override
    public float getFloat(String key) {
        return mSharedPref.getFloat(key, 0);
    }


    @Override
    public float getFloat(String key, float value) {
        return mSharedPref.getFloat(key, value);
    }


    @Override
    public double getDouble(String key) {
        return getDouble(key, 0);
    }


    @Override
    public double getDouble(String key, double value) {
        try {
            return Double.valueOf(mSharedPref.getString(key, String.valueOf(value)));
        } catch (Exception e) {
            return value;
        }
    }


    @Override
    public long getLong(String key) {
        return mSharedPref.getLong(key, 0);
    }


    @Override
    public long getLong(String key, long value) {
        return mSharedPref.getLong(key, value);
    }


    @Override
    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void edit() {
        mBulkDataUpdate = true;
        mEditor = mSharedPref.edit();
    }

    @Override
    public void commit() {
        mBulkDataUpdate = false;
        mEditor.commit();
        mEditor = null;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void doEdit() {
        if (!mBulkDataUpdate && mEditor == null) {
            mEditor = mSharedPref.edit();
        }
    }

    @Override
    public void doCommit() {
        if (!mBulkDataUpdate && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }


    @Override
    public <T> void saveListInPref(@Nullable String key, List<T> list) {
        try {

            if (key != null) {
                if (!key.trim().isEmpty()) {
                    doEdit();
                    Gson gson = new Gson();
                    String json = gson.toJson(list);
                    //save list against a key
                    saveList(key, json);
                } else {
                    logMsg();
                }
            } else {
                logMsg();
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());

        }

    }

    private void saveList(String key, String value) {
        doEdit();
        mEditor.putString(key, value);
        Logger.INSTANCE.d(TAG, "Saved list of custom object " + value);
        doCommit();
    }

    @Nullable
    @Override
    public <T> List<T> getSavedListFromPref(String key) {
        List<T> list = null;
        try {
            String serializedObject = mSharedPref.getString(key, null);
            Logger.INSTANCE.d(TAG, "Get the list of custom objects " + serializedObject);
            if (serializedObject != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<T>>() {
                }.getType();
                list = gson.fromJson(serializedObject, type);

            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return list;
    }

    private void logMsg() {
        Logger.INSTANCE.e(TAG,
                "Key, Value should not be empty or null.");
    }

    public boolean isNewUserRecorded() {
        boolean isNewUserRecorded;
        if(BlotoutAnalytics_Internal.getInstance().isProductionMode) {
            isNewUserRecorded = BOSharedPreferenceImpl.getInstance().getBoolean(BOCommonConstants.BO_ANALYTICS_ROOT_NEW_USER_DEFAULTS_KEY);
        } else {
            isNewUserRecorded = BOSharedPreferenceImpl.getInstance().getBoolean(BOCommonConstants.BO_ANALYTICS_ROOT_NEW_USER_DEFAULTS_KEY_STAGE);
        }
        return isNewUserRecorded;
    }

    public void saveNewUserRecorded() {
        if(BlotoutAnalytics_Internal.getInstance().isProductionMode) {
            BOSharedPreferenceImpl.getInstance().saveBoolean(BOCommonConstants.BO_ANALYTICS_ROOT_NEW_USER_DEFAULTS_KEY, true);
        } else {
            BOSharedPreferenceImpl.getInstance().saveBoolean(BOCommonConstants.BO_ANALYTICS_ROOT_NEW_USER_DEFAULTS_KEY_STAGE, true);
        }
    }

    public boolean isSDKFirstLaunched(){
        boolean isSDKFirstLaunch;
        if(BlotoutAnalytics_Internal.getInstance().isProductionMode) {
            isSDKFirstLaunch = BOSharedPreferenceImpl.getInstance().getBoolean(BOCommonConstants.BO_ANALYTICS_ROOT_SDK_LAUNCHED_DEFAULTS_KEY);
        } else {
            isSDKFirstLaunch = BOSharedPreferenceImpl.getInstance().getBoolean(BOCommonConstants.BO_ANALYTICS_ROOT_SDK_LAUNCHED_DEFAULTS_KEY_STAGE);
        }
        return isSDKFirstLaunch ;

    }

    public void saveSDKFirstLaunched(){
        if(BlotoutAnalytics_Internal.getInstance().isProductionMode) {
            BOSharedPreferenceImpl.getInstance().saveBoolean(BOCommonConstants.BO_ANALYTICS_ROOT_SDK_LAUNCHED_DEFAULTS_KEY, true);
        } else {
            BOSharedPreferenceImpl.getInstance().saveBoolean(BOCommonConstants.BO_ANALYTICS_ROOT_SDK_LAUNCHED_DEFAULTS_KEY_STAGE, true);
        }
    }

    public long getUserBirthTimeStamp(){
        long userCreatedTimestamp;
        if(BlotoutAnalytics_Internal.getInstance().isProductionMode) {
            userCreatedTimestamp = BOSharedPreferenceImpl.getInstance().getLong(BOCommonConstants.BO_ANALYTICS_USER_BIRTH_TIME_STAMP_KEY);
        } else {
            userCreatedTimestamp = BOSharedPreferenceImpl.getInstance().getLong(BOCommonConstants.BO_ANALYTICS_USER_BIRTH_TIME_STAMP_KEY_STAGE);
        }
        if(userCreatedTimestamp == 0) {
            userCreatedTimestamp = BODateTimeUtils.get13DigitNumberObjTimeStamp();
            setUserBirthTimeStamp(userCreatedTimestamp);
        }
        return userCreatedTimestamp;
    }

    public void setUserBirthTimeStamp(long timeStamp){
        if(BlotoutAnalytics_Internal.getInstance().isProductionMode) {
            BOSharedPreferenceImpl.getInstance().saveLong(BOCommonConstants.BO_ANALYTICS_USER_BIRTH_TIME_STAMP_KEY, timeStamp);
        } else {
            BOSharedPreferenceImpl.getInstance().saveLong(BOCommonConstants.BO_ANALYTICS_USER_BIRTH_TIME_STAMP_KEY_STAGE, timeStamp);
        }
    }

}

