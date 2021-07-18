package com.analytics.blotout.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Event(

        @SerializedName("sdkv")
        @Expose
        var sdkv: String? = null,

        @SerializedName("mid")
        @Expose
        var mid: String? = null,

        @SerializedName("userid")
        @Expose
        var userid: String? = null,

        @SerializedName("evn")
        @Expose
        var evn: String? = null,

        @SerializedName("evcs")
        @Expose
        var evcs: Int? = null,

        @SerializedName("type")
        @Expose
        var type: String? = null,

        @SerializedName("scrn")
        @Expose
        var scrn: String? = null,

        @SerializedName("evt")
        @Expose
        var evt: Long? = null,

        @SerializedName("session_id")
        @Expose
        var sessionId: String? = null,

        @SerializedName("screen")
        @Expose
        var screen: Screen? = null,

        @SerializedName("additionalData")
        @Expose
        var additionalData: HashMap<String, Any>? = null


)