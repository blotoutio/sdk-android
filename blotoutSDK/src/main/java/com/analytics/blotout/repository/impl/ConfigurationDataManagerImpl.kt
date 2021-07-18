package com.analytics.blotout.repository.impl

import com.analytics.blotout.model.*
import com.analytics.blotout.network.ApiDataProvider
import com.analytics.blotout.network.RemoteApiService
import com.analytics.blotout.repository.data.ConfigurationDataManager

class ConfigurationDataManagerImpl(private val service: RemoteApiService) : ConfigurationDataManager {


    override fun downloadManifestConfiguration(handler: ApiDataProvider<ManifestConfigurationResponse?>) {
        service.getSDKManifest()?.enqueue(handler)
    }

    override fun publishEvents(events: Events, handler: ApiDataProvider<Any?>) {
        service.postEvents(events)?.enqueue(handler)
    }

}