package com.blotout.model

import com.google.gson.annotations.SerializedName

data class VariableOption (
        @SerializedName("label")
        val label:String ,
        @SerializedName("key")
        var key:Int)
