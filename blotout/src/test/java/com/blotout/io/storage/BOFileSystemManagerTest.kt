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
 * Created by ankuradhikari on 27,November,2020
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [23], manifest = Config.NONE)
class BOFileSystemManagerTest {
    lateinit var context: Application
    lateinit var fileSystemManager: BOFileSystemManager
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
        BOSharedPreferenceImpl.getInstance(context.applicationContext)
        fileSystemManager = BOFileSystemManager.getInstance(context.applicationContext)
    }

    @Test
    fun testSDCardAvailable() {
        var isSDCardAvailable = fileSystemManager.isSDCardAvailable;
        Assert.assertFalse(isSDCardAvailable)
    }

    @Test
    fun testGetAvailableInternalMemory(){
        var availableMemory = fileSystemManager.availableInternalMemory
        Assert.assertNotNull(availableMemory)
    }

    @Test
    fun testGetAvailableExternalMemory(){
        var availableMemory = fileSystemManager.availableExternalMemory
        Assert.assertNotNull(availableMemory)
    }

    @Test
    fun testGetTotalExternalMemory(){
        var availableMemory = fileSystemManager.totalExternalMemory
        Assert.assertNotNull(availableMemory)
    }

    @Test
    fun testGetTotalInternalMemory(){
        var availableMemory = fileSystemManager.totalInternalMemory
        Assert.assertNotNull(availableMemory)
    }

    @Test
    fun testCreateRootDirectory(){
        fileSystemManager.createRootDirectory()
    }

    @Test
    fun testCreateDirectoryIfRequiredAndReturnPath() {
        var path = fileSystemManager.createDirectoryIfRequiredAndReturnPath("test.txt")
        Assert.assertNotNull(path)
    }

    @Test
    fun testGetBOSDKRootDirectory() {
        fileSystemManager.createRootDirectory()
        var path = fileSystemManager.bosdkRootDirectory
        Assert.assertNotNull(path)
    }

}