package com.blotout.io.utility

import android.app.Application
import com.blotout.analytics.BOSharedManager
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.blotout.utilities.BOServerDataConverter
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowEnvironment
import java.io.File
import java.util.ArrayList

/**
 * Created by ankuradhikari on 27,November,2020
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [18], manifest = Config.NONE)
class BOServerDataConverterTest {

    lateinit var context: Application

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
    }

    @Test
    fun initObject() {

    }
}