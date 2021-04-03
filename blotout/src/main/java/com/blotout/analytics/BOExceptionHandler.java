package com.blotout.analytics;

import androidx.annotation.NonNull;

import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.events.BOAEvents;
import com.blotout.events.BOAppSessionEvents;
import com.blotout.model.session.BOAppSessionDataModel;
import com.blotout.model.session.BOCrashDetail;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Blotout on 14,June,2020
 */
public class BOExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "BOExceptionHandler";

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        handleUncaughtException(t, e);
    }

    private static void handleUncaughtException(Thread thread, @NonNull Throwable e) {
        Logger.INSTANCE.e(TAG, e.getMessage());
        try {
            if (BOAEvents.isSessionModelInitialised) {
                HashMap<String, Object> crashDetail = new HashMap<>();
                crashDetail.put(BOCommonConstants.BO_SENT_TO_SERVER, false);
                crashDetail.put(BONetworkConstants.BO_MESSAGE_ID, BOCommonUtils.getMessageIDForEvent("AppCrashed"));
                crashDetail.put(BOCommonConstants.BO_TIME_STAMP, BODateTimeUtils.get13DigitNumberObjTimeStamp());
                crashDetail.put(BOCommonConstants.BO_NAME, getExceptionName(e));
                crashDetail.put("reason", e.getCause().toString());
                crashDetail.put("info", null);
                crashDetail.put(BONetworkConstants.BO_SESSION_ID,BOSharedManager.getInstance().sessionId);

                if (e.getStackTrace() != null) {
                    List<String> stackTrace = new ArrayList<>();

                    for (StackTraceElement element : e.getStackTrace()) {
                        stackTrace.add(element.getMethodName());
                        stackTrace.add(String.valueOf(element.getLineNumber()));
                        stackTrace.add(element.getFileName());
                        stackTrace.add(element.getClassName());
                    }
                    crashDetail.put("callStackSymbols", stackTrace);
                }
                crashDetail.put("callStackReturnAddress", null);

                BOCrashDetail crashDetails = BOCrashDetail.fromJsonDictionary(crashDetail);
                BOAppSessionDataModel dataModel = BOAppSessionDataModel.sharedInstanceFromJSONDictionary(null);
                List<BOCrashDetail> existingData = dataModel.getSingleDaySessions().getCrashDetails();
                if(crashDetail != null) {
                    existingData.add(crashDetails);
                }
                dataModel.getSingleDaySessions().setCrashDetails(existingData);

                BOAppSessionEvents appSessionEventL = BOAppSessionEvents.getInstance();
                appSessionEventL.appTerminationFunctionalityOnDayChange();
            }
        } catch (Exception exception) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
    }

    private String getExceptionStackString(StackTraceElement[] stack, String separator)
    {
        String stackString = "";

        if (stack == null)
            return null;

        for (StackTraceElement element: stack)
        {
            if (!stackString.isEmpty())
                stackString += separator;

            int lineNumber = (element.getClassName().contains("android.app.") || element.getClassName().contains("java.lang.")) ? -1 : element.getLineNumber();

            String stackItem = element.getClassName() + "."+
                    element.getMethodName()+"("+element.getFileName();

            if (lineNumber < 0)
                stackItem += ")";
            else
                stackItem += ":"+element.getLineNumber()+")";

            stackString += stackItem;

        }

        return stackString;
    }

    private static String getExceptionName(Throwable ex)
    {
        return ex.getClass().getSimpleName();
    }

    private static String getExceptionMessage(Throwable ex)
    {
        return ex == null ? null : ex.getMessage();
    }
}
