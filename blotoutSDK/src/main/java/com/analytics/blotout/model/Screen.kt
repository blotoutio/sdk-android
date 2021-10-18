package com.analytics.blotout.model

import android.content.Context
import com.analytics.blotout.deviceinfo.device.DeviceInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Screen(context: Context) {


    @SerializedName("width")
    @Expose
    private val width: Int = DeviceInfo(context).screenWidth

    @SerializedName("height")
    @Expose
    private val height: Int = DeviceInfo(context).screenHeight


}