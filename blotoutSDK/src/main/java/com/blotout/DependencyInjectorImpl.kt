package com.blotout

import android.app.Application
import android.content.Context
import com.android.installreferrer.api.ReferrerDetails
import com.blotout.data.database.EventDatabase
import com.blotout.network.HostConfiguration
import com.blotout.network.RemoteApiClient
import com.blotout.network.RemoteApiService
import com.blotout.referral.InstallRefferal
import com.blotout.repository.EventRepository
import com.blotout.repository.ManifestRepository
import com.blotout.repository.data.ConfigurationDataManager
import com.blotout.repository.data.SharedPrefernceSecureVault
import com.blotout.repository.impl.DataManagerImpl
import com.blotout.repository.impl.SharedPreferenceSecureVaultImpl
import com.blotout.util.Constant
import com.blotout.util.DateTimeUtils

class DependencyInjectorImpl private constructor(private val context: Context,
                                                 secureStorageService: SharedPrefernceSecureVault,
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

    override fun getSecureStorageService(): SharedPrefernceSecureVault {
        return mSecureStorageService
    }
}