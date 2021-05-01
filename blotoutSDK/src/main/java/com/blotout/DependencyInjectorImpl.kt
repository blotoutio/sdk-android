package com.blotout

import android.app.Application
import android.content.Context
import com.blotout.data.database.EventDatabase
import com.blotout.network.HostConfiguration
import com.blotout.network.RemoteApiClient
import com.blotout.network.RemoteApiService
import com.blotout.repository.EventRepository
import com.blotout.repository.ManifestRepository
import com.blotout.repository.data.ConfigurationDataManager
import com.blotout.repository.data.SharedPrefernceSecureVault
import com.blotout.repository.impl.DataManagerImpl
import com.blotout.repository.impl.FileManagerImpl
import com.blotout.repository.impl.SharedPreferenceSecureVaultImpl
import com.blotout.util.DateTimeUtils

class DependencyInjectorImpl private constructor(private val context: Context,
                                                 secureStorageService: SharedPrefernceSecureVault,
                                                 hostConfiguration: HostConfiguration,
                                                 fileManagerImpl: FileManagerImpl,
                                                 eventDatabase: EventDatabase) : DependencyInjector {


    private val mSecureStorageService = secureStorageService
    private val mHostConfiguration = hostConfiguration
    private val mFileManagerImpl = fileManagerImpl
    private val mEventDatabase = eventDatabase
    private val mContext = context


    companion object {
        private lateinit var instance: DependencyInjectorImpl
        private var sessionID :Long = 0

        fun init(
                application: Application,
                blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration
        ) {
            val secureVault = SharedPreferenceSecureVaultImpl(application.getSharedPreferences("vault", Context.MODE_PRIVATE), "crypto")
            var hostConfiguration = HostConfiguration(baseUrl = blotoutAnalyticsConfiguration.endPointUrl,baseKey = blotoutAnalyticsConfiguration.blotoutSDKKey)
            var fileManagerImpl = FileManagerImpl(application)
            var eventRepository = EventRepository(secureVault)
            var activityLifeCycleCallback = AnalyticsActivityLifecycleCallbacks(eventRepository,secureVault)
            var eventDB = EventDatabase.invoke(application)
            application.registerActivityLifecycleCallbacks(activityLifeCycleCallback)
            instance = DependencyInjectorImpl(application, secureVault, hostConfiguration, fileManagerImpl,eventDB)
            sessionID = DateTimeUtils().get13DigitNumberObjTimeStamp()
            blotoutAnalyticsConfiguration.save()
            eventRepository.publishEvent()
        }

        fun getInstance(): DependencyInjectorImpl {
            return instance
        }

        fun getSessionId():Long{
            return sessionID
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

    override fun getFileService(): FileManagerImpl {
        return mFileManagerImpl
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