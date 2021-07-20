package com.blotout.utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blotout.Controllers.BOSDKManifestController;
import com.blotout.constants.BOCommonConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class BODateTimeUtils {

    private static final String TAG = "BODateTimeUtils";

    /**
     * Gets current date
     *
     * @return: current date object
     */
    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * Gets formatted current date
     *
     * @return: date in string
     */
    @Nullable
    public static String getFormattedCurrentDateString(String dateFormatter) {
        String formattedDate = null;

        try {
            SimpleDateFormat df = new SimpleDateFormat(dateFormatter,
                    Locale.getDefault());
            formattedDate = df.format(getCurrentDate());
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return formattedDate;
    }

    @Nullable
    public static Date getFormattedCurrentDate(String formatter) {
        Date formattedDate = null;
        try {
            String currentDateString = getFormattedCurrentDateString(formatter);
            formattedDate = getDateFromString(currentDateString, formatter);
            return formattedDate;
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return formattedDate;
    }

    /**
     * Gets timestamp in mill seconds value
     *
     * @return current time in milli seconds
     */
    public static long getCurrentTimeStampMilliSeconds() {
        try {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime().getTime();
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return 0;
    }

    /**
     * @param : date string
     * @return: time in milli seconds
     */
    public static long getTimeInMilliSeconds(String dateString, String dateFormatter) {
        try {
        Date formattedDate = getDateFromString(dateString,
                dateFormatter);
        return formattedDate.getTime();
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return 0;
    }

    /**
     * Returns date object based on date interval supplied
     */
    @NonNull
    public static Date getDateWithTimeInterval(long timeinterval, String dateFormat) {
        try {
        Date formattedDate = new Date(timeinterval);
        return formattedDate;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * Returns date object based on date string supplied
     */
    @Nullable
    public static Date getDateFromString(@Nullable String dateString, String dateFormat) {

       if(dateString == null) {
           return null;
       }
        Date formattedDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        try {
            formattedDate = format.parse(dateString);
        } catch (ParseException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return formattedDate;
    }

    /**
     * Returns date object based on date string supplied
     * dateString in miliseconds
     */
    @Nullable
    public static String getDateStringFromString(@Nullable String dateString, String dateFormat) {

        if(dateString == null) {
            return null;
        }
        String formattedDateStr = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
            formattedDateStr = format.format(new Date(Long.parseLong(dateString)));
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return formattedDateStr;
    }

    /**
     * Returns date string from date object
     *
     * @return: date string in specified format
     */
    @Nullable
    public static String getStringFromDate(@Nullable Date date, String dateFormat) {

        if(date == null)
            return null;

        String dateTime = null;
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        try {
            dateTime = format.format(date);
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return dateTime;
    }

    /**
     * Gets diff of a suppied date from current date
     *
     * @param date: input date
     * @returnL: diff in  milliseconds
     */
    public static long getDateDiffFromCurrentDate(@NonNull Date date) {
        if(date == null)
            return 0;

        return (date.getTime() - Calendar.getInstance().getTimeInMillis());
    }

    @NonNull
    public static String convertMillis(long milis) {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milis),
                TimeUnit.MILLISECONDS.toMinutes(milis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milis)),
                TimeUnit.MILLISECONDS.toSeconds(milis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milis)));
    }

    /**
     * Returns timestamp in seconds
     */
    public static long getTimestampInSec(@Nullable Date date) {
        Date dateL = date != null ? date : new Date();
        long timeStamp = dateL.getTime() / 1000;
        return timeStamp;
    }

    public static long getTimestampInSec() {
        Date dateL = new Date();
        long timeStamp = dateL.getTime() / 1000;
        return timeStamp;
    }

    public static long get13DigitNumberObjTimeStampFor(@Nullable Date date) {
        Date dateL = date != null ? date : new Date();
        long timeStamp = dateL.getTime();
        return timeStamp;
    }

    public static long get13DigitNumberObjTimeStamp() {
        Date dateL = new Date();
        long timeStamp = dateL.getTime();
        return timeStamp;
    }

    public static boolean isDayMonthAndYearSameOfDate(@Nullable Date date1, @Nullable Date date2) {
        try {
            if (date1 != null && date2 != null) {

                if (date1.compareTo(date2) == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    public static boolean isDayMonthAndYearSameOfDate(@Nullable Date date, @Nullable String dateStr,
            @Nullable String format) {
        try {
            if (date != null && dateStr != null && format != null && !dateStr.equals("")) {
                SimpleDateFormat sdfo = new SimpleDateFormat(format);
                String date2 = getStringFromDate(date, format);
                Date d1, d2;
                // Get the two dates to be compared
                d1 = sdfo.parse(date2);
                d2 = sdfo.parse(dateStr);

                if (d1.compareTo(d2) == 0) {
                    return true;
                }
            }
        } catch (ParseException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    public static boolean isMonthAndYearSameOfDate(@Nullable Date date, @Nullable String dateStr,
                                                      @Nullable String format) {
        try {
            if (date != null && dateStr != null && format != null && !dateStr.equals("")) {
                SimpleDateFormat sdfo = new SimpleDateFormat(format);
                String date2 = getStringFromDate(date, format);
                Date d1, d2;
                // Get the two dates to be compared
                d1 = sdfo.parse(date2);
                d2 = sdfo.parse(dateStr);

                if (BODateTimeUtils.getMonthFromDate(d1) == BODateTimeUtils.getMonthFromDate(d2) && BODateTimeUtils.getYearFromDate(d1) == BODateTimeUtils.getYearFromDate(d2)) {
                    return true;
                }
            }
        } catch (ParseException e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return false;
    }

    public static boolean isMonthSameOfDate( @Nullable Date d1,  @Nullable Date d2) {
        if(d1 == null || d2 == null)
            return false;

        if (BODateTimeUtils.getMonthFromDate(d1) == BODateTimeUtils.getMonthFromDate(d2)) {
            return true;
        }
        return false;
    }

    public static boolean isWeekSameOfDate(@Nullable Date d1, @Nullable Date d2) {
        if(d1 == null || d2 == null)
            return false;

        if (BODateTimeUtils.getWeekFromDate(d1) == BODateTimeUtils.getWeekFromDate(d2)) {
            return true;
        }
        return false;
    }

    public static boolean isDateBetween(@Nullable Date testDate, @Nullable Date d1, @Nullable Date d2) {
        if(testDate == null || d1 == null || d2 == null)
            return false;
        return testDate.compareTo(d1) > 0 && testDate.compareTo(d2) < 0;
    }

    public static boolean isDateLessThan(@Nullable Date d1, @Nullable Date d2) {
        if(d1 == null || d2 == null)
            return false;
        return d1.compareTo(d2) < 0;
    }

    public static boolean isDateLessThanEqualTo( @Nullable Date d1, @Nullable Date d2) {
        if(d1 == null || d2 == null)
            return false;

        return d1.compareTo(d2) <= 0;
    }

    public static boolean isDateGreaterThan(@Nullable Date d1, @Nullable Date d2) {
        if(d1 == null || d2 == null)
            return false;

        return d1.compareTo(d2) > 0;
    }

    public static boolean isDateGreaterThanEqualTo(@Nullable Date d1, @Nullable Date d2) {
        if(d1 == null || d2 == null)
            return false;

        return d1.compareTo(d2) >= 0;
    }

    public static boolean equalTo(@Nullable Date d1, @Nullable Date d2) {
        if(d1 == null || d2 == null)
            return false;

        return d1.compareTo(d2) == 0;
    }

    public static long getTimeIntervalSince1970OfDate() {
        long millis = System.currentTimeMillis();
        //Divide millis by 1000 to get the number of seconds.
        long seconds = millis / 1000;
        return seconds;
    }

    public static long getTimeIntervalSinceReferenceDate(@NonNull Date refDate) {
        return refDate.getTime() / 1000;
    }

    public static long getTimeIntervalSinceNowOfDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime().getTime() / 1000;
    }

    /**
     * Gets year from curent date
     */
    public static int getYearFromCurrentDate() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * Gets month of from curent date
     */
    public static int getMonthFromCurrentDate() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;

    }

    /**
     * Gets Day of month from curent date
     */
    public static int getDayOfMonthFromCurrentDate() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Gets Day of week from from curent date
     */
    public static int getDayOfWeekFromCurrentDate() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * Gets day of year from curent date
     */
    public static int getDayOfYearFromCurrentDate() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Gets year from supplied date
     */
    public static int getYearFromDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * Gets month of supplied date
     */
    public static int getMonthFromDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;

    }

    /**
     * Gets month of from time interval
     */
    public static int getMonthFromTimeInterval(long dateInterval) {
        try {
            Date date = new Date(dateInterval);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(Calendar.MONTH) + 1;
        }catch (Exception e) {
                Logger.INSTANCE.e(TAG, e.toString());
        }
        return 0;
    }
    /**
     * Gets month of supplied date
     */
    public static int getWeekFromDate(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(Calendar.WEEK_OF_MONTH) + 1;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return 0;
    }

    /**
     * Gets Day of month supplied date
     */
    public static int getDayOfMonthFromDate(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(Calendar.DAY_OF_MONTH);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return 0;
    }

    /**
     * Gets Day of week from supplied date
     */
    public static int getDayOfWeekFromDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Gets day of year supplied date
     */
    public static int getDayOfYearFromDate(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(Calendar.DAY_OF_YEAR);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return 0;
    }

    /**
     * Returns abbreviated (3 letters) day of the week.
     *
     * @param date      :date      string
     * @param formatter :date formatter
     */
    @Nullable
    public static String getDayOfWeekAbbreviatedFromDateString(String date, String formatter) {
        try {
        Date dateDT = getDateFromString(date, formatter);

        if (dateDT == null) {
            return null;
        }
        // Get current date
        Calendar c = Calendar.getInstance();
        // it is very important to
        // set the date of
        // the calendar.
        c.setTime(dateDT);
        int day = c.get(Calendar.DAY_OF_WEEK);
        return getDayOfWeekString(day);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * Gets the name of the month from the given date.
     *
     * @return full name of month
     */
    @Nullable
    public static String getMonthFromDateString(String date, String formatter) {
        try {
        Date dateDT = getDateFromString(date, formatter);

        if (dateDT == null) {
            return null;
        }

        // Get current date
        Calendar c = Calendar.getInstance();
        // it is very important to
        // set the date of
        // the calendar.
        c.setTime(dateDT);
        int day = c.get(Calendar.MONTH);

        return getMonthOfYearString(day);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * Gets abbreviated name of the month from the given date.
     */
    @Nullable
    public static String getMonthAbbreviated(String date, String formatter) {
        Date dateDT = getDateFromString(date, formatter);

        if (dateDT == null) {
            return null;
        }

        // Get current date
        Calendar c = Calendar.getInstance();
        // it is very important to
        // set the date of
        // the calendar.
        c.setTime(dateDT);
        int day = c.get(Calendar.MONTH);
        return getAbbreviatedMonth(day);

    }

    /**
     * Gets AbbreviatedMonth name
     */
    @Nullable
    public static String getAbbreviatedMonth(int monthVal) {
        String dayStr = null;

        switch (monthVal) {

            case Calendar.JANUARY:
                dayStr = "Jan";
                break;

            case Calendar.FEBRUARY:
                dayStr = "Feb";
                break;

            case Calendar.MARCH:
                dayStr = "Mar";
                break;

            case Calendar.APRIL:
                dayStr = "Apr";
                break;

            case Calendar.MAY:
                dayStr = "May";
                break;

            case Calendar.JUNE:
                dayStr = "Jun";
                break;

            case Calendar.JULY:
                dayStr = "Jul";
                break;

            case Calendar.AUGUST:
                dayStr = "Aug";
                break;

            case Calendar.SEPTEMBER:
                dayStr = "Sep";
                break;

            case Calendar.OCTOBER:
                dayStr = "Oct";
                break;

            case Calendar.NOVEMBER:
                dayStr = "Nov";
                break;

            case Calendar.DECEMBER:
                dayStr = "Dec";
                break;
        }

        return dayStr;
    }

    /**
     * Gets day of week
     */
    @Nullable
    private static String getDayOfWeekString(int dayIntValue) {
        String dayStr = null;

        switch (dayIntValue) {

            case Calendar.SUNDAY:
                dayStr = "Sun";
                break;

            case Calendar.MONDAY:
                dayStr = "Mon";
                break;

            case Calendar.TUESDAY:
                dayStr = "Tue";
                break;

            case Calendar.WEDNESDAY:
                dayStr = "Wed";
                break;

            case Calendar.THURSDAY:
                dayStr = "Thu";
                break;

            case Calendar.FRIDAY:
                dayStr = "Fri";
                break;

            case Calendar.SATURDAY:
                dayStr = "Sat";
                break;
        }
        return dayStr;
    }

    /**
     * Gets full name of month
     *
     * @param monthIntVal: integer value for month
     * @return: full name of month
     */
    @Nullable
    private static String getMonthOfYearString(int monthIntVal) {
        String dayStr = null;

        switch (monthIntVal) {

            case Calendar.JANUARY:
                dayStr = "January";
                break;

            case Calendar.FEBRUARY:
                dayStr = "February";
                break;

            case Calendar.MARCH:
                dayStr = "March";
                break;

            case Calendar.APRIL:
                dayStr = "April";
                break;

            case Calendar.MAY:
                dayStr = "May";
                break;

            case Calendar.JUNE:
                dayStr = "June";
                break;

            case Calendar.JULY:
                dayStr = "July";
                break;

            case Calendar.AUGUST:
                dayStr = "August";
                break;

            case Calendar.SEPTEMBER:
                dayStr = "September";
                break;

            case Calendar.OCTOBER:
                dayStr = "October";
                break;

            case Calendar.NOVEMBER:
                dayStr = "November";
                break;

            case Calendar.DECEMBER:
                dayStr = "December";
                break;
        }
        return dayStr;
    }

    public static boolean isDateGreater(String dateStr1, String dateStr2, String formatter) {

        Date date1 = null;
        Date date2 = null;
        SimpleDateFormat sdf;

        try {
            sdf = new SimpleDateFormat(formatter);
            date1 = sdf.parse(dateStr1);
            date2 = sdf.parse(dateStr2);
            Logger.INSTANCE.d(TAG, "date1 : " + sdf.format(date1));
            Logger.INSTANCE.d(TAG, "date2 : " + sdf.format(date2));

        } catch (ParseException e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return isDateGreaterThan(date1, date2);
    }

    public static boolean AreDatesEqual(String dateStr1, String dateStr2, String formatter) {

        Date date1 = null;
        Date date2 = null;
        SimpleDateFormat sdf;

        try {
            sdf = new SimpleDateFormat(formatter);
            date1 = sdf.parse(dateStr1);
            date2 = sdf.parse(dateStr2);
            Logger.INSTANCE.d(TAG, "date1 : " + sdf.format(date1));
            Logger.INSTANCE.d(TAG, "date2 : " + sdf.format(date2));

        } catch (ParseException e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return equalTo(date1, date2);
    }

    public static boolean isDateWithinRange(@NonNull Date dateInQuestion, Date min, Date max) {
        return dateInQuestion.compareTo(min) * dateInQuestion.compareTo(max) >= 0;
    }

    public static Date getWeekStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        return calendar.getTime();
    }

    public static Date getWeekEndDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }


    public static int weekOfMonth(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Logger.INSTANCE.d(TAG,
                    "Current week of this month = " + calendar.get(Calendar.WEEK_OF_MONTH));
            return calendar.get(Calendar.WEEK_OF_MONTH);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return 0;
    }

    public static int weekOfYear(Date date) {
       try {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Logger.INSTANCE.d(TAG,
                ("Current week of this year = " + calendar.get(Calendar.WEEK_OF_YEAR)));
        return calendar.get(Calendar.WEEK_OF_YEAR);
       }catch (Exception e) {
           Logger.INSTANCE.e(TAG, e.getMessage());
       }
        return 0;
    }

    public static int monthOfYear(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Logger.INSTANCE.d(TAG,
                    ("Current month of this year = " + calendar.get(Calendar.MONTH)));
            return calendar.get(Calendar.MONTH);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return 0;
    }


    public static int weekOfYearForDateInterval(long dateInterval) {
        try {
            Date date = new Date(dateInterval);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Logger.INSTANCE.d(TAG,
                    ("Current week of this year = " + calendar.get(Calendar.WEEK_OF_YEAR)));
            return calendar.get(Calendar.WEEK_OF_YEAR);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return 0;
    }

    public static int weekOfYearForDateString(String dateStr) {
        try {
            Date date = getDateFromString(dateStr, BOCommonConstants.BO_DATE_FORMAT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Logger.INSTANCE.d(TAG,
                    ("Current week of this year = " + calendar.get(Calendar.WEEK_OF_YEAR)));
            return calendar.get(Calendar.WEEK_OF_YEAR);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return 0;
    }

    public static int monthOfYearForDateString(String dateStr) {
        try {
            Date date = getDateFromString(dateStr, BOCommonConstants.BO_DATE_FORMAT);;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Logger.INSTANCE.d(TAG,
                    ("Current week of this year = " + calendar.get(Calendar.MONTH)));
            return calendar.get(Calendar.MONTH);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return 0;
    }

    public static int monthOfYearForDateInterval(long dateInterval) {
        try {
            Date date = new Date(dateInterval);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Logger.INSTANCE.d(TAG,
                    ("Current week of this year = " + calendar.get(Calendar.MONTH)));
            return calendar.get(Calendar.MONTH);
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return 0;
    }

    public static Date monthStartDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        while (calendar.get(Calendar.DATE) > 1) {
            calendar.add(Calendar.DATE, -1);// Substract 1 day until first day of month.
        }
        long firstDayOfMonthTimestamp = calendar.getTimeInMillis();
        calendar.setTimeInMillis(firstDayOfMonthTimestamp);
        return calendar.getTime();
    }

    public static Date monthEndDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        return cal.getTime();
    }

    public static int monthLength(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public static Date getPreviousDayDateFrom(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @Nullable
    public static String getPreviousDayDateInFormat(Date date, String format) {
        if (date == null) {
            return null;
        }
    try {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return getStringFromDate(cal.getTime(),format);
    }catch (Exception e) {
        Logger.INSTANCE.e(TAG, e.toString());
    }
        return null;
    }

    public static long secondsIntervalbetween(@NonNull Date startDate, @NonNull Date endDate) {

        if (startDate == null || endDate == null) {
            return 0;
        }

        long diffInMs = endDate.getTime() - startDate.getTime();
        return TimeUnit.MILLISECONDS.toSeconds(diffInMs);
    }

    public static long milliSecondsIntervalBetween(@NonNull Date startDate, @NonNull Date endDate) {
        try {
            long diffInMs = endDate.getTime() - startDate.getTime();
            return diffInMs;
        }catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return 0;
    }

    public static Date dateByAddingTimeInterval(Date fileModificationDate, long expiryInterval){
        try {
            return new Date(fileModificationDate.getTime() + timeIntervalToMilliseconds(expiryInterval));
        } catch (Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }
        return fileModificationDate;
    }

    public static long timeIntervalToMilliseconds(double seconds) {
        return (long) seconds * 1000;
    }

    /**
     * This method used to roundoff timestamp to protect behavioural timestamp analysis
     * @param timeStamp
     * @return
     */
    public static long roundOffTimeStamp(long timeStamp) {
        try {

            if(timeStamp <= 0)
                return 0;

            if(BOSDKManifestController.getInstance().sdkMapUserId)
                return timeStamp;

            Date date = new Date(timeStamp);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if (calendar.get(Calendar.MINUTE) <= 58) {
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
            } else {
                calendar.add(Calendar.HOUR, 1);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
            }

            long roundOffTimeStamp = calendar.getTimeInMillis();
            calendar.setTimeInMillis(roundOffTimeStamp);

            return roundOffTimeStamp;

        } catch(Exception e) {
            Logger.INSTANCE.e(TAG, e.getMessage());
        }

        return 0;
    }
}
