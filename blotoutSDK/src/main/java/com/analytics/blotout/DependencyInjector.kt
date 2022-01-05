package com.analytics.blotout

import android.app.Application
import android.content.Context
import com.analytics.blotout.data.database.EventDatabase
import com.analytics.blotout.network.HostConfiguration
import com.analytics.blotout.network.RemoteApiDataSource
import com.analytics.blotout.network.RemoteApiService
import com.analytics.blotout.repository.ManifestRepository
import com.analytics.blotout.repository.data.ConfigurationDataManager
import com.analytics.blotout.repository.data.SharedPreferenceSecureVault
import java.lang.Appendable

interface DependencyInjector {

    fun getRemoteAPIDataSource():RemoteApiDataSource
    fun getManifestRepository():ManifestRepository
    fun getSecureStorageService() : SharedPreferenceSecureVault
    fun getHostService():HostConfiguration
    fun getApplication(): Application
    fun getConfigurationManager() : ConfigurationDataManager
    fun getEventDatabase(): EventDatabase
}