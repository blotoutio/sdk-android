package com.blotout.analytics;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.blotout.constants.BOCommonConstants;
import com.blotout.model.session.BOAppNavigation;
import com.blotout.network.BOAPIFactory;
import com.blotout.network.api.BOBaseAPI;
import com.blotout.referrerapi.BOInstallReferrerHelper;
import com.blotout.storage.BOFileSystemManager;
import com.blotout.storage.BOSharedPreferenceImpl;
import com.blotout.utilities.BOCommonUtils;
import com.blotout.utilities.BODateTimeUtils;
import com.blotout.utilities.BOEncryptionManager;
import com.blotout.utilities.Logger;

import java.util.Timer;

/**
 * Created by Blotout on 10,November,2019
 */
public class BOSharedManager {

    private static BOSharedManager instance;
    private Context context;
    private JobManager jobManager;
   private Application application;
   private BOActivityOrientationListener orientationListener;
   private BOAPIFactory apiFactory;
   public Timer currentTimer;
   public long  currentTime;
   public BOAppNavigation currentNavigation;
   private BOEncryptionManager encryptionManager;
   public  String sessionId;
    public static BOSharedManager getInstance() {

        if (instance != null) {
            return instance;
        }
        Logger.INSTANCE.e("BOSharedManager","Please use getInstance(context) for atleast once.");
        return instance;
    }

    public static BOSharedManager getInstance(@NonNull Context context) {

        if (instance == null) { //if there is no instance available... create new one
            instance = new BOSharedManager(context);
        }
        return instance;
    }

    private BOSharedManager(@NonNull Context context) {
        this.context = context;
        sessionId = "" + BODateTimeUtils.get13DigitNumberObjTimeStamp();
        BOSharedPreferenceImpl.getInstance(context);
        BOFileSystemManager.getInstance(context);
        BOFileSystemManager.getInstance().createRootDirectory();
        initJobManager();
        initActivityLifeCycleCallbacks();
        initInstallReferrerHelper();
        //Create root folder
        apiFactory = new BOAPIFactory(BOCommonConstants.SDK_VERSION, BOBaseAPI.getInstance().getBaseAPI());
        initOrientationListener();
    }

    public void initEncryptionManager() {
        this.encryptionManager = new BOEncryptionManager(BOEncryptionManager.ALGORITHM_AES_CBC_PKCS5Padding, BOCommonUtils.getPasspharseKey(),BOEncryptionManager.MODE_256BIT);
    }

    public BOEncryptionManager getEncryptionManager() {
        return this.encryptionManager;
    }

    private void startClosingService() {
        this.context.startService(new Intent(this.context, BOClosingService.class));
    }

    private void initOrientationListener() {
        orientationListener = new BOActivityOrientationListener(context);
        if (orientationListener.canDetectOrientation()) {
            orientationListener.enable();
        }
    }

    private void initActivityLifeCycleCallbacks() {
        application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(BOAnalyticsActivityLifecycleCallbacks.getInstance());
        application.registerComponentCallbacks(BOAnalyticsActivityLifecycleCallbacks.getInstance());
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public BOAPIFactory getAPIFactory() {
        return apiFactory;
    }

    public Context getContext() {
        return context;
    }


    private void initInstallReferrerHelper() {
        //Set first launch
        if (TextUtils.isEmpty(BOInstallReferrerHelper.getFirstLaunch())) {
            BOInstallReferrerHelper.setFirstLaunch(context);
        }
        //Initialize install referrer for getting install referrer callback
        BOInstallReferrerHelper.getInstance(context);
    }

    private void initJobManager() {
        Configuration configuration = new Configuration.Builder(this.context)
                .id("bo_basic_jobs")
                .minConsumerCount(1)
                .maxConsumerCount(3)
                .loadFactor(3)
                .consumerKeepAlive(120)
                .build();
        jobManager = new JobManager (configuration);
    }

}
