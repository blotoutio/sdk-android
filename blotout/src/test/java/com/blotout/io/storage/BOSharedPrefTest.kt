package com.blotout.io.storage

import android.app.Application
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
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
 * Created by ankuradhikari on 29,November,2020
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [23], manifest = Config.NONE)
class BOSharedPrefTest {
    lateinit var context: Application
    lateinit var sharedPref: BOSharedPreferenceImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
        BOSharedPreferenceImpl.getInstance(context.applicationContext)
        sharedPref = BOSharedPreferenceImpl.getInstance(context.applicationContext)
    }

    @Test
    fun testSaveString() {
        sharedPref.saveString("key","value")
        var value = sharedPref.getString("key")
        Assert.assertTrue(value == "value")
    }

    @Test
    fun testSaveInteger() {
        sharedPref.saveInteger("key",2)
        var value = sharedPref.getInt("key")
        Assert.assertTrue(value == 2)
    }

    @Test
    fun testSaveBoolean() {
        sharedPref.saveBoolean("key",true)
        var value = sharedPref.getBoolean("key")
        Assert.assertTrue(value == true)
    }

    @Test
    fun testSaveFloat() {
        sharedPref.saveFloat("key",1.0f)
        var value = sharedPref.getFloat("key")
        Assert.assertTrue(value == 1.0f)
    }

    @Test
    fun testSaveDouble() {
        sharedPref.saveDouble("key",4567890123.0)
        var value = sharedPref.getDouble("key")
        Assert.assertTrue(value == 4567890123.0)
    }

    @Test
    fun testSaveLong() {
        sharedPref.saveLong("key",4567890123)
        var value = sharedPref.getLong("key")
        Assert.assertTrue(value == 4567890123)
    }

    @Test
    fun testRemoveKey() {
        sharedPref.saveString("key","value")
        sharedPref.removeKey("key")
        var value = sharedPref.getString("key")
        Assert.assertTrue(value == null)
    }

    @Test
    fun testIsNewUserRecorded() {
        var newUser = sharedPref.isNewUserRecorded()
        Assert.assertFalse(newUser)
    }

    @Test
    fun testSaveNewUser() {
        sharedPref.saveNewUserRecorded()
        var newUser = sharedPref.isNewUserRecorded()
        Assert.assertTrue(newUser)
    }

    @Test
    fun testIsSDKLaunched() {
        var newUser = sharedPref.isSDKFirstLaunched()
        Assert.assertFalse(newUser)
    }

    @Test
    fun testSaveSDKLaunched() {
        sharedPref.saveSDKFirstLaunched()
        var newUser = sharedPref.isSDKFirstLaunched()
        Assert.assertTrue(newUser)
    }

    @Test
    fun testSavelist() {
        val list = listOf('1', '2', '3')
        sharedPref.saveListInPref("key", list)
    }

    @Test
    fun testGetSavedList() {
        val list = listOf('1', '2', '3')
        sharedPref.saveListInPref("key", list)
        var getList = sharedPref.getSavedListFromPref<String>("key")
        Assert.assertNotNull(getList)
    }
}