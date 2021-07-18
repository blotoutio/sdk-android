package com.analytics.blotout

import android.webkit.URLUtil
import com.analytics.blotout.util.Constant
import com.analytics.blotout.util.Errors

class BlotoutAnalyticsConfiguration {


    var blotoutSDKKey:String? = null
    var endPointUrl:String?=null


    fun validateRequest():Int {
        if (blotoutSDKKey.isNullOrEmpty())
            return Errors.ERROR_KEY_NOT_PROPER
        else if (endPointUrl.isNullOrEmpty() || !URLUtil.isValidUrl(endPointUrl))
            return Errors.ERROR_URL_NOT_PROPER

        return Errors.ERROR_CODE_NO_ERROR
    }

    fun save() {
        var storageService= DependencyInjectorImpl.getInstance().getSecureStorageService()
        storageService.storeString(Constant.SDK_KEY,blotoutSDKKey!!)
        storageService.storeString(Constant.SDK_END_POINT_URL,endPointUrl!!)
    }
}