package com.analytics.blotout

import android.app.Application
import android.util.Log
import com.android.installreferrer.api.ReferrerDetails
import com.analytics.blotout.data.database.EventDatabase
import com.analytics.blotout.network.HostConfiguration
import com.analytics.blotout.network.RemoteApiClient
import com.analytics.blotout.network.RemoteApiService
import com.analytics.blotout.referral.InstallRefferal
import com.analytics.blotout.repository.EventRepository
import com.analytics.blotout.repository.ManifestRepository
import com.analytics.blotout.repository.data.ConfigurationDataManager
import com.analytics.blotout.repository.data.SharedPreferenceSecureVault
import com.analytics.blotout.repository.impl.DataManagerImpl
import com.analytics.blotout.util.Constant
import com.analytics.blotout.util.DateTimeUtils

class DependencyInjectorImpl private constructor(
    application: Application,
    secureStorageService: SharedPreferenceSecureVault,
    hostConfiguration: HostConfiguration,
    eventDatabase: EventDatabase) : DependencyInjector {


    private val mSecureStorageService = secureStorageService
    private val mHostConfiguration = hostConfiguration
    private val mEventDatabase = eventDatabase
    private val mApplication = application
    var mReferrerDetails: ReferrerDetails? = null



    companion object {
        private const val TAG ="DependencyInjectorImpl"
        private lateinit var instance: DependencyInjectorImpl
        private var sessionID :Long = 0
        private lateinit var eventRepository :EventRepository

        @Synchronized
        fun init(
                application: Application,
                secureStorageService: SharedPreferenceSecureVault,
                hostConfiguration: HostConfiguration
        ) :Boolean{
            try {
                instance = DependencyInjectorImpl(application, secureStorageService, hostConfiguration, EventDatabase.invoke(application))
            }catch (e:Exception){
                Log.d(TAG,e.localizedMessage!!)
                return false
            }
            return true
        }

        fun getInstance(): DependencyInjectorImpl {
            return instance
        }

        fun getSessionId():Long{
            return sessionID
        }

        fun getEventRepository():EventRepository{
            return eventRepository
        }
    }

    private val dataManager : DataManagerImpl by lazy{
        DataManagerImpl(this)
    }
    private val mManifestRepository = ManifestRepository(dataManager.getConfigurationDataManager())

    override fun getRemoteAPIService(): RemoteApiService {
        return RemoteApiClient(getHostService()).getRemoteApiService()
    }

    override fun getHostService():HostConfiguration{
        return mHostConfiguration

    }

    override fun getApplication():Application {
        return mApplication
    }

    override fun getConfigurationManager(): ConfigurationDataManager {
        return dataManager.getConfigurationDataManager()
    }

    override fun getEventDatabase(): EventDatabase = mEventDatabase

    override fun getManifestRepository(): ManifestRepository = mManifestRepository

    override fun getSecureStorageService(): SharedPreferenceSecureVault {
        return mSecureStorageService
    }

    fun initialize(){
        try {
            sessionID = DateTimeUtils().get13DigitNumberObjTimeStamp()
            eventRepository = EventRepository(mSecureStorageService)
            val activityLifeCycleCallback =
                AnalyticsActivityLifecycleCallbacks(eventRepository, mSecureStorageService)
            mApplication.registerActivityLifecycleCallbacks(activityLifeCycleCallback)
            eventRepository.prepareSystemEvent(
                null,
                Constant.BO_SDK_START,
                null,
                Constant.BO_EVENT_SDK_START
            )
            eventRepository.publishEvent()
            InstallRefferal().startClient(mApplication)
        }catch (e:Throwable){
            Log.d(TAG,e.localizedMessage!!)
        }
    }
}