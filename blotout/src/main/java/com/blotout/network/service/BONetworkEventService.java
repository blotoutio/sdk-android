package com.blotout.network.service;

import com.blotout.analytics.BOSdkToServerFormat;
import com.blotout.analytics.BOSharedManager;
import com.blotout.constants.BOCommonConstants;
import com.blotout.constants.BONetworkConstants;
import com.blotout.eventsExecutor.BOInitializationExecutorHelper;
import com.blotout.network.api.BOBaseAPI;
import com.blotout.network.api.BOEventPostAPI;
import com.blotout.utilities.Logger;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by ankuradhikari on 14,January,2021
 */
public class BONetworkEventService {

    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BONetworkService";

    public static void sendSdkStartEvent() {

        BOInitializationExecutorHelper.getInstance().post(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<Object> call = null;
                    HashMap<String, Object> allEventsData = BOSdkToServerFormat.getInstance().createEventObject(BONetworkConstants.BO_SDK_START,
                            BONetworkConstants.BO_EVENT_SYSTEM_KEY, BONetworkConstants.BO_EVENT_SDK_START);

                    BOEventPostAPI eventPostAPI = BOSharedManager.getInstance().getAPIFactory().eventPostAPI;
                    call = eventPostAPI.postJson(BOBaseAPI.getInstance().getEventPost(), allEventsData);

                    Response resp = call.execute();
                    if (resp.isSuccessful()) {
                        Logger.INSTANCE.i(TAG,"sdk_start event sent");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
