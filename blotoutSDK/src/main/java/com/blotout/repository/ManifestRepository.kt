package com.blotout.repository

import android.util.Log
import com.blotout.DependencyInjectorImpl
import com.blotout.model.ManifestConfigurationRequest
import com.blotout.model.ManifestConfigurationResponse
import com.blotout.network.ApiDataProvider
import com.blotout.repository.data.ConfigurationDataManager
import com.blotout.util.Constant
import com.blotout.util.manifestFileName
import com.google.gson.Gson
import retrofit2.Call

class ManifestRepository(private val configurationDataManager: ConfigurationDataManager) {

    //var eventDeviceInfoGrain = 0
    //var serverBaseURL: String? = null
    //var eventPath: String? = null
    public var sdkPushSystemEvents = false
    public var sdkPIIPublicKey: String? = null
    public var sdkPHIPublicKey: String? = null

    /*enum class EventDeviceInfoGrainType(var type: Int){
        DO_NOT_PUSH(0),PUSH_ONLY_OS_VERSSION_DEVICE_MANUFATURE_DEVICE_MODEL(1),PUSH(2)
    }*/

    fun fetchManifestConfiguration() {

        val manifestConfigurationRequest = ManifestConfigurationRequest(
                lastUpdatedTime = "00",
                bundleId = "com.blotout.analytics"
        )


        configurationDataManager.downloadManifestConfiguration(manifestConfigurationRequest, object : ApiDataProvider<ManifestConfigurationResponse?>() {
            override fun onFailed(errorCode: Int, message: String, call: Call<ManifestConfigurationResponse?>) {
                Log.d("onFailed", errorCode.toString())
                Log.d("onFailed", message)

            }

            override fun onError(t: Throwable, call: Call<ManifestConfigurationResponse?>) {
                Log.d("onError", t.message.toString())

            }

            override fun onSuccess(manifestConfigurationResponse:  ManifestConfigurationResponse?) {
                Log.d("onSuccess", manifestConfigurationResponse?.variables?.get(0)?.variableName!!)
                /*var fileService = DependencyInjectorImpl.getInstance().getFileService()
                var manifestFileName = Constant.MANIFEST_FILE_NAME.manifestFileName()
                if (fileService.checkFileExist(manifestFileName))
                    fileService.deleteFilesAndDir(manifestFileName)

                fileService.writeToFile(manifestFileName, Gson().toJson(data))*/
                initManifestConfiguration(manifestConfigurationResponse)

            }

        })
    }

    fun initManifestConfiguration(manifestConfigurationResponse: ManifestConfigurationResponse) {
        /*var fileService = DependencyInjectorImpl.getInstance().getFileService()
        var manifestFileName = Constant.MANIFEST_FILE_NAME.manifestFileName()
        var manifestContent = fileService.readContentOfFileAtPath(manifestFileName)
        var manifetObject = Gson().fromJson(manifestContent, ManifestConfigurationResponse::class.java)*/
        var manifestConfigurations = manifestConfigurationResponse.variables?.get(0)
        for(manifestData in manifestConfigurationResponse.variables) {

            when (manifestConfigurations?.variableName) {
                //Constant.Event_DeviceInfoGrain -> eventDeviceInfoGrain = manifestConfigurations?.value!!.toInt()
                //Constant.Api_Endpoint -> serverBaseURL = manifestConfigurations?.value!!
                //Constant.EVENT_PATH -> eventPath = manifestConfigurations?.value!!
                Constant.Event_Push_System_Events -> sdkPushSystemEvents = manifestConfigurations?.value!!.toBoolean()
                Constant.Event_PII_Public_Key -> sdkPIIPublicKey = manifestConfigurations?.value!!
                Constant.Event_PHI_Public_Key -> sdkPHIPublicKey = manifestConfigurations?.value!!
            }
        }

    }
}