package com.blotout.io

import android.app.Application
import com.blotout.analytics.BlotoutAnalytics
import com.blotout.events.BOAEvents
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by ankuradhikari on 17,November,2020
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [18], manifest = Config.NONE)
class BOAnalyticsTest {

    lateinit var context: Application

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
    }

    @Test
    fun checkInitialization() {
        BlotoutAnalytics.getInstance().initializeAnalyticsEngine(context, "", "")
        Assert.assertTrue("", BlotoutAnalytics.getInstance().isSDKInitialized)
    }

    @Test
    fun checkSetEnabled() {
        BOAEvents.isAppLifeModelInitialised = true
        BOAEvents.isSessionModelInitialised = true
        Assert.assertTrue("", BlotoutAnalytics.getInstance().isSDKInitialized)
    }

    @Test
    fun checkIsDeviceCompromised() {
        Assert.assertFalse("", BlotoutAnalytics.getInstance().isDeviceCompromised)
    }

    @Test
    fun checkIsAppCompromised() {
        Assert.assertFalse("", BlotoutAnalytics.getInstance().isAppCompromised)
    }

    @Test
    fun checkIsSimulator() {
        Assert.assertFalse("", BlotoutAnalytics.getInstance().isSimulator)
    }

    @Test
    fun checkIsRunningOnVM() {
        Assert.assertFalse("", BlotoutAnalytics.getInstance().isRunningOnVM)
    }

    @Test
    fun checkIsNetworkProxied() {
        Assert.assertFalse("", BlotoutAnalytics.getInstance().isNetworkProxied)
    }

    @Test
    fun checkIsEnvironmentSecure() {
        Assert.assertTrue("", BlotoutAnalytics.getInstance().isEnvironmentSecure)
    }
}