package com.blotout.io.utility

import android.app.Application
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.blotout.utilities.BOEncryptionManager
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by ankuradhikari on 26,November,2020
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [18], manifest = Config.NONE)
class BOEncryptionManagerTest {
    lateinit var context: Application
    lateinit var encryptionManager:BOEncryptionManager;

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
        encryptionManager =  BOEncryptionManager(BOEncryptionManager.ALGORITHM_AES_CBC_PKCS5Padding,"12345678",256)

    }

    @Test
    fun initObject() {
        var encryptionManager256 =  BOEncryptionManager(BOEncryptionManager.ALGORITHM_AES_CBC_PKCS5Padding,"12345678",256)
        Assert.assertNotNull(encryptionManager256)

        var encryptionManager512 =  BOEncryptionManager(BOEncryptionManager.ALGORITHM_AES_CBC_PKCS5Padding,"12345678",256)
        Assert.assertNotNull(encryptionManager512)
    }

    @Test
    fun encryptString() {
        var encryptedString = encryptionManager.encrypt("Blotout Inc")
        Assert.assertNotNull(encryptedString)
    }

    @Test
    fun encryptLong() {
        var encryptedLong = encryptionManager.encrypt(6555555555222)
        Assert.assertNotNull(encryptedLong)
    }

    @Test
    fun encryptInteger() {
        var encryptedInteger = encryptionManager.encrypt(1004)
        Assert.assertNotNull(encryptedInteger)
    }

    @Test
    fun encryptDouble() {
        var encryptedDouble = encryptionManager.encrypt(1004.345)
        Assert.assertNotNull(encryptedDouble)
    }

    @Test
    fun encryptFloat() {
        var encryptedFloat = encryptionManager.encrypt(1004.12)
        Assert.assertNotNull(encryptedFloat)
    }

    @Test
    fun encryptBoolean() {
        var encryptedBoolean = encryptionManager.encrypt(true)
        Assert.assertNotNull(encryptedBoolean)
    }

    @Test
    fun decryptString() {
        var encryptedString = encryptionManager.encrypt("Blotout Inc")
        var decryptedString = encryptionManager.decryptString(encryptedString)
        Assert.assertNotNull(decryptedString)
    }

    @Test
    fun decryptLong() {
        var encryptedLong = encryptionManager.encrypt(6555555555222)
        var decryptedLong = encryptionManager.decryptLong(encryptedLong)
        Assert.assertNotNull(decryptedLong)
    }

    @Test
    fun decryptInteger() {
        var encryptedInteger = encryptionManager.encrypt(1004)
        var decryptedInteger = encryptionManager.decryptInteger(encryptedInteger)
        Assert.assertNotNull(decryptedInteger)
    }

    @Test
    fun decryptDouble() {
        var encryptedDouble = encryptionManager.encrypt(1004.345)
        var decryptedDouble = encryptionManager.decryptDouble(encryptedDouble)
        Assert.assertNotNull(decryptedDouble)
    }

    @Test
    fun decryptFloat() {
        var encryptedFloat = encryptionManager.encrypt(1004.12)
        var decryptedFloat = encryptionManager.decryptFloat(encryptedFloat)
        Assert.assertNotNull(decryptedFloat)
    }

    @Test
    fun decryptBoolean() {
        var encryptedBoolean = encryptionManager.encrypt(true)
        var decryptedBoolean = encryptionManager.decryptBoolean(encryptedBoolean)
        Assert.assertNotNull(decryptedBoolean)
    }

    @Test
    fun toHex() {
        var toHex = encryptionManager.toHex("Blotout Inc")
        Assert.assertNotNull(toHex)
    }

    @Test
    fun fromHex() {
        var toHex = encryptionManager.toHex("Blotout Inc")
        var fromHex = encryptionManager.fromHex(toHex)
        Assert.assertNotNull(fromHex)
    }

    @Test
    fun encryptWithBase64() {
        var encryptWithBase64 = encryptionManager.encryptWithBase64("Blotout Inc")
        Assert.assertNotNull(encryptWithBase64)
    }

    @Test
    fun decryptWithBase64() {
        var encryptWithBase64 = encryptionManager.encryptWithBase64("Blotout Inc")
        var decryptWithBase64 = encryptionManager.decryptWithBase64(encryptWithBase64)
        Assert.assertNotNull(decryptWithBase64)
    }

    @Test
    fun getMd5() {
        var md5String = BOEncryptionManager.md5("Blotout Inc")
        Assert.assertNotNull(md5String)
    }

    @Test
    fun getSha1() {
        var sha1String = BOEncryptionManager.sha1("Blotout Inc")
        Assert.assertNotNull(sha1String)

        var sha256String = BOEncryptionManager.sha256("Blotout Inc")
        Assert.assertNotNull(sha256String)

        var sha512String = BOEncryptionManager.sha512("Blotout Inc")
        Assert.assertNotNull(sha512String)
    }

    @Test
    fun encryptRSAText() {
        val publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCtfDKGDkF6Da5wvyA53G9naA3POeSrKSsi/AIAISLhKDCBXzXe7MQsoW7IAEqFuDh2578BdzuVFDO/b5q8af4u+GSBIarGM75/biUIV6PcrteywsbgOVsrs5NYgHRoojG283V/f2+aRDN0p30YrlI0msT4epnNbkczIFCoXqK2YQIDAQAB"
        val privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIfLCN3R8nWhzH90WSll9RZBkzLHlPBfxYkrYvy9LJCktETc2hp24DKYxbvA/YZY7bL73koXXvlZ+MzL9043wWBdfDoUYHAs1hm+uG4gC3v1ld/3BT9BW3oAWAsvE/M2UakIG4jt7CUPkSPmr7RhwCHk8W3WIQJrUCwHEXt3CRBpAgMBAAECgYB/6tsnWia0eP5S8h8ryEff91Xad/ftl8pVNPHCc9IPT5ghZopuc50vDIr4Gwy4Cf2gpXL5CJsG+aJOCbWlkRAvf44D3FZ6MUeEkCPQPvWEbN3WEm5j215vOjjLe6a+ltrD/Wn5dvB2Rw7GiadGsBhQdfzAjg62GtXfhXEIQrwtIQJBAL2Y4biTomyckygumozBKUBqpu+ocrFvgbrxPIyy/ch7JH7atKcZhU2Dme29u/diPIm+DRoaxZHMwVrOsIEZa18CQQC3WiAWKLvOFYqafAZRv61Lb3tO7rQKKBpFb0ajO2Whv7c5eri9JQnhz+mcIoB2cjIgdkL/1YWPQSjPSjM1nGE3AkBWyVdxdOrhip51wzdtfLHUUb2OcG3nuKIKn4hW40v17CBdjOEqqCzzuWzJfZM//xC1AHDg5SZQwggz9+6leaxDAkAiMMc8+pmS44d2KVyQT/sDzyUiYfbm0fWcLxTwCXp5QCPtrwnWE5sVJc09VV25OLBKR2NVC78dKJxwkzibba7fAkB5OvJKLKy0CbqUMCkwW9jDbEboW2a3fbM1dsC2JhMkkL2WPoJ7jLP5uSpVFWbS5uChnceEaS2Tpj5L6xB3ALe8"
        var encryptedRSA = BOEncryptionManager.encryptText("Blotout Inc".toByteArray(), publicKey)
        Assert.assertNotNull(encryptedRSA)
    }


}