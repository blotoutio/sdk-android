package com.analytics.blotout.repository

import android.util.Log
import com.analytics.blotout.model.ManifestConfigurationResponse
import com.analytics.blotout.model.Meta
import com.analytics.blotout.model.Result
import com.analytics.blotout.model.VariableOption
import com.analytics.blotout.network.ApiDataProvider
import com.analytics.blotout.repository.data.ConfigurationDataManager
import com.analytics.blotout.util.Constant
import retrofit2.Call
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ManifestRepository(private val configurationDataManager: ConfigurationDataManager) {


     var sdkPushSystemEvents = false
     var sdkPIIPublicKey: String? = null
     var sdkPHIPublicKey: String? = null
     var sdkSystemEevntsAllowed : List<VariableOption>? = null


    suspend fun fetchManifestConfiguration() = suspendCoroutine<Result<String>> { continuation ->
        run {

            configurationDataManager.downloadManifestConfiguration(object :
                ApiDataProvider<ManifestConfigurationResponse?>() {
                override fun onFailed(
                    errorCode: Int,
                    message: String,
                    call: Call<ManifestConfigurationResponse?>
                ) {
                    Log.d("onFailed", errorCode.toString())
                    Log.d("onFailed", message)
                    continuation.resume(Result.Error(Throwable(message)))

                }

                override fun onError(t: Throwable, call: Call<ManifestConfigurationResponse?>) {
                    Log.d("onError", t.message.toString())
                    continuation.resume(Result.Error(t))
                }

                override fun onSuccess(manifestConfigurationResponse: ManifestConfigurationResponse?) {
                    Log.d(
                        "onSuccess",
                        manifestConfigurationResponse?.variables?.get(0)?.variableName!!
                    )
                    continuation.resume(initManifestConfiguration(manifestConfigurationResponse))
                }

            })
        }
    }

    fun initManifestConfiguration(manifestConfigurationResponse: ManifestConfigurationResponse):Result<String> {

        for(manifestData in manifestConfigurationResponse.variables) {
            when (manifestData?.variableName) {
                Constant.Event_Push_System_Events -> sdkPushSystemEvents = manifestData?.value!!.toBoolean()
                Constant.Event_PII_Public_Key -> sdkPIIPublicKey = manifestData?.value!!
                Constant.Event_PHI_Public_Key -> sdkPHIPublicKey = manifestData?.value!!
                Constant.Event_Push_System_Events_Allowed -> sdkSystemEevntsAllowed = manifestData?.variableOptions
            }
        }
        return Result.Success("")
    }
}