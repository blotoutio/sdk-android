package com.blotout.io.utility

import android.app.Application
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.blotout.referrerapi.BOInstallReferrerHelper
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
 * Created by ankuradhikari on 16,December,2020
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [18], manifest = Config.NONE, assetDir = "/src/test/assets")
class BOInstallReferrarTest {
    lateinit var context: Application

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
        BOSharedPreferenceImpl.getInstance(context.applicationContext)
    }

    @Test
    fun test_init() {

    }

}