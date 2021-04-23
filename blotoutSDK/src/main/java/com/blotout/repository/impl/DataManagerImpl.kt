package com.blotout.repository.impl

import com.blotout.DependencyInjector
import com.blotout.repository.data.ConfigurationDataManager
import com.blotout.repository.data.DataManager

class DataManagerImpl(private val dependencyInjector: DependencyInjector) : DataManager {

    override fun getConfigurationDataManager(): ConfigurationDataManager {
        return CongigurationDataManagerImpl(dependencyInjector.getRemoteAPIService(),dependencyInjector.getContext())
    }
}