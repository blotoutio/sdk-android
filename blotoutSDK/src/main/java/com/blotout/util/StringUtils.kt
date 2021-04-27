package com.blotout.util

import android.util.Base64
import android.util.Log
import com.blotout.DependencyInjectorImpl
import okio.ByteString.Companion.decodeBase64
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

fun String.manifestFileName():String{
    var manifestDirName = DependencyInjectorImpl.getInstance().getFileService().getSDKManifestDirectoryPath()
    return manifestDirName + "/" + this + ".txt";
}



fun String.getMessageIDForEvent(): String? {
    return Base64.encodeToString(this.toByteArray(),Base64.DEFAULT)+"-"+CommonUtils().getUUID()+"-"+DateTimeUtils().get13DigitNumberObjTimeStamp()
}

fun String.codeForDevEvent(): Int  {
    if (this.isNullOrEmpty()) {
        return 0
    }

    return generateSubCode(stringToIntSum(this))
}

fun stringToIntSum(eventName: String): Int  {
    var eventNameL = eventName.toLowerCase()
    var sha1String = EncryptionUtils().sha1(eventNameL)
    var sum = 0
    for (index in 0 until sha1String!!.length) {
        val c: Char = sha1String.get(index)
        sum = sum + c.toInt()
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
    personalInformation.put("data" , encryptedData)
    personalInformation.put("iv", EncryptionUtils.CRYPTO_IVX_STRING)
    personalInformation.put("key ",encryptedSecretKey!!)
    return personalInformation
}