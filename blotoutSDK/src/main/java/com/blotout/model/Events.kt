package com.blotout.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Events {
    @SerializedName("meta")
    @Expose
    var meta: Meta? = null

    @SerializedName("events")
    @Expose
    var events: List<Event>? = null

}