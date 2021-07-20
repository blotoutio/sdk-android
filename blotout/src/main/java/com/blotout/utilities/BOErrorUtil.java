package com.blotout.utilities;


import com.blotout.analytics.BlotoutAnalytics;
import com.blotout.constants.BOCommonConstants;

/**
 * Created by Blotout on 31,May,2020
 */
public class BOErrorUtil {

    private static final String TAG = BOCommonConstants.TAG_PREFIX + "BOErrorUtil";
    private static volatile BOErrorUtil instance;

    public static BOErrorUtil getInstance() {

        if (instance == null) { //if there is no instance available... create new one
            synchronized (BOErrorUtil.class) {
                if (instance == null) {
                    instance = new BOErrorUtil();
                }
            }
        }
        return instance;
    }

}
