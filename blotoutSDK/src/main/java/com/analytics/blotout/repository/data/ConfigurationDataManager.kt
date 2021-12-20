package com.analytics.blotout.repository.data

import com.analytics.blotout.model.Events
import com.analytics.blotout.model.ManifestConfigurationResponse
import com.analytics.blotout.model.Result
import com.analytics.blotout.network.ApiDataProvider

interface ConfigurationDataManager {

    suspend fun downloadManifestConfiguration():Result<ManifestConfigurationResponse>
    fun publishEvents(events : Events, handler : ApiDataProvider<Any?>)
}