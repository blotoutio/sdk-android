package com.analytics.blotout

import android.app.Application
import android.content.Context
import android.util.Log
import com.analytics.blotout.model.*
import com.analytics.blotout.model.CompletionHandler
import com.analytics.blotout.network.ApiDataProvider
import com.analytics.blotout.network.HostConfiguration
import com.analytics.blotout.repository.EventRepository
import com.analytics.blotout.repository.impl.SharedPreferenceSecureVaultImpl
import com.analytics.blotout.util.Constant
import com.analytics.blotout.util.Errors
import com.analytics.blotout.util.serializeToMap
import kotlinx.coroutines.*
import retrofit2.Call
import java.lang.Exception

open class BlotoutAnalyticsInternal : BlotoutAnalyticsInterface {

    companion object {
        private const val TAG = "AnalyticsInternal"
        private var isSdkinitiliazed = false
    }


    override fun init(
        application: Application,
        blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration,
        completionHandler: CompletionHandler
    ) {
        val handler = CoroutineExceptionHandler { _, exception ->
            completionHandler.onError(code=ErrorCodes.ERROR_CODE_NETWORK_ERROR, msg = exception.localizedMessage!!)
        }
        CoroutineScope(Dispatchers.IO+handler).launch {

            val secureVault = SharedPreferenceSecureVaultImpl(application.getSharedPreferences("vault", Context.MODE_PRIVATE), "crypto")
            val hostConfiguration = HostConfiguration(baseUrl = blotoutAnalyticsConfiguration.endPointUrl, baseKey = blotoutAnalyticsConfiguration.blotoutSDKKey)

            DependencyInjectorImpl.init(
                application = application,
                secureStorageService = secureVault,
                hostConfiguration = hostConfiguration
            )
        when (blotoutAnalyticsConfiguration.validateRequest()) {
            Errors.ERROR_KEY_NOT_PROPER -> {
                completionHandler.onError(code=ErrorCodes.ERROR_CODE_SDK_KEY_NOT_PROPER, msg = "SDK key is invalid")
            }
            Errors.ERROR_URL_NOT_PROPER -> {
                completionHandler.onError(code=ErrorCodes.ERROR_CODE_END_POINT_URL_NOT_PROPER, msg = "End point url is invalid")
            }
            else -> {
                DependencyInjectorImpl.getInstance().getManifestRepository()
                        .fetchManifestConfiguration(object : ApiDataProvider<ManifestConfigurationResponse?>() {
                            override fun onFailed(
                                errorCode: Int,
                                message: String,
                                call: Call<ManifestConfigurationResponse?>
                            ) {
                                completionHandler.onError(code=errorCode, msg = message)
                            }

                            override fun onError(
                                t: Throwable,
                                call: Call<ManifestConfigurationResponse?>
                            ) {
                                completionHandler.onError(code=ErrorCodes.ERROR_CODE_NETWORK_ERROR, msg = t.localizedMessage!!)
                            }

                            override fun onSuccess(data: ManifestConfigurationResponse?) {
                                isSdkinitiliazed = true
                                DependencyInjectorImpl.getInstance().initialize()
                                completionHandler.onSuccess()
                            }

                        })
                }
            }
        }
    }

    @Synchronized
    override fun enable(enabled: Boolean) {
        try {
            DependencyInjectorImpl.getInstance().getSecureStorageService()
                .storeBoolean(Constant.IS_SDK_ENABLE, enabled)
        }catch (e:Exception){
            Log.e(TAG,e.toString())
        }
    }

