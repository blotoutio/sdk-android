package com.blotout.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Meta {

    @SerializedName("sdkv")
    @Expose
    var sdkv: String? = null

    @SerializedName("tz_offset")
    @Expose
    var tzOffset: Int? = null

    @SerializedName("user_id_created")
    @Expose
    var userIdCreated:Long? = null

    @SerializedName("plf")
    @Expose
    var plf: Int? = null

    @SerializedName("appn")
    @Expose
    var appn: String? = null

    @SerializedName("osv")
    @Expose
    var osv: String? = null

    @SerializedName("appv")
    @Expose
    var appv: String? = null

    @SerializedName("dmft")
    @Expose
    var dmft: String? = null

    @SerializedName("dm")
    @Expose
    var dm: String? = null

    @SerializedName("bnme")
    @Expose
    var bnme: String? = null

    @SerializedName("osn")
    @Expose
    var osn: String? = null

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

}