package com.blotout.repository.impl

import android.content.Context
import com.blotout.model.*
import com.blotout.network.ApiDataProvider
import com.blotout.network.RemoteApiService
import com.blotout.repository.data.ConfigurationDataManager

class ConfigurationDataManagerImpl(private val service: RemoteApiService) : ConfigurationDataManager {


    override fun downloadManifestConfiguration(handler: ApiDataProvider<ManifestConfigurationResponse?>) {
        service.getSDKManifest()?.enqueue(handler)
    }

    override fun publishEvents(events: Events, handler: ApiDataProvider<Any?>) {
        service.postEvents(events)?.enqueue(handler)
    }

}