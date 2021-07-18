package com.analytics.blotout

import android.app.Application
import com.analytics.blotout.model.EventStatus
import com.analytics.blotout.repository.EventRepository
import com.analytics.blotout.util.Constant
import com.analytics.blotout.util.Errors
import java.lang.Exception

open class BlotoutAnalyticsInternal : BlotoutAnalyticsInterface {

    companion object {
        const val TAG = "BlotoutAnalyticsInternal"
    }


    override fun init(application: Application, blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration) {
        when (blotoutAnalyticsConfiguration.validateRequest()) {
            Errors.ERROR_KEY_NOT_PROPER -> throw Exception("SDK key is invalid")
            Errors.ERROR_URL_NOT_PROPER -> throw  Exception("End point url is invalid")
            Errors.ERROR_CODE_NO_ERROR -> {
                DependencyInjectorImpl.init(application = application, blotoutAnalyticsConfiguration = blotoutAnalyticsConfiguration)
                DependencyInjectorImpl.getInstance().getManifestRepository().fetchManifestConfiguration()
            }
        }
    }

    override fun enable(enabled: Boolean) {
        DependencyInjectorImpl.getInstance().getSecureStorageService().storeBoolean(Constant.IS_SDK_ENABLE, enabled)
    }

    override fun capture(eventName: String, eventInfo: HashMap<String, Any>): EventStatus {
        try {
            var eventsRepository = EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
            return eventsRepository.prepareCodifiedEvent(eventName = eventName, eventInfo = eventInfo, withEventCode = 0)
        } catch (e: Exception) { }
        return EventStatus()
    }

    override fun capturePersonal(eventName: String, eventInfo: HashMap<String, Any>, isPHI: Boolean): EventStatus {
        try {
            var eventsRepository = EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
            return eventsRepository.preparePersonalEvent(eventName, eventInfo, isPHI)
        } catch (e: Exception) { }

        return EventStatus()
    }

    override fun mapID(userId: String?, provider: String?, withInformation: HashMap<String, Any>?): EventStatus {
        try {
            withInformation?.put(Constant.BO_EVENT_MAP_ID, userId!!)
            withInformation?.put(Constant.BO_EVENT_MAP_Provider, provider!!)
            var eventsRepository = EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
            return eventsRepository.prepareCodifiedEvent(eventName = Constant.BO_EVENT_MAP_ID, eventInfo = withInformation!!, withEventCode = Constant.BO_DEV_EVENT_MAP_ID)
        } catch (e: Exception) { }

        return EventStatus()
    }

    override fun getUserId(): String {
        try {
            return DependencyInjectorImpl.getInstance().getSecureStorageService().fetchString(Constant.BO_ANALYTICS_USER_UNIQUE_KEY)
        } catch (e: Exception) {
        }
        return ""
    }


}