package com.blotout

import android.app.Application
import android.content.Context
import androidx.lifecycle.Observer
import com.blotout.network.HostConfiguration
import com.blotout.repository.EventRepository
import com.blotout.repository.impl.FileManagerImpl
import com.blotout.repository.impl.SharedPrefernceSecureVaultImpl
import com.blotout.util.Constant
import com.blotout.util.Errors
import java.lang.Exception

open class BlotoutAnalyticsInternal:BlotoutAnalyticsInterface {

    companion object{
        const val TAG ="BlotoutAnalyticsInternal"
    }


    override fun init(application:Application, blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration){
        when(blotoutAnalyticsConfiguration.validateRequest()){
            Errors.ERROR_KEY_NOT_PROPER -> throw Exception("SDK key is invalid")
            Errors.ERROR_URL_NOT_PROPER -> throw  Exception("End point url is invalid")
            Errors.ERROR_CODE_NO_ERROR -> {
                DependencyInjectorImpl.init(application = application, blotoutAnalyticsConfiguration = blotoutAnalyticsConfiguration)
                DependencyInjectorImpl.getInstance().getManifestRepository().fetchManifestConfiguration()
            }
        }

    }

    override fun setEnabled(enabled:Boolean){
    }

    override fun capture(eventName :String, eventInfo:HashMap<String, Any>){
        var eventsRepository = EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
        eventsRepository.prepareCodifiedEvent(eventName=eventName,eventInfo = eventInfo,eventCode = 0)

    }

    override fun capturePersonal(eventName :String, eventInfo:HashMap<String, Any>, isPHI : Boolean){
        var eventsRepository = EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
        eventsRepository.preparePersonalEvent(eventName,eventInfo,isPHI)
    }

    override fun mapID(userId:String, provider:String, withInformation:HashMap<String,Any>){
        withInformation.put(Constant.BO_EVENT_MAP_ID,userId)
        withInformation.put(Constant.BO_EVENT_MAP_Provider,provider)
        var eventsRepository = EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
        eventsRepository.prepareCodifiedEvent(eventName = Constant.BO_EVENT_MAP_ID,eventInfo = withInformation,eventCode = Constant.BO_DEV_EVENT_MAP_ID)

    }


}