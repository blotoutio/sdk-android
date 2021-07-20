package com.blotout.io.device

import android.app.Application
import android.os.Build
import com.blotout.deviceinfo.device.DeviceInfo
import com.blotout.deviceinfo.device.model.Device
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
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

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
    }

    @Test
    fun testDeviceInfo() {
        var deviceInfo = DeviceInfo(context.applicationContext)
        Assert.assertNotNull(deviceInfo)
    }
}