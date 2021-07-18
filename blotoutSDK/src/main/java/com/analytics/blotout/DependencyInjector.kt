package com.analytics.blotout

import android.content.Context
import com.analytics.blotout.data.database.EventDatabase
import com.analytics.blotout.network.HostConfiguration
import com.analytics.blotout.network.RemoteApiService
import com.analytics.blotout.repository.ManifestRepository
import com.analytics.blotout.repository.data.ConfigurationDataManager
import com.analytics.blotout.repository.data.SharedPreferenceSecureVault

interface DependencyInjector {

    fun getRemoteAPIService():RemoteApiService
    fun getManifestRepository():ManifestRepository
    fun getSecureStorageService() : SharedPreferenceSecureVault
    fun getHostService():HostConfiguration
    fun getContext(): Context
    fun getConfigurationManager() : ConfigurationDataManager
    fun getEventDatabase(): EventDatabase
}