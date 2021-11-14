package com.analytics.blotout.util

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.util.*
import kotlin.collections.HashMap


fun Long.sizeFormatter(): String {
    var size = this
        try {
            var suffix: String? = null
            if (size >= 1024) {
                suffix = "KB"
                size /= 1024
                if (size >= 1024) {
                    suffix = "MB"
                    size /= 1024
                        if (size >= 1024) {
                            suffix = "GB"
                            size /= 1024
                             }
                    }
            }
            val stringBuilder = StringBuilder(java.lang.Long.toString(size))
            if (suffix != null) stringBuilder.append(" ").append(suffix)
            return stringBuilder.toString()
        } catch (e: Exception) {
            Log.e("", e.toString())
        }
     return java.lang.Long.toString(size)
    }

fun String.getMessageIDForEvent(): String {
    var base64Encoded = Base64.encodeToString(toByteArray(charset("UTF-8")), Base64.NO_WRAP)
    var trimForExtraChar = base64Encoded.substringBefore("=")
    return trimForExtraChar+"-"+CommonUtils().getUUID()+"-"+DateTimeUtils().get13DigitNumberObjTimeStamp()
}

fun String.codeForDevEvent(): Int  {
    if (this.isEmpty()) {
        return 0
    }

    return generateSubCode(stringToIntSum(this))
}

fun String.getVersion():String{
    return this + Constant.BOSDK_MAJOR_VERSION +"."+Constant.BOSDK_MINOR_VERSION+"."+Constant.BOSDK_PATCH_VERSION
}

fun Context.getVersion():String?{
    return this.packageManager?.getPackageInfo(this.packageName, PackageManager.GET_META_DATA)?.versionName
}

fun stringToIntSum(eventName: String): Int  {
    var eventNameL = eventName.lowercase(Locale.getDefault())
    var sha1String = EncryptionUtils().sha1(eventNameL)
    var sum = 0
    for (index in 0 until sha1String!!.length) {
        val c: Char = sha1String.get(index)
        sum += c.code
    }
    return sum.toInt()

}

fun generateSubCode(eventSum: Int): Int  {
    return Constant.BO_DEV_EVENT_CUSTOM_KEY +(eventSum % 8899)
}

fun String.encrypt(publicKey:String):HashMap<String,Any>{
    var secretKey: String = CommonUtils().getUUID()!!
    secretKey = secretKey.replace("-", "")
    val encryptionManager = EncryptionUtils(EncryptionUtils.ALGORITHM_AES_CBC_PKCS5Padding, secretKey, EncryptionUtils.MODE_256BIT)
    var encryptedData = Base64.encodeToString(encryptionManager.encrypt(this.toByteArray()), Base64.NO_WRAP)
    var encryptedSecretKey = EncryptionUtils().encryptText(secretKey.toByteArray(), publicKey)
    var personalInformation = HashMap<String,Any>()
    personalInformation["data"] = encryptedData
    personalInformation["iv"] = EncryptionUtils.CRYPTO_IVX_STRING
    personalInformation["key "] = encryptedSecretKey!!
    return personalInformation
}