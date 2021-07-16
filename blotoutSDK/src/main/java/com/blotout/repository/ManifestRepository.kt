package com.blotout.repository

import android.util.Log
import com.blotout.DependencyInjectorImpl
import com.blotout.model.ManifestConfigurationResponse
import com.blotout.model.VariableOption
import com.blotout.network.ApiDataProvider
import com.blotout.repository.data.ConfigurationDataManager
import com.blotout.util.Constant
import retrofit2.Call

class ManifestRepository(private val configurationDataManager: ConfigurationDataManager) {


     var sdkPushSystemEvents = false
     var sdkPIIPublicKey: String? = null
     var sdkPHIPublicKey: String? = null
     var sdkSystemEevntsAllowed : List<VariableOption>? = null


    fun fetchManifestConfiguration() {

        configurationDataManager.downloadManifestConfiguration(object : ApiDataProvider<ManifestConfigurationResponse?>() {
            override fun onFailed(errorCode: Int, message: String, call: Call<ManifestConfigurationResponse?>) {
                Log.d("onFailed", errorCode.toString())
                Log.d("onFailed", message)

            }

            override fun onError(t: Throwable, call: Call<ManifestConfigurationResponse?>) {
                Log.d("onError", t.message.toString())

            }

            override fun onSuccess(manifestConfigurationResponse:  ManifestConfigurationResponse?) {
                Log.d("onSuccess", manifestConfigurationResponse?.variables?.get(0)?.variableName!!)
                initManifestConfiguration(manifestConfigurationResponse)
            }

        })
    }

    fun initManifestConfiguration(manifestConfigurationResponse: ManifestConfigurationResponse) {

        for(manifestData in manifestConfigurationResponse.variables) {
            when (manifestData?.variableName) {
                Constant.Event_Push_System_Events -> sdkPushSystemEvents = manifestData?.value!!.toBoolean()
                Constant.Event_PII_Public_Key -> sdkPIIPublicKey = manifestData?.value!!
                Constant.Event_PHI_Public_Key -> sdkPHIPublicKey = manifestData?.value!!
                Constant.Event_Push_System_Events_Allowed -> sdkSystemEevntsAllowed = manifestData?.variableOptions
            }
        }
    }
}