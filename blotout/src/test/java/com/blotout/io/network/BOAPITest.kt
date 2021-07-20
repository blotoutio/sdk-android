package com.blotout.io.network;

import android.app.Application
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.blotout.network.BOAPIFactory
import com.blotout.network.api.BOBaseAPI
import com.blotout.storage.BOFileSystemManager
import com.blotout.storage.BOSharedPreferenceImpl
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by ankuradhikari on 27,September,2020
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [23], manifest = Config.NONE)
class BOAPITest {


        lateinit var context: Application
        @Before
        fun setUp() {
            MockitoAnnotations.initMocks(this)
            context = BOTestUtils.mockApplication()
            whenever(context.applicationContext).thenReturn(context)
            BOSharedPreferenceImpl.getInstance(context.applicationContext)
        }


    @Test
    fun testBaseUrl() {
        val api = BOBaseAPI.getInstance()
        val urlString = api.validateAndReturnServerEndPoint("http://api.blotout.io")
        Assert.assertEquals("", urlString, "http://api.blotout.io/sdk/")
    }

    @Test
    fun testBaseUrlWithSlash() {
        val api = BOBaseAPI.getInstance()
        val urlString = api.validateAndReturnServerEndPoint("http://api.blotout.io/")
        Assert.assertEquals("", urlString, "http://api.blotout.io/sdk/")
    }

    @Test
    fun testGetBaseAPI() {
        val path = BOBaseAPI.getInstance().baseAPI
        Assert.assertEquals("", path, "https://stage-sdk.blotout.io/sdk/")
    }

    @Test
    fun testGetManifestPath() {
        val path = BOBaseAPI.getInstance().manifestPath
        Assert.assertEquals("", path, "v1/manifest/pull")
    }

    @Test
    fun testGetEventPost() {
        val path = BOBaseAPI.getInstance().eventPost
        Assert.assertEquals("", path, "v1/events/publish")
    }

    @Test
    fun testGetSegmentFeedback() {
        val path = BOBaseAPI.getInstance().segmentFeedback
        Assert.assertEquals("", path, "v1/segment/custom/feedback")
    }

    @Test
    fun testGetSegmentPath() {
        val path = BOBaseAPI.getInstance().segmentPath
        Assert.assertEquals("", path, "v1/segment/pull")
    }

    @Test
    fun testGetFunnelFeedback() {
        val path = BOBaseAPI.getInstance().funnelFeedback
        Assert.assertEquals("", path, "v1/funnel/feedback")
    }

    @Test
    fun testGetFunnelPath() {
        val path = BOBaseAPI.getInstance().funnelPath
        Assert.assertEquals("", path, "v1/funnel/pull")
    }

    @Test
    fun testGetRetentionPath() {
        val path = BOBaseAPI.getInstance().retentionPublish
        Assert.assertEquals("", path, "v1/events/retention/publish")
    }

    @Test
    fun testGetGeoPath() {
        val path = BOBaseAPI.getInstance().geoPath
        Assert.assertEquals("", path, "v1/geo/city")
    }

    @Test
    fun testAPIFactory() {
        val factoryInstance = BOAPIFactory("1.0","https://sales.blotout.io")
        Assert.assertNotNull(factoryInstance)
    }
}
