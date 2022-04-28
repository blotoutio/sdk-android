package com.analytics.blotout.repository.impl

import com.analytics.blotout.model.*
import com.analytics.blotout.network.ApiDataProvider
import com.analytics.blotout.network.RemoteApiDataSource
import com.analytics.blotout.network.RemoteApiService
import com.analytics.blotout.repository.data.ConfigurationDataManager

class ConfigurationDataManagerImpl(private val dataSource: RemoteApiDataSource) : ConfigurationDataManager {


    override fun downloadManifestConfiguration(handler: ApiDataProvider<ManifestConfigurationResponse?>){
        return dataSource.getSDKManifest().enqueue(handler)
    }

    override fun publishEvents(events: Events, handler: ApiDataProvider<Any?>) {
        dataSource.postEvents(events).enqueue(handler)
    }

}