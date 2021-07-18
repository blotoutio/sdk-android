package com.analytics.blotout

import android.app.Application
import android.content.Context
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
import com.analytics.blotout.repository.impl.SharedPreferenceSecureVaultImpl
import com.analytics.blotout.util.Constant
import com.analytics.blotout.util.DateTimeUtils

class DependencyInjectorImpl private constructor(private val context: Context,
                                                 secureStorageService: SharedPreferenceSecureVault,
                                                 hostConfiguration: HostConfiguration,
                                                 eventDatabase: EventDatabase) : DependencyInjector {


    private val mSecureStorageService = secureStorageService
    private val mHostConfiguration = hostConfiguration
    private val mEventDatabase = eventDatabase
    private val mContext = context
    var mReferrerDetails: ReferrerDetails? = null



    companion object {
        private lateinit var instance: DependencyInjectorImpl
        private var sessionID :Long = 0
        private lateinit var eventRepository :EventRepository

        fun init(
                application: Application,
                blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration
        ) :Boolean{
            try {
                val secureVault = SharedPreferenceSecureVaultImpl(application.getSharedPreferences("vault", Context.MODE_PRIVATE), "crypto")
                var hostConfiguration = HostConfiguration(baseUrl = blotoutAnalyticsConfiguration.endPointUrl, baseKey = blotoutAnalyticsConfiguration.blotoutSDKKey)
                eventRepository = EventRepository(secureVault)
                var activityLifeCycleCallback = AnalyticsActivityLifecycleCallbacks(eventRepository, secureVault)
                application.registerActivityLifecycleCallbacks(activityLifeCycleCallback)
                instance = DependencyInjectorImpl(application, secureVault, hostConfiguration, EventDatabase.invoke(application))
                sessionID = DateTimeUtils().get13DigitNumberObjTimeStamp()
                blotoutAnalyticsConfiguration.save()
                eventRepository.prepareSystemEvent(null, Constant.BO_SDK_START, null, Constant.BO_EVENT_SDK_START)
                eventRepository.publishEvent()
                InstallRefferal().startClient(application)

            }catch (e:Exception){
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
        val mRemoteApiService = RemoteApiClient(getHostService()).getRemoteApiService()
        return mRemoteApiService
    }

    override fun getHostService():HostConfiguration{
        return mHostConfiguration

    }

    override fun getContext(): Context {
        return mContext
    }

    override fun getConfigurationManager(): ConfigurationDataManager {
        return dataManager.getConfigurationDataManager()
    }

    override fun getEventDatabase(): EventDatabase = mEventDatabase

    override fun getManifestRepository(): ManifestRepository = mManifestRepository

    override fun getSecureStorageService(): SharedPreferenceSecureVault {
        return mSecureStorageService
    }
}