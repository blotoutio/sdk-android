package com.analytics.blotout

import android.app.Application
import android.content.Context
import android.util.Log
import com.analytics.blotout.model.*
import com.analytics.blotout.network.HostConfiguration
import com.analytics.blotout.repository.EventRepository
import com.analytics.blotout.repository.impl.SharedPreferenceSecureVaultImpl
import com.analytics.blotout.util.Constant
import com.analytics.blotout.util.Errors
import com.analytics.blotout.util.serializeToMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

open class BlotoutAnalyticsInternal : BlotoutAnalyticsInterface {

    companion object {
        private const val TAG = "AnalyticsInternal"
    }


    override fun init(
        application: Application,
        blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration,
        completionHandler: CompletionHandler
    ) {
        CoroutineScope(Dispatchers.Default).launch {

            val secureVault = SharedPreferenceSecureVaultImpl(application.getSharedPreferences("vault", Context.MODE_PRIVATE), "crypto")
            val hostConfiguration = HostConfiguration(baseUrl = blotoutAnalyticsConfiguration.endPointUrl, baseKey = blotoutAnalyticsConfiguration.blotoutSDKKey)

            DependencyInjectorImpl.init(
                application = application,
                secureStorageService = secureVault,
                hostConfiguration = hostConfiguration
            )
        when (blotoutAnalyticsConfiguration.validateRequest()) {
            Errors.ERROR_KEY_NOT_PROPER -> {
                throw Exception("SDK key is invalid")
            }
            Errors.ERROR_URL_NOT_PROPER -> {
                throw  Exception("End point url is invalid")
            }
            else -> {
                val result = DependencyInjectorImpl.getInstance().getManifestRepository()
                        .fetchManifestConfiguration()
                    when(result){
                        is Result.Success->{
                            DependencyInjectorImpl.getInstance().initialize()
                            completionHandler.onSuccess()
                        }
                        is Result.Error->{
                            completionHandler.onError()
                        }
                    }
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
        CoroutineScope(Dispatchers.Default).launch {
            try {

                val eventsRepository =
                    EventRepository(
                        DependencyInjectorImpl.getInstance().getSecureStorageService()
                    )
                var result = eventInfo.let {
                    eventsRepository.prepareCodifiedEvent(
                        eventName = eventName,
                        eventInfo = eventInfo,
                        withEventCode = 0
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    @Synchronized
    override fun capturePersonal(
        eventName: String,
        eventInfo: HashMap<String, Any>,
        isPHI: Boolean
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val eventsRepository =
                    EventRepository(
                        DependencyInjectorImpl.getInstance().getSecureStorageService()
                    )
                var result = eventsRepository.preparePersonalEvent(eventName, eventInfo, isPHI)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    @Synchronized
    override fun mapID(mapIDData: MapIDData, withInformation: HashMap<String, Any>?) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val _withInformation = withInformation ?: hashMapOf()
                _withInformation[Constant.BO_EVENT_MAP_ID] = mapIDData.externalID
                _withInformation[Constant.BO_EVENT_MAP_Provider] = mapIDData.provider
                val eventsRepository =
                    EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
                var result =
                    eventsRepository.prepareCodifiedEvent(
                        eventName = Constant.BO_EVENT_MAP_ID,
                        eventInfo = _withInformation,
                        withEventCode = Constant.BO_DEV_EVENT_MAP_ID
                    )

            } catch (e: Exception) {
                Log.e(TAG,e.toString())
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
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val _withInformation = withInformation ?: hashMapOf()
                _withInformation.putAll(transactionData.serializeToMap())
                val eventsRepository =
                    EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
                var result =
                    eventsRepository.prepareCodifiedEvent(
                        eventName = Constant.BO_EVENT_TRANSACTION_NAME,
                        eventInfo = _withInformation,
                        withEventCode = Constant.BO_EVENT_TRANSACTION
                    )

            } catch (e: Exception) {
                Log.e(TAG,e.toString())
            }
        }
    }

    @Synchronized
    override fun item(itemData: Item, withInformation: HashMap<String, Any>?) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val _withInformation = withInformation ?: hashMapOf()
                _withInformation.putAll(itemData.serializeToMap())
                val eventsRepository =
                    EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
                var result =
                    eventsRepository.prepareCodifiedEvent(
                        eventName = Constant.BO_EVENT_TRANSACTION_NAME,
                        eventInfo = _withInformation,
                        withEventCode = Constant.BO_EVENT_TRANSACTION
                    )

            } catch (e: Exception) {
                Log.e(TAG,e.toString())
            }
        }
    }

    @Synchronized
    override fun persona(personaData: Persona , withInformation: HashMap<String, Any>?) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val _withInformation = withInformation ?: hashMapOf()
                _withInformation.putAll(personaData.serializeToMap())
                val eventsRepository =
                    EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
                var result =
                    eventsRepository.prepareCodifiedEvent(
                        eventName = Constant.BO_EVENT_TRANSACTION_NAME,
                        eventInfo = _withInformation,
                        withEventCode = Constant.BO_EVENT_TRANSACTION
                    )

            } catch (e: Exception) {
                Log.e(TAG,e.toString())
            }
        }
    }

}