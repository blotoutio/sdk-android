package com.analytics.blotout

import android.app.Application
import com.analytics.blotout.util.Errors
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class BloutoutPublicApiTest {

    lateinit var context: Application
    var blotoutAnalyticsConfiguration =  BlotoutAnalyticsConfiguration()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = Mockito.mock(Application::class.java)


    }

    @Test
    fun testKeysForPositive() {
        blotoutAnalyticsConfiguration.blotoutSDKKey = "KHPREXFRED7HMGB"
        blotoutAnalyticsConfiguration.endPointUrl = "https://stage.blotout.io/sdk/"
        var errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_CODE_NO_ERROR, errorCode)
    }

    @Test
    fun testKeysForNegative() {
        blotoutAnalyticsConfiguration.blotoutSDKKey = ""
        blotoutAnalyticsConfiguration.endPointUrl = "https://stage.blotout.io/sdk/"
        var errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_KEY_NOT_PROPER, errorCode)
    }

    @Test
    fun testUrlsForNegative() {
        blotoutAnalyticsConfiguration.blotoutSDKKey = "KHPREXFRED7HMGB"
        blotoutAnalyticsConfiguration.endPointUrl = ""
        var errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_URL_NOT_PROPER, errorCode)
    }

    @Test
    fun testSDKStart() {
        var sdkStart = DependencyInjectorImpl.init(context, blotoutAnalyticsConfiguration)
        Assert.assertEquals(false, sdkStart)
    }

    @Test
    fun testCaptureApi(){
        blotoutAnalyticsConfiguration.blotoutSDKKey = "KHPREXFRED7HMGB"
        blotoutAnalyticsConfiguration.endPointUrl = "https://stage.blotout.io/sdk/"
        BlotoutAnalytics.getInstance()?.init(context, blotoutAnalyticsConfiguration)
        val eventInfo = HashMap<String, Any>()
        eventInfo["Join Blotout Slack"] = 0
        var eventStatus = BlotoutAnalytics.capture("hello",eventInfo)
        Assert.assertEquals(true, eventStatus.isSuccess)
    }

}