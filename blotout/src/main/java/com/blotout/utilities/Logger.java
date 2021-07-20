package com.blotout.utilities;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BOCommonConstants;
import com.blotout.io.BuildConfig;

public enum Logger {

    INSTANCE;

    private static final String LOG_TAG = "BOLogger";

    Logger() {
    }

    public enum Level {
        VERBOSE, DEBUG, ERROR, RELEASE, DISABLED
    }

    @NonNull
    private Level level = Level.ERROR;

    public void setLogStatus(Context context, boolean enabled) {
        if (enabled) {
            level = Level.RELEASE;
        } else {
            level = Level.DISABLED;
        }
    }

    public void enableVerboseLog(boolean enable) {
        if (enable) {
            level = Level.VERBOSE;
        } else {
            level = Level.DISABLED;
        }
    }

    public void v(String tag, String info) {
        if (BuildConfig.DEBUG) {
            if (!isNull(tag, info)) {
                return;
            }
            Log.v(tag, info);
        }
    }

    public void d(String tag, String info) {
        if (BuildConfig.DEBUG) {
            if (!isNull(tag, info)) {
                return;
            }
            Log.d(tag, info);
        }

    }

    public void i(String tag, String info) {
        if (BuildConfig.DEBUG || BlotoutAnalytics_Internal.getInstance().isSDKLogEnabled) {
            if (!isNull(tag, info)) {
                return;
            }
            Log.i(tag, info);
        }
    }

    public void w(String tag, String info) {
        if (BuildConfig.DEBUG) {
            if (!isNull(tag, info)) {
                return;
            }
            Log.w(tag, info);
        }
    }

    public void e(String tag, String info) {
        if (BuildConfig.DEBUG) {
            if (!isNull(tag, info)) {
                return;
            }
            Log.e(tag, info);
        }

    }

    public void error(String tag, String info, Object thrown) {
        if (!isNull(tag, info)) {
            return;
        }
        Log.e(tag, info + stackTrace(((Throwable)thrown).getStackTrace(),3).toString());
    }

    private boolean isNull(@Nullable String tag, @Nullable String info) {
        boolean check;
        check = tag != null && info != null;
        return check;
    }

    public void methodLog(@Nullable String tag) {
        if (level != Level.VERBOSE || tag == null) {
            return;
        }

        try {
            if (!checkTag(tag)) {
                String[] message = stackTrace(Thread.currentThread().getStackTrace(), 3);
                Logger.INSTANCE.e(tag, message[0]);
            } else {
                Logger.INSTANCE.e(LOG_TAG, BOCommonConstants.EMPTY_STRING);
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(LOG_TAG, e.toString());
        }
    }

    private boolean checkTag(@NonNull String tag) {
        boolean check = false;
        check = tag.trim().isEmpty();
        return check;
    }

    @NonNull
    private String[] stackTrace(@Nullable StackTraceElement[] stackTrace, final int i) {
        if (stackTrace != null && stackTrace.length >= i) {
            final StackTraceElement ste = stackTrace[i];
            if (ste != null) {
                return new String[]{
                        " File Name: " + stackTrace[i].getFileName()
                                + " Class Name: " + stackTrace[i].getClassName()
                                + " Method: " + stackTrace[i].getMethodName() + "()"
                                + " Line Number: " + stackTrace[i].getLineNumber()
                };
            }
        }
        return new String[]{""};
    }

}
