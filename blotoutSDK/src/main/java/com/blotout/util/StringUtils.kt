package com.blotout.util

import android.util.Log
import com.blotout.DependencyInjectorImpl
import java.util.*


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
    return "" + CommonUtils().getAsciiCustomIntSum(this!!, false) + this.codeForDevEvent() + CommonUtils().getStringMD5CustomIntSumWithCharIndexAdded(this, false) + DateTimeUtils().get13DigitNumberObjTimeStamp()
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