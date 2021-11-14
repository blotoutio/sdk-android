package com.analytics.blotout

import android.util.Log
import android.webkit.URLUtil
import com.analytics.blotout.util.Constant
import com.analytics.blotout.util.Errors
import java.lang.Exception

class BlotoutAnalyticsConfiguration {


    var blotoutSDKKey: String? = null
    var endPointUrl: String? = null

    companion object {
        private const val TAG = "AnalyticsConfiguration"
    }

    fun validateRequest(): Int {
        if (blotoutSDKKey.isNullOrEmpty())
            return Errors.ERROR_KEY_NOT_PROPER
        else if (endPointUrl.isNullOrEmpty() || !URLUtil.isValidUrl(endPointUrl))
            return Errors.ERROR_URL_NOT_PROPER
        save()
        return Errors.ERROR_CODE_NO_ERROR
    }

    private fun save() {
        try {
            val storageService = DependencyInjectorImpl.getInstance().getSecureStorageService()
            storageService.storeString(Constant.SDK_KEY, blotoutSDKKey!!)
            storageService.storeString(Constant.SDK_END_POINT_URL, endPointUrl!!)
        } catch (e: Exception) {
            Log.e(TAG,e.localizedMessage)
        }
    }
}