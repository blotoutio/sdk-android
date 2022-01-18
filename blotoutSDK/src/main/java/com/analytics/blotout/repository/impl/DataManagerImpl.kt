package com.analytics.blotout.repository.impl

import com.analytics.blotout.DependencyInjector
import com.analytics.blotout.repository.data.ConfigurationDataManager
import com.analytics.blotout.repository.data.DataManager

class DataManagerImpl(private val dependencyInjector: DependencyInjector) : DataManager {

    override fun getConfigurationDataManager(): ConfigurationDataManager {
        return ConfigurationDataManagerImpl(dependencyInjector.getRemoteAPIDataSource())
    }
}