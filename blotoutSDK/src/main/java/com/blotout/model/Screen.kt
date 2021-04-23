package com.blotout.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Screen (

    @SerializedName("width")
    @Expose
    private val width: Int? = null,

    @SerializedName("height")
    @Expose
    private val height: Int? = null,

    @SerializedName("docHeight")
    @Expose
    private val docHeight: Int? = null,

    @SerializedName("docWidth")
    @Expose
    private val docWidth: Int? = null
)