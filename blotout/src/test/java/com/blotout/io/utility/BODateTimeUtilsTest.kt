package com.blotout.io.utility

import android.app.Application
import com.blotout.constants.BOCommonConstants
import com.blotout.io.BOTestUtils
import com.blotout.io.BuildConfig
import com.blotout.storage.BOSharedPreferenceImpl
import com.blotout.utilities.BODateTimeUtils
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by ankuradhikari on 16,November,2020
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [18], manifest = Config.NONE)
class BODateTimeUtilsTest {
    lateinit var context: Application

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = BOTestUtils.mockApplication()
        whenever(context.applicationContext).thenReturn(context)
    }

    @Test
    fun testContext() {
        val pref = BOSharedPreferenceImpl.getInstance(context.applicationContext)
        pref.saveString("key","value")
        val value = pref.getString("key")
        assertThat("value").isEqualTo(value);
    }

    @Test
    fun testCurrentDateGeneration() {
        val date = BODateTimeUtils.getCurrentDate()
        Assert.assertNotNull(date)
    }

    @Test
    fun testGetFormattedCurrentDateString() {
        val date = BODateTimeUtils.getFormattedCurrentDate(BOCommonConstants.BO_DATE_FORMAT)
        Assert.assertNotNull(date)
    }

    @Test
    fun testGetFormattedCurrentDate() {
        val date = BODateTimeUtils.getFormattedCurrentDate(BOCommonConstants.BO_DATE_FORMAT)
        Assert.assertNotNull(date)
    }


    @Test
    fun testGetCurrentTimeStampMilliSeconds() {
        val date = BODateTimeUtils.getCurrentTimeStampMilliSeconds()
        Assert.assertNotNull(date)
    }

    @Test
    fun testGetTimeInMilliSeconds() {
        val date = BODateTimeUtils.getTimeInMilliSeconds("2020-11-1",BOCommonConstants.BO_DATE_FORMAT)
        Assert.assertNotNull(date)
    }

    @Test
    fun testGetDateStringFromString() {
        val date = BODateTimeUtils.getDateStringFromString(""+BODateTimeUtils.getCurrentTimeStampMilliSeconds(),BOCommonConstants.BO_DATE_FORMAT)
        Assert.assertNotNull(date)
    }

    @Test
    fun testGetStringFromDate() {
        val date = BODateTimeUtils.getStringFromDate(BODateTimeUtils.getCurrentDate(),BOCommonConstants.BO_DATE_FORMAT)
        Assert.assertNotNull(date)
    }

    @Test
    fun testGetDateDiffFromCurrentDate() {
        val date = BODateTimeUtils.getDateDiffFromCurrentDate(BODateTimeUtils.getCurrentDate())
        Assert.assertNotNull(date)
    }

    @Test
    fun testGetTimestampInSec() {
        val date = BODateTimeUtils.getTimestampInSec(BODateTimeUtils.getCurrentDate())
        Assert.assertNotNull(date)
    }

    @Test
    fun testGetCurrentTimestampInSec() {
        val date = BODateTimeUtils.getTimestampInSec()
        Assert.assertNotNull(date)
    }

    @Test
    fun testGet13DigitNumberObjTimeStampFor() {
        val date = BODateTimeUtils.get13DigitNumberObjTimeStampFor(BODateTimeUtils.getCurrentDate())
        Assert.assertNotNull(date)
    }

    @Test
    fun testGetCurrent13DigitNumberObjTimeStampFor() {
        val date = BODateTimeUtils.get13DigitNumberObjTimeStamp()
        Assert.assertNotNull(date)
    }


    @Test
    fun testAllDateGeneration() {
        val date = BODateTimeUtils.getCurrentDate()
        val referenceDate = BODateTimeUtils.getDateWithTimeInterval(BODateTimeUtils.get13DigitNumberObjTimeStamp(), BOCommonConstants.BO_DATE_FORMAT)
        Assert.assertNotNull(date)
        val referenceTimeIntervalFromNow = BODateTimeUtils.getTimeIntervalSinceNowOfDate()
        Assert.assertTrue(referenceTimeIntervalFromNow > 0)
        val date1 = BODateTimeUtils.getDateFromString("2020-09-13T12:30:40.200Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date2 = BODateTimeUtils.getDateFromString("2020-09-13T12:30:41.200Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val miliSecondsInterval = BODateTimeUtils.milliSecondsIntervalBetween(date1!!, date2!!)
        Assert.assertTrue(miliSecondsInterval == 1000L)
        val secondsInterval = BODateTimeUtils.secondsIntervalbetween(date1, date2)
        Assert.assertTrue(secondsInterval == 1L)
    }

    @Test
    fun testTimestampLength() {
        val thirteenDigitTimeStampString = "" + BODateTimeUtils.get13DigitNumberObjTimeStamp()
        Assert.assertTrue(thirteenDigitTimeStampString.length == 13)
    }

    @Test
    fun testCalendarDay() {
        val day = BODateTimeUtils.getDayOfWeekAbbreviatedFromDateString("2020-09-13", BOCommonConstants.BO_DATE_FORMAT)
        Assert.assertTrue(day == "Sun")
        val date = BODateTimeUtils.getDateFromString("2020-09-13", BOCommonConstants.BO_DATE_FORMAT)
        val dayFromDate = BODateTimeUtils.getDayOfMonthFromDate(date)
        Assert.assertTrue(dayFromDate == 13)

        val dayFromCurrentDate = BODateTimeUtils.getDayOfMonthFromCurrentDate()
        Assert.assertNotNull(dayFromCurrentDate)

        val dayOfWeekFromCurrentDate = BODateTimeUtils.getDayOfWeekFromCurrentDate()
        Assert.assertNotNull(dayOfWeekFromCurrentDate)

        val dayOfYearFromCurrentDate = BODateTimeUtils.getDayOfYearFromCurrentDate()
        Assert.assertNotNull(dayOfYearFromCurrentDate)

        val dayOfWeekFromDate = BODateTimeUtils.getDayOfWeekFromDate(date)
        Assert.assertTrue(dayOfWeekFromDate == 1)

        val dayOfYearFromDate = BODateTimeUtils.getDayOfYearFromDate(date)
        Assert.assertTrue(dayOfYearFromDate == 257)

    }

    @Test
    fun testCalendarMonth() {
        val month = BODateTimeUtils.getMonthAbbreviated("2020-09-13", BOCommonConstants.BO_DATE_FORMAT)
        Assert.assertTrue(month == "Sep")
        val date = BODateTimeUtils.getDateFromString("2020-09-13", BOCommonConstants.BO_DATE_FORMAT)
        val monthFromDate = BODateTimeUtils.getMonthFromDate(date)
        Assert.assertTrue(monthFromDate == 9)

        val currentMonth = BODateTimeUtils.getMonthFromCurrentDate()
        assertThat(12).isEqualTo(currentMonth);

        val getMonthFromDateString = BODateTimeUtils.getMonthFromDateString("2020-09-13", BOCommonConstants.BO_DATE_FORMAT);
        assertThat("September").isEqualTo(getMonthFromDateString);

        val getAbbreviatedMonth = BODateTimeUtils.getAbbreviatedMonth(9)
        assertThat("Oct").isEqualTo(getAbbreviatedMonth)


       // val currentMonthFromTimeInterval = BODateTimeUtils.getMonthFromTimeInterval(BODateTimeUtils.getTimeIntervalSinceNowOfDate())
       // assertThat(11).isEqualTo(currentMonthFromTimeInterval);

        val monthStartDay = BODateTimeUtils.monthStartDay(date)
        Assert.assertNotNull(monthStartDay)

        val monthEndDay = BODateTimeUtils.monthEndDay(date)
        Assert.assertNotNull(monthEndDay)

    }

    @Test
    fun testCalendarYear() {
        val year = BODateTimeUtils.getYearFromCurrentDate()
        Assert.assertTrue(year == 2020)
    }


    @Test
    fun testDateComparison() {
        val date1 = BODateTimeUtils.getDateFromString("2020-09-15T12:30:40.200Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date2 = BODateTimeUtils.getDateFromString("2020-09-13T12:30:41.200Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val isDateGreater = BODateTimeUtils.isDateGreaterThan(date1, date2)
        Assert.assertTrue(isDateGreater)
        val isDateLessThan = BODateTimeUtils.isDateLessThan(date1, date2)
        Assert.assertFalse(isDateLessThan)
        val date3 = BODateTimeUtils.getDateFromString("2020-09-14T12:30:40.200Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val isDateEqual = BODateTimeUtils.equalTo(date1, date3)
        Assert.assertFalse(isDateEqual)
        val date4 = BODateTimeUtils.getDateFromString("2020-09-14T12:30:40.200Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val isDateBetween = BODateTimeUtils.isDateBetween(date4, date1, date2)
        Assert.assertFalse(isDateBetween)

        val isDateGreather = BODateTimeUtils.isDateGreater("2020-09-15T12:30:40.200Z","2020-09-13T12:30:40.200Z","yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        Assert.assertTrue(isDateGreather)

        val areDateEqual = BODateTimeUtils.AreDatesEqual("2020-09-15T12:30:40.200Z","2020-09-15T12:30:40.200Z","yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        Assert.assertTrue(areDateEqual)
    }

    @Test
    fun testDateEquality() {
        val date1 = BODateTimeUtils.getDateFromString("2020-09-15T12:30:40.200Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date2 = BODateTimeUtils.getDateFromString("2020-09-15T12:30:40.200Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val isDaySame = BODateTimeUtils.isDayMonthAndYearSameOfDate(date1, date2)
        Assert.assertTrue(isDaySame)
        val isMonthSame = BODateTimeUtils.isMonthSameOfDate(date1, date2)
        Assert.assertTrue(isMonthSame)

        val isMonthAndYearSame = BODateTimeUtils.isMonthAndYearSameOfDate(date1,"2020-09-15T12:30:40.200Z",BOCommonConstants.BO_TIME_DATE_FORMAT)
        Assert.assertTrue(isMonthAndYearSame)

        val isSameWeek = BODateTimeUtils.isWeekSameOfDate(date1,date2)
        Assert.assertTrue(isSameWeek)
    }

    @Test
    fun testGetTimeIntervalSince1970OfDate() {
        val value = BODateTimeUtils.getTimeIntervalSince1970OfDate()
        Assert.assertNotNull(value)
    }

    @Test
    fun testGetTimeIntervalSinceReferenceDate() {
        val value = BODateTimeUtils.getTimeIntervalSinceReferenceDate(BODateTimeUtils.getCurrentDate())
        Assert.assertNotNull(value)
    }

}