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

public class BONetworkSegmentExecutorHelper {

    private static final String TAG = "BONetworkSegmentExecutorHelper";
    private static volatile BONetworkSegmentExecutorHelper instance;
    private ThreadPoolExecutor networkExecutor;
    private ScheduledThreadPoolExecutor scheduledExecutor;
    private final Map<String,Runnable> runnableMap = new HashMap<>();


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

    public static BONetworkSegmentExecutorHelper getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BONetworkSegmentExecutorHelper.class) {
                if (instance == null) {
                    instance = new BONetworkSegmentExecutorHelper();
                }
            }
        }
        return instance;
    }

    private BONetworkSegmentExecutorHelper() {
        networkExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        scheduledExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);
    }

    private void shutDown() {
        networkExecutor.shutdown();
    }

    public void post(Runnable runnable) {
        Logger.INSTANCE.e(TAG,"");
        networkExecutor.execute(runnable);
    }

//    public void postDelayed(Runnable runnable, long delayTime) {
//        workerHandler.removeCallbacks(runnable);
//        workerHandler.postDelayed(runnable,delayTime);
//    }

    public void postDelayedWithKey(String runnableKey, Runnable runnable, long delayTime) {
        BONetworkSegmentExecutorHelper.ExecutorRunnableHolder oldRunnable = (BONetworkSegmentExecutorHelper.ExecutorRunnableHolder) runnableMap.get(runnableKey);
        if(oldRunnable != null) {
            scheduledExecutor.remove(oldRunnable);
        }

        BONetworkSegmentExecutorHelper.ExecutorRunnableHolder newRunnable = new BONetworkSegmentExecutorHelper.ExecutorRunnableHolder(runnable);
        this.runnableMap.put(runnableKey,newRunnable);

        scheduledExecutor.schedule(newRunnable,delayTime, TimeUnit.MILLISECONDS);
    }

//    public void removeCallback(Runnable runnable) {
//        workerHandler.removeCallbacks(runnable);
//    }

    public void removeCallbackForKey(String key) {
        BONetworkSegmentExecutorHelper.ExecutorRunnableHolder oldRunnable = (BONetworkSegmentExecutorHelper.ExecutorRunnableHolder) runnableMap.get(key);
        if(oldRunnable != null) {
            scheduledExecutor.remove(oldRunnable);
        }
    }
}
