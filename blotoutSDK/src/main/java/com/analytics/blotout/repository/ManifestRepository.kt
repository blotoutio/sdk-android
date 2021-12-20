package com.analytics.blotout.repository

import android.util.Log
import com.analytics.blotout.DependencyInjectorImpl
import com.analytics.blotout.deviceinfo.device.DeviceInfo
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ManifestRepository(private val configurationDataManager: ConfigurationDataManager) {


     var sdkPushSystemEvents = false
     var sdkPIIPublicKey: String? = null
     var sdkPHIPublicKey: String? = null
     var sdkSystemEevntsAllowed : List<VariableOption>? = null


    suspend fun fetchManifestConfiguration() : Result<String> {
            when (val result = configurationDataManager.downloadManifestConfiguration()) {
                is Result.Success -> {
                    DependencyInjectorImpl.getInstance().getSecureStorageService()
                        .storeString(Gson().toJson(result.data), Constant.MANIFEST_DATA)
                    return initManifestConfiguration(result.data)
                }
                else -> {
                    val manifestConfiguration = Gson().fromJson(
                        DependencyInjectorImpl.getInstance().getSecureStorageService()
                            .fetchString(Constant.MANIFEST_DATA),
                        ManifestConfigurationResponse::class.java
                    )
                    manifestConfiguration?.let {
                        return initManifestConfiguration(manifestConfiguration)
                    }?: run {
                        return Result.Error(InternalError(code = ERROR_CODE_MANIFEST_NOT_AVAILABLE))
                    }
                }
            }
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