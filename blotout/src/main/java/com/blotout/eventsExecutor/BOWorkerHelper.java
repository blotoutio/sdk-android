package com.blotout.eventsExecutor;

import android.os.Handler;
import android.os.HandlerThread;
import com.blotout.utilities.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Blotout on 05,April,2020
 */
public final class BOWorkerHelper {

    private static final String TAG = "BOWorkerHelper";
    private Handler workerHandler;
    private static volatile BOWorkerHelper instance;
    private final Map<String,Runnable> runnableMap = new HashMap<>();

    private class RunnableHolder implements Runnable {
        private Runnable runnableClient;
        private RunnableHolder(Runnable runnableClient) {
            this.runnableClient = runnableClient;
        }
        @Override
        public void run() {
            this.runnableClient.run();
        }
    }

    private BOWorkerHelper() {
        HandlerThread workerThread = new HandlerThread("BOWorkerHelper-Thread");
        workerThread.start();
        workerHandler = new Handler(workerThread.getLooper());
    }

    public static BOWorkerHelper getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOWorkerHelper.class) {
                if (instance == null) {
                    instance = new BOWorkerHelper();
                }
            }
        }
        return instance;
    }

    public void post(Runnable runnable) {
        Logger.INSTANCE.e(TAG,"");
        workerHandler.post(runnable);
    }

//    public void postDelayed(Runnable runnable, long delayTime) {
//        workerHandler.removeCallbacks(runnable);
//        workerHandler.postDelayed(runnable,delayTime);
//    }

    public void postDelayedWithKey(String runnableKey, Runnable runnable, long delayTime) {
        RunnableHolder oldRunnable = (RunnableHolder) runnableMap.get(runnableKey);
        if(oldRunnable != null) {
            workerHandler.removeCallbacks(oldRunnable);
        }

        RunnableHolder newRunnable = new RunnableHolder(runnable);
        this.runnableMap.put(runnableKey,newRunnable);

        workerHandler.postDelayed(newRunnable,delayTime);
    }

//    public void removeCallback(Runnable runnable) {
//        workerHandler.removeCallbacks(runnable);
//    }

    public void removeCallbackForKey(String key) {
        RunnableHolder oldRunnable = (RunnableHolder) runnableMap.get(key);
        if(oldRunnable != null) {
            workerHandler.removeCallbacks(oldRunnable);
        }
    }

}
