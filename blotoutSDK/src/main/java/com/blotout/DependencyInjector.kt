package com.blotout

import android.content.Context
import com.blotout.data.database.EventDatabase
import com.blotout.network.HostConfiguration
import com.blotout.network.RemoteApiService
import com.blotout.repository.ManifestRepository
import com.blotout.repository.data.ConfigurationDataManager
import com.blotout.repository.data.SharedPreferenceSecureVault

interface DependencyInjector {

    fun getRemoteAPIService():RemoteApiService
    fun getManifestRepository():ManifestRepository
    fun getSecureStorageService() : SharedPreferenceSecureVault
    fun getHostService():HostConfiguration
    fun getContext(): Context
    fun getConfigurationManager() : ConfigurationDataManager
    fun getEventDatabase(): EventDatabase
}