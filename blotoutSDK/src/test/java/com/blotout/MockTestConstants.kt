package com.blotout

import com.analytics.blotout.BlotoutAnalyticsConfiguration

object MockTestConstants {

    fun setupBlotoutAnalyticsConfiguration():BlotoutAnalyticsConfiguration {
        var blotoutAnalyticsConfiguration = BlotoutAnalyticsConfiguration()
        blotoutAnalyticsConfiguration.blotoutSDKKey = "KHPREXFRED7HMGB"
        blotoutAnalyticsConfiguration.endPointUrl = "https://stage.blotout.io/sdk/"
        return blotoutAnalyticsConfiguration
    }
}