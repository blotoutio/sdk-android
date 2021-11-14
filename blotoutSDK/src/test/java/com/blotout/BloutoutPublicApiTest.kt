package com.blotout

import android.app.Activity
import android.app.Application
import com.analytics.blotout.BlotoutAnalytics
import com.analytics.blotout.DependencyInjectorImpl
import com.analytics.blotout.model.CompletionHandler
import com.analytics.blotout.model.Result
import com.analytics.blotout.repository.EventRepository
import com.analytics.blotout.util.Errors
import junit.framework.Assert.assertFalse
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class BloutoutPublicApiTest {

    lateinit var context: Application

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = Mockito.mock(Application::class.java)
    }

    @Test
    fun testKeysForPositive() {
        var blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        var errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_CODE_NO_ERROR, errorCode)
    }

    @Test
    fun testKeysForNegative() {
        var blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        blotoutAnalyticsConfiguration.blotoutSDKKey = ""
        var errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_KEY_NOT_PROPER, errorCode)
    }

    @Test
    fun testUrlsForNegative() {
        var blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        blotoutAnalyticsConfiguration.endPointUrl = ""
        var errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_URL_NOT_PROPER, errorCode)
    }

    @Test
    fun testSDKStart() {
        var sdkStart = DependencyInjectorImpl.init(
            context,
            MockTestConstants.setupBlotoutAnalyticsConfiguration()
        )
        Assert.assertEquals(false, sdkStart)
    }

    @Test
    fun testCaptureApi(){
        var blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        BlotoutAnalytics.init(context, blotoutAnalyticsConfiguration,object : CompletionHandler{
            override fun onSuccess() {

            }
            override fun onError() {
            }
        })
        val eventInfo = HashMap<String, Any>()
        eventInfo["Join Blotout Slack"] = 0
        var eventsRepository =
            EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
        coroutineTestRule.runBlockingTest {
            var result = eventsRepository.prepareCodifiedEvent(
                eventName = "eventName",
                eventInfo = eventInfo,
                withEventCode = 0
            )
            when (result) {
                is Result.Success -> assertTrue(result.data !=null)
                is Result.Error -> assertFalse(result.errorData!=null)
            }
        }

    }

    @Test
    fun testCapturePersonalisPHIApi(){
        var blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        BlotoutAnalytics.init(context, blotoutAnalyticsConfiguration,object : CompletionHandler{
            override fun onSuccess() {

            }
            override fun onError() {
            }
        })
        val eventInfo = HashMap<String, Any>()
        eventInfo["Join Blotout Slack"] = 0
        var eventsRepository =
            EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
        coroutineTestRule.runBlockingTest {
            var result = eventsRepository.preparePersonalEvent(
                eventName = "Personal PHI events",
                eventInfo = eventInfo,
                isPHI = true
            )
            when (result) {
                is Result.Success -> assertTrue(result.data !=null)
                is Result.Error -> assertFalse(result.errorData!=null)
            }
        }

    }

    @Test
    fun testCapturePersonalApi(){
        var blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        BlotoutAnalytics.init(context, blotoutAnalyticsConfiguration,object : CompletionHandler{
            override fun onSuccess() {

            }
            override fun onError() {
            }
        })
        val eventInfo = HashMap<String, Any>()
        eventInfo["Join Blotout Slack"] = 0
        var eventsRepository =
            EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
        coroutineTestRule.runBlockingTest {
            var result = eventsRepository.preparePersonalEvent(
                eventName = "Non PHI Personal event",
                eventInfo = eventInfo,
                isPHI = false
            )
            when (result) {
                is Result.Success -> assertTrue(result.data !=null)
                is Result.Error -> assertFalse(result.errorData!=null)
            }
        }

    }

    @Test
    fun testCaptureSystemEventsApi(){
        var blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        BlotoutAnalytics.init(context, blotoutAnalyticsConfiguration,object : CompletionHandler{
            override fun onSuccess() {

            }
            override fun onError() {
            }
        })
        val eventInfo = HashMap<String, Any>()
        eventInfo["Join Blotout Slack"] = 0
        var eventsRepository =
            EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
        coroutineTestRule.runBlockingTest {
            eventsRepository.prepareSystemEvent(
                activity = context as Activity,
                eventName = "SDK Start",
                eventInfo = eventInfo,
                withEventCode = 11130
            )
        }
    }
}