package com.blotout.network.jobs;

import androidx.annotation.NonNull;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.blotout.constants.BOCommonConstants;
import com.blotout.utilities.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BOBaseJob extends Job {

    public BOBaseJob(@NonNull Params params) {
        super(params);
    }

    private static final String TAG = BOCommonConstants.TAG_PREFIX +"BOBaseJob";

    @Override
    public final void onAdded(){
        Logger.INSTANCE.d(TAG,"onAdded");
        onJobAdded();
    }
    public void onJobAdded(){}

    @Override
    public final void onRun() throws Throwable {
        Logger.INSTANCE.d(TAG,"onRun");
        try {
            onJobRun();
        }catch (Error error){
            //handleRetrofitError(error);
        }
    }

    public abstract void onJobRun() throws Throwable;

    private final void onCancel() {
        Logger.INSTANCE.d(TAG, "OnCancel");
        onJobCancel();
    }

    public void onJobCancel() {

    }

    private final boolean shouldReRunOnThrowable(Throwable throwable) {
        Logger.INSTANCE.d(TAG,"shouldReRunOnThrowable");
        return shouldReRunJobOnThrowable(throwable);
    }

    public boolean shouldReRunJobOnThrowable(Throwable throwable) { return false;}
}
