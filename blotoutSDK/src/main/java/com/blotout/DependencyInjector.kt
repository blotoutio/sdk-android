package com.blotout

import android.content.Context
import com.android.installreferrer.api.ReferrerDetails
import com.blotout.data.database.EventDatabase
import com.blotout.network.HostConfiguration
import com.blotout.network.RemoteApiService
import com.blotout.repository.ManifestRepository
import com.blotout.repository.data.ConfigurationDataManager
import com.blotout.repository.data.SharedPrefernceSecureVault
import com.blotout.repository.impl.FileManagerImpl

interface DependencyInjector {

    fun getRemoteAPIService():RemoteApiService
    fun getManifestRepository():ManifestRepository
    fun getSecureStorageService() : SharedPrefernceSecureVault
    fun getHostService():HostConfiguration
    fun getFileService() : FileManagerImpl
    fun getContext(): Context
    fun getConfigurationManager() : ConfigurationDataManager
    fun getEventDatabase(): EventDatabase
}