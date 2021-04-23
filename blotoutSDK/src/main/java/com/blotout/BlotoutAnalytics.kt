package com.blotout

object BlotoutAnalytics: BlotoutAnalyticsInternal() {

    private var instance: BlotoutAnalytics? = null

    @Synchronized
    private fun createInstance() {
        if (instance == null) {
            instance = BlotoutAnalytics
        }
    }

    fun getInstance(): BlotoutAnalytics? {
        if (instance == null) createInstance()
        return instance
    }
}