package com.blotout.io.device

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.blotout.deviceinfo.ads.AdvertisingIdClient
import com.blotout.eventsExecutor.BODeviceOperationExecutorHelper
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * Created by ankuradhikari on 30,November,2020
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [23], manifest = Config.NONE)
class BOAdsTest {

    lateinit var context: Application

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        val packageManager = mock(PackageManager::class.java)
        whenever(context.applicationContext).thenReturn(context)
        whenever(context.packageManager).thenReturn(packageManager)
    }

    @Test
    fun testGetAdvertisingIdInfo() {
        BODeviceOperationExecutorHelper.getInstance().post(Runnable {
            var adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.applicationContext)
            Assert.assertNotNull(adInfo)
        })
    }

}