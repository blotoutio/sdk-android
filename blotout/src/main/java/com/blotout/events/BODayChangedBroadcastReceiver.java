package com.blotout.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Blotout on 14,November,2019
 */

abstract class BODayChangedBroadcastReceiver extends BroadcastReceiver {

    public static Boolean isRegistered = false;

    @NonNull
    private Date date = new Date();
    @NonNull
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @NonNull
    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        isRegistered = true;
        return intentFilter;
    }

    public boolean isReceiverRegistered() {
        return isRegistered;
    }
    private boolean isSameDay(Date currentDate) {
        return dateFormat.format(currentDate).equals(dateFormat.format(date));
    }
       @Override
       public void onReceive(Context context, @NonNull Intent intent) {
           final String action = intent.getAction();

           if (action.equals(Intent.ACTION_TIME_CHANGED) ||
                   action.equals(Intent.ACTION_TIMEZONE_CHANGED) || action.equals(Intent.ACTION_DATE_CHANGED)) {
               onDayChanged();
           }
       }

        public abstract void onDayChanged();
}
