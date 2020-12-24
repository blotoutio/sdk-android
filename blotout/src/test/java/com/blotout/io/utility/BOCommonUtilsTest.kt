package com.blotout.io.utility

import android.app.Application
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.blotout.storage.BOSharedPreferenceImpl
import com.blotout.utilities.BOCommonUtils
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.collections.HashMap


/**
 * Created by ankuradhikari on 18,November,2020
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [18], manifest = Config.NONE, assetDir = "/src/test/assets")
class BOCommonUtilsTest {

    lateinit var context: Application

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
        BOSharedPreferenceImpl.getInstance(context.applicationContext)
    }

    @Test
    fun testEncodeToString() {
        var base64String = BOCommonUtils.encodeToString("testString")
        Assert.assertNotNull(base64String)
    }

    @Test
    fun testDecodeBase64() {
        var base64String = BOCommonUtils.encodeToString("testString")
        var decodeBase64Bytes = BOCommonUtils.decodeBase64(base64String)
        Assert.assertNotNull(decodeBase64Bytes)

        var decodeBytes = BOCommonUtils.decodeBase64(decodeBase64Bytes)
        Assert.assertNotNull(decodeBase64Bytes)
    }

    @Test
    fun testGetHashValue() {
        var hashValue = BOCommonUtils.getHashValue("deviceID")
        Assert.assertNotNull(hashValue)
    }

    @Test
    fun testGetMemoryThreshold() {
        var hashValue = BOCommonUtils.getMemoryThreshold(1024, 2048)
        Assert.assertNotNull(hashValue)
        Assertions.assertThat(50f).isEqualTo(hashValue);
    }

    @Test
    fun testSizeFormatter() {
        var value = BOCommonUtils.sizeFormatter(2048)
        Assert.assertNotNull(value)
        Assertions.assertThat("2 KB").isEqualTo(value);
    }

    @Test
    fun testLoadJSONFromAsset() {
        var value = BOCommonUtils.loadJSONFromAsset(RuntimeEnvironment.application,"app_session_default.json")
        Assert.assertNull(value)
    }

    @Test
    fun testGetJsonStringFromHashMap() {
        var hashMap = HashMap<String,Any>()
        hashMap.put("key","value")
        var value = BOCommonUtils.getJsonStringFromHashMap(hashMap)
        Assert.assertNotNull(value)
        Assertions.assertThat(value).isEqualTo("{\"key\":\"value\"}")
    }

    @Test
    fun testGetHashmapFromJsonString() {
        var value = BOCommonUtils.getHashmapFromJsonString("{\"key\":\"value\"}")
        Assert.assertNotNull(value)
   }

    @Test
    fun testLastPathComponent() {
        var value = BOCommonUtils.lastPathComponent("a/b/c/test.txt")
        Assertions.assertThat(value).isEqualTo("test.txt")
    }

    @Test
    fun testStringByDeletingPathExtension() {
        var value = BOCommonUtils.stringByDeletingPathExtension("test.txt")
        Assertions.assertThat(value).isEqualTo("test")
    }

    @Test
    fun testIsDeviceRooted() {
        var value = BOCommonUtils.isDeviceRooted()
        Assertions.assertThat(value).isEqualTo(false)
    }

    @Test
    fun testIsEmulator() {
        var value =  BOCommonUtils.isEmulator()
        Assertions.assertThat(value).isEqualTo(false)
    }

    @Test
    fun testGetSensorEnabled() {
        var value = BOCommonUtils.getSensorEnabled(context.applicationContext,1)
        Assertions.assertThat(value).isEqualTo(false)
    }

    @Test
    fun testIsMultitaskEnabled() {
        var value = BOCommonUtils.isMultitaskEnabled(context.applicationContext)
        Assertions.assertThat(value).isEqualTo(false)
    }

    @Test
    fun testGetNumberOfActiveCores() {
        var value = BOCommonUtils.getNumberOfActiveCores()
        Assertions.assertThat(value).isEqualTo(8)
    }

    @Test
    fun testGetCPUInfoUsingActivityManager() {
        var value = BOCommonUtils.getCPUInfoUsingActivityManager(context.applicationContext)
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetCPUUsage() {
        var value = BOCommonUtils.getCPUUsage(android.os.Process.myPid())
        Assert.assertNull(value)
    }


    @Test
    fun testISAccessoriesAttached() {
        var value = BOCommonUtils.iSAccessoriesAttached(context.applicationContext)
        Assert.assertFalse(value)
    }

    @Test
    fun testGetNameAccessoriesAttached() {
        var value = BOCommonUtils.getNameAccessoriesAttached(context.applicationContext)
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetWifiIpAddress() {
        var value = BOCommonUtils.getWifiIpAddress(context.applicationContext)
        Assert.assertNull(value)
    }

    @Test
    fun testGetIpAddress() {
        var value = BOCommonUtils.getIPAddress(true)
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetCellularIPAddress() {
        var value = BOCommonUtils.getCellularIPAddress()
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetMacAddress() {
        var value = BOCommonUtils.getMacAddress(context.applicationContext)
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetExternalIpAddress() {
        var value = BOCommonUtils.getExternalIpAddress()
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetNetMask() {
        var value = BOCommonUtils.getNetMask(context.applicationContext)
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetCellBroadcastAddress() {
        var value = BOCommonUtils.getCellBroadcastAddress()
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetWifiBroadcastAddress() {
        var value = BOCommonUtils.getWifiBroadcastAddress(context.applicationContext)
        Assert.assertNull(value)
    }

    @Test
    fun testGetRouterAddress() {
        var value = BOCommonUtils.getRouterAddress(context.applicationContext)
        Assert.assertNull(value)
    }

    @Test
    fun testGetCurrentSsid() {
        var value = BOCommonUtils.getCurrentSsid(context.applicationContext)
        Assert.assertNull(value)
    }

    @Test
    fun testSizeFormatterWithUnit() {
        var value = BOCommonUtils.sizeFormatterWithUnit(2048)
        Assertions.assertThat(value).isEqualTo("2 KB")
    }

    @Test
    fun testSizeFormatterWithoutUnit() {
        var value = BOCommonUtils.sizeFormatterWithoutUnit(2048)
        Assertions.assertThat(value).isEqualTo("2")
    }

    @Test
    fun testGetUsedMemorySize() {
        var value = BOCommonUtils.getUsedMemorySize()
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetUsedMemorySizeNumber() {
        var value = BOCommonUtils.getUsedMemorySizeNumber()
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetFreeMemorySize() {
        var value = BOCommonUtils.getFreeMemorySize()
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetFreeMemorySizeLong() {
        var value = BOCommonUtils.getFreeMemorySizeLong()
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetStringMD5CustomIntSumWithCharIndexAdded() {
        var value = BOCommonUtils.getStringMD5CustomIntSumWithCharIndexAdded("testEvent",true)
        print(value)
        Assert.assertNotNull(value)
    }

    @Test
    fun testConvertToHex() {
        var value = BOCommonUtils.convertToHex("testEvent".toByteArray())
        Assert.assertNotNull(value)
    }

    @Test
    fun testIsNumberChar() {
        var value = BOCommonUtils.isNumberChar('4'.toChar())
        Assert.assertTrue(value)
    }

    @Test
    fun testIntValueForChar() {
        var value = BOCommonUtils.intValueForChar('a'.toChar())
        Assertions.assertThat(value).isEqualTo(11)
    }

    @Test
    fun testGetAsciiCustomIntSum() {
        var value = BOCommonUtils.getAsciiCustomIntSum("testEvent",true)
        Assert.assertNotNull(value)
    }

    @Test
    fun testGenerateMessageIDForEvent() {
        var value = BOCommonUtils.generateMessageIDForEvent("testEvent","11011",1222233)
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetMessageIDForEventWithEventId() {
        var value = BOCommonUtils.getMessageIDForEventWithEventId("testEvent",11011)
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetMessageIDForEvent() {
        var value = BOCommonUtils.getMessageIDForEvent("testEvent")
        Assert.assertNotNull(value)
    }

    @Test
    fun testCodeForCustomCodifiedEvent() {
        var eventNameWithUnderscore = BOCommonUtils.codeForCustomCodifiedEvent("awesome_event")
        Assert.assertEquals(24008L,eventNameWithUnderscore)

        var eventNameWithSpace = BOCommonUtils.codeForCustomCodifiedEvent("some awesome event")
        Assert.assertEquals(24016L,eventNameWithSpace)

        var eventNameWithAscii = BOCommonUtils.codeForCustomCodifiedEvent("目_awesome_event")
        Assert.assertEquals(24049L,eventNameWithAscii)
    }

    @Test
    fun testIsPureAscii() {
        var value = BOCommonUtils.isPureAscii("testEvent")
        Assert.assertTrue(value)
    }

    @Test
    fun testCheckVPN() {
        var value = BOCommonUtils.checkVPN()
        Assert.assertFalse(value)
    }

    @Test
    fun testGetUUID() {
        var value = BOCommonUtils.getUUID()
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetPasspharseKey() {
        var value = BOCommonUtils.getPasspharseKey()
        Assert.assertNotNull(value)
    }

    @Test
    fun testIsEmpty() {
        var value = BOCommonUtils.isEmpty(null)
        Assert.assertTrue(value)

        var value1 = BOCommonUtils.isEmpty("Blotout")
        Assert.assertFalse(value1)
    }




}