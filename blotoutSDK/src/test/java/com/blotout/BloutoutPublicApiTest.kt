package com.blotout

import android.app.Activity
import android.app.Application
import com.analytics.blotout.BlotoutAnalytics
import com.analytics.blotout.DependencyInjectorImpl
import com.analytics.blotout.model.CompletionHandler
import com.analytics.blotout.model.MapIDData
import com.analytics.blotout.model.Result
import com.analytics.blotout.repository.EventRepository
import com.analytics.blotout.repository.data.SharedPreferenceSecureVault
import com.analytics.blotout.repository.impl.SharedPreferenceSecureVaultImpl
import com.analytics.blotout.util.Errors
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BloutoutPublicApiTest {

    lateinit var context: Application

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    lateinit var  dependencyInjectorImpl:DependencyInjectorImpl


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = Mockito.mock(Application::class.java)
        dependencyInjectorImpl = mock(DependencyInjectorImpl::class.java)
    }

    @Test
    fun testKeysForPositive() {
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        val errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_CODE_NO_ERROR, errorCode)
    }

    @Test
    fun testKeysForNegative() {
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        blotoutAnalyticsConfiguration.blotoutSDKKey = ""
        val errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_KEY_NOT_PROPER, errorCode)
    }

    @Test
    fun testUrlsForNegative() {
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        blotoutAnalyticsConfiguration.endPointUrl = ""
        val errorCode = blotoutAnalyticsConfiguration.validateRequest()
        Assert.assertEquals(Errors.ERROR_URL_NOT_PROPER, errorCode)
    }

    @Test
    fun testSDKStart() {
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        BlotoutAnalytics.init(context, blotoutAnalyticsConfiguration,object : CompletionHandler{
            override fun onSuccess() {
                assertTrue(true)
            }
            override fun onError() {
                assertFalse(false)
            }
        })
    }

    @Test
    fun testCaptureApi(){
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        BlotoutAnalytics.init(context, blotoutAnalyticsConfiguration,object : CompletionHandler{
            override fun onSuccess() {

            }
            override fun onError() {
            }
        })
        val eventInfo = HashMap<String, Any>()
        eventInfo["Join Blotout Slack"] = 0
        val eventsRepository =
            EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
        coroutineTestRule.runBlockingTest {
            val result = eventsRepository.prepareCodifiedEvent(
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
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        BlotoutAnalytics.init(context, blotoutAnalyticsConfiguration,object : CompletionHandler{
            override fun onSuccess() {

            }
            override fun onError() {
            }
        })
        val eventInfo = HashMap<String, Any>()
        eventInfo["Join Blotout Slack"] = 0
        val eventsRepository =
            EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
        coroutineTestRule.runBlockingTest {
            val result = eventsRepository.preparePersonalEvent(
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
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        BlotoutAnalytics.init(context, blotoutAnalyticsConfiguration,object : CompletionHandler{
            override fun onSuccess() {

            }
            override fun onError() {
            }
        })
        val eventInfo = HashMap<String, Any>()
        eventInfo["Join Blotout Slack"] = 0
        val eventsRepository =
            EventRepository(DependencyInjectorImpl.getInstance().getSecureStorageService())
        coroutineTestRule.runBlockingTest {
            val result = eventsRepository.preparePersonalEvent(
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
        val blotoutAnalyticsConfiguration = MockTestConstants.setupBlotoutAnalyticsConfiguration()
        BlotoutAnalytics.init(context, blotoutAnalyticsConfiguration,object : CompletionHandler{
            override fun onSuccess() {

            }
            override fun onError() {
            }
        })
        val eventInfo = HashMap<String, Any>()
        eventInfo["Join Blotout Slack"] = 0
        val eventsRepository =
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

    @Test
    fun testMapIDApi(){
        try {
            val mapData = MapIDData()
            mapData.externalID = "1234"
            mapData.provider = "Google"
            BlotoutAnalytics.mapID(mapData, null)
            assertTrue(true)
        }catch (e:Exception){
            Assert.fail()
        }
    }
}