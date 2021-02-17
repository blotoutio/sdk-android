package com.blotout.eventsExecutor;

import com.blotout.utilities.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Blotout on 06,April,2020
 */

public class BOInitializationExecutorHelper {

    private static final String TAG = "BODeviceOperationExecutorHelper";
    private static volatile BOInitializationExecutorHelper instance;
    private ThreadPoolExecutor networkExecutor;
    private ScheduledThreadPoolExecutor scheduledExecutor;
    private final Map<String, Runnable> runnableMap = new HashMap<>();

    private class ExecutorRunnableHolder implements Runnable {
        private Runnable runnableClient;

        private ExecutorRunnableHolder(Runnable runnableClient) {
            this.runnableClient = runnableClient;
        }

        @Override
        public void run() {
            this.runnableClient.run();
        }
    }

    public static BOInitializationExecutorHelper getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOInitializationExecutorHelper.class) {
                if (instance == null) {
                    instance = new BOInitializationExecutorHelper();
                }
            }
        }
        return instance;
    }

    private BOInitializationExecutorHelper() {
        networkExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        scheduledExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(3);
    }

    private void shutDown() {
        networkExecutor.shutdown();
    }

    public void post(Runnable runnable) {
        Logger.INSTANCE.e(TAG, "");
        networkExecutor.execute(runnable);
    }

    public void postDelayedWithKey(String runnableKey, Runnable runnable, long delayTime) {
        BOInitializationExecutorHelper.ExecutorRunnableHolder oldRunnable = (BOInitializationExecutorHelper.ExecutorRunnableHolder) runnableMap.get(runnableKey);
        if (oldRunnable != null) {
            scheduledExecutor.remove(oldRunnable);
        }

        BOInitializationExecutorHelper.ExecutorRunnableHolder newRunnable = new BOInitializationExecutorHelper.ExecutorRunnableHolder(runnable);
        this.runnableMap.put(runnableKey, newRunnable);

        scheduledExecutor.schedule(newRunnable, delayTime, TimeUnit.MILLISECONDS);
    }

//    public void removeCallback(Runnable runnable) {
//        workerHandler.removeCallbacks(runnable);
//    }

    public void removeCallbackForKey(String key) {
        BOInitializationExecutorHelper.ExecutorRunnableHolder oldRunnable = (BOInitializationExecutorHelper.ExecutorRunnableHolder) runnableMap.get(key);
        if (oldRunnable != null) {
            scheduledExecutor.remove(oldRunnable);
        }
    }

}
