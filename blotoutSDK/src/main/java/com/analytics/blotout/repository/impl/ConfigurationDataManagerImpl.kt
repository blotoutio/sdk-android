package com.analytics.blotout.repository.impl

import com.analytics.blotout.model.*
import com.analytics.blotout.network.ApiDataProvider
import com.analytics.blotout.network.RemoteApiDataSource
import com.analytics.blotout.network.RemoteApiService
import com.analytics.blotout.repository.data.ConfigurationDataManager

class ConfigurationDataManagerImpl(private val dataSource: RemoteApiDataSource) : ConfigurationDataManager {


    override suspend fun downloadManifestConfiguration(): Result<ManifestConfigurationResponse?> {
        return dataSource.getSDKManifest()
    }

    override fun publishEvents(events: Events, handler: ApiDataProvider<Any?>) {
        dataSource.postEvents(events).enqueue(handler)
    }

}