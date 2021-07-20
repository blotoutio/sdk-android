package com.blotout.analytics;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.blotout.events.BOAppSessionEvents;

public class BOClosingService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        // Handle application closing
        BOAppSessionEvents.getInstance().applicationWillTerminateNotification();
        // Destroy the service
        stopSelf();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