    @Synchronized
    override fun capture(eventName: String, eventInfo: HashMap<String, Any>){
        val sdkEnable = DependencyInjectorImpl.getInstance().getSecureStorageService()
            .fetchBoolean(Constant.IS_SDK_ENABLE)
        if(sdkEnable && isSdkinitiliazed) {
            CoroutineScope(Dispatchers.Default).launch {
                try {

                    val eventsRepository =
                        EventRepository(
                            DependencyInjectorImpl.getInstance().getSecureStorageService()
                        )
                    eventInfo.let {
                        eventsRepository.prepareCodifiedEvent(
                            eventName = eventName,
                            eventInfo = eventInfo
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

    @Synchronized
    override fun capturePersonal(
        eventName: String,
        eventInfo: HashMap<String, Any>,
        isPHI: Boolean
    ) {
        val sdkEnable = DependencyInjectorImpl.getInstance().getSecureStorageService()
            .fetchBoolean(Constant.IS_SDK_ENABLE)
        if(sdkEnable && isSdkinitiliazed) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val eventsRepository =
                    EventRepository(
                        DependencyInjectorImpl.getInstance().getSecureStorageService()
                    )
                eventsRepository.preparePersonalEvent(eventName, eventInfo, isPHI)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())

            }  }
        }
    }

    @Synchronized
    override fun mapID(mapIDData: MapIDData, withInformation: HashMap<String, Any>?) {
        val sdkEnable = DependencyInjectorImpl.getInstance().getSecureStorageService()
            .fetchBoolean(Constant.IS_SDK_ENABLE)
        if(sdkEnable && isSdkinitiliazed) {
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    val _withInformation = withInformation ?: hashMapOf()
                    _withInformation[Constant.BO_EVENT_MAP_ID] = mapIDData.externalID
                    _withInformation[Constant.BO_EVENT_MAP_Provider] = mapIDData.provider
                    val eventsRepository =
                        EventRepository(
                            DependencyInjectorImpl.getInstance().getSecureStorageService()
                        )
                    eventsRepository.prepareCodifiedEvent(
                        eventName = Constant.BO_EVENT_MAP_ID,
                        eventInfo = _withInformation
                    )

                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

    override fun getUserId(): String {
        try {
            return DependencyInjectorImpl.getInstance().getSecureStorageService()
                .fetchString(Constant.BO_ANALYTICS_USER_UNIQUE_KEY)
        } catch (e: Exception) {
            Log.e(TAG,e.toString())
        }
        return ""
    }

    @Synchronized
    override fun transaction(transactionData: TransactionData, withInformation: HashMap<String, Any>?) {
        val sdkEnable = DependencyInjectorImpl.getInstance().getSecureStorageService()
            .fetchBoolean(Constant.IS_SDK_ENABLE)
        if(sdkEnable && isSdkinitiliazed) {
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    val _withInformation = withInformation ?: hashMapOf()
                    _withInformation.putAll(transactionData.serializeToMap())
                    val eventsRepository =
                        EventRepository(
                            DependencyInjectorImpl.getInstance().getSecureStorageService()
                        )
                    eventsRepository.prepareCodifiedEvent(
                        eventName = Constant.BO_EVENT_TRANSACTION_NAME,
                        eventInfo = _withInformation
                    )

                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

    @Synchronized
    override fun item(itemData: Item, withInformation: HashMap<String, Any>?) {
        val sdkEnable = DependencyInjectorImpl.getInstance().getSecureStorageService()
            .fetchBoolean(Constant.IS_SDK_ENABLE)
        if(sdkEnable && isSdkinitiliazed) {
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    val _withInformation = withInformation ?: hashMapOf()
                    _withInformation.putAll(itemData.serializeToMap())
                    val eventsRepository =
                        EventRepository(
                            DependencyInjectorImpl.getInstance().getSecureStorageService()
                        )
                    eventsRepository.prepareCodifiedEvent(
                        eventName = Constant.BO_EVENT_TRANSACTION_ITEM_NAME,
                        eventInfo = _withInformation
                    )

                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

    @Synchronized
    override fun persona(personaData: Persona , withInformation: HashMap<String, Any>?) {
        val sdkEnable = DependencyInjectorImpl.getInstance().getSecureStorageService()
            .fetchBoolean(Constant.IS_SDK_ENABLE)
        if(sdkEnable && isSdkinitiliazed) {
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    val _withInformation = withInformation ?: hashMapOf()
                    _withInformation.putAll(personaData.serializeToMap())
                    val eventsRepository =
                        EventRepository(
                            DependencyInjectorImpl.getInstance().getSecureStorageService()
                        )
                    eventsRepository.prepareCodifiedEvent(
                        eventName = Constant.BO_EVENT_PERSONA_NAME,
                        eventInfo = _withInformation
                    )

                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

}