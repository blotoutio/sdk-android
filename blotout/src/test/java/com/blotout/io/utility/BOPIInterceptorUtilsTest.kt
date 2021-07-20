package com.blotout.io.utility

import android.app.Application
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.blotout.utilities.BOPIInterceptorUtils
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
class BOPIInterceptorUtilsTest {

    lateinit var context: Application

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
    }

    @Test
    fun testValidEmail() {
        var validEmail = BOPIInterceptorUtils.isValidEmail("ankur@blotout.io")
        Assert.assertTrue(validEmail)

        var inValidEmail = BOPIInterceptorUtils.isValidEmail("blotout.io")
        Assert.assertFalse(inValidEmail)

        var email:CharSequence  = "ankur@blotout.io"
        var validEmailChar = BOPIInterceptorUtils.isValidEmail(email)
        Assert.assertTrue(validEmailChar)
    }

    @Test
    fun testValidPhoneNumber() {
        var validPhone = BOPIInterceptorUtils.isValidPhoneNumber("123")
        Assert.assertTrue(validPhone)

        var inValidEmail = BOPIInterceptorUtils.isValidPhoneNumber("blotout.io")
        Assert.assertFalse(inValidEmail)
    }

    @Test
    fun testValidCard() {
        var validCreditCardNumber = BOPIInterceptorUtils.validateCreditCardNumber("5241780003212078")
        Assert.assertTrue(validCreditCardNumber)

        var validCreditCardNumberMasterCard = BOPIInterceptorUtils.validate("5241780003212078",1)
        Assert.assertTrue(validCreditCardNumberMasterCard)

        var inValidCreditCardNumber = BOPIInterceptorUtils.validateCreditCardNumber("524178000321207")
        Assert.assertFalse(inValidCreditCardNumber)
    }

    @Test
    fun testValidAadhar() {
        var validAadhar = BOPIInterceptorUtils.validateAadharNumber("701843581533")
        Assert.assertTrue(validAadhar)

        var inValidAadhar = BOPIInterceptorUtils.validateAadharNumber("123456781234")
        Assert.assertFalse(inValidAadhar)
    }

    @Test
    fun testValidSSN() {
        var validSSN = BOPIInterceptorUtils.isValidSSN("078-05-1120")
        Assert.assertTrue(validSSN)

        var inValidSSN = BOPIInterceptorUtils.validateAadharNumber("123456781234")
        Assert.assertFalse(inValidSSN)
    }

    @Test
    fun testValidZipCode() {
        var validZip = BOPIInterceptorUtils.isUSZipCodeValidation("72217")
        Assert.assertTrue(validZip)

        var inValidZip = BOPIInterceptorUtils.isUSZipCodeValidation("99123501")
        Assert.assertFalse(inValidZip)
    }
}