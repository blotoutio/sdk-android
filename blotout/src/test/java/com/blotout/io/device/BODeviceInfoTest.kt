package com.blotout.io.device

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import com.blotout.deviceinfo.device.DeviceInfo
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * Created by ankuradhikari on 30,November,2020
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [23], manifest = Config.NONE)
class BODeviceInfoTest {
    lateinit var context: Application
    lateinit var windowManagerMock:WindowManager
    lateinit var deviceInfo: DeviceInfo
    lateinit var resources:Resources;
    lateinit var display:Display;

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)

        deviceInfo = DeviceInfo(context.applicationContext)

        windowManagerMock = Mockito.mock(WindowManager::class.java)
        whenever(context.getSystemService(Context.WINDOW_SERVICE)).thenReturn(windowManagerMock)

        resources = Mockito.mock(Resources::class.java)
        whenever(context.resources).thenReturn(resources)

        display = Mockito.mock(Display::class.java)
        whenever(windowManagerMock.defaultDisplay).thenReturn(display)

    }

    @Test
    fun testDeviceInfo() {
        Assert.assertNotNull(deviceInfo)
    }

    @Test
    fun testSDKVersion() {
        Assert.assertNotNull(deviceInfo.sdkVersion)
        Assert.assertEquals(deviceInfo.sdkVersion,23)
    }

    @Test
    fun testDeviceName() {
        Assert.assertNotNull(deviceInfo.deviceName)
        Assert.assertEquals(deviceInfo.deviceName,"unknown")
    }

    @Test
    fun testDeviceWidth() {
        Assert.assertNotNull(deviceInfo.screenWidth)
    }

    @Test
    fun testDeviceHeight() {
        Assert.assertNotNull(deviceInfo.screenHeight)
    }

    @Test
    fun testDeviceDensity() {
        var displayMetrics = Mockito.mock(DisplayMetrics::class.java)
        whenever(context.resources.displayMetrics).thenReturn(displayMetrics)
        Assert.assertNotNull(deviceInfo.screenDensity)
    }

    @Test
    fun testDeviceLocale() {

        var config = Mockito.mock(Configuration::class.java)
        whenever(context.resources.configuration).thenReturn(config)
        print(deviceInfo.deviceLocale)
        Assert.assertNull(deviceInfo.deviceLocale)
    }


}