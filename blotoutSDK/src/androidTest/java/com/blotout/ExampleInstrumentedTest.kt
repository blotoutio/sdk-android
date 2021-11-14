package com.analytics.blotout

import android.app.Application
import com.analytics.blotout.model.CompletionHandler
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleInstrumentedTest {

    lateinit var context: Application

    lateinit var blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration



    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(context.applicationContext).thenReturn(context)
        blotoutAnalyticsConfiguration.blotoutSDKKey = "KHPREXFRED7HMGB"
        blotoutAnalyticsConfiguration.endPointUrl = "https://stage.blotout.io/sdk/"
        BlotoutAnalytics.init(
            application = context,
            blotoutAnalyticsConfiguration = blotoutAnalyticsConfiguration,object: CompletionHandler{
                override fun onSuccess() {
                }

                override fun onError() {
                }

            })
    }

    @Test
    fun testKeys() {
        var errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(0, errorCode)
    }
}