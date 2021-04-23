package com.blotout.repository.data

import com.blotout.model.Events
import com.blotout.model.ManifestConfigurationRequest
import com.blotout.model.ManifestConfigurationResponse
import com.blotout.network.ApiDataProvider

interface ConfigurationDataManager {

    fun downloadManifestConfiguration(manifestRequest : ManifestConfigurationRequest,handler : ApiDataProvider<ManifestConfigurationResponse?>)
    fun publishEvents(events : Events, handler : ApiDataProvider<Any?>)
}