package com.analytics.blotout.repository

import com.analytics.blotout.DependencyInjectorImpl
import com.analytics.blotout.model.ErrorCodes.ERROR_CODE_MANIFEST_NOT_AVAILABLE
import com.analytics.blotout.model.InternalError
import com.analytics.blotout.model.ManifestConfigurationResponse
import com.analytics.blotout.model.Result
import com.analytics.blotout.model.VariableOption
import com.analytics.blotout.network.ApiDataProvider
import com.analytics.blotout.repository.data.ConfigurationDataManager
import com.analytics.blotout.util.Constant
import com.google.gson.Gson
import retrofit2.Call

class ManifestRepository(private val configurationDataManager: ConfigurationDataManager) {


     var sdkPushSystemEvents = false
     var sdkPIIPublicKey: String? = null
     var sdkPHIPublicKey: String? = null
     var sdkSystemEevntsAllowed : List<VariableOption>? = null


     fun fetchManifestConfiguration(callback: ApiDataProvider<ManifestConfigurationResponse?>) {
            configurationDataManager.downloadManifestConfiguration(object : ApiDataProvider<ManifestConfigurationResponse?>() {
                override fun onFailed(
                    errorCode: Int,
                    message: String,
                    call: Call<ManifestConfigurationResponse?>
                ) {
                    val manifestConfiguration = getLocalManifestData()
                    manifestConfiguration?.let {
                         initManifestConfiguration(manifestConfiguration)
                    }?: run {
                        callback.onFailed(errorCode = ERROR_CODE_MANIFEST_NOT_AVAILABLE,message="",call)
                    }
                }

                override fun onError(t: Throwable, call: Call<ManifestConfigurationResponse?>) {
                    val manifestConfiguration = getLocalManifestData()
                    manifestConfiguration?.let {
                        initManifestConfiguration(manifestConfiguration)
                    }?: run {
                        callback.onError(t,call)
                    }
                }

                override fun onSuccess(data: ManifestConfigurationResponse?) {
                    initManifestConfiguration(data!!)
                    callback.onSuccess(data)
                }
            })
    }

    private fun getLocalManifestData(): ManifestConfigurationResponse? {
        return Gson().fromJson(
            DependencyInjectorImpl.getInstance().getSecureStorageService()
                .fetchString(Constant.MANIFEST_DATA),
            ManifestConfigurationResponse::class.java
        )

    }

    fun initManifestConfiguration(manifestConfigurationResponse: ManifestConfigurationResponse):Result<String> {

        for(manifestData in manifestConfigurationResponse.variables) {
            when (manifestData?.variableName) {
                Constant.Event_Push_System_Events -> sdkPushSystemEvents = manifestData.value!!.toBoolean()
                Constant.Event_PII_Public_Key -> sdkPIIPublicKey = manifestData.value!!
                Constant.Event_PHI_Public_Key -> sdkPHIPublicKey = manifestData.value!!
                Constant.Event_Push_System_Events_Allowed -> sdkSystemEevntsAllowed = manifestData.variableOptions
            }
        }
        return Result.Success("")
    }
}