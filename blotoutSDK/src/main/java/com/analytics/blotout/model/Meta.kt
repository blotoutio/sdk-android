package com.analytics.blotout.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Meta {

    @SerializedName("sdkv")
    @Expose
    var sdkv: String? = null

    @SerializedName("tz_offset")
    @Expose
    var tzOffset: Int? = null

    @SerializedName("jbrkn")
    @Expose
    var jbrkn: Boolean? = null

    @SerializedName("referrer")
    @Expose
    var referrer: String? = null

    @SerializedName("vpn")
    @Expose
    var vpn: Boolean? = null

    @SerializedName("dcomp")
    @Expose
    var dcomp: Boolean? = null

    @SerializedName("acomp")
    @Expose
    var acomp: Boolean? = null

    @SerializedName("user_agent")
    @Expose
    var user_agent: String? = null

    @SerializedName("appv")
    @Expose
    var appv: String? = null

    @SerializedName("page_title")
    @Expose
    var page_title: String? = null

    @SerializedName("user_app_guid")
    @Expose
    var user_app_guid:String?=""

}