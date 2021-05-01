package com.blotout.util

import android.text.TextUtils
import android.util.Log
import com.blotout.DependencyInjectorImpl
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class CommonUtils {

    private val TAG = "BOCommonUtils"

    fun getAsciiCustomIntSum(input: String, isCaseSenstive: Boolean): Int {
        var input = input
        input = if (isCaseSenstive) input else input.toLowerCase()
        var sum = 0
        if (isPureAscii(input)) {
            for (index in 0 until input.length) {
                val c = input[index]
                sum += c.toInt()
            }
        }
        return sum
    }

    fun isPureAscii(input: String?): Boolean {
        try {
            return Charset.forName("US-ASCII").newEncoder().canEncode(input)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return false
    }

    fun getStringMD5CustomIntSumWithCharIndexAdded(event: String, caseSensitive: Boolean): Int {
        var event = event
        var sum = 0
        try {
            event = if (caseSensitive) event else event.toLowerCase()
            val stringToHash = event
            val messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.update(stringToHash.toByteArray())
            val digiest = messageDigest.digest()
            val md5String: String = convertToHex(digiest)!!
            for (index in 0 until md5String.length) {
                val c = md5String[index]
                if (Character.isDigit(c)) {
                    sum = sum + Character.getNumericValue(c)
                } else {
                    sum = sum + c.toInt() + index
                }
            }
        } catch (e: NoSuchAlgorithmException) {
        }
        return sum
    }

    fun convertToHex(data: ByteArray): String? {
        val buf = StringBuffer()
        for (i in data.indices) {
            var halfbyte: Int = data[i] shl 4 and 0x0F
            var two_halfs = 0
            do {
                if (0 <= halfbyte && halfbyte <= 9) buf.append(('0'.toInt() + halfbyte).toChar()) else buf.append(('a'.toInt() + (halfbyte - 10)).toChar())
                halfbyte = data[i] and 0x0F
            } while (two_halfs++ < 1)
        }
        return buf.toString()
    }

    infix fun Byte.shl(that: Int): Int = this.toInt().shl(that)
    private infix fun Byte.and(that: Int): Int = this.toInt().and(that)


    fun getUserID(): String? {
        //user id generation update =
        //Epoc 13 Digit Time at start +
        // Client SDK Token + UUID generate once +
        // 10 digit random number+ 10 digit random number +
        // Epoc 13 Digit time at the end = Input for SHA 512 or UUID function in case it takes
        var deviceId = ""
        try {
            deviceId = DependencyInjectorImpl.getInstance().getSecureStorageService().fetchString(Constant.BO_ANALYTICS_USER_UNIQUE_KEY)
            if (deviceId != null && deviceId.length > 0) {
                return deviceId
            } else {

                val stringBuilder = StringBuilder()
                stringBuilder.append(DateTimeUtils().get13DigitNumberObjTimeStamp())
                stringBuilder.append(DependencyInjectorImpl.getInstance().getSecureStorageService().fetchString(Constant.SDK_KEY))
                stringBuilder.append(UUID.nameUUIDFromBytes(getUUID()!!.toByteArray()).toString())
                stringBuilder.append(generateNumber())
                stringBuilder.append(generateNumber())
                stringBuilder.append(DateTimeUtils().get13DigitNumberObjTimeStamp())
                val guidString: String = convertTo64CharUUID(EncryptionUtils().sha256(stringBuilder.toString()))!!
                deviceId = guidString
                        ?: UUID.nameUUIDFromBytes(stringBuilder.toString().toByteArray()).toString()
                //check for if SHA256 conversion failed
                deviceId = deviceId ?: getUUID()!!
                DependencyInjectorImpl.getInstance().getSecureStorageService().storeString(Constant.BO_ANALYTICS_USER_UNIQUE_KEY, deviceId)
            }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, e.toString())
        }
        return deviceId
    }

    fun generateNumber(): Long {
        return (Math.random() * 100000 + 3333300000L).toLong()
    }

    fun convertTo64CharUUID(stringToConvert: String?): String? {
        try {
            if (stringToConvert != null && stringToConvert.length > 0) {
                val str: String = stringToConvert
                val lengthsOfPart = ArrayList(Arrays.asList(16, 8, 8, 8, 24))
                val parts = ArrayList<String?>()
                var range = 0
                for (i in lengthsOfPart.indices) {
                    val stringOfRange = str.substring(range, range + lengthsOfPart[i])
                    parts.add(stringOfRange)
                    range += lengthsOfPart[i]
                }
                return TextUtils.join("-", parts)
            }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, e.toString())
        }
        return stringToConvert
    }

    fun getUUID(): String? {
        return UUID.randomUUID().toString()
    }

    fun getUserBirthTimeStamp(): Long {
        var userCreatedTimestamp: Long = 0
        try {
            userCreatedTimestamp =
                DependencyInjectorImpl.getInstance().getSecureStorageService().fetchLong(Constant.BO_ANALYTICS_USER_BIRTH_TIME_STAMP)

            if (userCreatedTimestamp == 0L) {
                userCreatedTimestamp = DateTimeUtils().get13DigitNumberObjTimeStamp()
                DependencyInjectorImpl.getInstance().getSecureStorageService().storeLong(Constant.BO_ANALYTICS_USER_BIRTH_TIME_STAMP,userCreatedTimestamp)
            }
        } catch (e: java.lang.Exception) {

        }
        return userCreatedTimestamp
    }


}