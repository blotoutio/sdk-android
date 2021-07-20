package com.blotout.analytics;

import android.os.Message;
import android.os.SystemClock;

import androidx.annotation.NonNull;

/**
 * Created by Blotout on 05,April,2020
 */
public abstract class BOHandlerMessage implements BOHandlerCallback {

    public BOHandlerMessage() {

    }

    @Override
    public abstract void handleMessage(BOMessage msg);

    public static class BOMessage {
        public Object obj;
        public int what;

        public BOMessage(int what, Object obj) {
            this.obj = obj;
            this.what = what;
        }

        public String toString() {
            return String.valueOf(SystemClock.uptimeMillis());
        }
    }
}
