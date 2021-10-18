package com.analytics.blotout

import android.app.Application
import com.analytics.blotout.model.EventStatus
import com.analytics.blotout.model.MapIDData
import com.analytics.blotout.model.Result
import com.analytics.blotout.repository.EventRepository
import com.analytics.blotout.util.Constant
import com.analytics.blotout.util.Errors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

open class BlotoutAnalyticsInternal : BlotoutAnalyticsInterface {

    companion object {
        const val TAG = "BlotoutAnalyticsInternal"
    }


    override fun init(
        application: Application,
        blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration,
        eventStatus: EventStatus
    ) {
        when (blotoutAnalyticsConfiguration.validateRequest()) {
            Errors.ERROR_KEY_NOT_PROPER -> {
                throw Exception("SDK key is invalid")
            }
            Errors.ERROR_URL_NOT_PROPER -> {
                throw  Exception("End point url is invalid")
            }
            else -> {
                CoroutineScope(Dispatchers.Default).launch {
                    DependencyInjectorImpl.init(
                        application = application,
                        blotoutAnalyticsConfiguration = blotoutAnalyticsConfiguration
                    )
                    var result = DependencyInjectorImpl.getInstance().getManifestRepository()
                        .fetchManifestConfiguration()
                    when(result){
                        is Result.Success->{
                            eventStatus.onSuccess()
                        }
                        is Result.Error->{
                            eventStatus.onError()
                        }
                    }
                }
            }
        }
    }

    override fun enable(enabled: Boolean) {
        DependencyInjectorImpl.getInstance().getSecureStorageService()
            .storeBoolean(Constant.IS_SDK_ENABLE, enabled)
    }

    override fun capture(eventName: String, eventInfo: HashMap<String, Any>) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                var eventsRepository =
                    EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
                var result = eventsRepository.prepareCodifiedEvent(
                    eventName = eventName,
                    eventInfo = eventInfo,
                    withEventCode = 0
                )
            } catch (e: Exception) {

            }
        }
    }

    override fun capturePersonal(
        eventName: String,
        eventInfo: HashMap<String, Any>,
        isPHI: Boolean
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                var eventsRepository =
                    EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
                var result = eventsRepository.preparePersonalEvent(eventName, eventInfo, isPHI)
            } catch (e: Exception) {
            }
        }
    }

    override fun mapID(mapIDData: MapIDData, withInformation: HashMap<String, Any>?) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                withInformation?.put(Constant.BO_EVENT_MAP_ID, mapIDData.externalID!!)
                withInformation?.put(Constant.BO_EVENT_MAP_Provider, mapIDData.provider!!)
                var eventsRepository =
                    EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
                var result = eventsRepository.prepareCodifiedEvent(
                    eventName = Constant.BO_EVENT_MAP_ID,
                    eventInfo = withInformation!!,
                    withEventCode = Constant.BO_DEV_EVENT_MAP_ID
                )
            } catch (e: Exception) {
            }
        }
    }

    override fun getUserId(): String {
        try {
            return DependencyInjectorImpl.getInstance().getSecureStorageService()
                .fetchString(Constant.BO_ANALYTICS_USER_UNIQUE_KEY)
        } catch (e: Exception) {

        }
        return ""
    }

}