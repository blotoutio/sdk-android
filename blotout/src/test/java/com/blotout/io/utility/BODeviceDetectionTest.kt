package com.blotout.io.utility

import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import android.view.Display
import android.webkit.WebSettings
import com.blotout.analytics.BOSharedManager
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.blotout.storage.BOSharedPreferenceImpl
import com.blotout.utilities.BODeviceDetection
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

/**
 * Created by ankuradhikari on 20,November,2020
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [18], manifest = Config.NONE, assetDir = "/src/test/assets")
class BODeviceDetectionTest {

    lateinit var context: Application

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
    }

    @Test
    fun testPlatformCode() {
        //BOSharedManager.getInstance(context.applicationContext);
        //var platform = BODeviceDetection.getDevicePlatformCode();
        //Assert.assertNotNull(platform)
    }

    @Test
    fun testAndroidTv() {
        var isAndroidTV = BODeviceDetection.isAndroidTV("Mozilla/5.0 (Linux; U; Android 4.2.2; he-il; NEO-X5-116A Build/JDQ39) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30\n" +
                "Amazon 4K Fire TV")
        Assert.assertTrue(isAndroidTV)
    }

    @Test
    fun testIsMobileDevice() {
        var isMobileDevice = BODeviceDetection.isMobileDevice("Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36 Samsung Galaxy S8")
        Assert.assertTrue(isMobileDevice)
    }

    @Test
    fun testIsHbbTVDevice() {
        var isHbbTVDevice = BODeviceDetection.isHbbTVDevice("Mozilla/5.0 (Linux armv9; U; en;) AppleWebKit/537.17 (Arcelik;O2;08.005.03;) CE-HTML/1.0 NETRANGEMMH HbbTV/1.2.1 (MWB;Arcelik_Gen3;RC_V2;Arcelik;O2;O2GRMR.-.-.-.-.V08.005.03;en-GB;wireless;prod;")
        Assert.assertTrue(isHbbTVDevice)
    }

    @Test
    fun testIsRokuDevice() {
        var isRokuDevice = BODeviceDetection.isRokuDevice("Mozilla/5.0 (CrKey armv7l 1.5.16041) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.0 Safari/537.36 Roku Ultra Roku4640X/DVP-7.70 (297.70E04154A) Minix NEO X5")
        Assert.assertTrue(isRokuDevice)
    }

    @Test
    fun testIsLGEDevice() {
        var isLGEDevice = BODeviceDetection.isLGEDevice("Mozilla/5.0 (Linux; U; Android 2.1-update1; en-us; LG-C710; Build/Eclair) AppleWebKit/530.17(KHTML, like Gecko) Version/4.0 Mobile Safari/530.17")
        Assert.assertTrue(isLGEDevice)
    }

    @Test
    fun testIsSamsungDevice() {
        var isSamsungDevice = BODeviceDetection.isSamsungDevice("Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36")
        Assert.assertTrue(isSamsungDevice)
    }
    @Test
    fun testIsAmazonFire() {
        var isAmazonFire = BODeviceDetection.isAmazonFire("Mozilla/5.0 (Linux; U; Android 4.2.2; he-il; NEO-X5-116A Build/JDQ39) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30\n" +
                "Amazon 4K Fire TV")
        Assert.assertTrue(isAmazonFire)
    }

    @Test
    fun testIsTabletDevice() {
        var resources = Mockito.mock(Resources::class.java)
        whenever(context.resources).thenReturn(resources)

        var configuration = Mockito.mock(Configuration::class.java)
        whenever(context.resources.configuration).thenReturn(configuration)

        var isTabletDevice = BODeviceDetection.isTabletDevice(context.applicationContext)
        Assert.assertFalse(isTabletDevice)
    }

    @Test
    fun testGetDeviceId() {
        var isDeviceId = BODeviceDetection.getDeviceId()
        Assert.assertNotNull(isDeviceId)
    }

    @Test
    fun testGenerateNumber() {
        var isGenerateNumber = BODeviceDetection.generateNumber()
        Assert.assertNotNull(isGenerateNumber)
    }

    @Test
    fun testConvertTo64CharUUID() {
        var isGUID = BODeviceDetection.convertTo64CharUUID("")
        Assert.assertNotNull(isGUID)
    }

    @Test
    fun testIsBadSerial() {
        var isBadSerial = BODeviceDetection.isBadSerial("")
        Assert.assertTrue(isBadSerial)
    }

    @Test
    fun testGetUniqueUserId() {
        var pref = BOSharedPreferenceImpl.getInstance(context.applicationContext)
        pref.saveString("com.blotout.sdk.Analytics.User.UniqueId","userid")
        var getUniqueUserId = BODeviceDetection.getUniqueUserId()
        Assert.assertEquals(getUniqueUserId,"userid")
    }

    @Test
    fun testIsBadDeviceId() {
        var isBadDeviceId = BODeviceDetection.isBadDeviceId("")
        Assert.assertTrue(isBadDeviceId)
    }

}