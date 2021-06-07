package com.blotout

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.Display
import android.view.WindowManager
import com.blotout.deviceinfo.device.DeviceInfo
import com.blotout.util.Errors
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class BloutoutDeviceInfoTest {

    lateinit var context: Application
    lateinit var deviceInfo: DeviceInfo
    lateinit var windowManagerMock:WindowManager
    lateinit var display: Display;
    lateinit var resources:Resources;
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = Mockito.mock(Application::class.java)
        deviceInfo = DeviceInfo(context)
        windowManagerMock = Mockito.mock(WindowManager::class.java)
        Mockito.`when`(context.getSystemService(Context.WINDOW_SERVICE)).thenReturn(windowManagerMock)

        display = Mockito.mock(Display::class.java)
        Mockito.`when`(windowManagerMock.defaultDisplay).thenReturn(display)

        resources = Mockito.mock(Resources::class.java)
        Mockito.`when`(context.resources).thenReturn(resources)

    }


    @Test
    fun testDeviceInfo() {
        Assert.assertNotNull(deviceInfo)
    }

    @Test
    fun testSDKVersion() {
        Assert.assertNotNull(deviceInfo.sdkVersion)
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
    fun testDeviceLocale() {
        var config = Mockito.mock(Configuration::class.java)
        Mockito.`when`(context.resources.configuration).thenReturn(config)
        Assert.assertNull(deviceInfo.deviceLocale)
    }

}